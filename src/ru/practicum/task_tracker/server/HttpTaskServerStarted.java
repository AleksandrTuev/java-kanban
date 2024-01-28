package server;

import java.io.IOException;

public class HttpTaskServerStarted {
    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }
}
