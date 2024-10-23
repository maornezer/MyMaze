package Server;

import java.io.InputStream;
import java.io.OutputStream;
/*
This is an interface class that explains the "contract" between the server and the strategy
 that will be implemented on it.
  That is, any class that implements this interface will have to implement specific functions to run any strategy on a server.
 */
public interface IServerStrategy {
    void applyStrategy(InputStream inFromClient, OutputStream outToClient);
}
/*
What is InputStream and OutputStream?
These are objects that represent a stream of information - the information that enters the server (via InputStream) and the information that leaves the server (via OutputStream). Here they represented the information flowing in and out.
What does applyStrategy do?
This function is the "contract". Each class that implements this interface will have to define the way the server will behave: how it will read the information (InputStream) and what it will do with the outgoing information (OutputStream).
 */