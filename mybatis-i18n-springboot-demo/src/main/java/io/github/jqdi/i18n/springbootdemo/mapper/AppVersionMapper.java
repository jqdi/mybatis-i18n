package io.github.jqdi.i18n.springbootdemo.mapper;

import io.github.jqdi.i18n.springbootdemo.entity.AppVersion;
import org.apache.ibatis.annotations.Param;

public interface AppVersionMapper {
    AppVersion selectLastByAppCode(@Param("appCode") String appCode);
}
