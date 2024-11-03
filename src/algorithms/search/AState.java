package algorithms.search;
import java.io.Serializable;

public abstract class AState implements Serializable
{
    /*
    This class defines a basic state with a string representation and a cost.
    It will be extended by specific state classes (like MazeState).
     */
    private String state; // A string representation of the state
    private double cost;  // The cost to reach this state

    // Constructor
    public AState(String state) {
        this.state = state;
        this.cost = 0;
    }

    // Returns the state string
    public String getState() {
        return state;
    }

    // Sets the cost to reach this state
    public void setCost(double cost) {
        this.cost = cost;
    }

    // Returns the cost to reach this state
    public double getCost() {
        return cost;
    }

    // Compares two states for equality
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AState aState = (AState) obj;
        return state.equals(aState.state);
    }

    // Generates a hash code for this state
    @Override
    public int hashCode() {
        return state.hashCode();
    }

    // Provides a string representation of the state
    @Override
    public String toString() {
        return state;
    }
}
