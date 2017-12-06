import Client.Client;

import java.io.IOException;

class ClientThread implements Runnable {
    private Client client;

    public void run() {
        client = new Client();
        System.out.println("Created client");
    }

    void connect() {
        try {
            client.connect();
            System.out.println("Client connected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Client getClient() {
        return client;
    }

}
