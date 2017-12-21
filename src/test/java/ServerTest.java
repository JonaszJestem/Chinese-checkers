import Server.Server;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerTest {
    private static Server s;
    private static Thread st;
    private static ArrayList<ClientThread> clients = new ArrayList<>();

    @BeforeClass
    public static void setUpServer() {
        s = new Server();
        st = new Thread(s);
        st.start();

        while (!s.isRunning()) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @BeforeClass
    public static void setUpClients() {
        for (int i = 0; i < 10; i++) {
            clients.add(new ClientThread());
            clients.get(i).run();
        }
    }

    @Test(timeout = 10000)
    public void shouldGetGamesFromServer() {
        s.addGame("1", 1);

        clients.get(0).connect();
        clients.get(0).getClient().getGamesFromServer();

        assertTrue(clients.get(0).getClient().getGames().size() != 0);
    }

    @Test
    public void shouldAcceptMultipleClientConnections() {
        for (ClientThread c : clients) {
            if (!c.getClient().isConnected) c.connect();
        }

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(10, s.getNumOfPlayers());
    }

    @Test
    public void shouldAllowClientToJoinGameIfNotFull() {
        s.addGame("2", 2);
        s.addGame("3", 3);

        for (ClientThread c : clients) {
            if (!c.getClient().isConnected) c.connect();
            c.getClient().joinGame(1);
        }

        for (ClientThread c : clients) {
            if (!c.getClient().isConnected) c.connect();
            if (!c.getClient().isInGame)
                c.getClient().joinGame(0);
        }

        assertEquals(3, s.getGame(1).getMaxPlayers());
        assertEquals(2, s.getGame(0).getMaxPlayers());
    }
}
