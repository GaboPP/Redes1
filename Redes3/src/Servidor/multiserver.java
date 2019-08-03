package Servidor;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class multiserver {
    private static int active_thread = 0;
    private static Object LOCK = new Object();
    public static void main(String[] args) throws IOException {
        
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Escoja una opcion:");
        System.out.println("1- Servidor");
        System.out.println("2- Cliente");

        String opcion_ejec = scanner.readLine();

        boolean listening = true;

        if (opcion_ejec.equals("1")){
            System.out.println("servidor");
            //System.out.println("Cuantos sockets desea usar para transmision de video");
            //get cantidad de sockets
            BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
            
            Thread multimedia = new Thread(new server_multimedia(queue));
            multimedia.start();
            
            synchronized (LOCK) {
                try{
                    LOCK.wait(500);
                }
                catch(InterruptedException e){
                    System.out.println(e);
                }
                System.out.println("Released");
            }
            String message = queue.poll();


            System.out.println("Servidor escuchando en el puerto: " + message);

            //try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
                System.out.println("Escoja una accion:");
                System.out.println("1- Mostrar videos disponibles");
                System.out.println("2- Reproducir Video");
                System.out.println("3- Salir de la App");
                System.out.println("4- Detener reproduccion actual");
                
                while (listening) {
                    //new multiserverthread(serverSocket.accept()).start();
                    BufferedReader scanner_server_opt = new BufferedReader(new InputStreamReader(System.in));
                    String server_opt = scanner_server_opt.readLine();
                    try{
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
                            queue.put("play");
                        }

                        else if(server_opt.equals("3")){ //Salir de la App
                            queue.put("close");
                        }

                        else if(server_opt.equals("4")){ //Detener reproduccion actual
                            queue.put("stop");
                        }
                        else{
                            System.out.println("Ingrese una accion valida");
                        }
                    }
                    catch(InterruptedException e){
                        System.out.println(e);
                    }
                    
                    System.out.println("Escoja una accion:");
                    System.out.println("1- Mostrar videos disponibles");
                    System.out.println("2- Reproducir Video");
                    System.out.println("3- Salir de la App");
                    System.out.println("4- Detener reproduccion actual");

                }
            //}
        }
        
        else if(opcion_ejec.equals("2")){
            BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
            Thread multimedia_client = new Thread(new server_multimedia(queue));
            multimedia_client.start();

            synchronized (LOCK) {
                try{
                    LOCK.wait(500);
                }
                catch(InterruptedException e){
                    System.out.println(e);
                }
                System.out.println("Released");
            }
            String message = queue.poll();

            System.out.println("Cliente as servidor escuchando en el puerto: " + message);

            //System.out.println("cliente");
            

            System.out.println("Ingrese direccion a conectar:");
            BufferedReader scanner_connection = new BufferedReader(new InputStreamReader(System.in));
            String direccion = scanner_connection.readLine();

            System.out.println("Ingrese puerto a conectar:");
            BufferedReader scanner_puerto = new BufferedReader(new InputStreamReader(System.in));
            String puerto = scanner_puerto.readLine();

            int portconnect = Integer.parseInt(puerto);;

            System.out.println("Cliente as servidor escuchando en el puerto: " + puerto);
            Socket conectSocket = new Socket(direccion, portconnect);


            BufferedReader in = new BufferedReader(new InputStreamReader(conectSocket.getInputStream()));
            String mesg;
            while((mesg = in.readLine()) != null){
                System.out.println("Mensaje desde servidor: " + mesg);
            }
            //String mesg = in.readLine();
            //System.out.println("Mensaje desde servidor: " + mesg);

        }
    }
}