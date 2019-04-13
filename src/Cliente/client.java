package Cliente;

import java.io.*;
import java.net.*;
import org.json.*;

public class client {
    public static void main(String[] args) throws IOException {

        String hostName =  "0.0.0.0"; //"10.6.43.187"; //args[0];
        int portNumber = 4444; //Integer.parseInt(args[1]);

        try (
                Socket kkSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()))
        ) {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            JSONObject fromServer;
            String fromUser;
            JSONArray response;
            String message;
            String file_line;
            String some;
            try {
                fromServer = new JSONObject(in.readLine());
                while (!fromServer.getString("message").equals("Bye.")) {
//                    System.out.println("empezamos denuevo");
                    if (fromServer.opt("response") != null) {
                        response = fromServer.getJSONArray("response");
                        System.out.println("ServerRR: " + response);
                    }
                    if (fromServer.opt("message").equals("Done!")){
                        out.println("Arigato!");
                        message = fromServer.getString("message");
                        System.out.println("Server: " + message);
                        fromServer = new JSONObject(in.readLine());
                    }
                    message = fromServer.getString("message");
                    System.out.println("Server: " + message);

//                    if (fromServer.equals("Bye."))
//                        break;

                    fromUser = stdIn.readLine();
                    boolean flag = true;

                    if (fromUser != null) {
//                        System.out.println("Client: " + fromUser);
                        out.println(fromUser);
                        if(fromUser.split(" ")[0].equals("get")){ //si el cliente envia get

                            Socket DSocket = new Socket(hostName, 4445);
                            System.out.println("aqui");
                            byte[] bytearray = new byte[1024];
                            int i;
                            BufferedInputStream input = new BufferedInputStream(DSocket.getInputStream());
                            DataInputStream dis = new DataInputStream(DSocket.getInputStream());
                            String file = dis.readUTF();
                            System.out.println("file = "+file);
                            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("./src/Cliente/"+file));
                            System.out.println("hola");
                            while ((i = input.read(bytearray)) != -1) {
                                System.out.println("i = "+i);
                                output.write(bytearray, 0, i);
                            }
                            System.out.println(i);
                            output.close();
                            dis.close();
                            DSocket.close();
                        }
                        if(fromUser.split(" ")[0].equals("put")) { //si el cliente envia get
                            System.out.println("hola");
                            Socket DSocket = new Socket(hostName, 4446);
                            System.out.println("hola");
                            String archivo = fromUser.split(" ")[1];
                            System.out.println(archivo);
                            try{
                                File file = new File("./src/Cliente/"+archivo);
                                System.out.println(file);
                                int i;
                                BufferedInputStream inp = new BufferedInputStream(new FileInputStream(file));
                                System.out.println(file);
                                BufferedOutputStream ou = new BufferedOutputStream(DSocket.getOutputStream());

                                DataOutputStream output = new DataOutputStream(DSocket.getOutputStream());
                                output.writeUTF(file.getName());

                                //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                                byte [] bytearray = new byte[8192];
                                while((i = inp.read(bytearray)) != -1){
                                    ou.write(bytearray,0,i);
                                }
                                //theOutput.put("ready","Descargando.");
                                //out.println(theOutput);

                                inp.close();
                                ou.close();
                                DSocket.close();

                            }
                            catch (IOException e){
                                System.out.println(e);
                                e.getCause();
                            }
                            DSocket.close();
                        }
                    }
                    some = in.readLine();
                    System.out.println("some = "+some);
                    fromServer = new JSONObject(some);
                    System.out.println("FromServer1: " + fromServer);
                }
            }
            catch(JSONException e) {
                System.out.println((e));
                e.getCause();
            }
        }
        catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}