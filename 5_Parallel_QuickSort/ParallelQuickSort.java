
/**
 * ParallelQuickSort.java 
 */

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Modified by: Rajesh Sakhamuru
 * 
 * Modified on: 8/4/2019
 * 
 * This program has been modified to use quick sort instead of merge sort to
 * sort an array of integers
 * 
 * 
 * This class is a simplified version of java.util.parallelSort().
 * 
 * @author philip gust
 */
@SuppressWarnings("serial")
public class ParallelQuickSort extends RecursiveAction
{

	/** value array, and indexes to interval [left …right) in the array. */
	int values[], left, right;

	/**
	 * Construct parallel sorter for values array from [left, right) using scratch
	 * array as temporary storage
	 * 
	 * @param values the array to sort
	 * @param left   index of first element to sort
	 * @param right  index beyond last value to sort
	 */
	ParallelQuickSort(int[] values, int left, int right)
	{
		this.values = values;
		this.left = left;
		this.right = right;
	}

	/**
	 * Divide input by pivot value which is the first unused pivot at the lowest
	 * index, and invoke sort them as concurrent sub-tasks. If size of input is
	 * below threshold or only one thread available, use standard sort.
	 *
	 * @see java.util.concurrent.RecursiveTask#compute()
	 */
	@Override
	protected void compute()
	{
		int len = right - left;
		if ((len <= 1) || (ForkJoinPool.getCommonPoolParallelism() == 1))
		{
			System.out.printf("%s: %d %d\n", Thread.currentThread().getName(), left, right);
		}
		else
		{
			int pivotIndex = left; // leftmost index of unsorted data is pivot
			int storeIndex = pivotIndex + 1;
			for (int i = storeIndex; i < values.length; i++)
			{
				// puts values less than pivot value to one side of the storeIndex
				if (values[i] < values[pivotIndex])
				{
					swap(values, i, storeIndex);
					storeIndex++;
				}
			}

			// places the pivot value between values less than and values greater than
			// it on either side by swapping with the storeIndex location
			swap(values, pivotIndex, storeIndex - 1);

			System.out.printf("%s: %d %d %d\n", Thread.currentThread().getName(), left, storeIndex, right);

			// run sub-tasks, automatically forking for one of the two, and wait for
			// completion of both sub-tasks
			invokeAll(new ParallelQuickSort(values, left, storeIndex),
					new ParallelQuickSort(values, storeIndex, right));
		}
	}

	/**
	 * swaps 2 values at their indicated indeces at x and y in the values array
	 *
	 * @param values - int[] - array of integers
	 * @param x      - int - one location of value to be swapped
	 * @param y      - int - other locaion of value to be swapped
	 */
	private void swap(int[] values, int x, int y)
	{
		int temp = values[x];
		values[x] = values[y];
		values[y] = temp;
	}

	/**
	 * Parallel sort an array
	 *
	 * @param values
	 */
	static public void sort(int[] values)
	{
		ForkJoinPool.commonPool().invoke(new ParallelQuickSort(values, 0, values.length));
	}

	/**
	 * Sort sample array using parallel sorter.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		int values[] = { 0, 2, 10, 5, -6, 7, 20, 2 };
//		int values[] = { 3, 2, 11, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1, 2, 3, 4, 5, 6, 7, 8, 7, 7 };
		ParallelQuickSort.sort(values);
		for (int val : values)
		{
			System.out.printf("%d ", val);
		}
		System.out.println();
	}
}
