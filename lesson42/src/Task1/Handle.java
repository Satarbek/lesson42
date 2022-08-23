package Task1;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Handle {

    public static void handle(Socket clientside) {
        System.out.printf("Client connected: %s%n", clientside);

        try(clientside;
            Scanner reader = getReader(clientside);
            PrintWriter writer = getWriter(clientside)){
            sendResponse("Hello " + clientside, writer);
            while (true) {
                String message = reader.nextLine().strip();
                System.out.printf("Got: %s%n", message);
                if (isEmptyMsg(message) || isQuitMsg(message)){
                    break;
                }
//                sendResponse(message.toUpperCase(), writer);
            }
        } catch (NoSuchElementException e){
            System.out.println("Client dropped the connection!");
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.printf("Client disconnected: %s%n", clientside);
    }

    private static PrintWriter getWriter(Socket clientside) throws IOException {
        OutputStream stream = clientside.getOutputStream();
        return new PrintWriter(stream);
    }

    private static Scanner getReader(Socket clientside) throws IOException {
        InputStream stream = clientside.getInputStream();
        InputStreamReader input = new InputStreamReader(stream, "UTF-8");
        return new Scanner(input);
    }

    private static boolean isQuitMsg(String message) {
        return "bye".equalsIgnoreCase(message);
    }

    private static boolean isEmptyMsg(String message) {
        return message == null || message.isBlank();
    }

    private static void sendResponse(String response, Writer writer) throws IOException {
        writer.write(response);
        writer.write(System.lineSeparator());
        writer.flush();
    }
}
