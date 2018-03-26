/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
    FileWriter writer;
    
    
    
    public Client(String ip, int port)
    {
        try {
            this.writer = new FileWriter("logfile.txt",true);
            this.serverAddress = ip;
            this.port = port;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Client()
    {
        try {
            this.writer = new FileWriter("logfile.txt",true);
            this.serverAddress = "";
            this.port = 0;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    
    public void testTCP(String ip, int port, String servizio)
    {
        try {
            
            input = new Scanner(System.in);
            InetAddress inetad = InetAddress.getByName(ip);
            String hn = inetad.getHostName();
            System.out.println(ANSI_BLUE + "Nome host: " + hn + ANSI_RESET);
            writer.append("Nome host: " + hn + "\r\n");
            System.out.println(ANSI_BLUE + "Testing TCP for " + servizio + ANSI_RESET);
            writer.append("Testing TCP for " + servizio + "\r\n");
            /**Realizzo la connessione vera e propria tramite ip e porta */
            connection = new Socket();
            connection.connect(new InetSocketAddress(ip,port),5000);
            System.out.println(ANSI_GREEN + "Connessione TCP aperta con l'host " + ip + ", porta: " + port + ", Servizio: " + servizio + ANSI_RESET);
            writer.append("Connessione TCP aperta con l'host " + ip + ", porta: " + port + ", Servizio: " + servizio + "\r\n");

        }
        catch(ConnectException e){
            try {
                System.err.println("Server non disponibile o porta " + port + " TCP chiusa!");
                writer.append("Server non disponibile o porta " + port + " TCP chiusa!" + "\r\n");
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch(UnknownHostException e1){
            try {
                System.err.println("Errore DNS!");
                writer.append("Errore DNS!" + "\r\n");
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
         catch(SocketTimeoutException exc) //Se non riceve risposta + chiusa
        {
            try //Se non riceve risposta + chiusa
            {
                System.out.println("Server non disponibile o porta " + port + " TCP chiusa!");
                writer.append("Server non disponibile o porta " + port + " TCP chiusa!" + "\r\n");
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            writer.append("Nome host: " + hn + "\r\n");
            System.out.println(ANSI_BLUE + "Testing UDP for " + servizio + ANSI_RESET);
            writer.append("Testing UDP for " + servizio + "\r\n");
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
            writer.append("UDP per indirizzo" + ip + ", porta: " + port + ", Servizio: " + servizio + " probabilmente aperto" + "\r\n");
        }
        catch(ConnectException e){
            try {
                System.err.println("Server non disponibile o porta " + port + " UDP chiusa!");
                writer.append("Server non disponibile o porta " + port + " UDP chiusa!" + "\r\n");
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch(UnknownHostException e1){
            try {
                System.err.println("Errore DNS!");
                writer.append("Errore DNS!" + "\r\n");
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        catch(PortUnreachableException ex) //se riceve questo errore la porta è chiusa
        {
            try //se riceve questo errore la porta è chiusa
            {
                System.err.println("Server non disponibile o porta " + port + " UDP chiusa!");
                writer.append("Server non disponibile o porta " + port + " UDP chiusa!" + "\r\n");
            } catch (IOException ex1) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        catch(SocketTimeoutException exc) //Se non riceve risposta è possibile che sia aperta comunque
        {
            try //Se non riceve risposta è possibile che sia aperta comunque
            {
                System.out.println(ANSI_GREEN + "Pacchetto UDP per indirizzo " + ip + ", porta: " + port + ", Servizio: " + servizio + " arrivato ma senza risposta" + ANSI_RESET);
                writer.append("Pacchetto UDP per indirizzo " + ip + ", porta: " + port + ", Servizio: " + servizio + " arrivato ma senza risposta" + "\r\n");
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void customPort()
    {
        try {
            writer = new FileWriter("logfile.txt",true);
            System.out.println("Inserisci indirizzo ip: ");
            String ip = input.nextLine();
            System.out.println("Inserisci porta: ");
            int porta = input.nextInt();
            System.out.println("Inserisci nome servizio: ");
            String service = input.next();
            testTCP(ip,porta,service);
            testUDP(ip,porta,service);
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void defaultPorts()
    {
        try {
            writer = new FileWriter("logfile.txt", true);
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
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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
