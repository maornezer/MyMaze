package algorithms.search;
import java.util.*;
/*
Breadth-First Search (BFS) Explanation:
Queue-Based Exploration: BFS explores the graph or maze level by level. It uses a queue to maintain the order of nodes to be visited. It first processes the start state, then its neighbors, then the neighbors of those neighbors, and so on. This ensures that states closer to the start are explored first, leading to the shortest path in terms of the number of steps.

Tracking Visited States: To avoid revisiting the same state multiple times, a Set is used to track which states have already been visited. This helps avoid unnecessary reprocessing and loops, making the algorithm more efficient.

Parent Map for Path Reconstruction: As BFS progresses, it records the parent of each state (the state from which it was reached) in a Map. This allows the algorithm to trace back the solution once the goal state is found, reconstructing the path from the start to the goal.

Goal Detection: The algorithm checks each state as it is dequeued to see if it matches the goal state. If the goal is reached, the algorithm stops and reconstructs the path to the goal using the parent map.

Guaranteed Shortest Path: BFS guarantees that it finds the shortest path (in terms of the number of steps) in an unweighted graph or maze. This is because it explores all nodes at the current distance from the start before moving on to nodes at the next distance.

Efficiency – Nodes Evaluated: The algorithm keeps track of the number of states (or nodes) it processes during the search. This is important for measuring the performance of the algorithm, as it shows how many nodes were checked before finding the goal.


 */
public class BreadthFirstSearch implements ISearchingAlgorithm
{
    private int numberOfNodesEvaluated;  // Keeps track of how many nodes were evaluated

    // Constructor
    public BreadthFirstSearch() {
        this.numberOfNodesEvaluated = 0;
    }

    // Returns the name of the algorithm
    @Override
    public String getName() {
        return "Breadth First Search (BFS)";
    }

    // Solves the searchable problem using BFS
    @Override
    public Solution solve(ISearchable searchable) {
        // Queue for BFS
        Queue<AState> queue = new LinkedList<>();
        Set<AState> visited = new HashSet<>();

        // Get the start and goal states
        AState startState = searchable.getStartState();
        AState goalState = searchable.getGoalState();

        // Enqueue the start state
        queue.add(startState);
        visited.add(startState);

        // Keep a map of parent states to reconstruct the solution path
        Map<AState, AState> parents = new HashMap<>();
        parents.put(startState, null);

        // While the queue is not empty
        while (!queue.isEmpty()) {
            AState currentState = queue.poll();  // Get the next state from the queue
            numberOfNodesEvaluated++;  // Increment the number of nodes evaluated

            // If we reached the goal, construct the solution
            if (currentState.equals(goalState)) {
                //System.out.println("Goal found!");
                return backtrackSolution(parents, currentState);
            }

            // Get all possible next states
            List<AState> possibleStates = searchable.getAllPossibleStates(currentState);
            for (AState nextState : possibleStates) {
                // If the next state has not been visited, enqueue it
                if (!visited.contains(nextState)) {
                    queue.add(nextState);
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

    // Helper method to reconstruct the solution path from the goal to the start
    private Solution backtrackSolution(Map<AState, AState> parents, AState goalState) {
        List<AState> path = new ArrayList<>();
        AState currentState = goalState;

        // Trace back the path from the goal state to the start state
        while (currentState != null)
        {
            //System.out.println("Backtracking: " + currentState);
            path.add(currentState);
            currentState = parents.get(currentState);
        }

        // Reverse the path to get the correct order from start to goal
        Collections.reverse(path);
        return new Solution(path);
    }
}
