package Client;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public InetAddress serverIP;
    public int serverPort;
    private IClientStrategy strategy;
    /**
     * constructor
     * @param serverIP the IP Address of the Server
     * @param serverPort The port the server is waiting for a client from
     * @param strategy the strategy of the client
     */
    public Client(InetAddress serverIP, int serverPort, IClientStrategy strategy)
    {
        if (serverIP == null || strategy == null || serverPort <= 0) {
            throw new IllegalArgumentException("Invalid parameters for Client constructor.");
        }
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.strategy = strategy;
    }
    /**
     * A function used to connect with the server and apply the Client strategy
     * Catches the exception in case the connection fails
     */
    public void communicateWithServer()
    {
        try (Socket serverSocket = new Socket(serverIP, serverPort)){ //serch the server
            System.out.println("Server started at " + serverIP.getHostAddress() + ":" + serverPort);
            strategy.clientStrategy(serverSocket.getInputStream(),serverSocket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
