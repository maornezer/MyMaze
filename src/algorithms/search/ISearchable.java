package algorithms.search;

import java.util.List;

public interface ISearchable
{
    // Returns the start state of the problem
    AState getStartState();

    // Returns the goal state of the problem
    AState getGoalState();

    // Returns a list of all possible states reachable from the given state
    List<AState> getAllPossibleStates(AState state);
}
