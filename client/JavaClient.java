package zadanie_dom.client;

import zadanie_dom.ServerConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class JavaClient {

    public static void main(String[] args){
        try(var socketTCP = new Socket(ServerConfiguration.hostname, ServerConfiguration.port);
            var socketUDP = new DatagramSocket(socketTCP.getLocalPort());
            var socketMulticastUDP = new MulticastSocket(ServerConfiguration.multicastPort)
            )
        {
            var bufferedReader = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()));
            var printWriter = new PrintWriter(socketTCP.getOutputStream(), true);
            var consoleReader = new BufferedReader(new InputStreamReader(System.in));
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

            System.out.println("Properly connected to TCP server!");

            executor.execute(new ClientTcpReader(bufferedReader));
            executor.execute(new ClientUdpReader(socketUDP));
            executor.execute(new ClientMulticastUdpReader(socketMulticastUDP));

            executor.shutdown();

            //nick
            printWriter.println(consoleReader.readLine());

            while(true){
                var message = consoleReader.readLine();

                if(message.equals("U")){
                    System.out.println("\u001B[33m" + "You are now using UDP channel. Send message" + "\u001B[0m");
                    var udpMessage = consoleReader.readLine().getBytes();

                    var packet = new DatagramPacket(udpMessage,
                            udpMessage.length,
                            InetAddress.getByName(ServerConfiguration.hostname),
                            ServerConfiguration.port);

                    socketUDP.send(packet);
                }
                else if(message.equals("M")){
                    System.out.println("\u001B[33m" + "You are now using multicast UDP channel. Send message" + "\u001B[0m");
                    var udpMessage = consoleReader.readLine().getBytes();

                    var packet = new DatagramPacket(udpMessage,
                            udpMessage.length,
                            InetAddress.getByName(ServerConfiguration.multicastHostname),
                            ServerConfiguration.multicastPort);

                    socketUDP.send(packet);
                }
                else{
                    if (socketTCP.isClosed() || executor.getActiveCount() < 3) {
                        System.out.println("\u001B[31m" + "Socket is closed from Server side. Disconnecting..." + "\u001B[0m");
                        return;
                    }
                    printWriter.println(message);
                }
            }
        }
        catch (IOException ex){
            System.out.println("Connection failed. Reason: " + ex.getMessage());
        }
    }
}
