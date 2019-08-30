package Nio;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class SimpleNioSocketServer {

    public static void main(String[] args) {
        Server server = new Server(8888);
        server.start();
    }

    private static class Server extends Thread {

        private int port;

        public Server(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try{
                Selector selector = Selector.open();
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

                while(true) {
                    selector.select(); // wait for connection
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                    while(keys.hasNext()) {
                        SelectionKey key = keys.next();
                        SocketChannel client =  ((ServerSocketChannel)key.channel()).accept();
                        client.write(Charset.defaultCharset().encode("hello world!"));
                        keys.remove();
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
