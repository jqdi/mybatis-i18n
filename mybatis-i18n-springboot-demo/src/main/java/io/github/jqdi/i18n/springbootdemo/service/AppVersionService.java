package io.github.jqdi.i18n.springbootdemo.service;

import io.github.jqdi.i18n.springbootdemo.entity.AppVersion;
import io.github.jqdi.i18n.springbootdemo.mapper.AppVersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppVersionService {

    @Autowired
    private AppVersionMapper appVersionMapper;

    public AppVersion selectLastByAppCode(String appCode) {
        return appVersionMapper.selectLastByAppCode(appCode);
    }
}
