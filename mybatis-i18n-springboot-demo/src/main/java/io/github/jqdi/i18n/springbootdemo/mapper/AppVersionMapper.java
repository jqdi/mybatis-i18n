package io.github.jqdi.i18n.springbootdemo.mapper;

import io.github.jqdi.i18n.core.annotation.ExcludeI18n;
import io.github.jqdi.i18n.springbootdemo.entity.AppVersion;
import org.apache.ibatis.annotations.Param;

public interface AppVersionMapper {
//    @ExcludeI18n // 排除国际化
    AppVersion selectLastByAppCode(@Param("appCode") String appCode);
}
