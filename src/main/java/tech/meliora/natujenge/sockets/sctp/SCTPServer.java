package tech.meliora.natujenge.sockets.sctp;

import com.sun.nio.sctp.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;

public class SCTPServer {

    public static void main(String[] args) throws IOException {

        //SCTP is not very common, I worked with SCTP for the first time this year
        //below repo on github was very helpful
        //https://github.com/msvoelker/java-sctp

        //NOTE: this uses new New IO (nio)
        //channels are FULL DUPLEX communication objects - unlike java.io with SIMPLEX input and output streams that are blocking
        //more about NIO to come in next few classes...
        int serverPort = 4949;
        SctpServerChannel serverSocketChannel = SctpServerChannel.open();
        InetSocketAddress serverAddress = new InetSocketAddress(serverPort);
        serverSocketChannel.bind(serverAddress);

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|binding server on port '" + serverPort + "'");

        //read and write done via buffers
        String messageToSend = "This is a sample message from server";
        ByteBuffer buffer = ByteBuffer.allocate(messageToSend.length());
        ByteBuffer receiveBuffer = ByteBuffer.allocate(messageToSend.length());

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|waiting for client connections");

        //accept a connection - blocking call too (java nio can  also block, depending on developer :-) )
        SctpChannel clientSocketChannel = serverSocketChannel.accept();

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|received a client connection '" + clientSocketChannel.getRemoteAddresses() + "'");


        buffer.clear(); //clear the buffer of any data
        buffer.put(messageToSend.getBytes());
        buffer.flip(); //in readiness for read...
        MessageInfo outMessageInfo = MessageInfo.createOutgoing(null,
                0);
        clientSocketChannel.send(buffer, outMessageInfo);

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|sent data to the client");

        buffer.clear();

        clientSocketChannel.shutdown();

        AssociationHandler assocHandler = new AssociationHandler();
        MessageInfo inMessageInfo = null;
        while (true) {
            inMessageInfo = clientSocketChannel.receive(receiveBuffer, System.out, assocHandler);
            if (inMessageInfo == null || inMessageInfo.bytes() == -1) {
                break;
            }
        }
        clientSocketChannel.close();

        serverSocketChannel.close();

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|bye");

    }


    static class AssociationHandler extends AbstractNotificationHandler<PrintStream> {
        public HandlerResult handleNotification(AssociationChangeNotification not,
                                                PrintStream stream) {
            stream.println("AssociationChangeNotification received: " + not);
            return HandlerResult.CONTINUE;
        }

        public HandlerResult handleNotification(ShutdownNotification not,
                                                PrintStream stream) {
            stream.println("ShutdownNotification received: " + not);
            return HandlerResult.RETURN;
        }
    }
}
