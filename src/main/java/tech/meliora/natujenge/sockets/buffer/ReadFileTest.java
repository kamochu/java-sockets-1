package tech.meliora.natujenge.sockets.buffer;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ReadFileTest {


    public static void main(String[] args) throws Exception {


        RandomAccessFile aFile = new RandomAccessFile("/home/sam/Documents/tech.meliora/repos/learning/natujenge/java-sockets-1/test.txt", "rw");
        FileChannel inChannel = aFile.getChannel();


        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf); //read into buffer - blocking
        while (bytesRead != -1) {

            buf.flip();  //make buffer ready for read

            while(buf.hasRemaining()){
                System.out.print((char) buf.get()); // read 1 byte at a time
            }

            buf.clear(); //make buffer ready for writing
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }
}
