package zadanie_dom.server.tcp;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BrokerTcp {
    private static final ConcurrentMap<Integer, PrintWriter> connectedClients = new ConcurrentHashMap<>();

    public synchronized static void sendToAll(int clientId, String clientName, String message){
        var messageToSend = String.format("%s >> %s", clientName.toUpperCase(), message);

        for(Integer id : connectedClients.keySet()){
            if (id.equals(clientId)) continue;

            connectedClients.get(id).println("\u001B[32m" + messageToSend + "\u001B[0m");
        }
    }

    public synchronized static void connectClient(int clientId, PrintWriter clientWriter){
        connectedClients.put(clientId, clientWriter);
        System.out.println("Client with ID " + clientId + " connected");
        System.out.println("---------------------------------");
    }

    public synchronized static void disconnectClient(int clientId){
        connectedClients.remove(clientId);
        System.out.println("Client with ID " + clientId + " disconnected");
        System.out.println("---------------------------------");
    }
}
