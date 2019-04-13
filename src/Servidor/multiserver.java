package Servidor;

import java.net.*;
import java.io.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class multiserver {
    private static int active_thread = 0;
    public static void main(String[] args) throws IOException {

//        if (args.length != 1) {
//            System.err.println("Usage: java KKMultiServer <port number>");
//            System.exit(1);
//        }

//        ExecutorService executor = Executors.newFixedThreadPool(3);

        int portNumber = 4444; //Integer.parseInt(args[0]);
        boolean listening = true;
        int n_active_threads = 2;

        String pathname;
        File file = new File("./src/Servidor/logs.txt");
        if (!file.exists())
        {
            file.createNewFile();
        }
        ThreadPool threadPool = new ThreadPool(n_active_threads);

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                FileWriter logs = new FileWriter(file.getAbsoluteFile(), true);
                Runnable worker = new multiserverthread(serverSocket.accept(), logs);
                threadPool.execute(worker);
//                threadPool.shutdown();
            }
        }
    }
}