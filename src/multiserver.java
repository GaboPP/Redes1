import java.net.*;
import java.io.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class multiserver {
    public static void main(String[] args) throws IOException {

//        if (args.length != 1) {
//            System.err.println("Usage: java KKMultiServer <port number>");
//            System.exit(1);
//        }

        ExecutorService executor = Executors.newFixedThreadPool(3);


        int portNumber = 4444; //Integer.parseInt(args[0]);
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                Runnable worker = new multiserverthread(serverSocket.accept());
                executor.execute(worker);
//                executor.shutdown();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}