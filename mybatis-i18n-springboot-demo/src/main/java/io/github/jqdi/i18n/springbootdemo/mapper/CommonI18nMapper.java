package io.github.jqdi.i18n.springbootdemo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.github.jqdi.i18n.springbootdemo.entity.CommonI18n;

public interface CommonI18nMapper {
    List<CommonI18n> selectByBusinessTypesBusinessIdsLocale(@Param("commonI18nList") List<CommonI18n> commonI18nList,
        @Param("locale") String locale);
}
