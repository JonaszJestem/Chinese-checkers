import org.junit.jupiter.api.Test;

import static java.lang.Thread.sleep;


class ServerTest {
    private Server s;
    private Thread st;

    @Test
    void shouldAcceptMultipleClientConnections() {
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
}
