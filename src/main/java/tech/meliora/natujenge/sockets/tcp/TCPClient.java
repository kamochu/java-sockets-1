package tech.meliora.natujenge.sockets.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;

public class TCPClient {

    public static void main(String[] args) throws IOException {

        //server details
        String ipAddress = "localhost";
        int port = 4747; //this is the server port on the TCPServer

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|establishing a connection to the server '" + ipAddress + ":" + port + "'");
        Socket socket = new Socket(ipAddress, port);

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|established a connection to the server '" + ipAddress + ":" + port + "', "
                + "local port '" + socket.getLocalPort() + "'");

        //Get OUTPUT stream - SIMPLEX communication stream
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //Get INPUT stream - SIMPLEX communication stream
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String message = "Hello Server!!";
        out.println(message); // slow... thread has to wait...

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|sent message to server");

        //this is a blocking call - you wait for the server to respond...
        String resp = in.readLine();
        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|received an answer from server: " + resp);

        //anyone can close connection - client or server
        in.close();
        out.close();
        socket.close();

        //this is a blocking call - you wait for the server to respond...
        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|closed connection and all associated resources ");

    }


}
