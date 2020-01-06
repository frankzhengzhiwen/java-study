package exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {

    private String code;

    private String message;

    public BaseException(String code, String message){
        super(message);
    }

    public BaseException(String code, String message, Throwable t){
        super(message, t);
    }
}
