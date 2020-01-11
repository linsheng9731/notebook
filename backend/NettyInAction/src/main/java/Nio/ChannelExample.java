package Nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelExample {

    public static void main(String[] args) {
        try {
            RandomAccessFile file = new RandomAccessFile("pom.xml", "rw");
            FileChannel channel = file.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(48);
            int bytesRead = channel.read(buf);
            while (bytesRead != -1) {
                buf.flip();
                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }
                buf.clear();
                bytesRead = channel.read(buf);
            }
            file.close();
        } catch (Exception e) {
            // ignore
        }


    }
}
