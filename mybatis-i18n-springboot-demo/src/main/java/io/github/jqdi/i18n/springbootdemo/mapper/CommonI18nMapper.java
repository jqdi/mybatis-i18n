package io.github.jqdi.i18n.springbootdemo.mapper;

import io.github.jqdi.i18n.springbootdemo.entity.CommonI18n;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommonI18nMapper {
    List<CommonI18n> selectByBusinessTypeBusinessidsLocale(@Param("businessType") String businessType,
        @Param("businessTypeIds") List<Integer> businessTypeIds, @Param("locale") String locale);
}
