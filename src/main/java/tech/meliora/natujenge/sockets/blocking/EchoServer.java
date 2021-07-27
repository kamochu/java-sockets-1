package tech.meliora.natujenge.sockets.blocking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;

public class EchoServer {

    public static void main(String[] args) throws Exception {

        int port = 6060;
        ServerSocket serverSocket = new ServerSocket(port);

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|started server");


        while (true){

            System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                    + "|waiting for a connection");

            Socket clientSocket = serverSocket.accept(); //blocking
            System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                    + "|client connection: " + clientSocket);

            //Get OUTPUT stream - SIMPLEX communication stream
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            //Get INPUT stream - SIMPLEX communication stream
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //blocking call too...
            String clientMessage = in.readLine(); // blocks....

            System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                    + "|message:  " + clientMessage
                    + "|from client '" + clientSocket.getInetAddress() + ": " + clientSocket.getPort() + "'");

            out.println(clientMessage);

            System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                    + "|sent a response message to the client"
                    + "|client '" + clientSocket.getInetAddress() + ": " + clientSocket.getPort() + "'");

            in.close();
            out.close();
            clientSocket.close();

            System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                    + "|closed server and client socket"
                    + "|bye");

        }

    }
}
