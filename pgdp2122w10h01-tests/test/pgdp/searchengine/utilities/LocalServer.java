package pgdp.searchengine.utilities;

import de.tum.in.test.api.AllowLocalPort;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AllowLocalPort
public class LocalServer extends Thread {

    private int port;
    private String host;
    private String path;

    public void setPort(int port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void run() {
        try(SSLServerSocket serverSocket = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(8080)) {
            Socket client = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            List<String> lines = new ArrayList<>();
            String line = in.readLine();
            while(line != null) {
                lines.add(line);
                line = in.readLine();
            }

            assertEquals(lines.get(0), "GET /" + path + " HTTP/1.1", "Nee nee nee Kinnas!");
            assertEquals(lines.get(1), "Host: " + host, "Host Post Most");

            PrintStream out = new PrintStream(client.getOutputStream());
            out.println("HTTP/1.1 200 OK");
            out.println("<html>Hoi</html>");
            out.println();
            out.flush();

            in.close();
            out.close();
            client.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) {
        int port = 8080;
        String serverHost = "localhost";
        String requestedPath = "somewhere/over/the/rainbow";

        LocalServer server = new LocalServer();
        server.setPort(port);
        server.setHost(serverHost);
        server.setPath(requestedPath);
        server.start();
    }
}
