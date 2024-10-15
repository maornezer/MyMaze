package test;
import IO.SimpleCompressorOutputStream;
import IO.SimpleDecompressorInputStream;
import algorithms.mazeGenerators.*;

import java.io.*;
import java.util.Arrays;

public class RunCompressDecompressMaze {
    public static void main(String[] args) {
        String mazeFileName = "savedMaze.maze";
        AMazeGenerator mazeGenerator = new MyMazeGenerator();
        Maze maze = mazeGenerator.generate(1000, 1000); // Generate new maze

        // 1. Print the size of the maze in memory before compression
        System.out.println("Maze size in bytes before compression: " + maze.toByteArray().length);

        try {
            // 2. Save the compressed maze to a file
            OutputStream out = new SimpleCompressorOutputStream(new FileOutputStream(mazeFileName));
            out.write(maze.toByteArray());
            out.flush();
            out.close();

            // 3. Print the size of the saved file
            File file = new File(mazeFileName);
            System.out.println("Compressed maze file size in bytes: " + file.length());

        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] savedMazeBytes = new byte[0];
        try {
            // 4. Read the maze from the file
            InputStream in = new SimpleDecompressorInputStream(new FileInputStream(mazeFileName));
            savedMazeBytes = new byte[maze.toByteArray().length];
            in.read(savedMazeBytes);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 5. Print the size of the decompressed maze
        System.out.println("Maze size after decompression: " + savedMazeBytes.length);

        Maze loadedMaze = new Maze(savedMazeBytes);
        boolean areMazesEquals = Arrays.equals(loadedMaze.toByteArray(), maze.toByteArray());

        // 6. Print whether the original and decompressed mazes are identical
        System.out.println(String.format("Are the original and loaded mazes equal: %s", areMazesEquals));
    }
}
