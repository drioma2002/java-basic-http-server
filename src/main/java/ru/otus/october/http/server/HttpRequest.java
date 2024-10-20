package ru.otus.october.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String rawRequest;
    private HttpMethod method;
    private String uri;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    private String body;
    private Exception exception;
    private static final Logger LOGGER = LogManager.getLogger(HttpRequest.class.getName());

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getUri() {
        return uri;
    }

    public String getRoutingKey() {
        return method + " " + uri;
    }

    public String getBody() {
        return body;
    }

    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        this.parse();
        this.parseHeaders();
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }

    private void parse() {
        int startIndex = rawRequest.indexOf(' ');
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        uri = rawRequest.substring(startIndex + 1, endIndex);
        method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        parameters = new HashMap<>();
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            uri = elements[0];
            String[] keysValues = elements[1].split("[&]");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                parameters.put(keyValue[0], keyValue[1]);
            }
        }
        if (method == HttpMethod.POST) {
            this.body = rawRequest.substring(rawRequest.indexOf("\r\n\r\n") + 4);
        }
    }

    private void parseHeaders() {
        int startIndex = rawRequest.indexOf("\r\n", rawRequest.indexOf(' ') + 1);
        int endIndex = rawRequest.indexOf("\r\n\r\n") - 4;

        String rawHeaders = rawRequest.substring(startIndex, endIndex);

        headers = new HashMap<>();

        String[] rawHeadersSplitted = rawHeaders.split("\r\n");

        for (String o : rawHeadersSplitted) {
            if (!o.contains(": ")) {
                continue;
            }
            String[] keyValue = o.split(": ", 2);
            headers.put(keyValue[0], keyValue[1]);
        }
    }

    public void info() {
        headers.forEach((key, value) -> LOGGER.debug("Request Headers: KEY=" + key + " VALUE=" + value));

        LOGGER.info("Method: " + method);
        LOGGER.info("URI: " + uri);
        LOGGER.info("Parameters: " + parameters);
    }
}
