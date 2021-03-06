package Servidor;

import java.io.File;
import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import java.io.PrintStream;
import java.util.Base64;

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

    private String[] commands = { "ls", "get", "put", "delete"};

    public Object processInput(String theInput, Socket socket,FileWriter logs) throws IOException {

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
            } else if (theInput.split(" ")[0].equalsIgnoreCase("get")) {
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
            } else if (theInput.split(" ")[0].equalsIgnoreCase("put")) {

                try {
                    System.out.println("escribiendo linea");
                    DserverSocket_get.close();
                    logs.append(LocalDateTime.now() + "\t" + " command" + "\t" + socket.getInetAddress() + ":" + socket.getPort() + " put "+ theInput.split(" ")[1] + "\n" );

                    Socket Dsocket = DserverSocket_put.accept();
                    //String file = theInput.split(" ")[1];
                    
                    
                    System.out.println("linea escrita");

                    byte[] bytearray = new byte[65536];
                    int i;
                    BufferedInputStream input = new BufferedInputStream(Dsocket.getInputStream());
                    DataInputStream dis = new DataInputStream(Dsocket.getInputStream());
                    String file = dis.readUTF();
                    
                    //System.out.println("folder = " + System.getProperty("user.dir"));
                    BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("./src/Servidor/"+file, true));
                    
                    //FileWriter output = new FileWriter("./src/Servidor/"+file, true);

                    while ((i = input.read(bytearray)) != -1) {
                        //byte[] encoded = Base64.decode(bytearray);
                        String s = Base64.getEncoder().encodeToString(bytearray);
                        //output.write(s);
                        byte[] encoded = Base64.getDecoder().decode(s.getBytes());
                        output.write(encoded,0,65536);
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
                }catch (Exception e) {
				      e.printStackTrace();
				      System.err.println("aFile");
				    }
                CurrentCommand = 2;
                state = sentcommands;
            } else if (theInput.split(" ")[0].equalsIgnoreCase("delete")) {
                try {
                	System.out.println(theInput);
                    DserverSocket_get.close();
                    DserverSocket_put.close();
                    logs.append(LocalDateTime.now() + "\t" + " command" + "\t" + socket.getInetAddress() + ":" + socket.getPort() + " delete "+ theInput.split(" ")[1] + "\n" );
                    
                    String file_name =  theInput.split(" ")[1];
                    String path = "./src/Servidor/" + file_name;
                    File file = new File(path);
                    if(file.delete()){
                        logs.append(LocalDateTime.now() + "\t" + " response" + "\t" + "servidor envia respuesta a " + socket.getInetAddress() + ":" + socket.getPort()+ "\n");
                        
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