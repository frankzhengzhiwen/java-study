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
     * 构建200响应头
     */
    public void build200(){
        this.setStatus(200);
        this.setMessage("OK");
    }

    /**
     * 构建307重定向响应头
     * @param location 重定向URL
     */
    public void build307(String location){
        this.setStatus(307);
        this.setMessage("Temporary Redirect");
        this.addHeader("Location", location);
        this.println("");
    }

    /**
     * 构建404找不到资源
     */
    public void build404(){
        this.setStatus(404);
        this.setMessage("Not Found");
        this.println("<h1>没有找到资源</h1>");
    }

    /**
     * 返回信息
     * @param content
     */
    public Response println(String content){
        body.append(content+"\r\n");
        return this;
    }

    /**
     * 添加响应头
     * @param key
     * @param value
     */
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * 根据设置的响应信息进行输出
     * @throws IOException
     */
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
