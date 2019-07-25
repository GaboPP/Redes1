package Servidor;

import java.net.*;
import java.io.*;

public class multiserver {
    private static int active_thread = 0;

    public static void main(String[] args) throws IOException {
        
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Escoja una opcion:");
        System.out.println("1- Servidor");
        System.out.println("2- Cliente");

        String opcion_ejec = scanner.readLine();

        int portNumber = 6666; //Integer.parseInt(args[0]);
        boolean listening = true;

        if (opcion_ejec.equals("1")){
            System.out.println("servidor");
            System.out.println("Servidor escuchando en el puerto 6666");

            try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
                System.out.println("Escoja una accion:");
                System.out.println("1- Mostrar videos disponibles");
                System.out.println("2- Reproducir Video");
                System.out.println("3- Salir de la App");
                System.out.println("4- Detener reproduccion actual");
                
                while (listening) {
                    new multiserverthread(serverSocket.accept()).start();
                    BufferedReader scanner_server_opt = new BufferedReader(new InputStreamReader(System.in));
                    String server_opt = scanner_server_opt.readLine();

                    if(server_opt.equals("1")){ //Mostrar videos disponibles

                        System.out.println("Videos Disponibles:");

                        //System.out.println("working on =" + System.getProperty("user.dir"));

                        File folder = new File("./media");
                        File[] listOfFiles = folder.listFiles();

                        for(int i = 0; i < listOfFiles.length;i++){
                            System.out.println(listOfFiles[i].getName());
                            
                        }
                    }

                    else if(server_opt.equals("2")){ //Reproducir Video
                        BufferedReader scanner_play_video = new BufferedReader(new InputStreamReader(System.in));
                        System.out.println("Escriba nombre de video a reproducir:");
                        String video2play = scanner_play_video.readLine();
                        //Abrir archivo y todo el tema para transmitirlo
                    }

                    else if(server_opt.equals("3")){ //Salir de la App
                        
                    }

                    else if(server_opt.equals("4")){ //Detener reproduccion actual
                        
                    }
                    else{
                        System.out.println("Ingrese una accion valida");
                    }
                    System.out.println("Escoja una accion:");
                    System.out.println("1- Mostrar videos disponibles");
                    System.out.println("2- Reproducir Video");
                    System.out.println("3- Salir de la App");
                    System.out.println("4- Detener reproduccion actual");

                }
            }
        }
        
        else if(opcion_ejec.equals("2")){
            System.out.println("cliente");
        }
    }
}