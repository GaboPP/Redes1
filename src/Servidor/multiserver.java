package Servidor;

import java.net.*;
import java.io.*;

public class multiserver {
    private static int active_thread = 0;
    public static void main(String[] args) throws IOException {

        int portNumber = 4444; //Integer.parseInt(args[0]);
        boolean listening = true;

        File file = new File("./src/Servidor/logs.txt");
        if (!file.exists())
        {
            file.createNewFile();
        }

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                FileWriter logs = new FileWriter(file.getAbsoluteFile(), true);
                new multiserverthread(serverSocket.accept(), logs).start();
            }
        }
    }
}