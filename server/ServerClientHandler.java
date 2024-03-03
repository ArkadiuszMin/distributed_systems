package zadanie_dom.server;

import zadanie_dom.server.tcp.BrokerTcp;
import zadanie_dom.server.udp.ClientList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ServerClientHandler implements Runnable{
    private final int clientId;
    private final Socket socket;
    private final PrintWriter printWriter;
    private final BufferedReader reader;
    private String clientName = "";

    public ServerClientHandler(int clientId, Socket socket) throws IOException {
        this.clientId = clientId;
        this.socket = socket;
        this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BrokerTcp.connectClient(clientId, printWriter);
    }

    public void handleClient() throws IOException{
        initUser();

        try{
            while(true){
                var message = reader.readLine();
                BrokerTcp.sendToAll(clientId, clientName, message);
            }
        }finally {
            cleanup();
        }
    }

    public void run(){
        try{
            handleClient();
        }catch(IOException ex){
        }
    }

    private void initUser() throws IOException{
        printWriter.println("\u001B[33m" + "Welcome! Please type in your nickname" + "\u001B[0m");

        clientName = reader.readLine();
        ClientList.setClientName(clientId, clientName);
        printWriter.println("\u001B[33m" + "Welcome " + "\u001B[36m" + clientName + "\u001B[33m" + " You can start typing now" + "\u001B[0m");
    }

    private void cleanup() throws IOException{
        BrokerTcp.disconnectClient(clientId);
        ClientList.removeClient(clientId);
        socket.close();
    }
}
