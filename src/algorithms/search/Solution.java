package algorithms.search;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Solution implements Serializable {
    private ArrayList<AState> solutionPath;  // Holds the sequence of states that form the solution

    // Constructor for an empty solution
    public Solution() {
        this.solutionPath = new ArrayList<>();  // Initialize an empty path
    }

    // Constructor for a solution with a given path
    public Solution(ArrayList<AState> solutionPath) {
        this.solutionPath = solutionPath;
    }

    // Returns the solution path as a list of states
    public ArrayList<AState> getSolutionPath() {
        return solutionPath;
    }

    // Prints the solution path
    @Override
    public String toString() {
        if (solutionPath.isEmpty()) {
            return "No solution found";
        }
        StringBuilder sb = new StringBuilder();
        for (AState state : solutionPath) {
            sb.append(state).append(" -> ");
        }
        return sb.substring(0, sb.length() - 4);  // Remove the last " -> "
    }
}
