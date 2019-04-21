package webserver;

import util.HttpRequestUtils;
import util.IOUtils;

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

            setMethod(line);

            setPath(line);
            setGetParameter(line);

            line = bufferedReader.readLine();

            setHeader(line, bufferedReader);

            setPostParameter(line, bufferedReader);

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
        if(getMethod().equals("POST")) {
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
            this.headerMap.put(headerSplitted[0].trim(), headerSplitted[1].trim());
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
            parameterMap = HttpRequestUtils.parseQueryString((pathSplitted[1].substring(index+1)));
        }
    }

    private void setPostParameter(String line, BufferedReader br) {
        try {
            if(!getMethod().equals("POST")) return;
            String body = IOUtils.readData(br, Integer.parseInt(headerMap.get("Content-Length")));
            this.parameterMap = HttpRequestUtils.parseQueryString(body);
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
