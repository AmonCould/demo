package util;

import java.io.Serializable;

public class SelfResponse<T> implements Serializable {
    /**
     * 返回数据代码
     */
    private int code;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回的数据
     */
    private T data;
    private SelfResponse(int code){
        this.code=code;
    }
    private SelfResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    private SelfResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private SelfResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    /**
     * 判断当前返回数据的状态标记是不是成功
     *
     * @return 当前返回数据是不是标记为操作成功
     */
    public boolean isSuccess() {
        return this.code == SelfResponseCode.SUCCESS.getCode();
    }

    /**
     * 获取当前返回数据之中包含的状态信息，以便调用者判断是不是成功
     *
     * @return 当前返回数据的状态码
     */
    public int getStatus() {
        return this.code;
    }

    /**
     * 获取当前返回数据之中包含的信息
     *
     * @return 返回数据之中的msg信息, 有可能包含着错误信息
     */
    public String getMsg() {
        return this.msg;
    }

    /**
     * 获取当前返回数据之中包含的数据
     *
     * @return 返回当前返回数据之中包含的数据
     */
    public T getData() {
        return this.data;
    }

    /**
     * 标记为成功信息，但是不附加数据和额外的Msg，仅表示操作成功
     *
     * @return 标记为成功返回信息
     */
    public static <T> SelfResponse<T> createBySuccess() {
        return new SelfResponse<>(SelfResponseCode.SUCCESS.getCode());
    }

    /**
     * 标记为成功信息，附加额外的Msg,但是不附加数据，表示操作成功，并返回额外的Msg
     *
     * @param msg 附加Msg
     * @param <T> 返回数据的类型
     * @return 带有附加Msg的标记为成功的返回信息
     */
    public static <T> SelfResponse<T> createBySuccessWithMsg(String msg) {
        return new SelfResponse<>(SelfResponseCode.SUCCESS.getCode(), msg);
    }

    /**
     * 创建标记为成功的信息，不附加额外的Msg和数据，表示操作成功，并且返回数据
     *
     * @param data 要返回的数据
     * @param <T>  返回数据的类型
     * @return 附加数据，不附加Msg，标记为成功的返回信息
     */
    public static <T> SelfResponse<T> createBySuccessWithData(T data) {
        return new SelfResponse<>(SelfResponseCode.SUCCESS.getCode(), data);
    }

    /**
     * 创建标记为成功的信息，附加额外的Msg，附加数据，表示操作成功，并且返回数据和额外的Msg
     *
     * @param msg  附加的Msg
     * @param data 要返回的数据
     * @param <T>  返回数据的类型
     * @return 附加数据，附加Msg，标记为成功的返回信息
     */
    public static <T> SelfResponse<T> createBySuccessWithDataAndMsg(String msg, T data) {
        return new SelfResponse<>(SelfResponseCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 创建标记为失败的信息，不附加额外的Msg
     *
     * @param <T> 返回数据的类型
     * @return 标记为失败的返回信息
     */
    public static <T> SelfResponse<T> createByError() {
        return new SelfResponse<>(SelfResponseCode.ERROR.getCode());
    }

    /**
     * 创建标记为失败的信息，附加额外的Msg
     *
     * @param <T> 返回数据的类型
     * @return 附加额外的Msg，标记为失败的返回信息
     */
    public static <T> SelfResponse<T> createByErrorWithMsg(String msg) {
        return new SelfResponse<>(SelfResponseCode.ERROR.getCode(), msg);
    }

    /**
     * 创建标记为失败的信息 附件额外的Msg 并附加数据
     * @param msg 额外的信息
     * @param data 附加的数据
     * @param <T> 返回数据的类型
     * @return 附加额外的信息 并附加数据 标记为错误的返回数据
     */
    public static <T> SelfResponse<T> createByErrorWithMsgAndData(String msg,T data){
        return new SelfResponse<>(SelfResponseCode.ERROR.getCode(),msg,data);
    }

    /**
     * 创建自定义状态码的错误信息，附加额外的Msg
     *
     * @param errorCode 状态码
     * @param msg       额外的Msg
     * @param <T>       返回数据的类型
     * @return 附加额外的Msg，自定义错误代码，标记为失败的返回信息
     */
    public static <T> SelfResponse<T> createByErrorCodeWithMsg(int errorCode, String msg) {
        return new SelfResponse<>(errorCode, msg);
    }
}
