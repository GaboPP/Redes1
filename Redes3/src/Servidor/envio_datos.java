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

    public void run(){
        System.out.println("conexion entrante");
        boolean flag = true;
        String message = "";
        
        try{
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("idle");
            while(flag){
                message = queue.poll();
                if(message != null){
                    if(message.equals("play")){
                        out.println("streaming");
                    }
                    else if(message.equals("stop")){
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