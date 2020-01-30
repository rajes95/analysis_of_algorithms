/*
 * Edited by:   Rajesh Sakhamuru
 * Modified on: 7/15/19
 *
 * Description of the steps that I applied to the original recursive version of
 * select0() to transform it into its iterative form:
 *
 * 1. Identify base condition (k==p) and invert it in a while loop
 *
 * 2. Reasign p between iterations in the same way that it would be rerun
 *    within each call of the recursive function
 *
 * 3. 'hi' and 'lo' are reassigned between iterations based on the select0()
 *    function calls in the recursive version within a logically similar
 *    if/else statement.
 *
 * 4. return same code (a[k]) after the while loop as you would if the base
 *    case was reached recursively.
 *
 *-------------------------------------------
 *
 * select.c
 *
 * Select the k-th smallest item in an unsorted array using Hoare's
 * select algorithm. The assignment is to use the techniques shown
 * in lecture 2 and the lecture notes to transform the recursive
 * function select0() into an iterative version, and to provide a
 * description in the comment block below of the steps that you
 * applied to the original recursive version to transform it into
 * its iterative form.
 */

/* Description of steps to transform recursive to iterative form.
 * Provide enough detail that the grader can follow your steps.
 *
 */

#include "CUnit/CUnit.h"
#include "CUnit/Basic.h"
#include <stdlib.h>

/**
 * A utility function to swap two values.
 * @param a the array to be partitioned
 * @param i the first value
 * @param j the second index
 */
void swap(int a[], int i, int j)
{
	int t = a[i];
	a[i] = a[j];
	a[j] = t;
}

/**
 * This function places the last element as pivot at
 * its correct position in sorted array 'a'
 * array, and places all smaller (smaller than pivot)
 * to left of pivot and all greater elements to right
 * of pivot.
 * @param a the array to be partitioned
 * @param lo starting index
 * @param hi ending index
 * @return the partitioning index
 */
int partition(int a[], int lo, int hi)
{
	int pv = hi; 	// pivot about hi value
	int i = lo - 1; 	// index of smaller element
	for (int j = lo; j <= hi - 1; j++)
	{
		if (a[j] <= a[pv])
		{
			swap(a, ++i, j);  // increment index of smaller element
		}
	}
	swap(a, ++i, pv); // increment index of smaller element
	return i;
}

/**
 * This function selects the kth smallest itmem from the
 * input array 'a' between the lower index 'lo' and the
 * higher index 'hi'.
 *
 * @param a array to be selected,
 * @param lo lower index
 * @param hi higher index
 * @param k kth smallest item index
 * @return the kth smallest item
 */
int select0(int a[], int lo, int hi, int k)
{
//	 Iterative function code:
	if (lo >= hi)
	{
		return a[lo];
	}
	else
	{
		// p is partitioning index
		int p = partition(a, lo, hi);
		while (k != p) // inverted base case
		{
			if (k >= p)
			{
				lo = p + 1;
			}
			else
			{
				hi = p - 1;
			}
			p = partition(a, lo, hi); // uses a modified lo or hi value
		}
		return a[k];
	}
//      ORIGINAL RECURSIVE CODE IN ELSE STATEMENT:
//		// Partitioning index
//		int p = partition(a, lo, hi);
//		if (k == p)
//		{
//			return a[k];
//		}
//		else if (k < p)
//		{
//			return select0(a, lo, p - 1, k);
//		}
//		else
//		{
//			return select0(a, p + 1, hi, k);
//		}
}

/**
 * This function selects the kth smallest itmem from the
 * input array 'a'.
 *
 * @param a array to be selected,
 * @param n number of items in array
 * @param k kth smallest item index
 * @return the kth smallest item
 */
int selectt(int a[], int n, int k)
{
	return select0(a, 0, n - 1, k);
}

/**
 * Tests select() by calling it with variously sized
 * arrays (1, 2, 3 and more elements) and selection
 * values (e.g. first value  last value, middle values).
 */
void test_select()
{
	// first value
	int array1[] = { 10 };
	int actual = selectt(array1, 1, 0);
	CU_ASSERT_EQUAL(actual, 10);

	// last value
	int array2[] = { 20, 10 };
	actual = selectt(array2, 2, 1);
	CU_ASSERT_EQUAL(actual, 20);

	// middle value
	int array3[] = { 30, 10, 20 };
	actual = selectt(array3, 3, 1);
	CU_ASSERT_EQUAL(actual, 20);

	// last value
	int array6[] = { 10, 90, 80, 30, 70, 50 };
	actual = selectt(array6, 6, 5);
	CU_ASSERT_EQUAL(actual, 90);

	// middle value
	int array7[] = { 60, 40, 30, 10, 50, 20, 70 };
	actual = selectt(array7, 7, 3);
	CU_ASSERT_EQUAL(actual, 40);

	// last value reverse order
	int array9[] = { 90, 80, 70, 60, 50, 40, 30, 20, 10 };
	actual = selectt(array9, 9, 8);
	CU_ASSERT_EQUAL(actual, 90);

	// first value already sorted
	int array10[] = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
	actual = selectt(array10, 10, 0);
	CU_ASSERT_EQUAL(actual, 10);

}

/**
 * Test all the functions for this application.
 *
 * @return test error code
 */
static CU_ErrorCode test_all(void)
{
	// initialize the CUnit test registry -- only once per application
	CU_initialize_registry();

	// add a suite to the registry with no init or cleanup
	CU_pSuite pSuite = CU_add_suite("TreeSet Tests", NULL, NULL);

	// add the tests to the suite
	CU_add_test(pSuite, "test select", test_select);

	// run all test suites using the basic interface
	CU_basic_set_mode(CU_BRM_VERBOSE);
	CU_basic_run_tests();

	// display information on failures that occurred
	CU_basic_show_failures(CU_get_failure_list());

	// Clean up registry and return status
	CU_cleanup_registry();
	return CU_get_error();
}

/**
 * Main program to invoke test functions
 *
 * @return the exit status of the program
 */
int main(void)
{

	// test all the functions
	CU_ErrorCode code = test_all();

	return (code == CUE_SUCCESS) ? EXIT_SUCCESS : EXIT_FAILURE;
}
