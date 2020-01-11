package Echo.EchoServer;

public class Main {

    public static void main(String[] args) {

        EchoServer server = new EchoServer(6060);
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
