
/**
 * Edited by: Rajesh Sakhamuru
 * Modified on: 7/27/19
 * 
 * Investment.java
 * 
 * This program selects an optimal investment portfolio
 * using a greedy algorithm that prioritizes the most
 * shares of the highest yielding bond first.
 * 
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

/**
 * This is a wrapper class for the Bond and HighestBondYieldComparator, and the
 * unit test class TestInvestment.
 */
public class Investment
{
	/**
	 * This class represents a Bond.
	 */
	static public final class Bond
	{
		// bond name
		public String name;
		// number of shares available for sale
		public float shares;
		// cost of each share
		public float cost;
		// yield per share (e.g. 0.06 for a 6% yield)
		public float yield;

		/**
		 * Default constructor for a bond with no data.
		 */
		public Bond()
		{
			this.name = "NONE";
			this.shares = 0;
			this.cost = 0;
			this.yield = 0;
		}

		/**
		 * This is the constructor for Bond that takes the Bond's name, shares
		 * available, cost per share, and % yield as parameters and assigns them to
		 * their respective instance variables in the new object.
		 * 
		 * @param name   - String - bond name
		 * @param shares - float - number of shares
		 * @param cost   - float - cost per share
		 * @param yield  - float - yield per share (e.g. 0.06 for a 6% yield)
		 */
		public Bond(String name, float shares, float cost, float yield)
		{
			this.name = name;
			this.shares = shares;
			this.cost = cost;
			this.yield = yield;
		}

		/**
		 * For easier calculation of total cost of all shares of this bond available.
		 * 
		 * @return the total cost of all of the shares available for sale of this bond
		 */
		public float totalCost()
		{
			return shares * cost;
		}

		/**
		 * for easier calculation of total profit from this bond
		 * 
		 * @return total profit from this bond
		 */
		public float totalProfit()
		{
			return shares * cost * yield;
		}

	}

	/**
	 * This class orders bonds from highest to lowest yield.
	 */
	static class HighestBondYieldComparator implements Comparator<Bond>
	{
		/**
		 * the compare method allows 2 bonds to be compared to each other. if bond1 has
		 * a higher yield than bond2, then it returns a negative number and if bond2 is
		 * higher it's a positive number. If they're equal it returns 0. This allows the
		 * comparator to sort by bond yield in descending order.
		 * 
		 * @param bond1 - Bond - bond with share prices of a company compared to bond2.
		 * @param bond2 - Bond - bond with share prices of a company compared to bond1.
		 */
		public int compare(Bond bond1, Bond bond2)
		{

			if (bond1.yield < bond2.yield)
			{
				return 1;
			}
			else if (bond1.yield == bond2.yield)
			{
				return 0;
			}
			else
			{
				return -1;
			}
		}

	}

	/**
	 * Returns the optimum list of bonds to buy from a bond priority queue.
	 * 
	 * @param total total amount available to invest
	 * @param bonds collection of investment opportunities
	 * @return a portfolio of bonds
	 */
	static Collection<Bond> invest(float total, Collection<Bond> bonds)
	{
		// your code here
		PriorityQueue<Bond> bondsQueue = new PriorityQueue<Bond>(bonds.size(), new HighestBondYieldComparator());

		// move bonds from PriorityQueue bondsQueue that can be bought to investments
		ArrayList<Bond> investments = new ArrayList<Bond>();
		float money = total;
		if (money == 0)
		{
			return investments;
		}
		
		// add all bonds in Collection<Bond> bonds into bondsQueue
		for (Bond b : bonds)
		{
			bondsQueue.add(b);
		}

		while (!bondsQueue.isEmpty())
		{
			Bond b = bondsQueue.poll();
			
			// creates a new bond so original bonds are not modified
			Bond c = new Bond(b.name,b.shares,b.cost,b.yield);
			
			// if it can be afforded to buy all of a certain type of share, then buy them
			// all
			if (c.totalCost() <= money)
			{
				if(c.shares == 0)
				{
					continue;
				}
				investments.add(c);
				money = money - c.totalCost();
				if (money == 0)
				{
					break;
				}
			}
			// else buy as many shares of the stock as you can
			else
			{
				float ratio = money / c.totalCost();
				c.shares = c.shares * ratio;
				investments.add(c);
				money = 0;
				break;
			}
		}		
		
////	Troubleshooting:		
//	System.out.println("initial money: " + total);
//	System.out.println("remaining money: " + money);
//	System.out.println("\nSHARES PURCHASED:");
//	for (Bond c : investments)
//	{
//		System.out.println("name: " + c.name + ", shares: " + c.shares + ", cost: " + c.cost + ", yield: " + c.yield
//				+ "\ntotalprofit: " + c.totalProfit() + ", totalcost: " + c.totalCost() + "\n");
//	}
//
//	System.out.println("end of this run\n-----\n");
		
		return investments;
	}

	/**
	 * Unit test class for investments.
	 */
	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	static public class TestInvestment
	{
		/**
		 * Total the bonds in a portfolio.
		 * 
		 * @param bonds the collection of bonds
		 * @return to total cost of the bonds
		 */
		float totalCost(Collection<Bond> bonds)
		{
			float total = 0;
			for (Bond bond : bonds)
			{
				total += bond.totalCost();
			}
			return total;
		}

		/**
		 * Total profit of the bonds in a portfolio.
		 * 
		 * @param bonds the collection of bonds
		 * @return to total profit of the bonds
		 */
		float totalProfit(Collection<Bond> bonds)
		{
			float total = 0;
			for (Bond bond : bonds)
			{
				total += bond.totalProfit();
			}
			return total;
		}

		/**
		 * Test purchasing all bonds with funds left over.
		 */
		@Test
		public void test_0010_tooMuchToInvest()
		{
			Bond[] bonds = { new Bond("ACME", 50f, 10f, 0.060f), new Bond("MERC", 20f, 20f, 0.095f),
					new Bond("COKE", 100f, 30f, 0.020f) };
			Collection<Bond> investments = invest(4000f, Arrays.asList(bonds));

			assertEquals(3, investments.size());
			assertEquals(3900, totalCost(investments), 0f);
			assertEquals(128f, totalProfit(investments), 0.001f);
		}

		/**
		 * Test purchasing a whole positions.
		 */
		@Test
		public void test_0020_wholePositions()
		{

			Bond[] bonds = { new Bond("ACME", 50f, 10f, 0.060f), new Bond("MERC", 20f, 20f, 0.095f),
					new Bond("COKE", 100f, 30f, 0.020f) };
			Collection<Bond> investments = invest(900f, Arrays.asList(bonds));

			assertEquals(2, investments.size());
			assertEquals(900f, totalCost(investments), 0.001f);
			assertEquals(68f, totalProfit(investments), 0.001f);
		}

		/**
		 * Test purchasing a fractional position.
		 */
		@Test
		public void test_0030_fractionalPositions()
		{

			Bond[] bonds = { new Bond("ACME", 50f, 10f, 0.060f), new Bond("MERC", 20f, 20f, 0.095f),
					new Bond("COKE", 100f, 30f, 0.020f) };
			Collection<Bond> investments = invest(1000f, Arrays.asList(bonds));

			assertEquals(3, investments.size());

			// verify costs of individual items in the portfolio.
			for (Bond bond : investments)
			{
				if ("ACME".equals(bond.name))
				{
					assertEquals(500f, bond.totalCost(), 0f);
				}
				if ("MERC".equals(bond.name))
				{
					assertEquals(400f, bond.totalCost(), 0f);
				}
				if ("COKE".equals(bond.name))
				{
					assertEquals(100f, bond.totalCost(), 0.01f);
				}
			}

			assertEquals(1000f, totalCost(investments), 0.001f);
			assertEquals(70f, totalProfit(investments), 0.001f);
		}

		/**
		 * Test purchasing a fractional position of the item with the best yield.
		 */
		@Test
		public void test_0040_fractionalFirstBond()
		{

			Bond[] bonds = { new Bond("ACME", 50f, 10f, 0.060f), new Bond("MERC", 20f, 20f, 0.095f),
					new Bond("COKE", 100f, 30f, 0.020f) };
			Collection<Bond> investments = invest(100f, Arrays.asList(bonds));

			assertEquals(1, investments.size());

			// verify cost of item in the portfolio.
			for (Bond bond : investments)
			{
				assertEquals(true, "MERC".equals(bond.name));
				assertEquals(100f, bond.totalCost(), 0.01f);
			}

			assertEquals(100f, totalCost(investments), 0.001f);
			assertEquals(9.5f, totalProfit(investments), 0.001f);
		}
		
		/**
		 * Test how purchasing works with 0 dollars to invest. 
		 */
		@Test
		public void test_0050_invest0Dollars()
		{

			Bond[] bonds = { new Bond("ACME", 50f, 10f, 0.060f), new Bond("MERC", 20f, 20f, 0.095f),
					new Bond("COKE", 100f, 30f, 0.020f) };
			Collection<Bond> investments = invest(0f, Arrays.asList(bonds));

			assertEquals(0, investments.size());
			assertEquals(0f, totalCost(investments), 0f);
			assertEquals(0f, totalProfit(investments), 0f);
		}
		
		/**
		 * Test purchasing but no shares are available.
		 */
		@Test
		public void test_0060_NoSharesAvailable()
		{

			Bond[] bonds = { new Bond("ACME", 0f, 10f, 0.060f), new Bond("MERC", 0f, 20f, 0.095f),
					new Bond("COKE", 0f, 30f, 0.020f) };
			Collection<Bond> investments = invest(1000f, Arrays.asList(bonds));

			assertEquals(0, investments.size());
			assertEquals(0f, totalCost(investments), 0f);
			assertEquals(0f, totalProfit(investments), 0f);
		}
	}

	/**
	 * Main program to drive unit tests.
	 * 
	 * @param args unused
	 */
	public static void main(String[] args)
	{
		Result result = JUnitCore.runClasses(TestInvestment.class);

		System.out.println("[Unit Test Results]");
		System.out.println();

		if (result.getFailureCount() > 0)
		{
			System.out.println("Test failure details:");
			for (Failure failure : result.getFailures())
			{
				System.out.println(failure.toString());
			}
			System.out.println();
		}

		int passCount = result.getRunCount() - result.getFailureCount() - result.getIgnoreCount();
		System.out.println("Test summary:");
		System.out.println("* Total tests = " + result.getRunCount());
		System.out.println("* Passed tests: " + passCount);
		System.out.println("* Failed tests = " + result.getFailureCount());
		System.out.println("* Inactive tests = " + result.getIgnoreCount());
	}

}