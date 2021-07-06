package tech.meliora.natujenge.sockets.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;

public class UDPClient {

    public static void main(String[] args) throws IOException {

        InetAddress serverAddress = InetAddress.getByName("localhost");
        int serverPort = 4848;
        //you do not specify the server port and address - see the difference
        DatagramSocket socket = new DatagramSocket();

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|udp client socket created on port '" + socket.getLocalPort() + "'");

        String message = "Hello World!";
        byte[] buffer = message.getBytes();

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|about to send message: " + message + "");

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
        socket.send(packet);

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|message sent");


        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|waiting for response from server");
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String receivedMessage = new String(packet.getData(), 0, packet.getLength());

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|message: " + receivedMessage + "|received response from server");

    }
}
