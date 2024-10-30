package test;
import Client.Client;
import Client.IClientStrategy;
import IO.SimpleDecompressorInputStream;
import Server.*;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class RunCommunicateWithServers
{
    public static void main(String[] args)
    {
        //Initializing servers
        Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());

        // Starting servers
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();

        //Communicating with servers
        CommunicateWithServer_MazeGenerating();
        CommunicateWithServer_SolveSearchProblem();

        //Stopping all servers
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }

    private static void CommunicateWithServer_MazeGenerating() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {

                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{10,10};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed withMyCompressor) from server
                        InputStream is = new SimpleDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        //SimpleDecompressorInputStream decompressorInputStream = new SimpleDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[124/*ENTER SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressedmaze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        //decompressorInputStream.read(decompressedMaze);
                        Maze maze = new Maze(decompressedMaze);
                        maze.print();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    private static void CommunicateWithServer_SolveSearchProblem() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {

                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        MyMazeGenerator mg = new MyMazeGenerator();
                        Maze maze = mg.generate(10, 10);
                        maze.print();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed withMyCompressor) from server
                        //Print Maze Solution retrieved from the server
                        System.out.println(String.format("Solution steps:"));
                        printMazeWithSolution(maze, mazeSolution);
                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                        for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                            System.out.println(String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    // Make printMazeWithSolution static to be called from main
    public static void printMazeWithSolution(Maze maze, Solution solution) {
        int[][] mazeArray = maze.getMaze();
        List<AState> solutionPath = solution.getSolutionPath();

        // Create a copy of the maze for printing the solution
        char[][] displayMaze = new char[mazeArray.length][mazeArray[0].length];

        // Initialize the display maze with the original maze values
        for (int i = 0; i < mazeArray.length; i++) {
            for (int j = 0; j < mazeArray[i].length; j++) {
                displayMaze[i][j] = mazeArray[i][j] == 1 ? '1' : '0'; // '1' for walls, '0' for empty spaces
            }
        }

        // Mark the solution path with '+'
        for (AState state : solutionPath) {
            if (state instanceof MazeState) {
                MazeState mazeState = (MazeState) state;
                displayMaze[mazeState.getRow()][mazeState.getCol()] = '█';

            }
        }

        // Mark the start and goal positions explicitly
        Position startPosition = maze.getStartPosition();
        Position goalPosition = maze.getGoalPosition();
        displayMaze[startPosition.getRow()][startPosition.getCol()] = 'S'; // Start position
        displayMaze[goalPosition.getRow()][goalPosition.getCol()] = 'E';   // End position

        // Print the maze with the solution path
        for (int i = 0; i < displayMaze.length; i++) {
            for (int j = 0; j < displayMaze[i].length; j++) {
                System.out.print(displayMaze[i][j] + " ");
            }
            System.out.println();
        }
    }

}
