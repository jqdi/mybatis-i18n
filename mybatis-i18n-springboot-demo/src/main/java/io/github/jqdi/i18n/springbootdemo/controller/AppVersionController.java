package io.github.jqdi.i18n.springbootdemo.controller;

import io.github.jqdi.i18n.core.annotation.ExcludeI18n;
import io.github.jqdi.i18n.springbootdemo.entity.AppVersion;
import io.github.jqdi.i18n.springbootdemo.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/appVersion")
public class AppVersionController {
    @Autowired
    private AppVersionService appVersionService;

//    @ExcludeI18n // 排除国际化
    @GetMapping("/selectLastByAppCode")
    public AppVersion selectLastByAppCode(String appCode) {
        AppVersion lastAppVersion = appVersionService.selectLastByAppCode(appCode);
        return lastAppVersion;
    }
}
