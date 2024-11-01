package Model;

import Client.*;
import IO.SimpleDecompressorInputStream;
import Server.Server;
import algorithms.mazeGenerators.Maze;
import algorithms.search.ISearchable;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;
import Server.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class MyModelServerClient {

    public Maze mymaze;
    public Solution solution;

    /**
     * the function that solve the maze that it's getting, using Server Client to choose in runtime the strategy
     * @param maze
     */
    public void ServerSolvingMaze(Maze maze){
        //Initializing server
        Server server = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        //Starting server
        server.start();
        //Communicating with server
        CommunicateWithServer_SolveSearchProblem(maze);
        //Stopping server
        server.stop();
    }

    /**
     * communicate server client to solve the maze with the way the client choose
     * @param  maze
     */
    private void CommunicateWithServer_SolveSearchProblem(Maze maze) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server?????????
                        toServer.flush();
                        //read generated maze (compressed with SimpleCompressor) from server
                        Solution mazeSolution = (Solution) fromServer.readObject();
                        solution = mazeSolution;
                        System.out.println("CommunicateWithServer_SolveSearchProblem: " + solution);
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

//    /**
//     * the function that generate the Maze using Server Client strategy to choose in runtime the type of generating
//     * @param row
//     * @param col
//     */
//    public void ServerGenerateMaze(int row, int col){
//        //Initializing server
//        Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
//        //Starting server
//        mazeGeneratingServer.start();
//        //Communicating with server
//        CommunicateWithServer_MazeGenerating(row,col);
//        //Stopping server
//        mazeGeneratingServer.stop();
//    }
//    /**
//     * communicate server client to generate the maze as the client wants
//     * @param row
//     * @param col
//     */
//    private void CommunicateWithServer_MazeGenerating(int row, int col) {
//        try {
//            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
//                @Override
//                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
//                    try {
//                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
//                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
//                        toServer.flush();
//                        int[] mazeDimensions = new int[]{row, col};
//                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
//                        toServer.flush();
//                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with SimpleCompressor) from server
//                        InputStream is = new SimpleDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
//                        byte[] decompressedMaze = new byte[10000000 /*CHANGE SIZE ACCORDING TO YOUR MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
//                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
//                        Maze maze = new Maze(decompressedMaze);
//                        mymaze = maze;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            client.communicateWithServer();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//    }



}