package mazeGenerator;

import java.util.*;

import maze.*;

/**
 * 
 * @author Huirong Huang
 * @author Yangming An
 * Class for generating maze using Recursive Backtracker.
 */
public class RecursiveBacktrackerGenerator implements MazeGenerator {

	 /** 
     * Use depth first search to fulfill a recursive backtracker to generate a maze.
     * 
     * ******************************************************************************************
     * 
     * ALGORITHM DFS (RecursiveBacktracker)
     * Perform a Depth first search on a maze with all walls in order to generate a perfect maze.
     * Input: Maze maze.
     * OUTPUT : Maze maze which is perfect.
     * 
     * 1: visited = {}, neighUnvisited = {}
     * 2: pick a random starting cell
     * 3: while (visited.size() < mazeSize)
     * 4: { 
     * 5:	add all unvisited neighbouring cells to neighUnvisited
     * 6: 	pick a random unvisited neighbouring cell
     * 7: 	carve path between them
     * 8:	move to that neighbour
     * 9:	repeat 5~8 as recursion
     * 10:	backtrack to a cell that has unvisited neighbours
     * 11:}			
     * 12:a perfect maze is generated 
     * 
     * ******************************************************************************************
     * 
	 * generator properties
	 */
	protected Cell startCell = null;
	protected Random random = new Random();
	// the collection of cells which the generator has gone through by order, used for backtracking
	protected Stack<Cell> path = new Stack<Cell>();
	// the collection of cells which the generator has visited
	protected ArrayList<Cell> visited = new ArrayList<Cell>();

	/**
	 * Function that generate a perfect maze from an all-wall initialized maze.
	 * 
	 * @param maze The reference of Maze object to generate.
	 */
	public void generateMaze(Maze maze) {

		int mazeSize = maze.sizeR * maze.sizeC;

		startMaze(maze);

		path.push(startCell);
		visited.add(startCell);

		// If current cell is one of the end of the tunnel, go through the tunnel and move to the other end.
		if (startCell.tunnelTo != null) {
			startCell = startCell.tunnelTo;
			path.push(startCell);
			visited.add(startCell);
		}

		// If the generator has visited all the cells, generation completed.
		while (visited.size() < mazeSize) {
			recursion(maze, startCell);
			backTracking();
		}

	} // end of generateMaze()

	/**
	 * Randomly pick a starting cell.
	 * 
	 * @param maze The reference of Maze object to pick a starting cell.
	 */
	public void startMaze(Maze maze) {

		int r, c = 0;

		// pick a random starting cell
		while (startCell == null) {
			r = random.nextInt(maze.sizeR);
			c = random.nextInt(maze.sizeC);
			startCell = maze.map[r][c];
		}

	} // end of startMaze()

	/**
	 * Pick a random unvisited neighbouring cell, then carve a path; continue until a cell that has no unvisited 
	 * neighbours is reached.
	 * 
	 * @param maze The reference of Maze object to do the recursion.
	 * @param currCell The current cell which the generator is visiting.
	 */
	public void recursion(Maze maze, Cell currCell) {

		// the collection of unvisited neighbouring cells, used for random picking
		ArrayList<Cell> neighUnvisited = new ArrayList<Cell>();
		Cell neighCell = null;

		int i = 0, j = 0;

		int num = -1, dir = -1;

		for (i = 0; i < Maze.NUM_DIR; i++) {
			if (currCell.neigh[i] != null && !visited.contains(currCell.neigh[i])) {
				neighUnvisited.add(currCell.neigh[i]);
			}
		}

		if (!neighUnvisited.isEmpty()) {
			num = random.nextInt(neighUnvisited.size());
			neighCell = neighUnvisited.get(num);

			neighUnvisited.clear();
			for (j = 0; j < Maze.NUM_DIR; j++) {
				if (currCell.neigh[j] == neighCell) {
					dir = j;
					// carve the path between the two cells
					currCell.wall[dir].present = false;
					currCell = neighCell;
					path.push(currCell);
					visited.add(currCell);

					currCell.wall[Maze.oppoDir[dir]].present = false;

					if (currCell.tunnelTo != null) {
						currCell = currCell.tunnelTo;
						path.push(currCell);
						visited.add(currCell);
					}

					recursion(maze, currCell);
				}
			}
		}
	} // end of recursion()

	/**
	 * Backtrack a cell.
	 */
	public void backTracking() {

		if (!path.isEmpty()) {
			
			// remove the cell the generator currently visited
			path.pop();
			if (!path.isEmpty()) {
				
				// get the latest cell the generator has visited (not the current cell)
				startCell = path.peek();
			}
		}
	} // end of backTracking()

} // end of class RecursiveBacktrackerGenerator
