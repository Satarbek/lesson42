package Task2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    private final int port;
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private Map<String, Socket> users = new HashMap<>();
    private List<String> userNames = List.of("Terminator", "T1000", "KyleReese", "SarahConnor", "Jack");
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
            var currentUser = makeUser(userNames.get(new Random().nextInt(userNames.size())));
            Socket clientSocket = server.accept();
            users.put(currentUser, clientSocket);
            pool.submit(() -> new Handle().handle(clientSocket, currentUser, users));
            }
        } catch (IOException e) {
            System.out.printf("Most likely the port %s is busy.%n", port);
            e.printStackTrace();
        }
    }

    private String makeUser(String name){
        if(users.containsKey(name)){
            return makeUser(userNames.get(new Random().nextInt(userNames.size())));
        }
        return name;
    }
}
