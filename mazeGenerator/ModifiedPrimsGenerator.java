package mazeGenerator;

import java.util.*;

import maze.*;

/**
 * 
 * @author Huirong Huang
 * @author Yangming An
 * Class for generating maze using Prim's algorithm.
 */
public class ModifiedPrimsGenerator implements MazeGenerator {

	/** 
     * Use modified Prim's algorithm to generate a maze.
     * 
     * ******************************************************************************************
     * 
     * ALGORITHM modified Prim's
     * Use modified Prim's algorithm on a maze with all walls in order to generate a perfect maze.
     * Input: Maze maze.
     * OUTPUT : Maze maze which is perfect.
     * 
     * 1: z = {}, f = {}
     * 2: pick a random starting cell
     * 3: put all neighbouring cells into f
     * 4: while (f.size() > 0)
     * 5: { 
     * 6:	randomly pick a cell c and remove it from f
     * 7: 	randomly pick a cell b in z which adjacent to cell c
     * 8: 	carve path between b and c
     * 9:	add cell c to z
     * 10:	add all neighbours of cell c to f
     * 11:}			
     * 12:a perfect maze is generated 
     * 
     * ******************************************************************************************
     * 
	 * generator properties
	 */
	protected Cell startCell = null;
	protected Random random = new Random();
	// Set z for adding cells
	protected Set<Cell> z = new HashSet<Cell>();
	// ArrayList f for storing all of the unvisited neighbours
	protected ArrayList<Cell> f = new ArrayList<Cell>();

	/**
	 * Function that generate a perfect maze from an all-wall initialized maze.
	 * 
	 * @param maze The reference of Maze object to generate.
	 */
	public void generateMaze(Maze maze) {

		int i = 0;

		startMaze(maze);
		z.add(startCell);
		
		// add all neighbours
		for (i = 0; i < Maze.NUM_DIR; i++) {
			if (startCell.neigh[i] != null && !f.contains(startCell.neigh[i])) {
				f.add(startCell.neigh[i]);
			}
		}
		while (!f.isEmpty()) {
			modiPrim(maze);
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
	 * @param maze The reference of Maze object to run modified prim's algorithm.
	 */
	public void modiPrim(Maze maze) {
		int num1 = -1, num2 = -1, dir = -1;
		int i = 0, j = 0, k = 0, l = 0;
		Cell b = null;
		Cell c = null;
		Set<Cell> x = new HashSet<Cell>();
		
		// Select cell c from set f and delete.
		num1 = random.nextInt(f.size());
		c = f.remove(num1);
		
		// put all the neighbours of c to x in order to randomly pick one
		for (i = 0; i < Maze.NUM_DIR; i++) {
			if (z.contains(c.neigh[i])) {
				x.add(c.neigh[i]);
			}
		}
		num2 = random.nextInt(x.size());

		for (Cell cell : x) {
			if (l == num2) {
				b = cell;
			}
			l++;
		}
		if (b != null) {
			for (j = 0; j < Maze.NUM_DIR; j++) {
				if (b == c.neigh[j]) {
					dir = j;
				}
			}
			
			// curve path between c and b
			c.wall[dir].present = false;
			b.wall[Maze.oppoDir[dir]].present = false;
			
			// Add c to set Z and all c's neighbors to set F.
			z.add(c);
			for (k = 0; k < Maze.NUM_DIR; k++) {
				if (c.neigh[k] != null && !f.contains(c.neigh[k]) && !z.contains(c.neigh[k])) {
					f.add(c.neigh[k]);
				}
			}
		}
	} //end of prims()

} // end of class ModifiedPrimsGenerator
