package Cliente;

import java.io.*;
import java.net.*;;
import org.json.*;

public class client {
    public static void main(String[] args) throws IOException {

//        if (args.length != 2) {
//            System.err.println(
//                    "Usage: java EchoClient <host name> <port number>");
//            System.exit(1);
//        }

        String hostName =  "0.0.0.0"; //args[0];
        int portNumber = 4444; //Integer.parseInt(args[1]);

        try (
                Socket kkSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(kkSocket.getInputStream()));
        ) {
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
            JSONObject fromServer;
            String fromUser;
            JSONArray response;
            String message;
            String file_line;

            try {
                fromServer = new JSONObject(in.readLine());
                while (!fromServer.getString("message").equals("Bye.")) {
                    if (fromServer.opt("response") != null) {
                            response = fromServer.getJSONArray("response");
                        System.out.println("ServerRR: " + response);
                    }
                    message = fromServer.getString("message");
                    System.out.println("Server1: " + message);

//                    if (fromServer.equals("Bye."))
//                        break;

                    fromUser = stdIn.readLine();
                    System.out.println(fromUser);
                    System.out.println(fromServer);
                    if (fromUser != null) {
                        System.out.println("Client: " + fromUser);
                        out.println(fromUser);
                        System.out.println(fromUser.split(" ")[0]);
                        if(fromUser.split(" ")[0].equals("get")){ //si el cliente envia get
                            fromServer = new JSONObject(in.readLine());
                            String file = fromUser.split(" ")[1];  //obtengo el nombre del archivo
                            BufferedWriter writer = null;
                            System.out.println("llegamos2");
                            System.out.println(fromServer);
                            while (!fromServer.getString("ready").equals("Done.")) {  //copio proceso de lectura desde el server para capturar cada linea del archivo
                                fromServer = new JSONObject(in.readLine());
                                System.out.println("llegamoshile");
                                if (fromServer.opt("line_file") != null) {
                                    System.out.println("llegamos3");

                                    file_line = fromServer.getString("line_file");
                                    System.out.println("Servertxt: " +  file_line);
                                    writer = new BufferedWriter(new FileWriter(file));  //creo archivo y lo dejo listo para escribir
                                    writer.write(file_line); //escribo linea
                                }
                            }
                            writer.close();  //cierro archivo o quizas puntero, no estoy seguro xD
                        }
                    }
                    fromServer = new JSONObject(in.readLine());
            }} catch(JSONException e) {
                System.out.println((e));
                e.getCause();
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}