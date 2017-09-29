/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
 
/**
 *
 * @author Peter
 */
public class EchoClient extends Thread {
 
    BufferedReader reader;
    PrintWriter writer;
 
    public Socket client;
 
    private static List<Socket> sockets = new ArrayList<>();
 
    public EchoClient(Socket client) throws IOException {
        this.writer = new PrintWriter(client.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.client = client;
        sockets.add(client);
    }
 
    @Override
    public void run() {
        try {
            writer.println("[type 'stop' to disconnect]");
 
            while (true) {
                String line = reader.readLine();
                if (line.trim().equals("UPPER#Hello World")) {
                    writer.println("HELLO WORLD");
                }
                if (line.trim().equals("LOWER#Hello World")) {
                    writer.println("hello world");
                }
                if (line.trim().equals("REVERSE#abcd")) {
                    writer.println("Dcab");
                }
                if (line.trim().equals("TRANSLATE#Dog")) {
                    writer.println("Hund");
                }
 
                if (line.trim().equals("stop")) {
                    writer.println("bye!");
                    break;
                }
                //writer.println("[message] " + line);
                sendToAllTCP("[message] " + line);
 
            }
        } catch (IOException e) {
            System.err.println("Exception caught: client disconnected.");
        } finally {
            try {
                client.close();
            } catch (IOException e) {;
            }
        }
    }
 
    public void sendToAllTCP(String message) {
 
        for (Socket z : sockets) {
            if (z != null) {
 
                PrintStream outToClient = null;
                try {
                    outToClient = new PrintStream(z.getOutputStream());
                    outToClient.println(message);
                } catch (IOException e) {
                    System.out.println("Caught an IO exception trying "
                            + "to send to TCP connections");
                    e.printStackTrace();
                }
            }
        }
    }
}