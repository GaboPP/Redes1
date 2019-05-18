package Servidor;

import java.net.*;
import java.io.*;

public class multiserverthread extends Thread {
    private Socket socket = null;
    private FileWriter logs;

    //int portNumberV1 = 4448;
    //int portNumberV2 = 4449;
    int portNumber = 4444 ;
    String hostNameV1 = "10.6.40.183"; // Maquina 43
    String hostNameV2 = "10.6.40.184"; // Maquina 44
    boolean connectionVM = true;

    public multiserverthread(Socket socket, FileWriter logs) {
        // super("multiserverthread");
        this.socket = socket;
        this.logs = logs;
        System.out.println(this.socket.getRemoteSocketAddress().toString() + " Conexion entrante");
    }

    public void run() {

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
            String inputLine;
            Object outputLine;
            protocol protocolo = new protocol();



            
            // Conect to VMs!
                

            Socket SocketV1 = new Socket(hostNameV1, portNumber);
            Socket SocketV2 = new Socket(hostNameV2, portNumber);

            //falta hacer la condicion en caso que uno de los sockets no conecte

            outputLine = protocolo.processInput(null, null, logs, connectionVM, SocketV1, SocketV2);
            out.println(outputLine);
            // System.out.println("Esto llega: " + in.readLine());
            
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine.split(" ")[0]);
                outputLine = protocolo.processInput(inputLine, socket, logs, connectionVM, SocketV1, SocketV2);
                out.println(outputLine);


                if (outputLine.equals("Bye"))
                    break;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}