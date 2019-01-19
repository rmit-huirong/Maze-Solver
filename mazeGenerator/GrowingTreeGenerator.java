package mazeGenerator;

import java.util.*;

import maze.*;

/**
 * 
 * @author Huirong Huang
 * @author Yangming An
 * Class for generating maze using growing tree.
 */
public class GrowingTreeGenerator implements MazeGenerator {
	// Growing tree maze generator. As it is very general, here we implement as
	// "usually pick the most recent cell, but occasionally pick a random cell"

	/** 
     * Use growing tree to generate a maze.
     * 
     * ******************************************************************************************
     * 
     * ALGORITHM growing tree
     * use growing tree on a maze with all walls in order to generate a perfect maze.
     * Input: Maze maze.
     * OUTPUT : Maze maze which is perfect.
     * 
     * 1: z = {}, visited = {}, neighUnvisited == {}
     * 2: pick a random starting cell and add it to z and visited
     * 3: while (z.size() > 0)
     * 4: { 
     * 5: 	10% to pick a random cell and 90% to pick the most recent cell from z and name it as b
     * 6:   if (b has at least one unvisited neigbour)
     * 7:	{
     * 8:		randomly select a neighbour from neighUnvisited
     * 9: 		carve path between them
     * 10:		add the neighbour to z
     * 11:	}
     * 12:	else
     * 13:	{
     * 14:		remove b from z
     * 15:	}
     * 16:}		
     * 17:a perfect maze is generated 
     * 
     * ******************************************************************************************
     * 
	 * generator properties
	 */
	// 10% to pick a random cell and 90% to pick the most recent cell
	double threshold = 0.1;

	protected Cell startCell = null;
	protected Random random = new Random();
	protected Stack<Cell> z = new Stack<Cell>();
	// the collection of all the visited cells, used for termination
	protected ArrayList<Cell> visited = new ArrayList<Cell>();

	/**
	 * Generate a new maze.
	 * 
	 * @param maze The reference of Maze object object to generate.
	 */
	public void generateMaze(Maze maze) {

		double numDouble = -1;
		int numInt = -1;

		startMaze(maze);

		z.add(startCell);
		visited.add(startCell);

		while (!z.isEmpty()) {
			
			// usually pick the most recent cell, occasionally pick a random cell from z
			numDouble = random.nextDouble(); // 0 - 1
			if (numDouble < threshold) {
				numInt = random.nextInt(z.size());
				startCell = z.get(numInt);
			} else {
				startCell = z.peek();
			}
			growingTree(maze, startCell);
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
	 * Function for using modified prim's algorithm.
	 * 
	 * @param maze
	 *            The reference of Maze object to run growing tree algorithm.
	 * @param b
	 *            The cell picked from z
	 */
	public void growingTree(Maze maze, Cell b) {

		ArrayList<Cell> neighUnvisited = new ArrayList<Cell>();
		Cell neighCell = null;

		int i = 0, j = 0;

		int num = -1, dir = -1;

		// add the unvisited neighbouring cells of cell b to neighUnvisited
		for (i = 0; i < Maze.NUM_DIR; i++) {
			if (b.neigh[i] != null && !visited.contains(b.neigh[i])) {
				neighUnvisited.add(b.neigh[i]);
			}
		}

		if (!neighUnvisited.isEmpty()) {
			num = random.nextInt(neighUnvisited.size());
			neighCell = neighUnvisited.get(num);

			neighUnvisited.clear();
			for (j = 0; j < Maze.NUM_DIR; j++) {
				if (b.neigh[j] == neighCell) {
					dir = j;
				}
			}
			b.wall[dir].present = false;
			neighCell.wall[Maze.oppoDir[dir]].present = false;

			z.add(neighCell);
			if (!visited.contains(neighCell)) {
				visited.add(neighCell);
			}
		} else {
			z.remove(b);
		}
	} // end of growingTree()
} // end of class GrowingTreeGenerator
