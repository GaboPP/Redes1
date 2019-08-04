package Servidor;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class hear_conn implements Runnable{
    BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
    int port = 0;

    public hear_conn(BlockingQueue<String> queue, int port) {
        this.queue = queue;
        this.port = port;
    }

    public void run(){
        boolean flag = true;
        String message;
        try{
            ServerSocket conn_socket = new ServerSocket(port);
            while(flag){
                //message = queue.poll();
                message = "a";
                if(message != null){
                    if(message.equals("close")){
                        flag = false;
                    }
                }
                new envio_datos(conn_socket.accept(), queue).start();
            }
            System.out.println("salio del while");
        }
        catch(IOException e){
            System.out.println(e);
        }
    }
}