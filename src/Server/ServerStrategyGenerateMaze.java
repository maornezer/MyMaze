//package Server;
//
//import java.io.InputStream;
//import java.io.OutputStream;
//import Server.IServerStrategy;
//import algorithms.mazeGenerators.Maze;
//import algorithms.mazeGenerators.MyMazeGenerator;
//
///**
// * ServerStrategyGenerateMaze is a class that implements the IServerStrategy interface.
// * The purpose of this strategy is to generate a maze based on the dimensions provided by the client
// * through the InputStream, and send the generated maze back to the client through the OutputStream.
// */
//public class ServerStrategyGenerateMaze implements IServerStrategy {
//
//    /**
//     * This method implements the contract of handling a client request.
//     * It reads the dimensions of a maze from the InputStream, generates a maze using MyMazeGenerator,
//     * and sends the maze data back to the client via the OutputStream.
//     *
//     * @param inFromClient The InputStream that receives data from the client.
//     * @param outToClient The OutputStream used to send data back to the client.
//     */
//    @Override
//    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
//        try {
//            // Read the maze size (rows and columns) from the client
//            byte[] sizeBuffer = new byte[8]; // Buffer to store the incoming size information (4 bytes for rows and 4 bytes for columns)
//            inFromClient.read(sizeBuffer); // Read 8 bytes from the client's input stream
//
//            // Convert the byte array to integers representing rows and columns
//            int rows = Maze.byteArrayToInt(sizeBuffer, 0); // Read the first 4 bytes as rows
//            int cols = Maze.byteArrayToInt(sizeBuffer, 4); // Read the next 4 bytes as columns
//
//            // Create an instance of MyMazeGenerator to generate the maze
//            MyMazeGenerator mazeGenerator = new MyMazeGenerator();
//            Maze maze = mazeGenerator.generate(rows, cols); // Generate the maze using the provided dimensions
//
//            // Convert the generated maze to a byte array format using the Maze's existing method
//            byte[] mazeByteArray = maze.toByteArray();
//
//            // Write the maze data to the output stream to send it back to the client
//            outToClient.write(mazeByteArray);
//            outToClient.flush(); // Ensure all data is sent immediately
//
//        } catch (Exception e) {
//            e.printStackTrace(); // Print the stack trace in case of an exception to aid debugging
//        }
//    }
//}
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
