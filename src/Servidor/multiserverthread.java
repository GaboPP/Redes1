package Servidor;

import java.net.*;
import java.io.*;

public class multiserverthread extends Thread {
    private Socket socket = null;
    private FileWriter logs;

    int portNumberV1 = 4448;
    int portNumberV2 = 4449;
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
                try (   
                    Socket SocketV1 = new Socket(hostNameV1, portNumberV1);
                    // Socket SocketV2 = new Socket(hostNameV2, portNumberV2);
                    ) 
                {
                    System.out.println("wena choro VM connected");
                } catch (UnknownHostException e) {
                    System.err.println("Don't know about host ");
                } catch (IOException e) {
                    System.err.println("Couldn't get I/O for the connection");
                    connectionVM = false;
                }









            outputLine = protocolo.processInput(null, null, logs, connectionVM);
            out.println(outputLine);
            // System.out.println("Esto llega: " + in.readLine());
            
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine.split(" ")[0]);
                outputLine = protocolo.processInput(inputLine, socket, logs, connectionVM);
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