# MyBatis国际化插件 (mybatis-i18n)

一个轻量级的MyBatis国际化解决方案，通过注解方式自动实现数据库字段的多语言替换，让国际化变得简单优雅。

## 核心特性

- **零侵入设计**：通过注解方式实现，无需修改现有业务代码
- **自动替换**：MyBatis查询结果自动进行国际化字段替换
- **灵活配置**：支持多种数据提供方式和自定义策略
- **Spring Boot集成**：提供starter模块，开箱即用
- **高性能**：基于MyBatis拦截器实现，性能损耗极低

## 模块说明

```
mybatis-i18n
├── mybatis-i18n-core                    -- 核心模块
│   ├── annotation                       -- 核心注解
│   │   └── @I18nField                  -- 国际化字段注解
│   ├── plugin                           -- MyBatis插件
│   │   ├── I18nFieldValueReplaceInterceptor -- 字段值替换拦截器
│   │   └── I18nReplaceHandler          -- 替换处理器
│   ├── provider                         -- 数据提供者
│   │   ├── I18nDataProvider            -- 国际化数据接口
│   │   ├── I18nDataProviderFactory     -- 数据提供者工厂
│   │   └── impl
│   │       └── DefaultI18nDataProvider -- 默认数据提供者
│   └── metadata                         -- 元数据处理
│       ├── I18nFieldInfo               -- 字段信息
│       └── RelatedI18nValueMapping     -- 关联映射
├── mybatis-i18n-spring-boot-starter     -- Spring Boot Starter
│   └── PluginsAutoConfiguration        -- 自动配置类
│   └── provider
│       ├── JdbcTemplateI18nDataProvider -- JdbcTemplate数据提供者
│       └── SpringI18nDataProviderFactory -- Spring数据提供者工厂
└── mybatis-i18n-springboot-demo         -- Spring Boot使用示例
```

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>io.github.jqdi</groupId>
    <artifactId>mybatis-i18n-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 实体类配置

在需要国际化的字段上添加 `@I18nField` 注解：

```java
@Data
@Accessors(chain = true)
public class AppVersion {
    private Integer id;
    private String appCode;
    private String version;
    private LocalDateTime releaseTime;
    private String downloadUrl;

    @I18nField(
        i18nTable = "app_version_i18n",           -- 国际化表名
        i18nColumn = "release_notes",             -- 国际化表中的字段名
        i18nRelatedColumn = "app_version_id",     -- 国际化表关联字段
        relatedValueFromField = "id",             -- 主表关联值来源字段
        i18nLocaleColumn = "locale"               -- 语言编码字段名（默认为locale）
    )
    private String releaseNotes;                  -- 将被国际化内容替换

    private String remark;
}
```

### 3. 数据库表结构

需要两张表：主表和国际化表

```sql
-- 主表
CREATE TABLE `app_version` (
   `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
   `app_code` varchar(32) NOT NULL COMMENT 'app_info.app_code',
   `version` varchar(32) NOT NULL COMMENT '版本号',
   `min_supported_version` varchar(32) DEFAULT NULL COMMENT '最低支持版本（低于此版本必须升级）',
   `release_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
   `download_url` varchar(255) NOT NULL COMMENT '安装包下载地址',
   `release_notes` text COMMENT '发布说明',
   PRIMARY KEY (`id`),
   KEY `idx_appcode_releasetime` (`app_code`,`release_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='APP版本';
-- 插入示例数据
INSERT INTO `app_version` (`id`, `app_code`, `version`, `min_supported_version`, `release_time`, `download_url`, `release_notes`, `remark`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES (4, 'jd_android', '1.0.2', '1.0.1', '2025-06-11 21:00:20', 'https://dw.jd.com/jd_andriod_1.0.2.apk', '修复了1.0.0中的重大bug，不再支持1.0.0版本', '', '2025-06-11 21:00:20', '2025-06-12 10:15:37', ' ', ' ');

-- 国际化表
CREATE TABLE `app_version_i18n` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `app_version_id` int(11) NOT NULL COMMENT 'app_version.id',
    `locale` varchar(8) NOT NULL DEFAULT '' COMMENT '地区编码',
    `release_notes` text COMMENT '发布说明',
    `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_appversionid_locale` (`app_version_id`,`locale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='APP版本-国际化';
-- 插入示例数据
INSERT INTO `app_version_i18n` (`id`, `app_version_id`, `locale`, `release_notes`, `remark`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (1, 4, 'en-US', 'Major bugs in version 1.0.0 have been fixed, and version 1.0.0 is no longer supported', '', '2026-02-11 17:47:56', 1, '2026-02-11 17:50:14', 1);
```


如果使用统一的国际化表，可不要app_version_i18n

```sql
-- 一张表存储所有的国际化翻译
CREATE TABLE `common_i18n` (
   `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
   `business_id` int NOT NULL COMMENT '业务ID',
   `business_type` varchar(32) NOT NULL COMMENT '业务类型(建议填写[表.字段]，如banner.title)',
   `locale` varchar(8) NOT NULL DEFAULT '' COMMENT '地区编码',
   `i18n_text` text COMMENT '国际化文案',
   PRIMARY KEY (`id`),
   UNIQUE KEY `uniq_businessid_businesstype_locale` (`business_id`,`business_type`,`locale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通用国际化';
-- 插入示例数据
INSERT INTO common_i18n` (`id`, `business_id`, `business_type`, `locale`, `i18n_text`, `remark`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (2, 4, 'app_version.release_notes', 'en-US', 'Major bugs in version 1.0.0 have been fixed, and version 1.0.0 is no longer supported', '', '2026-02-25 20:46:38', 1, '2026-02-25 20:48:24', 1);
```

### 4. 使用示例

```java
@RestController
@RequestMapping("/appVersion")
public class AppVersionController {
    
    @Autowired
    private AppVersionService appVersionService;
    
    @GetMapping("/selectLastByAppCode")
    public AppVersion selectLastByAppCode(String appCode) {
        return appVersionService.selectLastByAppCode(appCode);
    }
}
```

查询结果中的 `releaseNotes` 字段会自动根据请求头中的 `Accept-Language` 进行国际化替换。

## 核心注解详解

### @I18nField

用于标记需要国际化的字段。

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| i18nTable | String | 是 | - | 国际化表名 |
| i18nColumn | String | 是 | - | 国际化表中的字段名 |
| i18nRelatedColumn | String | 是 | - | 国际化表关联字段（用于关联主表） |
| relatedValueFromField | String | 是 | - | 主表中关联值来源的字段名 |
| i18nLocaleColumn | String | 否 | locale | 国际化语言编码字段名 |
| i18nDataProvider | Class | 否 | I18nDataProvider.class | 自定义数据提供者 |

## 高级配置

### 自定义数据提供者

如果默认的数据提供者不满足需求，可以实现 `I18nDataProvider` 接口：

```java
@Component
public class CustomI18nDataProvider implements I18nDataProvider {
    
    @Override
    public List<RelatedI18nValueMapping> getValueMapping(
        I18nFieldInfo i18nFieldInfo, 
        Set<Object> relatedFieldValueSet
    ) {
        String i18nTable = i18nFieldInfo.getI18nTable();
        String i18nColumn = i18nFieldInfo.getI18nColumn();
        String i18nRelatedColumn = i18nFieldInfo.getI18nRelatedColumn();
        String i18nLocaleColumn = i18nFieldInfo.getI18nLocaleColumn();
        
        List<RelatedI18nValueMapping> mappings = new ArrayList<>();
        
        for (Object relatedValue : relatedFieldValueSet) {
            String locale = LocaleContextHolder.getLocale().toLanguageTag();
            String i18nValue = fetchI18nValue(i18nTable, i18nRelatedColumn, 
                                              relatedValue, i18nLocaleColumn, 
                                              i18nColumn, locale);
            if (i18nValue != null) {
                mappings.add(new RelatedI18nValueMapping(relatedValue, i18nValue));
            }
        }
        
        return mappings;
    }
}
```

### 配置MyBatis插件

插件会自动注册，无需手动配置。

## 工作原理

1. **拦截查询**：MyBatis拦截器拦截查询结果
2. **解析注解**：扫描实体类中的 `@I18nField` 注解
3. **批量查询**：根据关联字段值批量查询国际化数据
4. **自动替换**：将查询结果中的字段值替换为对应的国际化内容

## 使用场景

- **应用版本管理**：不同语言环境显示对应的版本说明
- **商品信息**：多语言商品描述和规格说明
- **内容管理**：文章、公告等多语言内容展示
- **配置管理**：系统配置项的多语言支持
- **用户界面**：菜单、按钮文本等UI元素的多语言

## 性能说明

- 基于MyBatis拦截器实现，只在查询时增加一次国际化数据查询
- 支持批量查询，避免N+1问题
- 国际化数据查询结果可缓存
- 性能损耗极低，适合生产环境使用

## 常见问题

### 1. 如何设置默认语言？

在请求头中设置 `Accept-Language`，或在代码中设置默认值：

```java
LocaleContextHolder.setLocale(Locale.CHINA);
```

### 2. 国际化数据不存在会怎样？

如果国际化表中没有对应语言的数据，字段值保持原值不变。

### 3. 支持哪些语言？

支持所有标准的语言编码，如：`zh-CN`（简体中文）、`zh-TW`（繁体中文）、`en-US`（英语）、`ja-JP`（日语）等。


## 示例项目

完整示例请参考 [mybatis-i18n-springboot-demo](./mybatis-i18n-springboot-demo) 模块。

## 开源共建

### 开源协议

mybatis-i18n 遵循 [Apache 2.0 协议](https://www.apache.org/licenses/LICENSE-2.0.html)。
允许商业使用，但务必保留类作者、Copyright 信息。

### 贡献指南

欢迎提交Issue和Pull Request，让我们一起完善这个项目！

### 联系方式

📧 邮箱：[897665787@qq.com](mailto:897665787@qq.com)
