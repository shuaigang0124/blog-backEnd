package com.gsg.blog.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gsg.blog.dto.CommonDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 *
 * @author shuaigang
 * @date 2023/9/4 9:22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("app_version")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppVersion extends CommonDto implements Serializable {

    private static final long serialVersionUID = -5496868413009490981L;


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 版本编码
     */
    @TableField("version_code")
    private Integer versionCode;

    /**
     * 版本
     */
    @TableField("version")
    private String version;

    /**
     * 应用密钥
     */
    @TableField("digest")
    private String digest;

    /**
     * 更新说明
     */
    @TableField("features")
    private String features;

    /**
     * 下载地址
     */
    @TableField("download_url")
    private String downloadUrl;

    /**
     * 逻辑删除,0-未删除,1-已删除,默认值0
     */
    @TableField("deleted")
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField("gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @TableField("gmt_modified")
    private Date gmtModified;

}
