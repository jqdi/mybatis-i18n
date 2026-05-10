package io.github.jqdi.i18n.springbootdemo.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import io.github.jqdi.i18n.springbootdemo.entity.CommonI18n;
import io.github.jqdi.i18n.springbootdemo.mapper.CommonI18nMapper;

@Service
public class CommonI18nService {
    @Autowired
    private CommonI18nMapper commonI18nMapper;

    public List<CommonI18n> selectByBusinessTypesBusinessIdsLocale(List<CommonI18n> commonI18nList, String locale) {
        if (CollectionUtils.isEmpty(commonI18nList)) {
            return Collections.emptyList();
        }
        return commonI18nMapper.selectByBusinessTypesBusinessIdsLocale(commonI18nList, locale);
    }
}
