package exception;

public class SystemException extends BaseException {

    private static final String CODE = "500";

    private static final String PREFIX = "服务器错误：";

    public SystemException(String message) {
        super(CODE, PREFIX+message);
    }

    public SystemException(String message, Throwable t) {
        super(CODE, PREFIX+message, t);
    }
}
