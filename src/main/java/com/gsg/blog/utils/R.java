package com.gsg.blog.utils;

import com.gsg.blog.ex.ServiceException;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author shuaigang
 * @since JDK 11.0.10
 * ClassName: R
 * date: 2021/3/5 16:50
 */
@Data
@Accessors(chain = true)
public class R<T> implements Serializable {

    /**
     *  自定义错误码避开协议错误码
     *  【4004-参数错误】 【4005-未查询到结果信息】 【4006-token已失效】
     *  【5001-系统内部错误，（通用未定义的错误）】
     * */

    /** 400 INVALID REQUEST 用户发出的请求有错误，服务器没有进行新建或修改数据的操作。*/
    public static final int INVALID_REQUEST = 400;

    /** 404 NOT FOUND 用户发出的请求针对的是不存在的记录，服务器没有进行操作。 */
    public static final int NOT_FOUND = 404;

    /** 200 OK：服务器成功 */
    public static final int OK = 200;

    /** 4001 unauthorized 未认证登录*/
    public static final int UNAUTHORIZED = 4001;

    /** 4003 forbidden 权限不足，访问被禁止 */
    public static final int FORBIDDEN = 4003;

    /** 4004 errorParams 参数错误*/
    public static final int ERROR_PARAMS = 4004;

    /** 4005 listEmpty 未查询到结果信息 */
    public static final int LIST_EMPTY = 4005;

    /** 4006 tokenIsExpired token已失效 */
    public static final int TOKEN_IS_EXPIRED = 4006;

    /** 4014 wrongPassword 密码错误。 */
    public static final int WRONG_PASSWORD = 4014;

    /** 5001 internalServerError 系统内部错误，（通用未定义的错误） */
    public static final int INTERNAL_SERVER_ERROR = 5001;

    private int code;
    private String message;
    private T customData;

    /**
     * 根据传入的错误编码、错误消息构造返回值 R
     * @param message 消息
     */
    public static R<?> construct(int code, String message){
        return new R<>().setCode(code).setMessage(message);
    }

    /**
     * 根据传入的错误编码、错误消息、以及返回结构体构造返回值 R
     * @param message 消息
     * @param data 数据
     */
    public static R<?> construct(int code, String message, Object data){
        return new R<>().setCode(code).setMessage(message).setCustomData(data);
    }

    /**
     * 服务器成功返回用户请求的数据
     * @param message 消息
     */
    public static R ok(String message){
        return new R().setCode(OK).setMessage(message);
    }

    /**
     * 服务器成功返回用户请求的数据
     * @param data 数据
     */
    public static R ok(Object data){
        return new R().setMessage("OK").setCode(OK).setCustomData(data);
    }

    /**
     * 服务器成功返回用户请求的数据
     * @param message 消息
     * @param data 数据
     */
    public static R ok(String message, Object data){
        return new R().setMessage(message).setCode(OK).setCustomData(data);
    }

    /**
     * 将异常消息复制到返回结果集中
     */
    public static R failed(ServiceException e){
        return new R().setCode(e.getCode()).setMessage(e.getMessage());
    }

    /**
     * 服务器发生错误，用户将无法判断发出的请求是否成功。
     */
    public static R failed(Throwable e){
        return new R().setCode(INTERNAL_SERVER_ERROR).setMessage(e.getMessage());
    }

}
