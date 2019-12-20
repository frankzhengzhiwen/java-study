import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Task implements Runnable {

    /**
     * 保存会话信息
     */
    private static final Map<String, Object> SESSION_MAP = new HashMap<>();

    private Socket socket;

    public Task(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Request  request  = Request.build(socket.getInputStream());
            Response response = Response.build(socket.getOutputStream());

            if("/307".equals(request.getUri())) {
                response.build307("https://www.baidu.com");
            }else if("/setCookie".equals(request.getUri())) {
                response.build200();
                response.addHeader("Set-Cookie", "studentid=" + UUID.randomUUID());
                response.println("<h1>Set Cookie OK</h1>");
            }else if("POST".equals(request.getMethod()) && "/login".equals(request.getUri())){
                response.build200();
                response.println("<h2>欢迎您，用户"+request.getParameter("username")+"</h2>");
                String sessionId = UUID.randomUUID().toString();
                response.addHeader("Set-Cookie", "JSESSIONID="+sessionId);
                SESSION_MAP.put(sessionId, "用户名："+request.getParameter("username")+"，计算机系2020届学生");
            }else if("/getSession".equals(request.getUri())){
                response.build200();
                response.println("<h1>"+ SESSION_MAP.get(request.getCookie("JSESSIONID"))+"</h1>");
            }else if("GET".equals(request.getMethod())) {
                InputStream is = Server.class.getClassLoader().getResourceAsStream("."+request.getUri());
                if(is == null){
                    response.build404();
                }else{
                    response.build200();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.println(line);
                    }
                }
            }else{
                response.build404();
            }
            response.flush();
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
