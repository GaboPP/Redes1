package Servidor;

import java.io.File;
import java.net.*;
import java.io.*;
import org.json.*;

public class protocol {
    private static final int WAITING = 0;
    private static final int validate_u = 1;
    private static final int validate_p = 2;
    private static final int sentcommands = 3;
    private static final int ANOTHER = 4;



    private String pass = "";
    private int state = WAITING;
    private int CurrentCommand = -1;

    private String[] commands = { "ls", "get", "put", "delete"};

    public Object processInput(String theInput, Socket socket,FileWriter logs) throws IOException {

        JSONObject theOutput = new JSONObject();
        String[] directorio;
        System.out.println(theInput);
        if (state == WAITING) {
            try {
                theOutput.put("message","***Area 51***//> insert username/password: ");
//                theOutput.put("message","Write Command: ");
            }
            catch(JSONException e) {
                e.getCause();
            }
            state = validate_u;
        } else if (state == validate_u) {
            try {
                String credentials;
                String username;
                String password;
                BufferedReader validate_file = new BufferedReader(new FileReader("./src/Servidor/login.txt"));
                while((credentials = validate_file.readLine())!= null){
                    System.out.println("st: ");
                    System.out.println(credentials);

//                    if (theInput.contains("/")) {
                    username = theInput.split("/")[0];
                    password = theInput.split("/")[1];
                    if (username.equalsIgnoreCase(credentials.split(":")[0]) && password.equalsIgnoreCase(credentials.split(":")[1])) {
                        theOutput.put("message","Done!");
                        state = validate_p;
                        validate_file.close();
                        logs.append("Fecha" + " Connection" + "11111 concección entrante\n");

                    }
                    else {
                        theOutput.put("message","//> Wrong USER or PASS we see you/// username/password:  ");
                        state = validate_u;
                        validate_file.close();
                    }
                }
            }
            catch (IOException e){
                e.getCause();
                System.out.println(e);
            }
            catch(JSONException e) {
                e.getCause();
                System.out.println(e);
            }

        } else if (state == validate_p) {
            try {
                 theOutput.put("message", "Write Command: ");
                 state = sentcommands;
            }
            catch(JSONException e){
                e.getCause();
                System.out.println(e);
            }
        } else if (state == sentcommands) {
            if (theInput.equalsIgnoreCase("ls")) {
                System.out.println("ls here");
                try {
                    logs.append("Fecha" + " Command" + "1.1.1. ls");
                    theOutput.put("message","Want another action? (y/n)");
                    directorio = listar_directorio();
                    JSONArray dir = new JSONArray(directorio);
                    theOutput.put("response", dir);
                    System.out.println(theOutput);
                    logs.append("Fecha" + " response" + "servidor envia respuesta a 1.1.1.1");
                }
                catch(JSONException e) {
                    e.getCause();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CurrentCommand = 0;
                state = ANOTHER;
            } else if (theInput.split(" ")[0].equalsIgnoreCase("get")) {
                try {
                    logs.append("Fecha" + " Command" + "1.1.1. get" + "archivo X");
                    ServerSocket DserverSocket = new ServerSocket(4445);
                    Socket Dsocket = DserverSocket.accept();
                    System.out.println("get here");
                    download(theInput.split(" ")[1],Dsocket, theOutput);
                    theOutput.put("message","Want another action? (y/n)");
                    DserverSocket.close();


                    System.out.println("download end");

                    logs.append("Fecha" + " response" + "servidor envia respuesta a 1.1.1.1");
                }
                catch(JSONException e) {
                   e.getCause();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CurrentCommand = 1;
                state = ANOTHER;
            } else if (theInput.split(" ")[0].equalsIgnoreCase("put")) {

                try {
                    logs.append("Fecha" + " Command" + "1.1.1. put" + "archivo X");
                    ServerSocket DserverSocket = new ServerSocket(4446);
                    Socket Dsocket = DserverSocket.accept();
                    byte[] bytearray = new byte[1024];
                    int i;
                    BufferedInputStream input = new BufferedInputStream(Dsocket.getInputStream());
                    DataInputStream dis = new DataInputStream(Dsocket.getInputStream());
                    String file = dis.readUTF();
                    System.out.println("file = "+file);
                    BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("./src/Servidor/"+file));
                    System.out.println("hola");
                    while ((i = input.read(bytearray)) != -1) {
                        System.out.println("i = "+i);
                        output.write(bytearray, 0, i);
                    }
                    logs.append("Fecha" + " response" + "servidor envia respuesta a 1.1.1.1");
                    theOutput.put("message","Want another action? (y/n)");
                    System.out.println(i);
                    output.close();
                    dis.close();
                    DserverSocket.close();

                }
                 catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CurrentCommand = 2;
                state = ANOTHER;
            } else if (theInput.split(" ")[0].equalsIgnoreCase("delete")) {
                try {
                    logs.append("Fecha" + " Command" + "1.1.1. delete" + "archivo X");
                    String file_name =  theInput.split(" ")[1];
                    String path = "./src/Servidor/" + file_name;
                    File file = new File(path);
                    if(file.delete()){
                        logs.append("Fecha" + " response" + "servidor envia respuesta a 1.1.1.1");
                        theOutput.put("message",file_name + " deleted of root directory"+" Want another action? (y/n)");System.out.println(file_name + " eliminado del directrio raiz");
                    }else logs.append("Fecha" + " response" + "servidor envia respuesta a 1.1.1.1"); theOutput.put("message",file_name + " not found!"+" Want another action? (y/n)");System.out.println(file_name + " este archivo no existe!");


                }
                catch(JSONException e) {
                    e.getCause();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CurrentCommand = 3;
                state = ANOTHER;
            } else {
                try {
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
                    System.out.println("client says: yes");
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
                    System.out.println("aaaaaa: " + theInput);
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