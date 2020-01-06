package exception;

public class ClientException extends BaseException {

    private static final String CODE = "400";

    private static final String PREFIX = "客户端错误：";

    public ClientException(String message) {
        super(CODE, PREFIX+message);
    }

    public ClientException(String message, Throwable t) {
        super(CODE, PREFIX+message, t);
    }
}
