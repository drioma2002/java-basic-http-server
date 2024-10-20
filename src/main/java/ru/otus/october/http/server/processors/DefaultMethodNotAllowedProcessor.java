package ru.otus.october.http.server.processors;

import ru.otus.october.http.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DefaultMethodNotAllowedProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        String response = "" +
                "HTTP/1.1 405 Method not allowed\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body><h1>Method not allowed</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
