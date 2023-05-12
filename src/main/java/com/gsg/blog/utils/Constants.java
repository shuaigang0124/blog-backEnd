package com.gsg.blog.utils;

/**
 * 常量接口
 *  存放公共常使用的常量
 * @author shuaigang
 */
public interface Constants {

    /*常量*/
    /**
     * 语言ID
     */

    String TYPE_0 = "0";

    /** 临时目录 */
    String TEMP = "temp";

    /** 正式目录 */
    String FORMAL = "formal";

    /** 路径前缀 */
    String PREFIX = "/gsg";

    String AVATAR_PATH = "avatarPath";

    /** 空白符 */
    String BLANK = "";

    /**
     * @Description 是否失效
     **/
    String IS_EXPIRED = "isExpired";

    /** 标志符 */
    String FLAG = "Y";

    /** 分隔符1 */
    String DELIMITER_1 = "~";

    /** 分隔符2 */
    String DELIMITER_2 = ",";

    /** 分隔符3 */
    String DELIMITER_3 = "##_##";

    /**
     * http请求
     */
    String HTTP = "http://";

    /**
     * https请求
     */
    String HTTPS = "https://";


    /** 校验返回结果码 */
    String UNIQUE = "0";
    String NOT_UNIQUE = "1";

    String CONTENT_TYPE = "multipart/form-data";

    String GET = "GET";

    String WEBP = ".webp";
    String PNG = ".png";
    String JPG = ".jpg";
    String JPEG = ".jpeg";
    String GIF = ".gif";
    String BMP = ".bmp";

    int CONTENT_MAX = 100;

    String GSG = "GSG";

    String SQL_ERROR = "com.mysql.jdbc";
    String JAVA_SQL_ERROR = "java.sql";


}
