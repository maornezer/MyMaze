package algorithms.search;
public interface ISearchingAlgorithm
{
    // Returns the name of the algorithm
    String getName();

    // Performs the search and returns the solution to the given problem
    Solution solve(ISearchable searchable);

    // Returns the number of nodes that were evaluated during the search
    int getNumberOfNodesEvaluated();
}
