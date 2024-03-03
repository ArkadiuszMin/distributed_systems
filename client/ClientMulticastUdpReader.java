package zadanie_dom.client;

import zadanie_dom.ServerConfiguration;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ClientMulticastUdpReader implements Runnable{

    private final MulticastSocket socket;
    public ClientMulticastUdpReader(MulticastSocket socket) throws IOException {
        this.socket = socket;
        var address = InetAddress.getByName(ServerConfiguration.multicastHostname);
        socket.joinGroup(address);
    }

    private void handleReader() throws IOException{
        try{
            var buf = new byte[1024];
            var receivedPacket = new DatagramPacket(buf, buf.length);

            while(true){
                socket.receive(receivedPacket);
                var rawMessage = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
                var message = String.format("\u001B[35mMULTICAST >> %s \u001B[0m", rawMessage);
                System.out.println(message);
            }
        }finally {
            socket.leaveGroup(InetAddress.getByName(ServerConfiguration.multicastHostname));
            socket.close();
        }
    }

    public void run(){
        try {
            handleReader();
        } catch (IOException ex) {
            //
        }
    }
}
