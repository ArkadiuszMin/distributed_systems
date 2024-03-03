package zadanie_dom.server.udp;

import zadanie_dom.ServerConfiguration;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class BrokerUdp implements Runnable{
    public void manageUdpCommunication() throws IOException {
        var buffer = new byte[1024];
        var message = new DatagramPacket(buffer, buffer.length);

        try(var socket = new DatagramSocket(ServerConfiguration.port)){
            while(true){
                socket.receive(message);
                var received = new String(message.getData(), 0, message.getLength());

                var port = message.getPort();
                String nickname = "";
                int id = -1;
                var clients = ClientList.getClients();

                for(int clientId : clients.keySet()){
                    var clientInfo = clients.get(clientId);
                    if (clientInfo.port() != port){
                        continue;
                    }

                    id = clientId;
                    nickname = clientInfo.name();
                    break;
                }

                var messageToSend = String.format("" +
                        "\u001B[32m%s_UDP >> %s \u001B[0m",
                        nickname.toUpperCase(),
                        received).getBytes();

                sendMessage(id, messageToSend, socket);
            }

        }
    }

    public void run(){
        try{
            manageUdpCommunication();
        }catch (Exception ex){}
    }

    private void sendMessage(int senderId, byte[] message, DatagramSocket socket) throws IOException{
        for(int clientId : ClientList.getClients().keySet()){
            var clientInfo = ClientList.getClients().get(clientId);
            if(clientId != senderId){
                var packetToSend = new DatagramPacket(message, message.length, clientInfo.address(), clientInfo.port());
                socket.send(packetToSend);
            }
        }
    }
}
