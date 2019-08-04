package Servidor;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class envio_datos extends Thread{
    BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
    private Socket socket = null;

    public envio_datos(Socket socket, BlockingQueue<String> queue) {
        // super("multiserverthread");
        this.socket = socket;
        this.queue = queue;
        //probable haya que incluir mas cosas en lo que recibe
    }

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
        System.out.println("conexion entrante");
        boolean flag = true;
        String message = "";

        int trans_port = findFreePort();
        ServerSocket transmision = new ServerSocket(trans_port);
        transmision.accept();
 
        try{
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(Integer.toString(trans_port));
            out.println("idle");
            while(flag){
                message = queue.poll();
                if(message != null){
                    if(message.contains("play")){
                        String movie = message.split(" ")[1];
                        out.println("streaming");
                        boolean flag1 = true ;
                        while(flag1){
                            out.println("streaming");
                            
                            message = queue.poll();
                            if(message != null){
                                if(message.equals("stop")){
                                    flag1 = false;
                                }
                            }
                        }
                    }
                    if(message.equals("stop")){
                        out.println("stop");
                        out.println("idle");
                    }
                    else if(message.equals("close")){
                        out.println("close");
                    }
                }
            }
        }
        catch(IOException e){
            System.out.println(e);
        }
         
    }
}