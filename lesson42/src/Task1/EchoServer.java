package Task1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    private final int port;
    private final ExecutorService pool = Executors.newCachedThreadPool();

    private EchoServer(int port) {
        this.port = port;
    }

    public static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    public void run() {
        try (var server = new ServerSocket(port)) {
            System.out.printf("The server started on port %s%n", port);
            while (!server.isClosed()){
            Socket clientSocket = server.accept();
            pool.submit(() -> Handle.handle(clientSocket));
            }
        } catch (IOException e) {
            System.out.printf("Most likely the port %s is busy.%n", port);
            e.printStackTrace();
        }
    }


}
