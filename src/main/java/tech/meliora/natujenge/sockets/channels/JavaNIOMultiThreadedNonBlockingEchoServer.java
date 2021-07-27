package tech.meliora.natujenge.sockets.channels;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JavaNIOMultiThreadedNonBlockingEchoServer {

    static Selector selector;
    static int serverPort = 5252;
    static int bufferSize = 5;

    public static void main(String[] args) throws IOException {
        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|server about to start");

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(serverPort));

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|server bound at port : " + serverPort);

        selector = Selector.open();

        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|serverChannel: " + serverSocketChannel + "|configured to non blocking mode");

        while (true) {

            System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                    + "|waiting on the selector");

            int noOfSelectedKeys = selector.select();
            System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                    + "|keys: " + noOfSelectedKeys);

            Set<SelectionKey> selectedKeys = selector.selectedKeys();

            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {

                SelectionKey key = keyIterator.next();

                if (key.isValid()) {

                    if (key.isAcceptable()) {
                        // a connection was accepted by a ServerSocketChannel.
                        accept(key);
                    } else if (key.isReadable()) {
                        // a channel is ready for reading
                        read(key);
                    } else if (key.isWritable()) {
                        // a channel is ready for writing
                        write(key);
                    }
                }

                keyIterator.remove();

                selectedKeys.remove(key);

                System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                        + "|removed |" + selectedKeys.size());
            }

        }

    }


    private static void accept(SelectionKey key) throws IOException {

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|accept : " + key.channel());


        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        SocketChannel clientChannel = serverSocketChannel.accept();

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|client: " + clientChannel.getRemoteAddress() + "|accepted client connection");

        clientChannel.configureBlocking(false);

        SelectionKey selectionKey = clientChannel.register(key.selector(), SelectionKey.OP_READ);
        selectionKey.attach(ByteBuffer.allocate(bufferSize));

    }

    private static void read(SelectionKey key) throws IOException {

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|read : " + key.channel());

        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        clientChannel.read(buffer);

        buffer.flip(); // for reading purposes...

        //read
        System.out.print(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|client: " + clientChannel.getRemoteAddress() + "|data : ");
        while (buffer.hasRemaining()) {
            System.out.print((char) buffer.get());
        }
        System.out.println("");

        key.interestOps(SelectionKey.OP_WRITE);
    }

    private static void write(SelectionKey key) throws IOException {

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|write : " + key.channel());

        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        buffer.rewind(); // rewind for sake of reading...

        clientChannel.write(buffer);

        //read
        System.out.print(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|client: " + clientChannel.getRemoteAddress() + "|writing");

        buffer.compact();

        key.interestOps(SelectionKey.OP_READ);
    }

}
