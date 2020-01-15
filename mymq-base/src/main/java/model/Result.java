package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.PrintWriter;
import java.io.StringWriter;

@Getter
@Setter
@ToString
public class Result<T> {

    private Boolean success;

    private String code;

    private String message;

    private String stackTrace;

    private T data;

    public static Result buildOK(){
        Result result = new Result();
        result.success = true;
        result.code = "200";
        result.message = "OK";
        return result;
    }

    public static Result buildError(){
        return buildError(null);
    }

    public static Result buildError(Throwable t){
        Result result = new Result();
        result.success = false;
        result.code = "500";
        result.message = "Server Error";
        if(t != null){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            result.stackTrace = sw.toString();
        }
        return result;
    }
}
