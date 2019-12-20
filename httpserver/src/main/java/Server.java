import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    /**
     * 端口号
     */
    private static final int PORT = 9999;

    private static final Map<String, Object> SESSION = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        ExecutorService POOL = Executors.newCachedThreadPool();
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                POOL.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Request  request  = Request.build(socket.getInputStream());
                            Response response = Response.build(socket.getOutputStream());

                            if("/307".equals(request.getUri())) {
                                response.setStatus(307);
                                response.setMessage("Temporary Redirect");
                                response.addHeader("Location", "http://45.40.254.164");
                                response.println("");
                            }else if("/setCookie".equals(request.getUri())) {
                                response.setStatus(200);
                                response.setMessage("OK");
                                response.addHeader("Set-Cookie", "studentid=" + UUID.randomUUID());
                                response.println("<h1>Set Cookie OK</h1>");
                            }else if("GET".equals(request.getMethod()) && "/login.html".equals(request.getUri())) {
                                response.setStatus(200);
                                response.setMessage("OK");
                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(
                                                Server.class.getClassLoader().getResourceAsStream("login.html")));
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    response.println(line);
                                }
                            }else if("POST".equals(request.getMethod()) && "/login".equals(request.getUri())){
                                response.setStatus(200);
                                response.setMessage("OK");
                                response.println("<h2>欢迎您，用户"+request.getParameter("username")+"</h2>");
                                String sessionId = UUID.randomUUID().toString();
                                response.addHeader("Set-Cookie", "JSESSIONID="+sessionId);
                                SESSION.put(sessionId, "用户名："+request.getParameter("username")+"，计算机系2020届学生");
                            }else if("/getSession".equals(request.getUri())){
                                response.setStatus(200);
                                response.setMessage("OK");
                                response.println("<h1>"+SESSION.get(request.getCookie("JSESSIONID"))+"</h1>");
                            }else{
                                response.setStatus(404);
                                response.setMessage("Not Found");
                                response.println("<h1>没有找到资源</h1>");
                            }
                            response.flush();
                            socket.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
