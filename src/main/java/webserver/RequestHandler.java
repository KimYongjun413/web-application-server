package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = bufferedReader.readLine();

            Map<String, String> headerMap = new HashMap<>();
            String requstType = line.split(" ")[0];
            if (requstType.equals("GET")) {
                String url = HttpRequestUtils.getUrl(line);
                if (url.equals("/index.html")) {
                    while (line != null && !"".equals(line)) {
                        log.debug("{}", line);
                        line = bufferedReader.readLine();
                    }

                    byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                    DataOutputStream dos = new DataOutputStream(out);
                    response200Header(dos, body.length);
                    responseBody(dos, body);

                } else if (url.equals("/user/form.html")) {

                    while (line != null && !"".equals(line)) {
                        log.debug("{}", line);
                        line = bufferedReader.readLine();
                    }
                    byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                    DataOutputStream dos = new DataOutputStream(out);
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                } else if (url.equals("/user/login.html")) {
                    while (line != null && !"".equals(line)) {
                        log.debug("{}", line);
                        line = bufferedReader.readLine();
                    }
                    byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                    DataOutputStream dos = new DataOutputStream(out);
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                } else if (url.equals("/user/login_failed.html")) {
                    while (line != null && !"".equals(line)) {
                        log.debug("{}", line);
                        line = bufferedReader.readLine();
                    }
                    byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                    DataOutputStream dos = new DataOutputStream(out);
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                } else if (url.equals("/user/list.html")) {

                    boolean isLogin = false;
                    // 사용자가 "로그인" 상태인지 판단
                    while (line != null && !"".equals(line)) {
                        if(line.contains("logined=true")) {
                            isLogin = true;
                        }
                        line = bufferedReader.readLine();
                    }

                    if(isLogin) {
                        // 사용자 목록 가져오기
                        Collection<User> allUser = DataBase.findAll();

                        // StringBuilder를 활용해 사용자 목록을 출력하는 HTML을 동적으로 생성한 후 응답으로 보낸다.
                        StringBuilder sbHtml = new StringBuilder();
                        sbHtml.append("<table>");
                        for(User user : allUser)
                        {
                            sbHtml.append("<tr>");
                            sbHtml.append("<td>").append(user.getUserId()).append("</td>");
                            sbHtml.append("<td>").append(user.getName()).append("</td>");
                            sbHtml.append("<td>").append(user.getEmail()).append("</td>");
                            sbHtml.append("</tr>");
                        }
                        sbHtml.append("</table>");

                        byte[] body = sbHtml.toString().getBytes();
                        DataOutputStream dos = new DataOutputStream(out);
                        response200Header(dos, body.length);
                        responseBody(dos, body);
                    }else {
                        url = "user/login.html";
                        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                        DataOutputStream dos = new DataOutputStream(out);
                        response200Header(dos, body.length);
                        responseBody(dos, body);
                    }
                }
            } else {
                String token = line.split(" ")[1];
                if (token.equals("/user/login")) {
                    while (line != null && !"".equals(line)) {
                        String headerLine[] = line.split(":");
                        if (headerLine.length > 1) {
                            log.debug("{} : {}", headerLine[0], headerLine[1]);
                            headerMap.put(headerLine[0], headerLine[1]);
                        }
                        line = bufferedReader.readLine();
                    }
                    String postBody = IOUtils.readData(bufferedReader, Integer.parseInt(headerMap.get("Content-Length").trim()));
                    log.debug(postBody);

                    Map<String,String> parameters = HttpRequestUtils.parseQueryString(postBody);
                    User loginUser = DataBase.findUserById(parameters.get("userId"));
                    if(loginUser != null &&  loginUser.getPassword().equals(parameters.get("password"))) {
                        log.debug("Login 성공!!!!");
                        DataOutputStream dos = new DataOutputStream(out);
                        responseLoginHeader(dos, "logined=true");
                    }else {
                        log.debug("Login 실패(아이디 입력 안하거나 아이디 혹은 비밀번호가 다름)");
                        DataOutputStream dos = new DataOutputStream(out);
                        responseLoginHeader(dos, "logined=false");
                    }


                } else {

                    while (line != null && !"".equals(line)) {
                        String headerLine[] = line.split(":");
                        if (headerLine.length > 1) {
                            log.debug("{} : {}", headerLine[0], headerLine[1]);
                            headerMap.put(headerLine[0], headerLine[1]);
                        }
                        line = bufferedReader.readLine();
                    }
                    String postBody = IOUtils.readData(bufferedReader, Integer.parseInt(headerMap.get("Content-Length").trim()));
                    log.debug(postBody);
                    User user = HttpRequestUtils.setUserByPost(postBody);
                    log.debug(String.valueOf(user));

                    DataBase.addUser(user);

                    DataOutputStream dos = new DataOutputStream(out);
                    response302Header(dos);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
