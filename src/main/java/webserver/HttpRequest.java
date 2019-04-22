package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private HttpMethod httpMethod;
    private String method;
    private String path;
    Map<String, String> headers = new HashMap<>();
    Map<String, String> params = new HashMap<>();

    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            if(line == null) {
                return;
            }

            setMethod(line);
            setPath(line);
            setGetParameter(line);

            line = br.readLine();

            setHeader(line, br);
            setPostParameter(line, br);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMethod(String line) {
        String method = line.split(" ")[0];
        this.method = method;
    }

    private void setPath(String line) {
        String[] pathSplitted = line.split(" ");
        httpMethod = HttpMethod.valueOf(getMethod());
        if(httpMethod.isPost()) {
            this.path = pathSplitted[1];
            return;
        }

        int index = pathSplitted[1].indexOf("?");
        if( index == -1) {
            this.path = pathSplitted[1];
        }else {
            this.path = pathSplitted[1].substring(0,index);
        }

    }

    private void setHeader(String line, BufferedReader br) {
        while(line != null && !line.equals("")) {
            String[] headerSplitted = line.split(":");
            this.headers.put(headerSplitted[0].trim(), headerSplitted[1].trim());
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setGetParameter(String line) {
        String[] pathSplitted = line.split(" ");
        int index = pathSplitted[1].indexOf("?");
        if( index > -1) {
            params = HttpRequestUtils.parseQueryString((pathSplitted[1].substring(index+1)));
        }
    }

    private void setPostParameter(String line, BufferedReader br) {
        try {
            httpMethod = HttpMethod.valueOf(getMethod());
            if(!httpMethod.isPost()) return;
            String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
            this.params = HttpRequestUtils.parseQueryString(body);
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
        return headers.get(connection);
    }

    public String getParameter(String userId) {
        return params.get(userId);
    }

    public enum HttpMethod {
        GET,
        POST;

        public boolean isPost() {
            return this == POST;
        }
    }
}
