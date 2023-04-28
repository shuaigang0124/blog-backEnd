package com.gsg.blog.ex;

import com.gsg.blog.utils.R;

/**
 * 业务处理异常基类
 * @author shuaigang
 * @date  2023/4/28 19:35
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = -5027424785213629228L;
    private int code = R.INTERNAL_SERVER_ERROR;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    protected ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ServiceException(int code) {
        this.code = code;
    }

    public ServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ServiceException(String message, Throwable cause,
                            int code) {
        super(message, cause);
        this.code = code;
    }

    public ServiceException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public ServiceException(String message, Throwable cause,
                            boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    /** 400 INVALID REQUEST
     用户发出的请求有错误，服务器没有进行新建或修改数据的操作。*/
    public static ServiceException invalidRequest(String message){
        return new ServiceException(message, R.INVALID_REQUEST);
    }

    /** 4004 errorParams 参数错误*/
    public static ServiceException errorParams(String message){
        return new ServiceException(message,R.ERROR_PARAMS);
    }

    /** 4005 listEmpty 未查询到结果信息 */
    public static ServiceException listEmpty(String message) {
        return new ServiceException(message, R.LIST_EMPTY);
    }

    /** 404 NOT FOUND 用户发出的请求针对的是不存在的记录，服务器没有进行操作。  */
    public static ServiceException notFound(String message){
        return new ServiceException(message, R.LIST_EMPTY);
    }

    /** 5001 internalServerError 系统内部错误，（通用未定义的错误）
     * 服务器忙的异常*/
    public static ServiceException busy(){
        return new ServiceException("数据库忙", R.INTERNAL_SERVER_ERROR);
    }
    public static ServiceException busy(String message){
        return new ServiceException(message, R.INTERNAL_SERVER_ERROR);
    }

}
