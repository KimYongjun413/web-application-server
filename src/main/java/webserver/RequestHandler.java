package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;

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

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            String line = bufferedReader.readLine();

            Map<String, String> headerMap = new HashMap<>();
            String url = HttpRequestUtils.getUrl(line);
            if(url.equals("/index.html")) {
                while(line != null && !"".equals(line)) {
                    log.debug("{}",line);
                    line = bufferedReader.readLine();
                }

                byte[] body = Files.readAllBytes(new File("./webapp" + url ).toPath());
                DataOutputStream dos = new DataOutputStream(out);
                response200Header(dos, body.length);
                responseBody(dos, body);

            } else if(url.equals("/user/form.html")) {

                while(line != null && !"".equals(line)) {
                    log.debug("{}",line);
                    line = bufferedReader.readLine();
                }
                byte[] body = Files.readAllBytes(new File("./webapp" + url ).toPath());
                DataOutputStream dos = new DataOutputStream(out);
                response200Header(dos, body.length);
                responseBody(dos, body);
            } else{
                while(line != null && !"".equals(line)) {
                    String headerLine[] = line.split(":");
                    if(headerLine.length > 1) {
                        log.debug("{} : {}",headerLine[0] , headerLine[1]);
                        headerMap.put(headerLine[0], headerLine[1]);
                    }
                    line = bufferedReader.readLine();

                }

                String postBody = IOUtils.readData(bufferedReader, Integer.parseInt(headerMap.get("Content-Length").trim()));
                log.debug(postBody);

                User user = HttpRequestUtils.setUserByPost(postBody);
                log.debug(String.valueOf(user));

                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos);
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
