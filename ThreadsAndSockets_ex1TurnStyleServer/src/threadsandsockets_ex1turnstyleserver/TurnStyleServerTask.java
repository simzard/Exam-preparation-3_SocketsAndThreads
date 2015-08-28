/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadsandsockets_ex1turnstyleserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simon
 */
public class TurnStyleServerTask extends Thread {

    private static List<PrintWriter> outs = new ArrayList();

    private static int idCounter = 0;

    private Socket clientSocket;
    private int number;

    public int getNumber() {
        return number;
    }

    private static volatile int spectators = 0;

    private static TurnStyleProtocol tsp = new TurnStyleProtocol();
    
    public static void incSpectators(int count) {
        spectators += count;
    }

    public static int getSpectators() {
        return spectators;
    }

    public TurnStyleServerTask(Socket s) {
        clientSocket = s;
        number = idCounter++;
        // when making a task increment the output streams by one
        
        try {
            outs.add(new PrintWriter(s.getOutputStream(), true));
            //ins.add(new BufferedReader(new InputStreamReader(allSockets.get(i).getInputStream())));

        } catch (IOException ex) {
            Logger.getLogger(TurnStyleServerTask.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    // send the message to all the connected clients
    public void printAll(String string) {
        for (PrintWriter os : outs) {
            os.println(string);
        }
    }

    public void run() {

        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));) {
            String inputLine;
            String outputLine = null;
            

            out.println("Enter TURNSTYLE or MONITOR");
            while ((inputLine = in.readLine()) != null) {

                outputLine = tsp.processInput(inputLine);
                if (outputLine.equals("QUIT")) {
                    out.println("Bad info - Quitting...");
                    return;
                } else if (outputLine.equals("TOTALAMOUNT")) {
                    int totalSpectators = getSpectators();
                    out.println("Total amount of spectators: " + totalSpectators);
                    return;
                } else if (outputLine.equals("WAITFORID")) {
                    out.println("Enter id in the form T-n , where n is an int");
                } else if (outputLine.equals("IDEXISTS")) {
                    out.println("ID is allready connected try another!");
                }
                
                else {
                    String[] tokens = outputLine.split(" ");
                    
                    System.out.println("token[0]: " + tokens[0] + " tokens[1]:" + tokens[1]);
                    if (tokens[0].equals("INC")) {
                        out.println("Incrementing by " + tokens[1]);
                        incSpectators(Integer.parseInt(tokens[1]));
                    } else if (tokens[0].equals("ID")) {
                        out.println(outputLine);
                        out.println(("Enter Count n, where n is an integer"));
                    }

                }

            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + "or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
