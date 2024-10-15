


package Server;

import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
//import IO.MyCompressorOutputStream;
import IO.SimpleCompressorOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class ServerStrategyGenerateMaze implements IServerStrategy {
    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        try {
            Scanner scanner = new Scanner(inFromClient);
            int rows = scanner.nextInt();  // Read number of rows
            int cols = scanner.nextInt();  // Read number of columns

            // Generate the maze
            AMazeGenerator mazeGenerator = new MyMazeGenerator();
            Maze maze = mazeGenerator.generate(rows, cols);

            // Compress the maze and send it back
            SimpleCompressorOutputStream compressor = new SimpleCompressorOutputStream(outToClient);
            compressor.write(maze.toByteArray());

        } catch (IOException e) {
            System.out.println("Error generating maze for client.");
        }
    }
}
