/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadsandsockets_ex1turnstyleserver;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author simon
 */
public class ThreadsAndSockets_ex1TurnStyleServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java ThreadsAndSockets_ex1TurnStyleServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));

        while (true) {
            new TurnStyleServerTask(serverSocket.accept()).start();

        }
    }
}
