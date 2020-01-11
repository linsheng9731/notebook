package Nio;

import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

public class PipeExample {

    public static void main(String[] args) {
        try {
            Pipe pipe = Pipe.open();
            Pipe.SinkChannel sinkChannel = pipe.sink();
            Pipe.SourceChannel sourceChannel = pipe.source();
            SinkThread sinkThread = new SinkThread(sinkChannel);
            SourceThread sourceThread = new SourceThread(sourceChannel);
            sinkThread.start();
            sourceThread.start();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static class SinkThread extends Thread {

        private Pipe.SinkChannel channel;

        public SinkThread(Pipe.SinkChannel channel){
            this.channel = channel;
        }

        @Override
        public void run() {

            try {
                String data = "New String to write " + System.currentTimeMillis();
                ByteBuffer buf = ByteBuffer.allocate(data.length());
                buf.clear();
                buf.put(data.getBytes());

                // set position = 0
                buf.flip();

                while(buf.hasRemaining()){
                    channel.write(buf);
                }
            } catch (Exception e) {
                // ignore
            }

        }
    }


    private static class SourceThread extends Thread {

        private Pipe.SourceChannel channel;

        public SourceThread(Pipe.SourceChannel channel){
            this.channel = channel;
        }

        @Override
        public void run() {

            try {
                ByteBuffer buf = ByteBuffer.allocate(48);
                int bytesRead = channel.read(buf);
                System.out.println(bytesRead);
                System.out.println(new String(slice(bytesRead, buf.array())));
            } catch (Exception e) {
                // ignore
            }

        }

        private byte[] slice(int index, byte[] buf) {
            byte[] newBuf = new byte[index];
           for(int i = 0; i< index; i ++) {
               newBuf[i] = buf[i];
           }
           return newBuf;
        }
    }


}
