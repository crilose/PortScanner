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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cristiano
 */
public class Client {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    Socket connection = null; /** Socket per la connessione */
    String serverAddress; /** Indirizzo IP del server con cui comunicare */
    InetAddress ip;
    int port; /**Porta del server con cui comunicare */
    Scanner input = new Scanner(System.in); /**Scanner per l'input di stringhe. */
    String[] servizi = {"HTTP","SMTP","FTP","SSH","Telnet","DNS","Kerberos","LDAP","IRC"};
    int [] defPorts = {80,25,20,22,23,53,88,636,6667};
    DatagramSocket clientSocket;
    
    
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
   
    
    public void testTCP(String ip, int port, String servizio)
    {
        try {
            
            input = new Scanner(System.in);
            InetAddress inetad = InetAddress.getByName(ip);
            String hn = inetad.getHostName();
            System.out.println(ANSI_BLUE + "Nome host: " + hn + ANSI_RESET);
            System.out.println(ANSI_BLUE + "Testing TCP for " + servizio + ANSI_RESET);
            /**Realizzo la connessione vera e propria tramite ip e porta */
            connection = new Socket();
            connection.connect(new InetSocketAddress(ip,port),5000);
            System.out.println(ANSI_GREEN + "Connessione TCP aperta con l'host " + ip + ", porta: " + port + ", Servizio: " + servizio + ANSI_RESET);
        }
        catch(ConnectException e){
            System.err.println("Server non disponibile o porta " + port + " TCP chiusa!");
        }
        catch(UnknownHostException e1){
            System.err.println("Errore DNS!");
            
        }
        
         catch(SocketTimeoutException exc) //Se non riceve risposta + chiusa
        {
            System.out.println("Server non disponibile o porta " + port + " TCP chiusa!");
        }
        
        catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
     public void testUDP(String ip, int port, String servizio)
    {
        try {
            
            InetAddress inetad = InetAddress.getByName(ip);
            String hn = inetad.getHostName();
            System.out.println(ANSI_BLUE + "Nome host: " + hn + ANSI_RESET);
            System.out.println(ANSI_BLUE + "Testing UDP for " + servizio + ANSI_RESET);
            clientSocket = new DatagramSocket();
            byte [] bytes = new byte[128];
            DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length);
            clientSocket.setSoTimeout(5000);
            clientSocket.connect(inetad, port);
            clientSocket.send(sendPacket);
            clientSocket.isConnected();
            sendPacket = new DatagramPacket(bytes, bytes.length);
            clientSocket.receive(sendPacket);
            clientSocket.close();
            System.out.println(ANSI_GREEN + "UDP per indirizzo" + ip + ", porta: " + port + ", Servizio: " + servizio + " probabilmente aperto" + ANSI_RESET);
        }
        catch(ConnectException e){
            System.err.println("Server non disponibile o porta " + port + " UDP chiusa!");
        }
        catch(UnknownHostException e1){
            System.err.println("Errore DNS!");
            
        }
        catch(PortUnreachableException ex) //se riceve questo errore la porta è chiusa
        {
            System.err.println("Server non disponibile o porta " + port + " UDP chiusa!");
        }
        catch(SocketTimeoutException exc) //Se non riceve risposta è possibile che sia aperta comunque
        {
            System.out.println(ANSI_GREEN + "Pacchetto UDP per indirizzo " + ip + ", porta: " + port + ", Servizio: " + servizio + " arrivato ma senza risposta" + ANSI_RESET);
        }
        
        
        catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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
            testTCP(ip,porta,service);
            testUDP(ip,porta,service);

    }
    
    public void defaultPorts()
    {
        System.out.println("Inserisci indirizzo ip: ");
        String ip = input.nextLine();
        for(int i=0;i<servizi.length;i++)
        {
            testTCP(ip,defPorts[i],servizi[i]);
        }
        for(int i=0;i<servizi.length;i++)
        {
            testUDP(ip,defPorts[i],servizi[i]);
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
