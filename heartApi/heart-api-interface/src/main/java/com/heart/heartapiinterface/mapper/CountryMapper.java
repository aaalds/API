package com.heart.heartapiinterface.mapper;

import com.heart.heartapiinterface.domain.Country;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.ManagedBean;

/**
* @author OTTO
* @description 针对表【country】的数据库操作Mapper
* @createDate 2023-05-01 17:29:06
* @Entity generator.domain.Country
*/
@Mapper
public interface CountryMapper extends BaseMapper<Country> {

}




