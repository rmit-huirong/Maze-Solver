package mazeSolver;

import java.util.*;

import maze.*;

/**
 * 
 * @author Huirong Huang
 * @author Yangming An
 * Class for solving maze using bidirectional recursive backtracker.
 */
public class BiDirectionalRecursiveBacktrackerSolver implements MazeSolver {

	/** 
     * Use depth first search to fulfill a recursive backtracker to solve a maze.
     * 
     * ******************************************************************************************
     * 
     * ALGORITHM DFS (biRecursiveBacktracker)
     * Perform two Depth first search from both entrance and exit to solve a perfect maze.
     * Input: Maze maze.
     * OUTPUT :  Maze maze which is solved.
     * 
     * 1: visitedA = {}, visitedB = {}, neighUnvisited = {}, explored = {}
     * 2: pick two starting cells respectively for recursion A and recursion B
     * 3: while (A and B are not met)
     * 4: { 
     * 5:	add all unvisited neighbouring cells to neighUnvisited
     * 6: 	pick a random unvisited neighbouring cell
     * 7:	move to that neighbour
     * 8:	repeat 5~8 as recursion A for cell A
     * 9:	backtrack to a cell for cell A that has unvisited neighbours
     * 10:  repeat 5~8 as recursion B for cell B
     * 11:	backtrack to a cell for cell B that has unvisited neighbours
     * 12:}			
     * 13:the perfect maze is solved
     * 
     * ******************************************************************************************
     * 
	 * generator properties
	 */
	boolean isSolved = false;
	protected Cell cellA = null;
	protected Cell cellB = null;
	protected Random random = new Random();
	
	// the collection of cells which the generator has gone through by order, used for backtracking
	protected Stack<Cell> pathA = new Stack<Cell>();
	protected Stack<Cell> pathB = new Stack<Cell>();
	
	protected ArrayList<Cell> visitedA = new ArrayList<Cell>();
	protected ArrayList<Cell> visitedB = new ArrayList<Cell>();
	protected Set<Cell> explored = new HashSet<Cell>();

	/**
	 * Function for bidirectional recursive backtracker.
	 * 
	 * @param maze The reference of Maze object to be solved.
	 */
	public void solveMaze(Maze maze) {
		boolean hasMet = false;
		int i = 0;
		cellA = maze.entrance;
		cellB = maze.exit;

		pathA.push(cellA);
		visitedA.add(cellA);
		maze.drawFtPrt(cellA);
		explored.add(cellA);
		pathB.push(cellB);
		visitedB.add(cellB);
		maze.drawFtPrt(cellB);
		explored.add(cellB);

		if (cellA.tunnelTo != null) {
			cellA = cellA.tunnelTo;
			pathA.push(cellA);
			visitedA.add(cellA);
			maze.drawFtPrt(cellA);
			explored.add(cellA);
		}

		if (cellB.tunnelTo != null) {
			cellB = cellB.tunnelTo;
			pathB.push(cellB);
			visitedB.add(cellB);
			maze.drawFtPrt(cellB);
			explored.add(cellB);
		}
		
		// stop when A and B have met
		while (!hasMet) {
			recursion(maze, cellA);
			backTrackingA();
			recursion(maze, cellB);
			backTrackingB();
			for (i = 0; i < visitedB.size(); i++) {
				if (visitedA.contains(visitedB.get(i))) {
					hasMet = true;
				}
			}
			for (i = 0; i < visitedA.size(); i++) {
				if (visitedB.contains(visitedA.get(i))) {
					hasMet = true;
				}
			}
		}
		isSolved = true;
	} // end of solveMaze()

	/**
	 * Pick a random unvisited neighbouring cell, move to that cell; continue until a cell that has no unvisited
	 *  neighbours is reached.
	 */
	public void recursion(Maze maze, Cell currCell) {

		ArrayList<Cell> neighUnvisited = new ArrayList<Cell>();
		Cell neighCell = null;

		int i = 0;

		int num = -1;

		if (currCell == cellA) {
			for (i = 0; i < Maze.NUM_DIR; i++) {
				if (currCell.neigh[i] != null && !visitedA.contains(currCell.neigh[i])) {
					if (currCell.wall[i].present == false) {
						neighUnvisited.add(currCell.neigh[i]);
					}
				}
			}
		} else {
			for (i = 0; i < Maze.NUM_DIR; i++) {
				if (currCell.neigh[i] != null && !visitedB.contains(currCell.neigh[i])) {
					if (currCell.wall[i].present == false) {
						neighUnvisited.add(currCell.neigh[i]);
					}
				}
			}
		}

		if (!neighUnvisited.isEmpty()) {
			num = random.nextInt(neighUnvisited.size());
			neighCell = neighUnvisited.get(num);

			neighUnvisited.clear();
			if (currCell == cellA) {
				currCell = neighCell;
				cellA = currCell;
				pathA.push(cellA);
				visitedA.add(cellA);
				maze.drawFtPrt(cellA);
				explored.add(cellA);
			} else {
				currCell = neighCell;
				cellB = currCell;
				pathB.push(cellB);
				visitedB.add(cellB);
				maze.drawFtPrt(cellB);
				explored.add(cellB);
			}

			if (currCell.tunnelTo != null) {
				currCell = currCell.tunnelTo;
				if (currCell.tunnelTo == cellA) {
					cellA = currCell;
					pathA.push(cellA);
					visitedA.add(cellA);
					maze.drawFtPrt(cellA);
					explored.add(cellA);
				} else {
					cellB = currCell;
					pathB.push(cellB);
					visitedB.add(cellB);
					maze.drawFtPrt(cellB);
					explored.add(cellB);
				}
			}

			recursion(maze, currCell);

		}
	} // end of recursion()

	/**
	 * Backtrack thr cell A that has unvisited neighbours and repeat the recursive function.
	 */
	public void backTrackingA() {

		if (!pathA.isEmpty()) {
			pathA.pop();
			if (!pathA.isEmpty()) {
				cellA = pathA.peek();
			}
		}
	} // end of startMaze()

	/**
	 * Backtrack the cell B that has unvisited neighbours and repeat the recursive function.
	 */
	public void backTrackingB() {

		if (!pathB.isEmpty()) {
			pathB.pop();
			if (!pathB.isEmpty()) {
				cellB = pathB.peek();
			}
		}
	} // end of startMaze()

	/**
	 * Use after solveMaze(maze), to check whether the maze is solved.
	 * @return True if solved. Otherwise false.
	 */
	public boolean isSolved() {
		if (isSolved) {
			return true;
		} else {
			return false;
		}
	} // end if isSolved()

	/**
	 * Use after solveMaze(maze), counting the number of cells explored in solving process.
	 * @return The number of cells explored.
	 * It is not required to be accurate and no marks are given (or lost) on it. 
	 */
	public int cellsExplored() {

		// two same cells are repeated.
		return explored.size();
	} // end of cellsExplored()

} // end of class BiDirectionalRecursiveBackTrackerSolver
