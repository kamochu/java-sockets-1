package tech.meliora.natujenge.sockets.sctp;

import com.sun.nio.sctp.*;

import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.time.LocalTime;

public class SCTPClient {

    public static void main(String[] args) throws Exception {

        int serverPort = 4949;
        InetSocketAddress serverAddress = new InetSocketAddress("localhost", serverPort);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        SctpChannel socketChannel = SctpChannel.open(serverAddress, 0, 0);

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|connected to server");


        AssociationHandler assocHandler = new AssociationHandler();


        MessageInfo messageInfo = null;
        messageInfo = socketChannel.receive(buffer, System.out, assocHandler);
        buffer.flip();
        String receivedMessage = new String(buffer.array());

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|read data: [ " + receivedMessage + "]");

        buffer.clear();


        socketChannel.close();

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|close");


    }

    static class AssociationHandler extends AbstractNotificationHandler<PrintStream> {
        public HandlerResult handleNotification(AssociationChangeNotification not,
                                                PrintStream stream) {
            if (not.event().equals(AssociationChangeNotification.AssocChangeEvent.COMM_UP)) {
                int outbound = not.association().maxOutboundStreams();
                int inbound = not.association().maxInboundStreams();
                stream.printf("New association setup with %d outbound streams" +
                        ", and %d inbound streams.\n", outbound, inbound);
            }

            return HandlerResult.CONTINUE;
        }

        public HandlerResult handleNotification(ShutdownNotification not,
                                                PrintStream stream) {
            stream.printf("The association has been shutdown.\n");
            return HandlerResult.RETURN;
        }
    }
}
