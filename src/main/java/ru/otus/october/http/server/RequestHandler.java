package ru.otus.october.http.server;

import java.io.IOException;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final Socket socket;
    private final Dispatcher dispatcher;

    public RequestHandler(Socket socket, Dispatcher dispatcher) {
        this.socket = socket;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[8192];
            int n = socket.getInputStream().read(buffer);
            String rawRequest = new String(buffer, 0, n);
            HttpRequest request = new HttpRequest(rawRequest);
            request.info();
            dispatcher.execute(request, socket.getOutputStream());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
