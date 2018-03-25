/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cristiano
 */
public class Client {
    
    Socket connection = null; /** Socket per la connessione */
    String serverAddress; /** Indirizzo IP del server con cui comunicare */
    int port; /**Porta del server con cui comunicare */
    Scanner input = new Scanner(System.in); /**Scanner per l'input di stringhe. */
    String[] servizi = {"HTTP","SMTP","FTP","SSH","Telnet","DNS","Kerberos","LDAP","IRC"};
    int [] defPorts = {80,25,20,22,23,53,88,636,6667};
    
    
    public Client(String ip, int port)
    {
        this.serverAddress = ip;
        this.port = port;
    }
    
    public Client()
    {
        this.serverAddress = "";
        this.port = 0;
    }
    
    
    public void startConnection(String ip, int port, String servizio)
    {
        try {
            input = new Scanner(System.in);
            /**Realizzo la connessione vera e propria tramite ip e porta */
            connection = new Socket(ip, port);
            System.out.println("Connessione TCP aperta con l'host " + ip + ", porta: " + port + ", Servizio: " + servizio);
            
            
        }catch(ConnectException e){
            System.err.println("Server non disponibile o porta " + port + " chiusa!");
        }
        catch(UnknownHostException e1){
            System.err.println("Errore DNS!");
            
        } catch (IOException ex) {
            System.err.println(ex);
        } 
    }
    
    public void customPort()
    {
        System.out.println("Inserisci indirizzo ip: ");
        String ip = input.nextLine();
        System.out.println("Inserisci porta: ");
        int porta = input.nextInt();
        System.out.println("Inserisci nome servizio: ");
        String service = input.next();
        startConnection(ip,porta,service);
    }
    
    public void defaultPorts()
    {
        System.out.println("Inserisci indirizzo ip: ");
        String ip = input.nextLine();
        for(int i=0;i<servizi.length;i++)
        {
            startConnection(ip,defPorts[i],servizi[i]);
        }
        
    }
    
    public void closeConnection()
    {
        try {
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
