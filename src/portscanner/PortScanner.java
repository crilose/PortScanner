/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import java.util.Scanner;

/**
 *
 * @author Cristiano
 */
public class PortScanner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Client client = new Client();
        Scanner in = new Scanner(System.in);
        
        while(true)
        {
            System.out.println("Scrivi CUSTOM per provare porta a scelta.");
            System.out.println("Scrivi DEFAULT per provare alcune porte note.");
            String scelta = in.nextLine();
            
            switch(scelta)
            {
                case "CUSTOM":
                    client.customPort();
                    break;
                
                case "DEFAULT":
                    client.defaultPorts();
                    break;
                
                default:
                    System.out.println("Scelta non accettata.");
                    break;
            }
        }
        
    }
    
}
