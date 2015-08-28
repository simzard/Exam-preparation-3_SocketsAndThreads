/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadsandsockets_ex1turnstyleserver;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simon
 */
public class TurnStyleProtocol {

    private final static int WAIT_FOR_CLIENT_DESC = 0;
    private final static int WAIT_FOR_TURNSTYLE_ID = 1;
    private final static int WAIT_FOR_TURNSTYLE_COUNT = 2;

    private int state = WAIT_FOR_CLIENT_DESC;

    private List<String> savedIds = new ArrayList();
    
    public String processInput(String input) {
        String output = "";

        if (state == WAIT_FOR_CLIENT_DESC) {
            if (input.equals("TURNSTYLE")) {
                state = WAIT_FOR_TURNSTYLE_ID;
                output = "WAITFORID";
            } else if (input.equals("MONITOR")) {
                output = "TOTALAMOUNT"; // the monitor wants to see all the counts.
            } else {
                output = "QUIT";
            }

        } else if (state == WAIT_FOR_TURNSTYLE_ID) {
            String[] tokens = input.split("-");
            if (tokens.length != 2) {
                output = "QUIT";
            } else {
                int turnStyleIdNumber;
                try {
                    turnStyleIdNumber = Integer.parseInt(tokens[1]);
                    // test if the ID's allready connected
                    if (savedIds.contains(input)) {
                        // wrong id
                        output = "IDEXISTS";
                        state = WAIT_FOR_TURNSTYLE_ID;
                    } else {
                        System.out.println("adding id: " + input);
                        savedIds.add(input);
                    }
                    
                    output = "ID " + turnStyleIdNumber;
                    state = WAIT_FOR_TURNSTYLE_COUNT;
                } catch (NumberFormatException e) {
                    output = "QUIT";
                }
                
            }
        } else if (state == WAIT_FOR_TURNSTYLE_COUNT) {
            String[] tokens = input.split(" ");
            if (tokens.length != 2) {
                output = "QUIT";
            }
            int turnStyleCount;

            try {
                turnStyleCount = Integer.parseInt(tokens[1]);
                System.out.println("number: " + turnStyleCount);
                output = "INC " + turnStyleCount;
            } catch (NumberFormatException e) {
                output = "QUIT";
            }
        } else {
            output = "QUIT";
        }

        
        
        return output;
    }
}
