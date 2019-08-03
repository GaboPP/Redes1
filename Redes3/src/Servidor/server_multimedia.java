package Servidor;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class server_multimedia implements Runnable{
    private Socket socket = null;

    BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    server_multimedia(BlockingQueue<String> queue) {
        this.queue = queue;
    }


    //aqui hay que hacer el trabajo de sockets para que se mantenga escuchando por conexiones
    //idealmente usar todo este codigo para la implementacion del cliente como servidor

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

    public void run(){
        int puerto = findFreePort();
        //System.out.println("El puerto libre encontrado es:" + puerto);
        //BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
        try{
            queue.put(Integer.toString(puerto));
        }
        catch(InterruptedException e){
            System.out.println(e);
        }
        synchronized (this) {
            notifyAll(); 
        } 
        int portmult = puerto;
        try (ServerSocket serverSocket = new ServerSocket(portmult)) {
            
            while(true){
                Socket serv = serverSocket.accept();
                int i = 0;
                for(i = 0; i < 3; i++){
                    System.out.println(i);
                }
                PrintWriter out = new PrintWriter(serv.getOutputStream(), true);
                out.println("Hola");
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

}