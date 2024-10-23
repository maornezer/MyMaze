package Client;

import Client.Strategy.IClientStrategy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public InetAddress serverIP;
    public int serverPort;
    private IClientStrategy strategy;
    public Client(InetAddress serverIP, int serverPort, IClientStrategy strategy)
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.strategy = strategy;
    }
    public void start()
    {
        try (Socket serverSocket = new Socket(serverIP, serverPort)){ //serch the server
            System.out.println("Server started at " + serverIP.getHostAddress() + ":" + serverPort);
            strategy.applyStrategy(serverSocket.getInputStream(),serverSocket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
