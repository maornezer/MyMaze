package algorithms.search;
import java.util.*;
/*
Explanation of the code:
Stack using Deque: In DFS, we use a stack that implements "Last In, First Out" (LIFO) behavior. To do this, we use the Deque class and make it behave like a stack (stack) inside the Queue class.
Fixed cost: as in BFS, also in DFS each step costs the same (cost 1), we don't need a complicated heuristic function.
Search process: DFS each time selects the last node added to the stack and starts exploring it in depth before going back.
explanation:
Stack: instead of a queue, we use a stack (Deque) that allows for "Last In, First Out" (LIFO) operation. This is a natural structure for DFS, as it explores each path before going back and testing other paths.
Tracking the states we have visited: We use the fence group to keep track of the states we have visited, which prevents repetition of previous states and optimizes the search.
Retrieving the path: As in BFS, we keep the parents of each state in the mouth so that we can restore the path from the initial state to when a solution is found.
 */
public class DepthFirstSearch implements ISearchingAlgorithm {
    private int numberOfNodesEvaluated;  // Tracks how many nodes were evaluated

    // Constructor
    public DepthFirstSearch() {
        this.numberOfNodesEvaluated = 0;
    }

    // Returns the name of the algorithm
    @Override
    public String getName() {
        return "Depth First Search";
    }

    // Solves the searchable problem using DFS
    @Override
    public Solution solve(ISearchable searchable) {
        // Use a stack for DFS
        Deque<AState> stack = new ArrayDeque<>();
        Set<AState> visited = new HashSet<>();

        // Get start and goal states
        AState startState = searchable.getStartState();
        AState goalState = searchable.getGoalState();

        // Push the start state onto the stack
        stack.push(startState);
        visited.add(startState);

        // Keep track of parents to backtrack the solution path
        Map<AState, AState> parents = new HashMap<>();
        parents.put(startState, null);

        // Main DFS loop
        while (!stack.isEmpty()) {
            AState currentState = stack.pop();  // Get the current state (LIFO)
            numberOfNodesEvaluated++;

            // If we reached the goal state, reconstruct the solution
            if (currentState.equals(goalState)) {
                //System.out.println("Goal found!");
                return backtrackSolution(parents, currentState);
            }

            // Get possible next states
            List<AState> possibleStates = searchable.getAllPossibleStates(currentState);
            for (AState nextState : possibleStates) {
                // If the next state has not been visited
                if (!visited.contains(nextState)) {
                    stack.push(nextState);  // Push to the stack for LIFO behavior
                    visited.add(nextState);
                    parents.put(nextState, currentState);  // Track the parent for solution path
                }
            }
        }

        // If no solution was found, return an empty solution
        return new Solution();
    }

    // Returns the number of nodes evaluated during the search
    @Override
    public int getNumberOfNodesEvaluated() {
        return numberOfNodesEvaluated;
    }

    // Helper method to backtrack and create the solution
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
