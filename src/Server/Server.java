package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//    ServerSocket myServer;
//    try
//    {
//        myServer = new ServerSocket(port number);
//    }
//    catch(IOException e)
//    {
//        System.out.println(e);
//    }
/*
The central department, the one responsible for managing the server and running the strategy.
Its purpose is to run the server in the background, manage client requests, and work with defined strategies.
 */
/**
 * The Server class represents a server that listens for client connections
 * and performs a specific action using a given strategy.
 */
public class Server {

    private int port; //the port number where the server will listen for connections from clients.
    private int listeningInterval; //how long the server will wait between checking for new connections.
    private IServerStrategy serverStrategy; //The strategy (defined by the IServerStrategy interface) to be used by the server when a client sends a request.
    private volatile boolean stop; //a boolean variable that indicates whether the server should stop running.
    private ExecutorService threadPool; // Thread pool to handle multiple clients
    //volatile boolean stop:
    //
    //The volatile marking indicates that the value of the variable can be changed at any moment by different processes (threads),
    // therefore every time it is accessed, it must be read directly from the main memory (RAM) and not use a copy stored in the processor's cache (cache).
    //In the case of your server, since there may be additional processes that change the value of stop (for example when calling the stop() function),
    // you must make sure that all access to the variable is consistent and up-to-date.
    /**
     * Constructor to initialize the server with given parameters.
     *
     * @param port Port to listen on.
     * @param listeningInterval The interval for accepting client connections.
     * @param serverStrategy The strategy used to handle client requests.
     */
    public Server(int port, int listeningInterval, IServerStrategy serverStrategy) {
        this.port = port;
        this.listeningInterval = listeningInterval;
        this.serverStrategy = serverStrategy;
        this.threadPool = Executors.newFixedThreadPool(10); // Default thread pool size
        //// Create a cached thread pool to handle multiple clients concurrently
    }
    /**
     * Starts the server and listens for client connections.
     * This method runs the server in a separate thread.
     * It accepts client connections and assigns each connection to a new thread from the thread pool.
     */
    public void start() {
        //() -> runServer() is a lambda expression, which is a shorthand way to define an implementation of the Runnable interface.
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server started, waiting for clients...");

                while (!stop) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        //The accept() method waits for a client to connect and returns a Socket object representing the client connection.
                        //This call is blocking, meaning it waits until a connection is made.
                        System.out.println("Client connected.");
                        threadPool.submit(() -> handleClient(clientSocket));
                        //Once a client connects, the server submits a new task to the thread pool.
                        //The task (() -> handleClient(clientSocket)) is a lambda expression that calls the handleClient() method with the connected clientSocket.
                        //This allows multiple clients to be handled concurrently.
                    } catch (IOException e) {
                        if (stop) {
                            System.out.println("Server is shutting down.");
                        } else {
                            System.out.println("Error accepting client connection.");
                        }
                    }
                }
                threadPool.shutdown();
            } catch (IOException e) {
                System.out.println("Could not start server on port " + port);
            }
            //After the server stops (when stop is true), the thread pool is shut down.
            //This prevents new tasks from being submitted and gracefully shuts down existing threads.
        }).start();
    }
    /**
     * Handles a connected client by applying the server strategy.
     * This method is called by threads in the thread pool when a client is connected.
     *
     * @param clientSocket The client's socket.
     */
    private void handleClient(Socket clientSocket) {
        try {
            serverStrategy.handleClient(clientSocket.getInputStream(), clientSocket.getOutputStream());
            clientSocket.close();// Close the client socket after handling the request.
        } catch (IOException e) {
            System.out.println("Error handling client.");
        }
    }
    /**
     * Stops the server by setting the stop flag and shutting down the thread pool.
     * This method will cause the server to stop accepting new client connections.
     */
    public void stop() {
        stop = true;
    }
}
