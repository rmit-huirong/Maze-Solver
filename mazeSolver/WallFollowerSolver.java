package mazeSolver;

import java.util.*;

import maze.*;

/**
 * 
 * @author Huirong Huang
 * @author Yangming An
 * Class for solving maze using Wall Follower.
 */
public class WallFollowerSolver implements MazeSolver {
	
	/** 
     * Use wall follower algorithm to solve the maze.
     * 
     * ******************************************************************************************
     * 
     * ALGORITHM wall follower (right-handed)
     * Use wall follower algorithm on a perfect maze in order to solve it.
     * Input: Maze maze.
     * OUTPUT : Maze maze which is solved.
     * 
     * 1: visited = {}
     * 2: pick the entrance of maze as the starting cell
     * 3: get the entrance direction
     * 4: while (currentCell != maze.exit)
     * 5: {
     * 6:   if (current cell is the end of a tunnel)
     * 7:	{
     * 8:		go through the tunnel to get to the other end
     * 9:       if its a dead end, move back
     * 10:	}
     * 11:  turn right (turn 135 degrees if its a hex maze)
     * 12: 	while (there is no way)
     * 13: 	{
     * 14:		turn left (turn 45 degrees if its a hex maze)
     * 15: 	}
     * 16:	move to next cell on that direction
     * 17:}
     * 18:the perfect maze is solved 
     * 
     * ******************************************************************************************
     * 
	 * generator properties
	 */
	Set<Cell> visited = new HashSet<Cell>();
	Cell startCell = null;
	Cell currCell = null;
	boolean isSolved = false;
	public final static int hexDir[] = { 0, 1, 2, 3, 4, 5 };
	public final static int hexRightestDir[] = { 4, 5, 0, 1, 2, 3 };
	public final static int hexLeftDir[] = { 1, 2, 3, 4, 5, 0 };
	public final static int normDir[] = { 0, 2, 3, 5 };
	public final static int normRightestDir[] = { 5, 0, 2, 3 };
	public final static int normLeftDir[] = { 2, 3, 5, 0 };
	
	// the collection of marked tunnels
	Set<Cell> tunnelMarked = new HashSet<Cell>();
	
	// the collection of gone through tunnels
	Set<Cell> tunnelGoThrough = new HashSet<Cell>();

	
	int dir = -1, oppoDir = -1, rightest = -1, nextDir = -1, correctDir = -1;

	/**
	 * Function for right-handed wall follower.
	 * 
	 * @param maze The reference of Maze object to be solved.
	 */
	public void solveMaze(Maze maze) {

		int a = 0, b = 0, c = 0, d = 0, i = 0, j = 0;
		int normDirIndex = -1, normNextDirIndex = -1, hexDirIndex = -1, hexNextDirIndex = -1, tunnelIndex = -1;
		
		// the list of the tunnels
		Cell[][] tunnelList = new Cell[maze.sizeTunnel][2];
		
		// the original direction when entered the tunnels
		int[] tunnelDir = new int[maze.sizeTunnel];
		
		// the rights for accessing the tunnels
		boolean[] noTunnelAccess = new boolean[maze.sizeTunnel];
		
		// the times of accessing the tunnels
		int[] tunnelAcessTimes = new int[maze.sizeTunnel];
		if (maze.type == Maze.TUNNEL)
		{			
			for (a = 0; a < maze.sizeR; a++)
			{
				for (b = 0; b < maze.sizeC; b++)
				{
					if (maze.map[a][b].tunnelTo != null && !tunnelMarked.contains(maze.map[a][b].tunnelTo))
					{
						tunnelList[c][0] = maze.map[a][b];
						tunnelList[c][1] = maze.map[a][b].tunnelTo;
						tunnelMarked.add(maze.map[a][b]);
						tunnelMarked.add(maze.map[a][b].tunnelTo);
						c++;
					}
				}
			}
			
		}		
		startCell = maze.entrance;
		currCell = startCell;
		maze.drawFtPrt(currCell);
		visited.add(currCell);		
		
		// get the direction of entrance
		while (oppoDir == -1) {

			if (currCell.neigh[i] == null && currCell.wall[i] != null) {
				oppoDir = i;
			}
			i++;
		}

		dir = Maze.oppoDir[oppoDir];
		if (maze.type == Maze.NORMAL || maze.type == Maze.TUNNEL)
		{
			for (j = 0; j < normDir.length; j++) {
				if (dir == normDir[j]) {
					normDirIndex = j;
				}
			}
		}
		else
		{
			for (j = 0; j < hexDir.length; j++) {
				if (dir == hexDir[j]) {
					hexDirIndex = j;
				}
			}
		}

		while (currCell != maze.exit) {

			if (currCell.tunnelTo != null)
			{
				// get the index of current tunnel
				for (d = 0; d < maze.sizeTunnel; d++) 
				{
					if (currCell.tunnelTo == tunnelList[d][0] || currCell.tunnelTo == tunnelList[d][1])
					{
						tunnelIndex = d;
					}
				}
				int deadEnd = 0;
				for (i = 0; i < normDir.length; i++)
				{
					dir = normDir[i]; 
					if (currCell.wall[dir] != null)
					{
						if (currCell.wall[dir].present == true)
						{
							deadEnd++;
						}
					}
				}
				
				// if this is a dead end, that is all four sides with walls, move back and set the access to false
				if (deadEnd == 4)
				{
					currCell = currCell.tunnelTo;
					noTunnelAccess[tunnelIndex] = true;
					maze.drawFtPrt(currCell);
					visited.add(currCell);
				}
				// if this tunnel has been gone through and can be gone through again
				if (tunnelGoThrough.contains(currCell) && !noTunnelAccess[tunnelIndex])
				{
					boolean changeDir = true;
					int times = 0;
					
					// check four sides if there is a way to move
					do
					{
						if (currCell.neigh[dir] != null && !visited.contains(currCell.neigh[dir])) {
							if (currCell.wall[dir] != null) {
								if (!currCell.wall[dir].present) {
									correctDir = dir;
									currCell = currCell.neigh[dir];
									maze.drawFtPrt(currCell);
									visited.add(currCell);
									changeDir = false;
								}
							}
						}
						
						// change direction
						if (changeDir)
						{
							for (j = 0; j < normDir.length; j++) {
								if (dir == normDir[j]) {
									normDirIndex = j;
								}
							}
							dir = normRightestDir[normDirIndex];
							times++;
						}
					} while(changeDir && times < 3);
					
					// there is no way to move rather than move back in the tunnel
					if (times >= 3)
					{
						for (d = 0; d < maze.sizeTunnel; d++)
						{
							if (currCell.tunnelTo == tunnelList[d][0] || currCell.tunnelTo == tunnelList[d][1])
							{
								dir = tunnelDir[d];
								noTunnelAccess[d] = true;
								currCell = currCell.tunnelTo;
								tunnelAcessTimes[tunnelIndex]++;
								maze.drawFtPrt(currCell);
								for (j = 0; j < normDir.length; j++) {
									if (dir == normDir[j]) {
										normDirIndex = j;
									}
								}
							}
						}
					}					
				}
				
				// the tunnel hasn't been gone through
				else
				{
					visited.add(currCell);
					tunnelGoThrough.add(currCell);
					currCell = currCell.tunnelTo;
					tunnelAcessTimes[tunnelIndex]++;
					maze.drawFtPrt(currCell);
					visited.add(currCell);
					tunnelGoThrough.add(currCell);
					for (d = 0; d < maze.sizeTunnel; d++)
					{
						if (currCell.tunnelTo == tunnelList[d][0] || currCell.tunnelTo == tunnelList[d][1])
						{
							tunnelDir[d] = dir;
						}
						
					}
				}
				
				// a tunnel can be accessed no more than 8 times
				if (tunnelAcessTimes[tunnelIndex] >= 8)
				{
					noTunnelAccess[tunnelIndex] = true;
				}
				
			}	
			
				//change direction to rightest
				if (maze.type == Maze.NORMAL || maze.type == Maze.TUNNEL)
				{
					rightest = normRightestDir[normDirIndex];
				}
				else if (maze.type == Maze.HEX)
				{
					rightest = hexRightestDir[hexDirIndex];
				}
	
				nextDir = rightest; 
	
				if (maze.type == Maze.NORMAL || maze.type == Maze.TUNNEL)
				{
					for (j = 0; j < normDir.length; j++) {
						if (nextDir == normDir[j]) {
							normNextDirIndex = j; 
						}
					}
				}
				else
				{
					for (j = 0; j < hexDir.length; j++) {
						if (nextDir == hexDir[j]) {
							hexNextDirIndex = j; 
						}
					}
				}
				do {
					if (currCell.neigh[nextDir] != null) {
						if (currCell.wall[nextDir] != null) {
							if (!currCell.wall[nextDir].present) {
								correctDir = nextDir;
							} else {
								if (maze.type == Maze.NORMAL || maze.type == Maze.TUNNEL)
								{
									for (j = 0; j < normDir.length; j++) {
										if (nextDir == normDir[j]) {
											normNextDirIndex = j; 
										}
									}
									nextDir = normLeftDir[normNextDirIndex];
								}
								else
								{
									for (j = 0; j < hexDir.length; j++) {
										if (nextDir == hexDir[j]) {
											hexNextDirIndex = j; 
										}
									}
									nextDir = hexLeftDir[hexNextDirIndex];
								}
							}
						} else {
							if (maze.type == Maze.NORMAL || maze.type == Maze.TUNNEL)
							{
								for (j = 0; j < normDir.length; j++) {
									if (nextDir == normDir[j]) {
										normNextDirIndex = j; 
									}
								}
								nextDir = normLeftDir[normNextDirIndex];
							}
							else
							{
								for (j = 0; j < hexDir.length; j++) {
									if (nextDir == hexDir[j]) {
										hexNextDirIndex = j; 
									}
								}
								nextDir = hexLeftDir[hexNextDirIndex];
							}
						}
					} else {
						if (maze.type == Maze.NORMAL || maze.type == Maze.TUNNEL)
						{
							for (j = 0; j < normDir.length; j++) {
								if (nextDir == normDir[j]) {
									normNextDirIndex = j; 
								}
							}
							nextDir = normLeftDir[normNextDirIndex];
						}
						else
						{
							for (j = 0; j < hexDir.length; j++) {
								if (nextDir == hexDir[j]) {
									hexNextDirIndex = j; 
								}
							}
							nextDir = hexLeftDir[hexNextDirIndex];
						}
					}
				} while (correctDir == -1);
				
				currCell = currCell.neigh[correctDir];
				
				maze.drawFtPrt(currCell);
				visited.add(currCell);
				dir = correctDir;
				
				if (maze.type == Maze.NORMAL || maze.type == Maze.TUNNEL)
				{
					for (j = 0; j < normDir.length; j++) {
						if (dir == normDir[j]) {
							normDirIndex = j;
						}
					}
					
				}
				else
				{
					for (j = 0; j < hexDir.length; j++) {
						if (dir == hexDir[j]) {
							hexDirIndex = j;
						}
					}
				}
				correctDir = -1;
			}
		
		
		if (currCell == maze.exit)
		{
			isSolved = true;
		}
	} // end of solveMaze()

	/**
	 * Use after solveMaze(maze), to check whether the maze is solved.
	 * @return True if solved. Otherwise false.
	 */
	public boolean isSolved() {
		if (isSolved)
		{
			return true;
		}
		else
		{
			return false;
		}
	} // end if isSolved()

	/**
	 * Use after solveMaze(maze), counting the number of cells explored in solving process.
	 * @return The number of cells explored.
	 * It is not required to be accurate and no marks are given (or lost) on it. 
	 */
	public int cellsExplored() {
		return visited.size();
	} // end of cellsExplored()

} // end of class WallFollowerSolver
