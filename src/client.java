
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

            try {
                fromServer = new JSONObject(in.readLine());
                while (!fromServer.getString("message").equals("Bye.")) {
                    if (fromServer.opt("response") != null) {
                        response = fromServer.getJSONArray("response");
                        System.out.println("Server: " + response);
                    }
                    message = fromServer.getString("message");
                    System.out.println("Server: " + message);

//                    if (fromServer.equals("Bye."))
//                        break;

                    fromUser = stdIn.readLine();
                    if (fromUser != null) {
                        System.out.println("Client: " + fromUser);
                        out.println(fromUser);
                        if(fromUser.split(" ")[0] == "get"){ //si el cliente envia get

                            String file = fromUser.split(" ")[1];  //obtengo el nombre del archivo
                            BufferedWriter writer = null;
                            while (!fromServer.getString("message").equals("Done.")) {  //copio proceso de lectura desde el server para capturar cada linea del archivo
                                if (fromServer.opt("response") != null) {
                                    response = fromServer.getJSONArray("response");
                                    System.out.println("Server: " + response);
                                    writer = new BufferedWriter(new FileWriter(file));  //creo archivo y lo dejo listo para escribir
                                    writer.write(fromServer.getString("message")); //escribo linea
                                }
                                message = fromServer.getString("message");
                            }
                            writer.close();  //cierro archivo o quizas puntero, no estoy seguro xD
                        }
                    }
                    fromServer = new JSONObject(in.readLine());
            }} catch(JSONException e) {
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