package util;

public enum SelfResponseCode {
    /**
     * 错误
     */
    ERROR(500, "ERROR"),
    /**
     * 成功
     */
    SUCCESS(200, "SUCCESS"),
    /**
     * 非法参数
     */
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT"),
    NO_PROJECT_AUTH(3, "NO_PROJECT_AUTH");

    private final int code;
    private final String desc;

    SelfResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
