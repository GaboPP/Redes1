package Servidor;

import java.net.*;
import java.io.*;

public class server_multimedia extends Thread{
    private Socket socket = null;


    //aqui hay que hacer el trabajo de sockets para que se mantenga escuchando por conexiones
    //idealmente usar todo este codigo para la implementacion del cliente como servidor


    public void run() {
        int i = 0;
        for(i = 0; i < 3; i++){
            System.out.println(i);
        }
    }


}