
import java.net.*;
import java.io.*;

public class protocol {
    private static final int WAITING = 0;
    private static final int sentcommands = 1;
    private static final int SentAction = 2;
    private static final int ANOTHER = 3;


    private int state = WAITING;
    private int CurrentCommand = -1;

    private String[] commands = { "ls", "get", "put", "delete"};

    public String processInput(String theInput) {
        String theOutput = null;

        if (state == WAITING) {
            theOutput = "Write Command: ";
            state = sentcommands;
        } else if (state == sentcommands) {
            if (theInput.equalsIgnoreCase("ls")) {
                listar_directorio();
                theOutput = " Want another action? (y/n)";
                CurrentCommand = 0;
                state = ANOTHER;
            } else if (theInput.equalsIgnoreCase("get")) {
                theOutput = " Want another action? (y/n)";
                CurrentCommand = 1;
                state = ANOTHER;
            } else if (theInput.equalsIgnoreCase("put")) {
                theOutput = " Want another action? (y/n)";
                CurrentCommand = 2;
                state = ANOTHER;
            } else if (theInput.equalsIgnoreCase("delete")) {
                theOutput = " Want another action? (y/n)";
                CurrentCommand = 3;
                state = ANOTHER;
            } else {
                theOutput = "Invalid command " +
                        "Try again. Write Command: ";
            }
        } else if (state == ANOTHER) {
            if (theInput.equalsIgnoreCase("y")) {
                theOutput = "Write command: ";
                CurrentCommand = -1;
                state = sentcommands;
            } else if (theInput.equalsIgnoreCase("n")) {
                theOutput = "Bye.";
                state = WAITING;
            } else {
                theOutput = "What do you say? ";
            }
        }
        return theOutput;
    }

    private String[] listar_directorio() {
        File dir = new File("./");
        String [] ficheros = dir.list();
        if (ficheros == null)
            System.out.println("No hay ficheros en el directorio especificado");
        return ficheros;
    }
}