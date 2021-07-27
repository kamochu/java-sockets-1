package tech.meliora.natujenge.sockets.channels;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JavaNIOMultiThreadedBlockingEchoServer {

    public static void main(String[] args) throws IOException {
        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|server about to start");

        int serverPort = 5151;
        int bufferSize = 4;
        int poolSize = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(serverPort));

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|server bound at port : " + serverPort);

        while (true) {

            System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                    + "|waiting for a client connection");


            SocketChannel clientChannel = serverSocketChannel.accept(); //blocking

            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    try {

                        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                                + "|client: " + clientChannel.getRemoteAddress() + "|accepted client connection");

                        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

                        //blocks until the client sends data to us...
                        int bytesRead;
                        while ((bytesRead = clientChannel.read(buffer)) != -1) {

                            System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                                    + "|client: " + clientChannel.getRemoteAddress() + "|read " + bytesRead + " bytes");

                            buffer.flip(); //prepare to read

                            //read
                            System.out.print(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                                    + "|client: " + clientChannel.getRemoteAddress() + "|data : ");
                            while (buffer.hasRemaining()) {
                                System.out.print((char) buffer.get());
                            }
                            System.out.println("");

                            buffer.rewind(); // to allow us read again

                            clientChannel.write(buffer); //blocks...

                            System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                                    + "|client: " + clientChannel.getRemoteAddress() + "|written data to client");

                            buffer.compact();

                        }
                    } catch (IOException e) {

                        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                                + "|error handling client connection: " + e.getMessage());
                    }

                }
            });


        }

    }


}
