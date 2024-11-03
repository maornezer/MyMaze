package Server;

import IO.SimpleCompressorOutputStream;
import algorithms.mazeGenerators.*;

import java.io.*;

/**
 * ServerStrategyGenerateMaze is a class that implements the IServerStrategy interface.
 * It generates a maze based on the client's request and sends it back using a compressed representation.
 */
public class ServerStrategyGenerateMaze implements IServerStrategy {

    /**
     * A function that receives a request from a client
     * and generates a maze according to the parameters (row, column) requested by the client.
     *
     * @param inFromClient The server's input stream to receive data from the client.
     * @param outToClient  The server's output stream to send data to the client.
     */
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        try (ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
             ObjectOutputStream toClient = new ObjectOutputStream(outToClient);

             ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
             SimpleCompressorOutputStream compressor = new SimpleCompressorOutputStream(byteOutputStream)) {

            // Read maze dimensions from the client (rows and columns)
            int[] mazeSizes = (int[]) fromClient.readObject(); // [rows, cols]
            // The client sends an int array containing the number of rows and columns for the maze.

            // Generate the maze using MyMazeGenerator
            MyMazeGenerator mazeGenerator = new MyMazeGenerator();
            //AMazeGenerator mazeGenerator = new AMazeGenerator();

            Maze maze = mazeGenerator.generate(mazeSizes[0], mazeSizes[1]);
            // Use MyMazeGenerator to create a new maze with the specified dimensions.

            // Compress the generated maze
            compressor.write(maze.toByteArray());
            compressor.flush(); // Ensure all data is compressed properly
            // The generated maze is converted to a byte array, which is then compressed using SimpleCompressorOutputStream.

            // Send the compressed maze data back to the client
            toClient.writeObject(byteOutputStream.toByteArray());
            toClient.flush(); // Ensure all data is sent to the client
            // The compressed byte array is written to the client's output stream as an object.

        } catch (Exception e) {
            e.printStackTrace();
            // In case of any exceptions, print the stack trace for debugging purposes.
        }
    }
}
