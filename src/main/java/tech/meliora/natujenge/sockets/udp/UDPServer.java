package tech.meliora.natujenge.sockets.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalTime;

public class UDPServer {

    public static void main(String[] args) throws IOException {

        //create a socket
        int port = 4848;
        DatagramSocket socket = new DatagramSocket(4848);
        byte[] buffer = new byte[256];

        //receive data
        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|udp service listening for connections on '" + port + "'");

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|waiting to receive data from client");
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        String receivedMessage = new String(packet.getData(), 0, packet.getLength());

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|received data from '" + packet.getAddress() + ": " + packet.getPort()
                + "'|message: " + receivedMessage);


        //write back response - ECHO server logic - note that we have created a datagram packet...
        InetAddress clientAddress = packet.getAddress();
        int clientPort = packet.getPort();
        packet = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
        socket.send(packet);

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|received data from '" + packet.getAddress() + ": " + packet.getPort()
                + "'|sent echo response.");

        socket.close();
        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|closing the server - stop listening for connections");

        //how does netstat command look like?
    }
}
