package Server;

import java.io.InputStream;
import java.io.OutputStream;
import Server.IServerStrategy;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

/**
 * ServerStrategyGenerateMaze is a class that implements the IServerStrategy interface.
 * The purpose of this strategy is to generate a maze based on the dimensions provided by the client
 * through the InputStream, and send the generated maze back to the client through the OutputStream.
 */
public class ServerStrategyGenerateMaze implements IServerStrategy {

    /**
     * This method implements the contract of handling a client request.
     * It reads the dimensions of a maze from the InputStream, generates a maze using MyMazeGenerator,
     * and sends the maze data back to the client via the OutputStream.
     *
     * @param inFromClient The InputStream that receives data from the client.
     * @param outToClient The OutputStream used to send data back to the client.
     */
    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        try {
            // Read the maze size (rows and columns) from the client
            byte[] sizeBuffer = new byte[8]; // Buffer to store the incoming size information (4 bytes for rows and 4 bytes for columns)
            inFromClient.read(sizeBuffer); // Read 8 bytes from the client's input stream

            // Convert the byte array to integers representing rows and columns
            int rows = Maze.byteArrayToInt(sizeBuffer, 0); // Read the first 4 bytes as rows
            int cols = Maze.byteArrayToInt(sizeBuffer, 4); // Read the next 4 bytes as columns

            // Create an instance of MyMazeGenerator to generate the maze
            MyMazeGenerator mazeGenerator = new MyMazeGenerator();
            Maze maze = mazeGenerator.generate(rows, cols); // Generate the maze using the provided dimensions

            // Convert the generated maze to a byte array format using the Maze's existing method
            byte[] mazeByteArray = maze.toByteArray();

            // Write the maze data to the output stream to send it back to the client
            outToClient.write(mazeByteArray);
            outToClient.flush(); // Ensure all data is sent immediately

        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace in case of an exception to aid debugging
        }
    }
}
