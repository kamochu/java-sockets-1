package tech.meliora.natujenge.sockets.blocking;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServerNIOBlocking {

    public static void main(String[] args) throws Exception {

        int port = 6061;
        int bufferSize = 50;
        int threads = 2;

        ExecutorService executorService = Executors.newFixedThreadPool(threads);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|started server");

        serverSocketChannel.bind(new InetSocketAddress(6061));


        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                + "|bind successful: " + serverSocketChannel);


        while (true) {

            System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                    + "|waiting for a connection ");

            SocketChannel clientChannel = serverSocketChannel.accept(); //blocking

            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    try{

                        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                                + "|new client connection: " + clientChannel);

                        //read from connection
                        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

                        int bytesRead;
                        while ((bytesRead = clientChannel.read(buffer)) != -1) {

                            //flip for sake of reading
                            buffer.flip();
                            System.out.print(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                                    + "|bytesRead: " + bytesRead + "|data: ");
                            for (int i = 0; i < buffer.limit(); i++) {
                                System.out.print((char) buffer.get(i));
                            }
                            System.out.println("");
                            //write

                            System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                                    + "|new client connection: " + clientChannel);
                            clientChannel.write(buffer);

                            buffer.compact();

                        }

                    }catch(Exception ex){
                        System.out.println(LocalTime.now().toString() + "|" + Thread.currentThread().getName()
                                + "|error: " + ex.getMessage());
                    }


                }
            });


        }

    }
}
