package tech.meliora.natujenge.sockets.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class BufferTest {

    public static void main(String[] args) {


        //declare a byte buffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        // ByteBuffer buffer = ByteBuffer.allocateDirect(10); - if you are reusing the buffer, takes time to initialize

        System.out.println("After declaration");
        printMeta(buffer);

        //no need to specify the index
        write(buffer, 3);
        System.out.println("After adding 3 elements ");
        printMeta(buffer);

        write(buffer, 2);
        System.out.println("After adding 2 more elements");
        printMeta(buffer);

        //prepare to read
        buffer.flip(); //changes limit = position and moves position to 0
        System.out.println("After buffer.flip()");
        printMeta(buffer);

        //read 2 elements
        read(buffer,2);
        System.out.println("After read 2");
        printMeta(buffer);

        //clear - does it clear data?... nope, just moves the position...
        buffer.clear();
        System.out.println("After buffer.clear()");
        printMeta(buffer);


    }

    private static void printMeta(ByteBuffer buffer) {

        System.out.println("****************************************");
        System.out.println("capacity    : " + buffer.capacity());
        System.out.println("position    : " + buffer.position());
        System.out.println("limit       : " + buffer.limit());

        System.out.print("data       : [");
        for (int i = 0; i < buffer.limit(); i++) {

            System.out.print(buffer.get(i) + " ");
        }

        System.out.println("]");

        System.out.println("****************************************");
    }


    private static void write(ByteBuffer buffer, int noOfElements) {
        for (int i = 0; i < noOfElements; i++) {
            System.out.print(buffer.get(i) + " ");
            buffer.put((byte) ((i + 1) * 10)); // just adding
        }
    }

    private static void read(ByteBuffer buffer, int noOfElements) {
        System.out.print("data read     : [");
        for (int i = 0; i < noOfElements; i++) {
            if (buffer.hasRemaining()) {
                System.out.print(buffer.get() + " ");
            }
        }
        System.out.println("]");
    }
}
