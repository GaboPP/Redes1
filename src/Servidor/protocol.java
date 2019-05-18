package Servidor;

import java.io.File;
import java.net.*;
import java.io.*;
import java.time.LocalDateTime;

import org.json.*;

public class protocol {
    private static final int WAITING = 0;
    private static final int validate_u = 1;
    private static final int validate_p = 2;
    private static final int sentcommands = 3;
    private static final int ANOTHER = 4;



    private String pass = "";
    private int state = validate_p;
    private int CurrentCommand = -1;
    private String disponibilidad = "";
    
    String hostNameV1 = "10.6.40.183"; // Maquina 43
    String hostNameV2 = "10.6.40.184"; // Maquina 44
    boolean connectionVM = true;
    //int portNumberV1 = 4448;
    //int portNumberV2 = 4449;
    int portNumber = 4444 ;

    private String[] commands = { "ls", "get", "put", "delete"};

    public Object processInput(String theInput, Socket socket, FileWriter logs) throws IOException {
        
        JSONObject theOutput = new JSONObject();
        String[] directorio;
        if (state == validate_p) {
            try {
                 theOutput.put("message", "Write Command: ");
                 state = sentcommands;
            }
            catch(JSONException e){
                e.getCause();
                System.out.println(e);
            }
        } else if (state == sentcommands) {
            ServerSocket DserverSocket_get = new ServerSocket(4445);
            ServerSocket DserverSocket_put = new ServerSocket(4446);
            System.out.println("sockets creados");
            if (theInput.equalsIgnoreCase("ls")) {




                try (
                    Socket SocketV1 = new Socket(hostNameV1, portNumber);
                    // Socket SocketV2 = new Socket(hostNameV1, portNumber);
                    Socket SocketV2 = new Socket(hostNameV2, portNumber); // Recuerda descomentaaaaaaaaaaaaaaaaaaaar gabrieellllllll!
                ) {
                    System.out.println("VM  conectada");
                    connectionVM = true;
                    disponibilidad = "[VMs activated]";

                    DserverSocket_get.close();
                    DserverSocket_put.close();
                    //System.out.println("ls here");
                    try {
                        logs.append(LocalDateTime.now() + "\t" + " command" + "\t" + socket.getInetAddress() + ":" + socket.getPort() + " ls \n");
                        theOutput.put("message",disponibilidad + " Write Command: ");
                        directorio = listar_directorio();
                        JSONArray dir = new JSONArray(directorio);
                        System.out.println(dir);
                        theOutput.put("response", dir);
                        //System.out.println(theOutput);
                        logs.append(LocalDateTime.now() +"\t" + " response" + "\t" + "servidor envia respuesta a " +socket.getInetAddress() + ":" + socket.getPort()+"\n");
    
                }
                    catch(JSONException e) {
                        e.getCause();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    CurrentCommand = 0;
                    state = sentcommands;
                }catch (IOException e) {
                    logs.append(LocalDateTime.now() + "\t" + " command" + "\t" + socket.getInetAddress() + ":" + socket.getPort() + " ls but VM no connected\n");
                    System.out.println("VM no conectada");
                    connectionVM = false;
                    String [] ficheros = {"Not found files"};
                    disponibilidad = "[Las maquinas NO se encuentran disponibles]";
                    theOutput.put("message",disponibilidad + "\nWrite Command: ");
                    JSONArray dir = new JSONArray(ficheros);
                    System.out.println(dir);
                    theOutput.put("response", dir);
                    CurrentCommand = 0;
                    state = sentcommands;logs.append(LocalDateTime.now() +"\t" + " response" + "\t" + "servidor envia respuesta a " +socket.getInetAddress() + ":" + socket.getPort()+"\n");
    
                }




            } else if (theInput.split(" ")[0].equalsIgnoreCase("get")) {


                
        try (
            Socket SocketV1 = new Socket(hostNameV1, portNumber);
            // Socket SocketV2 = new Socket(hostNameV1, portNumber);
            Socket SocketV2 = new Socket(hostNameV2, portNumber); // Recuerda descomentaaaaaaaaaaaaaaaaaaaar gabrieellllllll!
        ) {
            System.out.println("VM  conectada");
            connectionVM = true;
            disponibilidad = "[VM activated]";
            
            try {
                DserverSocket_put.close();
                
                Socket Dsocket = DserverSocket_get.accept();
                logs.append(LocalDateTime.now() + "\t" + " command" + "\t" + socket.getInetAddress() + ":" + socket.getPort() + " get "+ theInput.split(" ")[1] + "\n" );
                
                System.out.println("get here");
                
                download(theInput.split(" ")[1],Dsocket, theOutput);

                theOutput.put("message"," Write Command: ");
                DserverSocket_get.close();


                logs.append(LocalDateTime.now() + "\t" + " response" + "\t" + "servidor envia respuesta a " + socket.getInetAddress() + ":" + socket.getPort()+ "\n");
            }
            catch(JSONException e) {
               e.getCause();
            } catch (IOException e) {
                e.printStackTrace();
            }
            CurrentCommand = 1;
            state = sentcommands;
        }catch (IOException e) {
            logs.append(LocalDateTime.now() + "\t" + " command" + "\t" + socket.getInetAddress() + ":" + socket.getPort() + " get "+ theInput.split(" ")[1] + " but VM no connected" + "\n" );
            System.out.println("VM no conectada");
            connectionVM = false;
            disponibilidad = "[Las maquinas NO se encuentran disponibles]";
            theOutput.put("message",disponibilidad + "\n Write Command: ");
            CurrentCommand = 1;
            state = sentcommands;
            logs.append(LocalDateTime.now() + "\t" + " response" + "\t" + "servidor envia respuesta a " + socket.getInetAddress() + ":" + socket.getPort()+ "\n");
        }

        } else if (theInput.split(" ")[0].equalsIgnoreCase("put")) {






                
        try (
            Socket SocketV1 = new Socket(hostNameV1, portNumber);
            // Socket SocketV2 = new Socket(hostNameV1, portNumber);
            Socket SocketV2 = new Socket(hostNameV2, portNumber); // Recuerda descomentaaaaaaaaaaaaaaaaaaaar gabrieellllllll!
        ) {
            System.out.println("VM  conectada");
            connectionVM = true;
            disponibilidad = "[VM activated]";
            try {
                DserverSocket_get.close();
                logs.append(LocalDateTime.now() + "\t" + " command" + "\t" + socket.getInetAddress() + ":" + socket.getPort() + " put "+ theInput.split(" ")[1] + "\n" );

                Socket Dsocket = DserverSocket_put.accept();
                byte[] bytearray = new byte[1024];
                int i;
                BufferedInputStream input = new BufferedInputStream(Dsocket.getInputStream());
                DataInputStream dis = new DataInputStream(Dsocket.getInputStream());
                String file = dis.readUTF();
                
                System.out.println("folder = " + System.getProperty("user.dir"));
                BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("./src/Servidor/"+file));
                
                while ((i = input.read(bytearray)) != -1) {
                    
                    output.write(bytearray, 0, i);
                }
                logs.append(LocalDateTime.now() + "\t" + " response" + "\t" + "servidor envia respuesta a " + socket.getInetAddress() + ":" + socket.getPort()+ "\n");
                
                theOutput.put("message"," Write Command: ");
                
                output.close();
                dis.close();
                DserverSocket_put.close();

            }
             catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CurrentCommand = 2;
            state = sentcommands;
        }catch (IOException e) {
            logs.append(LocalDateTime.now() + "\t" + " command" + "\t" + socket.getInetAddress() + ":" + socket.getPort() + " put "+ theInput.split(" ")[1] + "but VM no connected" + "\n" );
            System.out.println("VM no conectada");
            connectionVM = false;
            disponibilidad = "[Las maquinas NO se encuentran disponibles]";
            theOutput.put("message",disponibilidad + "\n Write Command: ");
            CurrentCommand = 2;
            state = sentcommands;
            logs.append(LocalDateTime.now() + "\t" + " response" + "\t" + "servidor envia respuesta a " + socket.getInetAddress() + ":" + socket.getPort()+ "\n");
        }

        } else if (theInput.split(" ")[0].equalsIgnoreCase("delete")) {




                try (
                    Socket SocketV1 = new Socket(hostNameV1, portNumber);
                    // Socket SocketV2 = new Socket(hostNameV1, portNumber);
                    Socket SocketV2 = new Socket(hostNameV2, portNumber); // Recuerda descomentaaaaaaaaaaaaaaaaaaaar gabrieellllllll!
                ) {
                    System.out.println("VM  conectada");
                    connectionVM = true;
                    disponibilidad = "[VM activated]";
                    
                try {
                    DserverSocket_get.close();
                    DserverSocket_put.close();
                    logs.append(LocalDateTime.now() + "\t" + " command" + "\t" + socket.getInetAddress() + ":" + socket.getPort() + " delete "+ theInput.split(" ")[1] + "\n" );
                    
                    String file_name =  theInput.split(" ")[1];
                    String path = "./src/Servidor/index_" + file_name;
                    File file = new File(path);
                    PrintWriter out1 = new PrintWriter(SocketV1.getOutputStream(), true);
                    PrintWriter out2 = new PrintWriter(SocketV2.getOutputStream(), true);

                    if(file.delete()){
                        logs.append(LocalDateTime.now() + "\t" + " response" + "\t" + "servidor envia respuesta a " + socket.getInetAddress() + ":" + socket.getPort()+ "\n");
                        
                        System.out.println(theInput);
                        out1.println(theInput);
                        out2.println(theInput);

                        theOutput.put("message",file_name + " deleted of root directory"+" Write Command: ");System.out.println(file_name + " eliminado del directrio raiz");
                    }else
                        logs.append(LocalDateTime.now() + "\t" + " response" + "\t" + "servidor envia respuesta a " + socket.getInetAddress() + ":" + socket.getPort()+ "\n");
                        
                        theOutput.put("message",file_name + " not found!"+" Write Command: ");
                        


                }
                catch(JSONException e) {
                    e.getCause();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CurrentCommand = 3;
                state = sentcommands;
                }catch (IOException e) {
                    logs.append(LocalDateTime.now() + "\t" + " command" + "\t" + socket.getInetAddress() + ":" + socket.getPort() + " delete "+ theInput.split(" ")[1] +  " but VM no connected " + "\n" );
                    
                    logs.append(LocalDateTime.now() + "\t" + " response" + "\t" + "servidor envia respuesta a " + socket.getInetAddress() + ":" + socket.getPort() + "\n");
                    System.out.println("VM no conectada");
                    connectionVM = false;
                    disponibilidad = "[Las maquinas NO se encuentran disponibles]";
                    theOutput.put("message","file unable to be deleted of root directory"+" Write Command: ");
                    CurrentCommand = 3;
                    state = sentcommands;
                }

            } else {
                try {
                    DserverSocket_get.close();
                    DserverSocket_put.close();
                    theOutput.put("message","Invalid command " +
                            "Try again. Write Command: ");
                }
                catch(JSONException e) {
                    e.getCause();
                }
            }
        } else if (state == ANOTHER) {
            if (theInput.equalsIgnoreCase("y")) {

                try {
                    //System.out.println("client says: yes");
                    theOutput.put("message","Write Command: ");
                }
                catch(JSONException e) {
                    e.getCause();
                }
                CurrentCommand = -1;
                state = sentcommands;
            } else if (theInput.equalsIgnoreCase("n")) {
                try {
                    logs.close();
                    theOutput.put("message","Bye.");
                }
                catch(JSONException e) {
                    e.getCause();
                }
                state = WAITING;
            } else if (theInput.equalsIgnoreCase("gracias")) {
                try {
                    theOutput.put("message","Want another action? (y/n)");
                }
                catch(JSONException e) {
                    e.getCause();
                }
                state = WAITING;
            } else {
                try {
                    //System.out.println("aaaaaa: " + theInput);
                    theOutput.put("message","What do you say? ");
                }
                catch(JSONException e) {
                    e.getCause();
                }
            }
        }
        return theOutput;
    }

    private String[] listar_directorio() {
        File dir = new File("./src/Servidor");
        String [] ficheros = dir.list();
        if (ficheros == null)
            System.out.println("No hay ficheros en el directorio especificado");
        return ficheros;
    }

    private void download( String archivo, Socket socket, JSONObject theOutput){
        try{
            File file = new File("./src/Servidor/"+archivo);
            int i;
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            BufferedOutputStream ou = new BufferedOutputStream(socket.getOutputStream());

            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(file.getName());

            //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            byte [] bytearray = new byte[8192];
            while((i = in.read(bytearray)) != -1){
                ou.write(bytearray,0,i);
            }
            //theOutput.put("ready","Descargando.");
            //out.println(theOutput);

            in.close();
            ou.close();
            socket.close();

        }
        catch (IOException e){
            System.out.println(e);
            e.getCause();
        }
    }
}