package algorithms.mazeGenerators;


public class Maze
{

    private int [][] maze;
    private Position startPosition;
    private Position goalPosition;

    /**
     * constructor 1
     * setting the startPosition of the Maze to be {0, 0}
     * setting the goalPosition of the Maze to be {mazeArr.length - 1, mazeArr[0].length - 1}
     */
    public Maze(int rows, int cols) {
        // Input validation: Ensure rows and columns are positive
        if (rows <= 1 || cols <= 1) {
            throw new IllegalArgumentException("Rows and columns must be greater than one.");
        }

        // Initialize the maze with the given dimensions
        this.maze = new int[rows][cols];

        // Initialize the maze with all 0s
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = 0;
            }
        }

        // Initialize the start and goal positions
        this.startPosition = new Position(0, 0);  // Top-left corner (0, 0)
        this.goalPosition = new Position(rows - 1, cols - 1);  // Bottom-right corner
    }
    // Set the value of a specific cell
    public void setCell(int row, int col, int value) {
        maze[row][col] = value;
    }

    // Get the value of a specific cell
    public int getCell(int row, int col) {
        return maze[row][col];
    }
    public Position getStartPosition() {return startPosition;}

    public Position getGoalPosition() {return goalPosition;}

    public void print() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (i == startPosition.getRow() && j == startPosition.getCol()) {
                    System.out.print("S ");
                } else if (i == goalPosition.getRow() && j == goalPosition.getCol()) {
                    System.out.print('E');
                } else {
                    System.out.print(maze[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
    public int[][] getMaze() {
        return maze;
    }

    public byte[] toByteArray() {
        // Size: 4 bytes for rows, 4 for cols, 4 for start row, 4 for start col,
        // 4 for goal row, 4 for goal col, and the rest for maze data.
        int size = 24 + (maze.length * maze[0].length);
        byte[] byteArray = new byte[size];

        // Store number of rows (4 bytes)
        byteArray[0] = (byte) (maze.length >> 24);
        byteArray[1] = (byte) (maze.length >> 16);
        byteArray[2] = (byte) (maze.length >> 8);
        byteArray[3] = (byte) maze.length;

        // Store number of columns (4 bytes)
        byteArray[4] = (byte) (maze[0].length >> 24);
        byteArray[5] = (byte) (maze[0].length >> 16);
        byteArray[6] = (byte) (maze[0].length >> 8);
        byteArray[7] = (byte) maze[0].length;

        // Store start position (4 bytes for row and 4 bytes for col)
        byteArray[8] = (byte) (startPosition.getRow() >> 24);
        byteArray[9] = (byte) (startPosition.getRow() >> 16);
        byteArray[10] = (byte) (startPosition.getRow() >> 8);
        byteArray[11] = (byte) startPosition.getRow();

        byteArray[12] = (byte) (startPosition.getCol() >> 24);
        byteArray[13] = (byte) (startPosition.getCol() >> 16);
        byteArray[14] = (byte) (startPosition.getCol() >> 8);
        byteArray[15] = (byte) startPosition.getCol();

        // Store goal position (4 bytes for row and 4 bytes for col)
        byteArray[16] = (byte) (goalPosition.getRow() >> 24);
        byteArray[17] = (byte) (goalPosition.getRow() >> 16);
        byteArray[18] = (byte) (goalPosition.getRow() >> 8);
        byteArray[19] = (byte) goalPosition.getRow();

        byteArray[20] = (byte) (goalPosition.getCol() >> 24);
        byteArray[21] = (byte) (goalPosition.getCol() >> 16);
        byteArray[22] = (byte) (goalPosition.getCol() >> 8);
        byteArray[23] = (byte) goalPosition.getCol();

        // Store maze values (0s and 1s)
        int index = 24;
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                byteArray[index++] = (byte) maze[i][j];
            }
        }

        return byteArray;  // Return the constructed byte array
    }
    /**
     * Constructs a Maze object from a byte array.
     * The byte array contains the maze's dimensions, start and goal positions,
     * and the values of the maze cells.
     *
     * @param b the byte array representing the maze.
     */
    //The process of reconstructing a labyrinth from compression
    public Maze(byte[] b) {
        //We use & to ensure that the value is kept positive. The reason is that in Java, a byte value can be negative (minus 128 to 127), and the mask 0xFF (which is 255 in decimal) ensures that the value stays in the range 0-255.
        //After each shift, we use the OR operator to combine all the parts of the bytes into a whole value (32 parts). This is how the full number of "rows" or "cols" is obtained.
        // Retrieve the number of rows
        int rows = ((b[0] & 0xFF) << 24) | ((b[1] & 0xFF) << 16) | ((b[2] & 0xFF) << 8) | (b[3] & 0xFF);

        // Retrieve the number of columns
        int cols = ((b[4] & 0xFF) << 24) | ((b[5] & 0xFF) << 16) | ((b[6] & 0xFF) << 8) | (b[7] & 0xFF);

        // Initialize the maze
        this.maze = new int[rows][cols];

        // Retrieve start position (row and column)
        int startRow = ((b[8] & 0xFF) << 24) | ((b[9] & 0xFF) << 16) | ((b[10] & 0xFF) << 8) | (b[11] & 0xFF);
        int startCol = ((b[12] & 0xFF) << 24) | ((b[13] & 0xFF) << 16) | ((b[14] & 0xFF) << 8) | (b[15] & 0xFF);
        this.startPosition = new Position(startRow, startCol);

        // Retrieve goal position (row and column)
        int goalRow = ((b[16] & 0xFF) << 24) | ((b[17] & 0xFF) << 16) | ((b[18] & 0xFF) << 8) | (b[19] & 0xFF);
        int goalCol = ((b[20] & 0xFF) << 24) | ((b[21] & 0xFF) << 16) | ((b[22] & 0xFF) << 8) | (b[23] & 0xFF);
        this.goalPosition = new Position(goalRow, goalCol);

        // Fill in the maze values
        int index = 24;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.maze[i][j] = b[index++];
            }
        }
    }

        public static int byteArrayToInt(byte[] byteArray, int start) {
        // Combine 4 consecutive bytes into an integer using bitwise operations
        return ((byteArray[start] & 0xFF) << 24) | ((byteArray[start + 1] & 0xFF) << 16) | ((byteArray[start + 2] & 0xFF) << 8) | (byteArray[start + 3] & 0xFF);
    }
}
