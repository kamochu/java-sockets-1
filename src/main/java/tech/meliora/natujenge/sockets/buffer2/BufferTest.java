package tech.meliora.natujenge.sockets.buffer2;

import java.nio.ByteBuffer;

public class BufferTest {

    public static void main(String[] args) {


        byte[] bytes = new byte[10];

        ByteBuffer buffer = ByteBuffer.allocate(10);

        //
        printBufferMeta(buffer);


        System.out.println("write to buffer");
        buffer.put((byte) 27);
        buffer.put((byte) 28);
        buffer.put((byte) 29);

        printBufferMeta(buffer);

        System.out.println("flip for purpose");
        buffer.flip(); //read - change limit = position, position = 0

        printBufferMeta(buffer);


        System.out.println("reading the buffer");
        readBuffer(buffer, 3);
        printBufferMeta(buffer);


        System.out.println("rewind");
        buffer.rewind();
        printBufferMeta(buffer);
        readBuffer(buffer, 10);

        //
        System.out.println("compact buffer for next read");
        buffer.compact();
        printBufferMeta(buffer);

        //add
        buffer.put((byte) 30);
        buffer.flip();
        readBuffer(buffer, 10);

        System.out.println("rewind");
        buffer.rewind();
        readBuffer(buffer, 10);
    }

    public static void printBufferMeta(ByteBuffer buffer) {

        System.out.println("**********");
        System.out.println("capacity    :" + buffer.capacity());
        System.out.println("position    :" + buffer.position());
        System.out.println("limit       :" + buffer.limit());

    }

    public static void readBuffer(ByteBuffer buffer, int elements) {
        System.out.print("data        [");

        for (int i = 0; i < elements; i++) {
            if (buffer.hasRemaining()) {
                System.out.print(buffer.get() + " ");
            } else {
                break;
            }
        }

        System.out.println("]");
    }
}
