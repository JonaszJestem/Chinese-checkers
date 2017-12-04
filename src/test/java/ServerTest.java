import org.junit.Test;

import java.io.IOException;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerTest {
    private Server s;
    private Thread st;
    private ClientThread ct;

    @Test(timeout = 5000)
    public void shouldGetGamesFromServer() {
        setUpServer();
        setUpClient();

        //Wait for server to set up
        while (!s.isReady()) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        s.addGame("1", 1);
        //connect
        try {
            ct.getClient().connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ct.getClient().getGamesFromServer();

        while (true) {
            assertTrue(ct.getClient().games.size() != 0);
        }
    }

    @Test
    public void shouldAcceptMultipleClientConnections() {
        setUpServer();

        ClientThread c = new ClientThread();
        ClientThread d = new ClientThread();
        Thread ct = new Thread(c);
        Thread cd = new Thread(d);
        ct.start();
        cd.start();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void setUpServer() {
        s = new Server();
        st = new Thread(s);
        st.start();
    }

    private void setUpClient() {
        ct = new ClientThread();
        ct.run();
    }

}
