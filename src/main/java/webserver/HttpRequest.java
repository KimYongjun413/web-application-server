package webserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String method;
    private String path;
    Map<String, String> headerMap = new HashMap<>();
    Map<String, String> parameterMap = new HashMap<>();

    public HttpRequest(InputStream in) {

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = bufferedReader.readLine();

            //HTTP 메소드 분리

            //URL 분리

            //헤더 분리

            //본문 분리

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMethod() {
        return method;
    }


    public String getPath() {
        return path;
    }

    public String getHeader(String connection) {
        return headerMap.get(connection);
    }

    public String getParameter(String userId) {
        return parameterMap.get(userId);
    }
}
