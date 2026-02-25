package io.github.jqdi.i18n.springbootdemo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class CommonI18n {
    private Integer id;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务ID
     */
    private Integer businessId;

    /**
     * 国际化语言编码
     */
    private String locale;

    /**
     * 文案
     */
    private String text;

    /**
     * 国际化文案
     */
    private String i18nText;

    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
