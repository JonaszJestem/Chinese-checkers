import java.io.IOException;

class ClientThread implements Runnable {
    private Client client;

    public void run() {
        client = new Client();
        System.out.println("Created client");
        try {
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client getClient() {
        return client;
    }

}
