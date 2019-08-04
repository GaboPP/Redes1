package Servidor;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class multiserver {
    private static int active_thread = 0;
    private static Object LOCK = new Object();

    private static int findFreePort() {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(0);
			socket.setReuseAddress(true);
			int port = socket.getLocalPort();
			try {
				socket.close();
			} catch (IOException e) {
				// Ignore IOException on close()
			}
			return port;
		} catch (IOException e) { 
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
		throw new IllegalStateException("Could not find a free TCP/IP port to start embedded Jetty HTTP Server on");
	}

    public static void main(String[] args) throws IOException {
        
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Escoja una opcion:");
        System.out.println("1- Servidor");
        System.out.println("2- Cliente");

        String opcion_ejec = scanner.readLine();

        boolean listening = true;

        if (opcion_ejec.equals("1")){
            BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
            
            Thread hear_conn = new Thread(new hear_conn(queue,6666));
            hear_conn.start();
            
            /*synchronized (LOCK) {
                try{
                    LOCK.wait(500);
                }
                catch(InterruptedException e){
                    System.out.println(e);
                }
                System.out.println("Released");
            }
            String message = queue.poll();*/


            System.out.println("Servidor escuchando en el puerto: 6666");

            //try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
                System.out.println("Escoja una accion:");
                System.out.println("1- Mostrar videos disponibles");
                System.out.println("2- Reproducir Video");
                System.out.println("3- Salir de la App");
                System.out.println("4- Detener reproduccion actual");
                boolean flag = true;
                while (flag) {
                    //new multiserverthread(serverSocket.accept()).start();
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
                        try{
                            queue.put("play");
                        }
                        catch(InterruptedException e){
                            System.out.println(e);
                        }
                    }

                    else if(server_opt.equals("3")){ //Salir de la App
                        //queue.put("close");
                        try{
                            queue.put("close");
                        }
                        catch(InterruptedException e){
                            System.out.println(e);
                        }
                    }

                    else if(server_opt.equals("4")){ //Detener reproduccion actual
                        try{
                            queue.put("stop");
                        }
                        catch(InterruptedException e){
                            System.out.println(e);
                        }
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
            //}
        }
        
        else if(opcion_ejec.equals("2")){
            BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
            int port = findFreePort();
            Thread hear_conn = new Thread(new hear_conn(queue, port));
            hear_conn.start();

            /*synchronized (LOCK) {
                try{
                    LOCK.wait(500);
                }
                catch(InterruptedException e){
                    System.out.println(e);
                }
                System.out.println("Released");
            }
            String message = queue.poll();*/

            System.out.println("Cliente as servidor escuchando en el puerto: " + port);

            //System.out.println("cliente");
            

            System.out.println("Ingrese direccion a conectar:");
            BufferedReader scanner_connection = new BufferedReader(new InputStreamReader(System.in));
            String direccion = scanner_connection.readLine();

            System.out.println("Ingrese puerto a conectar:");
            BufferedReader scanner_puerto = new BufferedReader(new InputStreamReader(System.in));
            String puerto = scanner_puerto.readLine();

            int portconnect = Integer.parseInt(puerto);;

            //System.out.println("Cliente as servidor escuchando en el puerto: " + puerto); esto creo que estaba mal
            Socket conectSocket = new Socket(direccion, portconnect);


            BufferedReader in = new BufferedReader(new InputStreamReader(conectSocket.getInputStream()));
            String mesg;
            while((mesg = in.readLine()) != null){
                System.out.println("Mensaje desde servidor: " + mesg);
            }
            System.out.println("mesg era null");
            //String mesg = in.readLine();
            //System.out.println("Mensaje desde servidor: " + mesg);

        }
    }
}