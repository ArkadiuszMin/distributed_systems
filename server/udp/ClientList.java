package zadanie_dom.server.udp;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClientList {
    private static final ConcurrentMap<Integer, ClientInfo> clients = new ConcurrentHashMap<>();

    public static void addClient(int clientId, InetAddress address, int port){
        clients.put(clientId, new ClientInfo(port, address, ""));
    }

    public static void setClientName(int clientId, String name){
        var record= clients.remove(clientId);
        clients.put(clientId, new ClientInfo(record.port, record.address, name));
    }

    public static void removeClient(int clientId){
        clients.remove(clientId);
    }

    public static Map<Integer, ClientInfo> getClients(){
        return clients;
    }

    public record ClientInfo(int port, InetAddress address, String name){}
}
