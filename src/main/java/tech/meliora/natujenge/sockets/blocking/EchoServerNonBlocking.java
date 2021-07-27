package tech.meliora.natujenge.sockets.blocking;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Set;

public class EchoServerNonBlocking {

    static int bufferSize = 50;
    static Selector selector;

    public static void main(String[] args) throws Exception {


        selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.bind(new InetSocketAddress(6062));

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|started server");

        serverSocketChannel.configureBlocking(false) ; //non blocking
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //selection key = channel, selector, attachment...

        while (true){

            selector.select(); //blocking....

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keys = selectedKeys.iterator();

            while (keys.hasNext()){

                SelectionKey key = keys.next();

                if(key.isValid()){

                    if(key.isAcceptable()){

                        accept(key);

                    } else if(key.isReadable()){

                        read(key);

                    } else if(key.isWritable()){

                        write(key);

                    }
                }


                keys.remove();


            }

        }

    }


    public static void accept(SelectionKey key) throws  Exception{

        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        SocketChannel clientChannel = serverSocketChannel.accept();
        ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|client connection: "+ clientChannel);

        clientChannel.configureBlocking(false);
        SelectionKey selectionKey= clientChannel.register(selector, SelectionKey.OP_READ);
        selectionKey.attach(buffer);

    }

    public static void read(SelectionKey key) throws  Exception{

        SocketChannel clientChannel = (SocketChannel)  key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|about to read: "+ clientChannel);

        //add some logic to detect closed connections...
        clientChannel.read(buffer);

        buffer.flip(); // prepare for reading from buffer

        //reading
        System.out.print(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|data: ");
        while(buffer.hasRemaining()) {
            System.out.print((char) buffer.get());
        }
        System.out.println("");

        key.interestOps(SelectionKey.OP_WRITE);
    }

    public static void write(SelectionKey key) throws  Exception{

        SocketChannel clientChannel  =  (SocketChannel) key.channel();

        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.rewind(); // we reread the same data we read... echo service

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|write to channel: "+ clientChannel);

        clientChannel.write(buffer);

        key.interestOps(SelectionKey.OP_READ);

    }
}
