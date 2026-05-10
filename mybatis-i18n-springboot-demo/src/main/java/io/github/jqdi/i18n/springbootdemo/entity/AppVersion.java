package io.github.jqdi.i18n.springbootdemo.entity;

import io.github.jqdi.i18n.core.annotation.I18nField;
import io.github.jqdi.i18n.core.annotation.I18nTable;
import io.github.jqdi.i18n.springbootdemo.i18n.CommonI18nDataProvider;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@I18nTable(i18nTable = "app_version_i18n", i18nRelatedColumn = "app_version_id", relatedValueFromField = "id")
//@I18nTable(i18nTable = "app_version_i18n", i18nRelatedColumn = "app_version_id", relatedValueFromField = "id", i18nDataProvider = CommonI18nDataProvider.class) // 统一国际化表方式
public class AppVersion {
	private Integer id;

	/**
	 * app_info.app_code
	 */
	private String appCode;

	/**
	 * 版本号
	 */
	private String version;

	/**
	 * 最低支持版本（低于此版本必须升级）
	 */
	private String minSupportedVersion;

	/**
	 * 发布时间
	 */
	private LocalDateTime releaseTime;

	/**
	 * 安装包下载地址
	 */
	private String downloadUrl;

	/**
     * 发布说明
     */
	@I18nField(i18nColumn = "release_notes")
    private String releaseNotes;

	private String remark;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
	private String createBy;
	private String updateBy;
}
