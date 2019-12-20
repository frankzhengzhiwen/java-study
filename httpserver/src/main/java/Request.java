import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Request {

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求路径
     */
    private String uri;

    /**
     * Http版本
     */
    private String version;

    /**
     * 请求头
     */
    private Map<String, String> headers = new HashMap<>();

    /**
     * 请求参数
     */
    private Map<String, String> parameters = new HashMap<>();

    private Map<String, String> cookies = new HashMap<>();

    /**
     * 统一编码
     */
    private static final String CHARSET = "UTF-8";

    private Request() {

    }

    /**
     * 解析Http请求并生成请求对象
     *
     * @param is Http请求流
     * @return 请求对象
     * @throws IOException
     */
    public static Request build(InputStream is) throws IOException {
        Request        request = new Request();
        BufferedReader reader  = new BufferedReader(new InputStreamReader(is, CHARSET));
        String line = reader.readLine();
        System.out.println("请求行:"+line);
        // 处理请求行
        if(line != null){
            String[] requestLineArray = line.split(" ");
            request.setMethod(requestLineArray[0]);
            request.setUri(requestLineArray[1]);
            request.setVersion(requestLineArray[2]);
        }


        // 处理请求头
        while ((line = reader.readLine()) != null && line.length() != 0) {
            System.out.println("请求头："+line);
            String[] headerArray = line.split(":");
            request.addHeader(headerArray[0].trim(), headerArray[1].trim());
            if("Cookie".equalsIgnoreCase(headerArray[0].trim())){
                String[] cookies = headerArray[1].trim().split(";");
                for(String cookie : cookies){
                    String[] cookieArray = cookie.split("=");
                    request.cookies.put(cookieArray[0].trim(), cookieArray[1].trim());
                }
            }
        }
        // 处理请求体
        if ("POST".equals(request.getMethod())) {
            int    len   = Integer.parseInt(request.getHeader("Content-Length"));
            char[] chars = new char[len];
            reader.read(chars, 0, len);
            System.out.println("请求体："+String.valueOf(chars));
            for (String requestParam : String.valueOf(chars).split("&")) {
                String[] paramArray = requestParam.split("=");
                request.addParameter(paramArray[0], paramArray[1]);
            }
        }
        return request;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void addParameter(String key, String value) {
        parameters.put(key, value);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getCookie(String key){

        return cookies.get(key);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", version='" + version + '\'' +
                ", headers=" + headers +
                ", parameters=" + parameters +
                '}';
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }
}
