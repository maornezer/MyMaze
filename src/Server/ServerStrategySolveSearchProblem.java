package Server;
import algorithms.mazeGenerators.Maze;
import algorithms.search.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ServerStrategySolveSearchProblem implements the IServerStrategy interface.
 * It is responsible for solving maze problems based on client requests.
 * The strategy is to solve a maze and either provide an existing solution or generate a new one.
 */
public class ServerStrategySolveSearchProblem implements IServerStrategy {

    private static final String TEMP_DIRECTORY_PATH = System.getProperty("java.io.tmpdir"); // Temp directory for storing solutions
    private static Map<Integer, String> solutionsCache = new HashMap<>(); // Cache to store solutions using maze hash

    private Object filetolook = new Object();


    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        try (ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
             ObjectOutputStream toClient = new ObjectOutputStream(outToClient)) {

            // Read maze from client
            Maze maze = (Maze) fromClient.readObject(); // Deserialize the maze object from client input

            // Compute hash of the maze to determine if we already have a solution
            int mazeHash = maze.hashCode(); // Generate hash based on maze structure

            // Check if the solution already exists in the cache
            if (solutionsCache.containsKey(mazeHash)) {
                // Load existing solution
                String solutionFileName = solutionsCache.get(mazeHash);
                try (ObjectInputStream solutionReader = new ObjectInputStream(new FileInputStream(solutionFileName))) {
                    Solution existingSolution = (Solution) solutionReader.readObject();
                    toClient.writeObject(existingSolution); // Send the existing solution back to client
                    return;
                }
            }

            // If no solution exists, solve the maze
            Configurations con = Configurations.getInstance();
            String solutionType = con.getProp("mazeSearchingAlgorithm");
            ISearchingAlgorithm searcher;
            //choose the Solving type using the configuration file
            if(solutionType.equals("BreadthFirstSearch"))
            {
                System.out.println("class ServerStrategySolveSearchProblem: Breadth first search");
                searcher = new BreadthFirstSearch();
            }
            else if(solutionType.equals("BestFirstSearch"))
            {
                System.out.println("class ServerStrategySolveSearchProblem: Best first search");
                searcher= new BestFirstSearch();
            }
            else{
                System.out.println("class ServerStrategySolveSearchProblem: Depth first search");
                searcher = new DepthFirstSearch();
            }

            SearchableMaze searchableMaze = new SearchableMaze(maze);
            Solution solution = searcher.solve(searchableMaze); // Solve the maze

            // Save the new solution to a file
            String solutionFileName = TEMP_DIRECTORY_PATH + "maze_solution_" + mazeHash + ".sol";
            solutionsCache.put(mazeHash, solutionFileName);
            try (ObjectOutputStream solutionWriter = new ObjectOutputStream(new FileOutputStream(solutionFileName))) {
                solutionWriter.writeObject(solution);
            }

            // Send the new solution to the client
            toClient.writeObject(solution);

        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions and print stack trace for debugging
        }
    }
}
