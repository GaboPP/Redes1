package Servidor;

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

    public Object processInput(String theInput, Socket socket) {

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
                    theOutput.put("message","Want another action? (y/n)");
                    directorio = listar_directorio();
                    JSONArray dir = new JSONArray(directorio);
                    theOutput.put("response", dir);
                    System.out.println(theOutput);
                }
                catch(JSONException e) {
                    e.getCause();
                }
                CurrentCommand = 0;
                state = ANOTHER;
            } else if (theInput.split(" ")[0].equalsIgnoreCase("get")) {

                try {
                    System.out.println("get here");
                    download(theInput.split(" ")[1],socket,theOutput);
                    theOutput.put("ready","Done.");
                    theOutput.put("message","Want another action? (y/n)");
                    System.out.println(theOutput);
                }
                catch(JSONException e) {
                    e.getCause();
                }
                CurrentCommand = 1;
                state = ANOTHER;
            } else if (theInput.equalsIgnoreCase("put")) {

                try {
                    theOutput.put("message","Want another action? (y/n)");
                }
                catch(JSONException e) {
                    e.getCause();
                }
                CurrentCommand = 2;
                state = ANOTHER;
            } else if (theInput.equalsIgnoreCase("delete")) {
                try {
                    theOutput.put("message","Want another action? (y/n)");
                }
                catch(JSONException e) {
                    e.getCause();
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
                    theOutput.put("message","Write Command: ");
                }
                catch(JSONException e) {
                    e.getCause();
                }
                CurrentCommand = -1;
                state = sentcommands;
            } else if (theInput.equalsIgnoreCase("n")) {
                try {
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

    private void download( String archivo, Socket socket, JSONObject file_shard){
        try{
            File file = new File(archivo);
            BufferedReader br_aux = new BufferedReader(new FileReader("./src/Servidor/"+file));
            String st;
            int count_lines = 1;
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            long n_lines = br_aux.lines().count();
            br_aux.close();
            BufferedReader br = new BufferedReader(new FileReader("./src/Servidor/"+file));
            while((st = br.readLine())!= null){
                System.out.println("st: ");
                System.out.println(st);
                file_shard.put("line_file",st);
                file_shard.put("ready", "not done");
                file_shard.put("message", "Downloading: " + 100*(count_lines/n_lines) + "%");
                out.println(file_shard);
            }
        }
        catch (IOException e){
            System.out.println(e);
            e.getCause();
        }
        catch (JSONException e){
            System.out.println(e);
            e.getCause();
        }
    }
}