package zadanie_dom.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientUdpReader implements Runnable{
    private final DatagramSocket socket;

    public ClientUdpReader(DatagramSocket socket){
        this.socket = socket;
    }

    private void handleReader() throws IOException {
        var buf = new byte[1024];
        var receivedPacket = new DatagramPacket(buf, buf.length);

        while(true){
            socket.receive(receivedPacket);
            System.out.println(new String(receivedPacket.getData(), 0, receivedPacket.getLength()));
        }
    }

    public void run(){
        try{
            handleReader();
        }catch(IOException ex){
            //
        }
    }


}
