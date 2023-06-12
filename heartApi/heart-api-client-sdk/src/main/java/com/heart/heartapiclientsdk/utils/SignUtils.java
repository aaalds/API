package com.heart.heartapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 签名工具
 * @ClassName SignUtils
 * @Description TODO
 * @Author OTTO
 * @Date 2022/12/1 00:11
 */
public class SignUtils {

    /**
     * 生成签名
     * @param
     * @param secretKey
     * @return
     */
    public static String getSign(String body, String secretKey,String none,String currentTime) {
        Digester md5=new Digester(DigestAlgorithm.SHA256);
        String content= body+"."+secretKey+"."+none+"."+currentTime;
        return md5.digestHex(content);
    }

}
