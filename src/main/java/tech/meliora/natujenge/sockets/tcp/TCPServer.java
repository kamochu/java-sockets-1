package tech.meliora.natujenge.sockets.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;

public class TCPServer {

    public static void main(String[] args) throws IOException {

        int port = 4747;
        ServerSocket serverSocket = new ServerSocket(4747);

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|server listening at port '" + port + "'");

        //this is  a blocking call, the thread involved
        // (in this case main thread waits for a socket connection from client)
        Socket clientSocket = serverSocket.accept();

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|received a connection from client "
                + "'" + clientSocket.getInetAddress() + ": " + clientSocket.getPort() + "'");

        //Get OUTPUT stream - SIMPLEX communication stream
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        //Get INPUT stream - SIMPLEX communication stream
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        //blocking call too...
        String clientMessage = in.readLine();

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|message:  " + clientMessage
                + "|from client '" + clientSocket.getInetAddress() + ": " + clientSocket.getPort() + "'");

        out.println("Hello Client from the server");

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|sent a response message to the client"
                + "|client '" + clientSocket.getInetAddress() + ": " + clientSocket.getPort() + "'");

        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|closed server and client socket"
                + "|bye");

        //remember to demonstrate netstat command
    }
}
