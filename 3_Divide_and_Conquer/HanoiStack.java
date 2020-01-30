
/**
 * HanoiStack.java
 * 
 * The Tower of Hanoi puzzle is about moving a stack of decreasing sized disks
 * from 1 rod (in a 3 rod system) to another rod.
 * 
 * The Rules:
 * 
 * • Only one disk can be moved at a time.
 * 
 * • Each one moves the top disk from one stack on top of another stack.
 * 
 * • No disk may be placed on top of a smaller disk.
 * 
 * • The minimal number of moves required to solve a Tower of Hanoi puzzle is 2n
 * − 1, where n is the number of disks.
 * 
 * 
 * 
 * @author Rajesh Sakhamuru
 * @version 7/17/2019
 * 
 */
public class HanoiStack
{

	/**
	 * The main of this program runs the Tower of Hanoi puzzle from pole 0 to 2
	 * using 1, 2, 3 and 4 disks in separate simulations. Each simulation is called
	 * with the hanoi() method.
	 * 
	 * @param args - String[] - not used
	 */
	public static void main(String[] args)
	{
		hanoi(1, 0, 2);
		hanoi(2, 0, 2);
		hanoi(3, 0, 2);
		hanoi(4, 0, 2);
	}

	/**
	 * This function runs one simulation of the Tower of Hanoi puzzle. It is run
	 * based on the given number of disks, source pole and destination pole.
	 * 
	 * Tower is initialized to a 3x(ndisks) array. The puzzle solution is arrived at
	 * through an iterative loop of the algorithm run using moveDisk() and checking
	 * after each run of the algorithm that the solution has been reached.
	 * 
	 * 
	 * @param ndisks - int - number of disks on the src pole
	 * @param src    - int - number representing source pole 0, 1, or 2.
	 * @param dest   - int - number representing destination pole 0, 1, or 2.
	 */
	private static void hanoi(int ndisks, int src, int dest)
	{
		// number of steps taken towards completing the puzzle.
		int stepCount = 0;
		// 3 x ndisks array representing the 3 poles.
		int array[][] = new int[3][ndisks];

		initializeTower(array, ndisks, src);

		towerPrint(array, ndisks);

		if (ndisks % 2 == 0) // even ndisks
		{
			while (true)
			{
				if (checkFinished(array[dest]))
				{
					System.out.printf("DISKS: %d, STEPS: %d \n\n", ndisks, stepCount);
					System.out.println("FINISHED\n--------------------");
					break;
				}
				moveDisk(array, 0, 1);
				stepCount++;
				towerPrint(array, ndisks);

				moveDisk(array, 0, 2);
				stepCount++;
				towerPrint(array, ndisks);

				moveDisk(array, 1, 2);
				stepCount++;
				towerPrint(array, ndisks);

			}
		}
		else // odd ndisks
		{
			moveDisk(array, 0, 2);
			stepCount++;
			towerPrint(array, ndisks);

			while (true)
			{
				if (checkFinished(array[dest]))
				{
					System.out.printf("DISKS: %d, STEPS: %d \n\n", ndisks, stepCount);
					System.out.println("FINISHED\n--------------------");
					break;
				}

				moveDisk(array, 0, 1);
				stepCount++;
				towerPrint(array, ndisks);

				moveDisk(array, 1, 2);
				stepCount++;
				towerPrint(array, ndisks);

				moveDisk(array, 0, 2);
				stepCount++;
				towerPrint(array, ndisks);

			}
		}

	}

	/**
	 * takes a 3x(ndisks) array and initializes the src pole to have the disks with
	 * the smallest one on top. The largest disk is 1. The smallest disk is
	 * 'ndisks'. The "top" of the stack is the one with the highest index and does
	 * not hold 0.
	 * 
	 * @param array  - int[][] - 3xndisks array with everything == to null. It is
	 *               initialized with integers in this function
	 * @param ndisks - int - number of disks to be put on the starting src pole
	 * @param src    - the number indicating which pole in the array is to be a src
	 *               pole where all of the disks start at
	 */
	private static void initializeTower(int array[][], int ndisks, int src)
	{
		for (int n = 0; n < 3; n++)
		{
			if (n == src)
			{
				// largest disk is 1. smallest disk is 'ndisks'.
				for (int d = 0; d < ndisks; d++)
				{
					array[n][d] = d + 1;
				}
			}
			else
			{
				for (int d = 0; d < ndisks; d++)
				{
					array[n][d] = 0;
				}
			}
		}
	}

	/**
	 * This method takes the hanoi puzzle array and the numbers of 2 poles as src
	 * and dest. The method moves the smallest tile between the 2 poles to the other
	 * pole.
	 * 
	 * @param array - int[][] - 3xndisks array representing the tower of hanoi
	 *              puzzle
	 * @param src   - int - one of the poles, disk can move to or from it
	 * @param dest  - int - one of the poles, disk can move to or from it
	 */
	private static void moveDisk(int array[][], int src, int dest)
	{

		int srcTop = 0;
		int srcHeight = 0;
		int destTop = 0;
		int destHeight = 0;

		// largest disk is 1. smallest disk is highest integer.
		// find highest disk on src pole
		for (int n = array[src].length - 1; n >= 0; n--) // Searches from top of pole
		{
			if (array[src][n] != 0)
			{
				srcTop = array[src][n];
				srcHeight = n;
				break;
			}
		}
		// find highest disk on dest pole
		for (int n = array[dest].length - 1; n >= 0; n--)
		{
			if (array[dest][n] != 0)
			{
				destTop = array[dest][n];
				destHeight = n;
				break;
			}
		}

		// if dest has any disk and that the top of dest pole is greater than than
		// (smaller disk than) the top of the source pole, then dest is moved to src
		if ((destTop > 0) && (srcTop < destTop))
		{
			// dest moved to src
			if (srcTop != 0)
			{
				srcHeight++;
			}
			array[src][srcHeight] = array[dest][destHeight];
			array[dest][destHeight] = 0;
			System.out.printf("Move disk from pole %d to pole %d\n\n", dest, src);
		}
		// if src has any disk and that the top of src pole is greater than than
		// (smaller disk than) the top of the dest pole, then src is moved to dest
		else if ((srcTop > 0) && (destTop < srcTop))
		{
			// src moved to dest
			if (destTop != 0)
			{
				destHeight++;
			}
			array[dest][destHeight] = array[src][srcHeight];
			array[src][srcHeight] = 0;
			System.out.printf("Move disk from pole %d to pole %d\n\n", src, dest);

		}
	}

	/**
	 * This method checks if the dest row (given as parameter) is full (has no '0s')
	 * which indicates that the puzzle has been solved.
	 * 
	 * @param array - int[] - destination pole provided as parameter
	 * @return boolean - true if puzzle is solved. otherwise, false.
	 */
	private static boolean checkFinished(int array[])
	{

		boolean finished = true;

		for (int d = 0; d < array.length; d++)
		{
			if (array[d] == 0)
			{
				finished = false;
				break;
			}
		}
		return finished;
	}

	/**
	 * Prints the three towers (labeled from 0 to 2) in 3 rows from largest disks
	 * (smallest number) on the left to smallest disk (highest number) on the right
	 * on each pole.
	 * 
	 * @param array  - int[][] - 3xndisks array representing the tower of hanoi
	 *               puzzle
	 * @param ndisks - int - number of disks to be put on the starting src pole
	 */
	private static void towerPrint(int array[][], int ndisks)
	{
		for (int n = 0; n < 3; n++)
		{
			System.out.printf("%d: ", n);
			for (int d = 0; d < ndisks; d++)
			{
				if (ndisks - 1 != d)
				{
					System.out.printf("%d,", array[n][d]);
				}
				else
				{
					System.out.printf("%d", array[n][d]);
				}

			}
			System.out.println();
		}
		System.out.println();
	}
}
