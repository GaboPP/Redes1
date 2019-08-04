package Servidor;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class receive_mult implements Runnable{
    BlockingQueue<String>queue = new new LinkedBlockingQueue<String>();;
    int port = port;
    String host = host;

    public receive_mult(BlockingQueue<String> queue, int port, String host) {
        this.queue = queue;
        this.port = port;
        this.host = host;
    }

    public static void descargar(String filename, String host, int puerto){
        String path = System.getProperty("user.dir");
        //File file = new File(path + "/" + filename);
        int fileSize = 64*1024*1024; //16 mega bytes
        int bytesRead;
        int current = 0;
        //int puerto = 5900;
        Socket socketCliente = null;

        try {
            socketCliente = new Socket(host, puerto);
            System.out.println (host);
            BufferedReader br = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            String confirmacion = br.readLine();

            if((confirmacion.equals("ERROR"))){
                System.out.println("El archivo no esta disponible en estos momentos, intentelo mas tarde");
                socketCliente.close();
                return;
            }

            System.out.println(confirmacion);
            byte []byteArray = new byte [fileSize];
            InputStream inputStream = socketCliente.getInputStream();
            FileOutputStream fileOutput = new FileOutputStream(path + "/" + filename);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
            bytesRead = inputStream.read(byteArray, 0, byteArray.length);
            current = bytesRead;

            do{
                bytesRead = inputStream.read(byteArray, current, (byteArray.length - current));
                if (bytesRead >= 0) current += bytesRead;
            }while(bytesRead > -1);

            bufferedOutput.write(byteArray, 0, current);
            bufferedOutput.flush();
            System.out.println("DESCARGA COMPLETA");

            inputStream.close();
            fileOutput.close();
            bufferedOutput.close();
            socketCliente.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        boolean flag = true;
        String message = "";
        while(flag){
            message = queue.poll();
            if(message != null){
                String[] partes = message.split(" ");
                String filename = partes[1];
                if(partes[0].equals("streaming")){
                    descargar(filename, host, port)
                }
            }
        }
    }
}
