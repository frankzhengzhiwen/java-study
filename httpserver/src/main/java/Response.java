import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Response {

    /**
     * 响应状态码
     */
    private int status;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应头
     */
    private Map<String, String> headers = new HashMap<>();

    /**
     * 响应打印流
     */
    private PrintWriter writer;

    /**
     * 统一编码
     */
    private static final String CHARSET = "UTF-8";

    /**
     * 响应内容
     */
    private StringBuilder body = new StringBuilder();

    private Response(){

    }

    public static Response build(OutputStream os) throws UnsupportedEncodingException {
        Response response = new Response();
        response.writer = new PrintWriter(new OutputStreamWriter(
                os, CHARSET), true);
        return response;
    }

    /**
     * 返回信息
     * @param content
     */
    public Response println(String content){
        body.append(content+"\r\n");
        return this;
    }

    public void flush() throws IOException {
        writer.println("HTTP/1.1 "+status+" "+message);
        writer.println("Content-Type: text/html; charset=utf-8");
        writer.println("Content-Length: "+body.toString().getBytes(CHARSET).length);
        headers.forEach((k,v)->{
            writer.println(k+": "+v);
        });
        writer.println();
        writer.println(body.toString());
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }
}
