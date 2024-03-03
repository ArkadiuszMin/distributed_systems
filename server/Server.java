package zadanie_dom.server;

import zadanie_dom.ServerConfiguration;
import zadanie_dom.server.udp.BrokerUdp;
import zadanie_dom.server.udp.ClientList;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    public static void main(String args[]) throws IOException {
        int clientNumber = 0;
        System.out.println("---------------------------------");
        System.out.println("SERVER WORKING");
        System.out.println("SERWER NAME: "  + ServerConfiguration.hostname);
        System.out.println("SERWER PORT: " + ServerConfiguration.port);
        System.out.println("---------------------------------");

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        executor.execute(new BrokerUdp());

        try(var listeningSocket = new ServerSocket(ServerConfiguration.port))
        {
            while(true){
                var clientSocket = listeningSocket.accept();

                ClientList.addClient(clientNumber, clientSocket.getInetAddress(), clientSocket.getPort());

                executor.execute(new ServerClientHandler(clientNumber, clientSocket));

                clientNumber+=1;
            }
        }
        finally {
            executor.shutdown();
        }

    }
}
