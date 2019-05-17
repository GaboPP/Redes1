package Cliente;

import java.io.*;
import java.net.*;
import org.json.*;
import java.util.concurrent.TimeUnit;

public class client {
    public static void main(String[] args) throws IOException {


        String hostName =  "10.6.40.183"; //"10.6.43.187"; //args[0]; //Maquina 43 = "10.6.40.183" //Maquina 44 "10.6.40.184"
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
                    //System.out.println("empezamos denuevo");
                    if (fromServer.opt("response") != null) {
                        response = fromServer.getJSONArray("response");
                        System.out.println("Server: " + response);
                    }
                    if (fromServer.opt("message").equals("Done!")){
                        out.println("Arigato!");
                        message = fromServer.getString("message");
                        System.out.println("Server: " + message);
                        fromServer = new JSONObject(in.readLine());
                    }
                    message = fromServer.getString("message");
                    System.out.println("Server: " + message);

                    fromUser = stdIn.readLine();
                    boolean flag = true;

                    if (fromUser != null) {
//                        System.out.println("Client: " + fromUser);
                        out.println(fromUser);
                        TimeUnit.SECONDS.sleep(1);
                        if(fromUser.split(" ")[0].equals("get")){ //si el cliente envia get

                            Socket DSocket = new Socket(hostName, 4445);
                            //System.out.println("aqui");
                            byte[] bytearray = new byte[1024];
                            int i;
                            BufferedInputStream input = new BufferedInputStream(DSocket.getInputStream());
                            DataInputStream dis = new DataInputStream(DSocket.getInputStream());
                            String file = dis.readUTF();
                            //System.out.println("file = "+file);
                            System.out.println("folder = " + System.getProperty("user.dir"));
                            File newfile = new File(file);
                            newfile.createNewFile();
                            FileOutputStream ffile = new FileOutputStream(newfile);
                            BufferedOutputStream output = new BufferedOutputStream(ffile);
                            //System.out.println("hola");
                            while ((i = input.read(bytearray)) != -1) {
                                //System.out.println("i = "+i);
                                output.write(bytearray, 0, i);
                            }
                            //System.out.println(i);
                            output.close();
                            dis.close();
                            DSocket.close();
                        }
                        if(fromUser.split(" ")[0].equals("put")) { //si el cliente envia get
                            
                            Socket DSocket = new Socket(hostName, 4446);
                            
                            String archivo = fromUser.split(" ")[1];
                            
                            try{
                                File file = new File(archivo);
                                System.out.println(file);
                                int i;
                                BufferedInputStream inp = new BufferedInputStream(new FileInputStream(file));
                                //System.out.println(file);
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
                    //System.out.println("some = "+some);
                    fromServer = new JSONObject(some);
                    //System.out.println("FromServer1: " + fromServer);
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
            e.printStackTrace();   
            System.exit(1);
        }
        catch(InterruptedException e){
            e.printStackTrace();   
        }
    }
}