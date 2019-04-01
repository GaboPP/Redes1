import java.net.*;
import java.io.*;

public class multiserverthread implements Runnable {
    private Socket socket = null;

    public multiserverthread(Socket socket) {
//        super("multiserverthread");
        this.socket = socket;
        System.out.println(this.socket.getRemoteSocketAddress().toString() + " Conexión entrante");
    }

    public void run() {

        try (
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
        ) {
            String inputLine;
            Object outputLine;
            protocol protocolo = new protocol();
            outputLine = protocolo.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = protocolo.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye"))
                    break;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}