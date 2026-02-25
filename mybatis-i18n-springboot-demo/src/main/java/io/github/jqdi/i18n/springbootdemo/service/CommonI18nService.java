package io.github.jqdi.i18n.springbootdemo.service;

import io.github.jqdi.i18n.springbootdemo.entity.CommonI18n;
import io.github.jqdi.i18n.springbootdemo.mapper.CommonI18nMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class CommonI18nService {
    @Autowired
    private CommonI18nMapper commonI18nMapper;

    public List<CommonI18n> selectByBusinessTypeBusinessidsLocale(String businessType, List<Integer> businessTypeIdList,
                                                                  String locale) {
        if (CollectionUtils.isEmpty(businessTypeIdList)) {
            return Collections.emptyList();
        }
        return commonI18nMapper.selectByBusinessTypeBusinessidsLocale(businessType, businessTypeIdList, locale);
    }
}
