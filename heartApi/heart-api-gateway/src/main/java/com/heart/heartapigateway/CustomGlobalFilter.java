package com.heart.heartapigateway;

import com.heart.heartApiCommon.model.entity.InterfaceInfo;
import com.heart.heartApiCommon.model.entity.User;
import com.heart.heartApiCommon.service.InnerInterfaceInfoService;
import com.heart.heartApiCommon.service.InnerUserInterfaceInfoService;
import com.heart.heartApiCommon.service.InnerUserService;
import com.heart.heartapiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    //private static final List<String> IP_WHITE_LIST = Collections.singletonList("127.0.0.1");

    private static final String INTERFACE_HOST="http://localhost:8123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.用户发送请求到API网关
        //2.请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST+request.getPath().value();
        String method = request.getMethod().toString();
        log.info("请求唯一标识:" + request.getId());
        log.info("请求路径:" + path);
        log.info("请求方法:" + method);
        log.info("请求参数:" + request.getQueryParams());
        log.info("请求来源地址:" + request.getRemoteAddress());
        String sourceAddress = Objects.requireNonNull(request.getLocalAddress()).getHostString();
        log.info("请求来源地址:" + sourceAddress);
        ServerHttpResponse response = exchange.getResponse();
        //3.访问控制-黑白名单
//        if (!IP_WHITE_LIST.contains(sourceAddress)) {
//            return handleNoAuth(response);
//        }
        //4.用户鉴权（判断ak，sk是否合法）
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        //实际情况是去数据库中查看是否已分配给用户
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("invokeUser出现错误!", e);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }
        if (nonce==null||Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }
        //时间和当前时间不能超过五分钟
        long currentTime = System.currentTimeMillis() / 1000;
        final long FIVE_MINUTES = 60 * 5L;
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
            return handleNoAuth(response);
        }
        if (nonce!=null) {
            // 判断是否重复请求
            String ak = (String) stringRedisTemplate.opsForValue().get(nonce);
            if (ak!=null) {
                return handleNoAuth(response);// 4.2标记此请求正在被处理或已被处理
            } else {
                stringRedisTemplate.opsForValue().set(nonce,accessKey,5, TimeUnit.MINUTES);
                System.out.println(stringRedisTemplate.opsForValue().get(nonce));
            }
        }
        //实际是从数据库中查secretKey
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.getSign(body, secretKey,nonce,timestamp);
        if (sign == null || !sign.equals(serverSign)) {
            return handleNoAuth(response);
        }
        //5.请求的模拟接口是否存在？以及请求方法是否匹配?
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.error("interfaceInfo出现错误!", e);
        }
        if(interfaceInfo==null){
            return handleNoAuth(response);
        }
        //是否还剩调用次数
        try {
            innerUserInterfaceInfoService.validLeftNum(invokeUser.getId(),interfaceInfo.getId());
        } catch (Exception e) {
            log.error("剩余次数不足！",e);
            return handleNoAuth(response);
        }
        //6.请求转发，调用模拟接口
        //Mono<Void> filter = chain.filter(exchange);
        //7.请求转发，调用模拟接口+响应日志
        return handleResponse(exchange, chain,interfaceInfo.getId(),invokeUser.getId());
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain,long interfaceInfoId,long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            //缓冲区工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            //拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                //装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    //等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        //log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //往返回值里面写数据
                            //拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        //8.调用成功，接口调用次数+1 invokeCount
                                        try {
                                            innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount出错!",e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        sb2.append("<--- {} {} \n");
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        //rspArgs.add(requestUrl);
                                        String data = new String(content, StandardCharsets.UTF_8);//data
                                        sb2.append(data);
                                        //打印日志
                                        log.info(sb2.toString(), rspArgs.toArray(), data);//log.info("<-- {} {}\n", originalResponse.getStatusCode(), data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                //设置response对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应错误.\n" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }


}
