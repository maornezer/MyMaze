package algorithms.search;
/*
Explanation:
Priority Queue: Unlike BFS, which uses a simple queue, Best-First Search uses a priority queue (often implemented as a heap). In this algorithm, states are prioritized based on their estimated distance (or cost) to the goal.
Heuristic-Based Search: The algorithm uses a heuristic to estimate how close each state is to the goal and selects the state with the best (lowest) estimated cost to explore next. This makes the search more efficient in some cases but doesn't guarantee the shortest path in terms of steps.
 Best-First Search Explanation:
Priority Queue: This algorithm uses a priority queue to prioritize the exploration of nodes. States are enqueued based on their cost, which is determined by a heuristic function.

Heuristic Function: The algorithm uses a heuristic (such as the Manhattan distance between two points) to estimate the cost of moving from the current state to the goal state. This makes the search more targeted toward the goal.

Backtracking: Similar to BFS, we maintain a parent map to reconstruct the solution path once the goal is reached.

Efficiency: The algorithm evaluates fewer nodes by focusing on the most promising paths. However, depending on the heuristic, it may not always find the shortest path in terms of steps.
 */
import java.util.*;

public class BestFirstSearch implements ISearchingAlgorithm {
    private int numberOfNodesEvaluated;  // Keeps track of how many nodes were evaluated

    // Constructor
    public BestFirstSearch() {
        this.numberOfNodesEvaluated = 0;
    }

    // Returns the name of the algorithm
    @Override
    public String getName() {
        return "Best First Search";
    }

    // Solves the searchable problem using Best-First Search
    @Override
    public Solution solve(ISearchable searchable) {
        // Priority queue for Best-First Search
        PriorityQueue<AState> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(AState::getCost));
        Set<AState> visited = new HashSet<>();

        // Get the start and goal states
        AState startState = searchable.getStartState();
        AState goalState = searchable.getGoalState();

        // Set initial cost for the start state and enqueue it
        startState.setCost(0);
        priorityQueue.add(startState);
        visited.add(startState);

        // Keep a map of parent states to reconstruct the solution path
        Map<AState, AState> parents = new HashMap<>();
        parents.put(startState, null);

        // While the priority queue is not empty
        while (!priorityQueue.isEmpty()) {
            AState currentState = priorityQueue.poll();  // Get the state with the lowest cost
            numberOfNodesEvaluated++;  // Increment the number of nodes evaluated

            // If we reached the goal, construct the solution
            if (currentState.equals(goalState)) {
                //System.out.println("Goal found!");
                return backtrackSolution(parents, currentState);
            }

            // Get all possible next states
            List<AState> possibleStates = searchable.getAllPossibleStates(currentState);
            for (AState nextState : possibleStates) {
                // If the next state has not been visited
                if (!visited.contains(nextState)) {
                    nextState.setCost(currentState.getCost() + heuristic(nextState, goalState));
                    priorityQueue.add(nextState);
                    visited.add(nextState);
                    parents.put(nextState, currentState);  // Keep track of how we reached this state
                }
            }
        }

        // If the goal was not found, return an empty solution
        return new Solution();
    }

    // Returns the number of nodes evaluated during the search
    @Override
    public int getNumberOfNodesEvaluated() {
        return numberOfNodesEvaluated;
    }

    // Heuristic function to estimate the cost to the goal
    private double heuristic(AState currentState, AState goalState) {
        // In a maze, a common heuristic is the Manhattan distance between two points
        MazeState current = (MazeState) currentState;
        MazeState goal = (MazeState) goalState;
        return Math.abs(current.getRow() - goal.getRow()) + Math.abs(current.getCol() - goal.getCol());
    }

    // Helper method to reconstruct the solution path from the goal to the start
    private Solution backtrackSolution(Map<AState, AState> parents, AState goalState) {
        ArrayList<AState> path = new ArrayList<>();
        AState currentState = goalState;

        // Trace back the path from the goal state to the start state
        while (currentState != null) {
            path.add(currentState);
            currentState = parents.get(currentState);
        }

        // Reverse the path to get the correct order from start to goal
        Collections.reverse(path);
        return new Solution(path);
    }
}
