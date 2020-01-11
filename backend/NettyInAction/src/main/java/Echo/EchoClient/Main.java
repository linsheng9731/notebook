package Echo.EchoClient;

/**
 * Run {@link Echo.EchoServer.Main} at first.
 * @see Echo.EchoClient.EchoClient
 */
public class Main {

    public static void main(String[] args) {

        EchoClient client = new EchoClient("127.0.0.1", 6060);
        try {
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
