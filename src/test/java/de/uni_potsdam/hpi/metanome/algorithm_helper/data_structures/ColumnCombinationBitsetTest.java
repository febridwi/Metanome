package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import com.google.common.collect.ImmutableList;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import org.apache.lucene.util.OpenBitSet;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ColumnCombinationBitsetTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link ColumnCombinationBitset#ColumnCombinationBitset(int...)}
	 * 
	 * The empty constructor should return a valid instance.
	 */
	@Test
	public void testColumnCombinationBitset() {
		// Setup 
		ColumnCombinationBitset columnCombination = new ColumnCombinationBitset();
		
		// Execute functionality
		// Check result
		columnCombination.toString();
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#ColumnCombinationBitset(ColumnCombinationBitset)}
	 * 
	 * The constructor should create a copy (new instance) of the current column combination. The bitset in the copy
	 * should be cloned.
	 */
	@Test
	public void testColumnCombinationBitsetCopy() {
		// Setup
		// Expected values
		ColumnCombinationBitset expectedFirstColumnCombination = new ColumnCombinationBitset(2, 3, 6);
		
		// Execute functionality
		ColumnCombinationBitset actualCopy = new ColumnCombinationBitset(expectedFirstColumnCombination);
		
		// Check result
		assertNotSame(expectedFirstColumnCombination, actualCopy);
		assertEquals(expectedFirstColumnCombination, actualCopy);
		expectedFirstColumnCombination.removeColumn(3);
		// Column 3 should still be set on copy.
		assertTrue(actualCopy.bitset.get(3));
	}
	
	/**
	 * TODO docs
	 */
	@Test
	public void testConstructor() {
		// Setup
		ColumnCombinationBitset columnCombination = new ColumnCombinationBitset(0, 3); // 1001
		// Expected values
		long[] expectedBits = {0x9}; // 1001
		
		// Check result
		assertEquals(new OpenBitSet(expectedBits, expectedBits.length), columnCombination.bitset);
        assertEquals(2, columnCombination.size());
	}
	
	/**
	 * TODO docs
	 */
	@Test
	public void testSetColumnsOpenBitSet() {
		// Setup
		ColumnCombinationBitset columnCombination = new ColumnCombinationBitset();
		// Expected values
		long[] expectedBits = {3};
		OpenBitSet expectedBitSet = new OpenBitSet(expectedBits, 1);
		
		// Execute functionality
		columnCombination.setColumns(expectedBitSet);
		
		// Check result
		assertEquals(expectedBitSet, columnCombination.bitset);
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#equals(Object)} and {@link ColumnCombinationBitset#hashCode()}
	 * 
	 * Two column combinations should be equal if their bitset is equal. 
	 */
	@Test
	public void testEqualsHashCode() {
		// Setup
		ColumnCombinationBitset columnCombination1 = new ColumnCombinationBitset(0, 3);
		ColumnCombinationBitset columnCombination2Equals = new ColumnCombinationBitset(0, 3);
		ColumnCombinationBitset columnCombination3Unequals = new ColumnCombinationBitset(0, 2, 3);
		ColumnCombinationBitset columnCombination1WrongSize = new ColumnCombinationBitset(0, 3);
		columnCombination1WrongSize.size = 0;
		
		// Execute functionality
		// Check result
		assertEquals(columnCombination1, columnCombination1);
		assertEquals(columnCombination1.hashCode(), columnCombination1.hashCode());
		assertNotSame(columnCombination1, columnCombination2Equals);
		assertEquals(columnCombination1, columnCombination2Equals);
		assertEquals(columnCombination1.hashCode(), columnCombination2Equals.hashCode());
		assertNotEquals(columnCombination1, columnCombination3Unequals);
		assertNotEquals(columnCombination1.hashCode(), columnCombination3Unequals.hashCode());
		assertNotEquals(columnCombination1, columnCombination1WrongSize);
		assertNotEquals(columnCombination1.hashCode(), columnCombination1WrongSize.hashCode());
	}
	
	/**
	 * TODO docs
	 */
	@Test
	public void testContainsSubsetDoesNotWriteSets() {
		// Setup
		// TODO fixture
		int[] subSetColumns = {0, 3};
		ColumnCombinationBitset subSet = new ColumnCombinationBitset(subSetColumns);
		OpenBitSet expectedSubSetBitSet = subSet.bitset.clone();
		int[] superSetColumns = {0, 2, 3};
		ColumnCombinationBitset superSet = new ColumnCombinationBitset(superSetColumns);
		OpenBitSet expectedSuperSetBitSet = superSet.bitset.clone();
		
		// Execute functionality
		superSet.containsSubset(subSet);
		
		// Check result
		assertEquals(expectedSuperSetBitSet, superSet.bitset);
		assertEquals(expectedSubSetBitSet, subSet.bitset);		
	}
	
	/**
	 * TODO docs
	 */
	@Test
	public void testContainsSubset() {
		// Setup
		ColumnCombinationBitset subSet = new ColumnCombinationBitset(0, 3);
		ColumnCombinationBitset superSet = new ColumnCombinationBitset(0, 2, 3);
		
		// Execute functionality
		// Check result
		assertTrue(superSet.containsSubset(subSet));
		assertTrue(superSet.containsSubset(superSet));
		assertFalse(subSet.containsSubset(superSet));		
	}
	
	/**
	 * TODO docs
	 */
	@Test
	public void testContainsRealSubset() {
		// Setup
		ColumnCombinationBitset subSet = new ColumnCombinationBitset(0, 3);
		ColumnCombinationBitset superSet = new ColumnCombinationBitset(0, 2, 3);
		
		// Execute functionality
		// Check result
		assertTrue(superSet.containsRealSubset(subSet));
		assertFalse(superSet.containsRealSubset(superSet));
	}

	/**
	 * TODO docs
	 */
	@Test
	public void testIsSubsetOf() {
		// Setup
		ColumnCombinationBitset subSet = new ColumnCombinationBitset(0, 3);
		ColumnCombinationBitset superSet = new ColumnCombinationBitset(0, 2, 3);
		
		// Execute functionality
		// Check result
		assertTrue(subSet.isSubsetOf(superSet));
		assertTrue(subSet.isSubsetOf(subSet));
		assertFalse(superSet.isSubsetOf(subSet));
	}
	
	/**
	 * TODO docs
	 */
	@Test
	public void testIsRealSubsetOf() {
		// Setup
		ColumnCombinationBitset subSet = new ColumnCombinationBitset(0, 3);
		ColumnCombinationBitset superSet = new ColumnCombinationBitset(0, 2, 3);
		
		// Execute functionality
		// Check result
		assertTrue(subSet.isRealSubsetOf(superSet));
		assertFalse(subSet.isRealSubsetOf(subSet));
		assertFalse(superSet.isRealSubsetOf(subSet));
		assertFalse(superSet.isRealSubsetOf(superSet));	
	}
	
	/**
	 * String representation should contain the bits from the bitset.
	 */
	@Test
	public void testToString() {
		// Setup
		int[] bitsetColumns = {0, 3};
		ColumnCombinationBitset columnCombination = new ColumnCombinationBitset(bitsetColumns);
		// Expected values
		String expectedStringRepresentation = "1001";
		
		// Execute functionality
		// Check result
		assertTrue(columnCombination.toString().contains(expectedStringRepresentation));		
	}

	/**
	 * Test method of {@link ColumnCombinationBitset#getNSubsetColumnCombinations(int)}
	 * 
	 * Should return the column combinations that are subsets with n columns.
	 * E.g.: the 3-subsets of 1011 are 0011, 1001, 1010. 
	 */
	@Test
	public void testGetNSubsetColumnCombinations() {
		// Setup
		int[] superSetColumns = {0, 2, 3};
		ColumnCombinationBitset superSet = new ColumnCombinationBitset(superSetColumns);
		// Expected values
		List<ColumnCombinationBitset> expected2SubsetColumnCombinations = new LinkedList<ColumnCombinationBitset>();
		int[] expected2SubSet1 = {0, 2};
		expected2SubsetColumnCombinations.add(new ColumnCombinationBitset(expected2SubSet1));
		int[] expected2SubSet2 = {0, 3};
		expected2SubsetColumnCombinations.add(new ColumnCombinationBitset(expected2SubSet2));
		int[] expected2SubSet3 = {2, 3};
		expected2SubsetColumnCombinations.add(new ColumnCombinationBitset(expected2SubSet3));
		
		// Execute functionality
		List<ColumnCombinationBitset> actual2SubsetColumnCombinations = superSet.getNSubsetColumnCombinations(2);
		
		// Check result
		assertThat(actual2SubsetColumnCombinations, 
				IsIterableContainingInAnyOrder.containsInAnyOrder(
						expected2SubsetColumnCombinations.toArray(
								new ColumnCombinationBitset[expected2SubsetColumnCombinations.size()])));	
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#getNSubsetColumnCombinationsSupersetOf(ColumnCombinationBitset, int)}
	 * 
	 * If n is closer to the super set the top down strategy should be chosen otherwise bottom up generation is used.
	 */
	@Test
	public void testGetNSubsetColumnCombinationsSupersetOfChoosesEfficientImplementation() {
		// Setup
		ColumnCombinationBitset abcdeg = spy(new ColumnCombinationBitset(0, 1, 2, 3, 4, 6));
		ColumnCombinationBitset c = new ColumnCombinationBitset(2);
		
		// Execute functionality 
		// Check result
		abcdeg.getNSubsetColumnCombinationsSupersetOf(c, 4);
		verify(abcdeg).getNSubsetColumnCombinationsSupersetOfTopDown(c, 4);
		abcdeg.getNSubsetColumnCombinationsSupersetOf(c, 3);
		verify(abcdeg).getNSubsetColumnCombinationsSupersetOfBottomUp(c, 3);
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#getNSubsetColumnCombinationsSupersetOf(ColumnCombinationBitset, int)}
	 * 
	 * TODO docs
	 */
	@Test
	public void testGetNSubsetColumnCombinationsSupersetOf() {
		// Setup
		int[] superSetColumns = {0, 2, 3, 4};
		ColumnCombinationBitset superSet = new ColumnCombinationBitset(superSetColumns);
		int[] subSetColumns = {2, 3};
		ColumnCombinationBitset subSet = new ColumnCombinationBitset(subSetColumns);
		// Expected values
		List<ColumnCombinationBitset> expected3SubsetColumnCombinations = new LinkedList<ColumnCombinationBitset>();
		int [] expected3SubSet1 = {0, 2, 3};
		expected3SubsetColumnCombinations.add(new ColumnCombinationBitset(expected3SubSet1));
		int [] expected3SubSet2 = {4, 2, 3};
		expected3SubsetColumnCombinations.add(new ColumnCombinationBitset(expected3SubSet2));
		
		// Execute functionality
		List<ColumnCombinationBitset> actual3SubsetColumnCombinations = 
				superSet.getNSubsetColumnCombinationsSupersetOf(subSet, 3);
		
		// Check result
		assertThat(actual3SubsetColumnCombinations,
				IsIterableContainingInAnyOrder.containsInAnyOrder(
						expected3SubsetColumnCombinations.toArray(
								new ColumnCombinationBitset[expected3SubsetColumnCombinations.size()])));
	}
	
	/**
	 * TODO docs
	 */
	@Test
	public void testGetNSubsetColumnCombinationsN() {
		// Setup
		ColumnCombinationBitset superSet = new ColumnCombinationBitset(0, 2, 3);
		// Expected values
		List<ColumnCombinationBitset> expected1SubsetColumnCombinations = new LinkedList<ColumnCombinationBitset>();
		expected1SubsetColumnCombinations.add(new ColumnCombinationBitset(0));
		expected1SubsetColumnCombinations.add(new ColumnCombinationBitset(2));
		expected1SubsetColumnCombinations.add(new ColumnCombinationBitset(3));
		
		// Execute functionality
		List<ColumnCombinationBitset> actual1SubsetColumnCombinations = superSet.getNSubsetColumnCombinations(1);
		
		// Check result
		assertThat(actual1SubsetColumnCombinations,
				IsIterableContainingInAnyOrder.containsInAnyOrder(
						expected1SubsetColumnCombinations.toArray(
								new ColumnCombinationBitset[expected1SubsetColumnCombinations.size()])));
	}
	
	// FIXME have bigger test case for getNSubsetColumnCombinationsSupersetOf
	
	/**
	 * Test method for {@link ColumnCombinationBitset#getNSubsetColumnCombinationsSupersetOf(ColumnCombinationBitset, int)}
	 * 
	 * The method should return also return the unreal subset when requested with a parameter
	 * n that is equal to the set bits in the given superset. 
	 */
	@Test
	public void testGetNSubsetColumnCombinationsSupersetOf2() {
		// Setup
		ColumnCombinationBitset superset = new ColumnCombinationBitset(0, 1);
		ColumnCombinationBitset subset = new ColumnCombinationBitset(1);
		
		// Execute functionality
		List<ColumnCombinationBitset> actual3SubsetColumnCombinations = superset.getNSubsetColumnCombinationsSupersetOf(subset, 2);
		
		// Check result
		assertEquals(1, actual3SubsetColumnCombinations.size());
		assertEquals(superset, actual3SubsetColumnCombinations.get(0));
	}
	
	@Test
	public void testGetNSubsetColumnCombinationsSupersetOfInversed() {
		// Setup
		ColumnCombinationBitset ab = new ColumnCombinationBitset(0, 1);
		ColumnCombinationBitset b = new ColumnCombinationBitset(1);
		ColumnCombinationBitset abc = new ColumnCombinationBitset(0, 1, 2);
		ColumnCombinationBitset bc = new ColumnCombinationBitset(1, 2);
		
		// Execute functionality
		List<ColumnCombinationBitset> actualSubsetColumnCombinations = abc.getNSubsetColumnCombinationsSupersetOf(b, 2);
		
		// Check result
		assertEquals(2, actualSubsetColumnCombinations.size());
		assertTrue(actualSubsetColumnCombinations.contains(bc));
		assertTrue(actualSubsetColumnCombinations.contains(ab));
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#getNSubsetColumnCombinationsSupersetOfTopDown(ColumnCombinationBitset, int)}
	 * and {@link ColumnCombinationBitset#getNSubsetColumnCombinationsSupersetOfBottomUp(ColumnCombinationBitset, int)}.
	 * 
	 * When generating subsets that should be greater than or less than the superset an empty list should be returned.
	 */
	@Test
	public void testGetNSubsetColumnCombinationsSupersetOfTopDownNGreaterSuperset() {
		// Setup
		ColumnCombinationBitset abcd = new ColumnCombinationBitset(0, 1, 2, 3);
		ColumnCombinationBitset bc = new ColumnCombinationBitset(1, 2);
		
		// Execute functionality
		// Check result
		assertTrue(abcd.getNSubsetColumnCombinationsSupersetOfTopDown(bc, 5).isEmpty());
		assertTrue(abcd.getNSubsetColumnCombinationsSupersetOfTopDown(bc, 1).isEmpty());
		assertTrue(abcd.getNSubsetColumnCombinationsSupersetOfBottomUp(bc, 5).isEmpty());
		assertTrue(abcd.getNSubsetColumnCombinationsSupersetOfBottomUp(bc, 1).isEmpty());
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#getNSubsetColumnCombinationsSupersetOfTopDown(ColumnCombinationBitset, int)}
	 * and {@link ColumnCombinationBitset#getNSubsetColumnCombinationsSupersetOfBottomUp(ColumnCombinationBitset, int)}.
	 * 
	 * When generating subsets that should be equal to the subset or superset return those sets.
	 */
	@Test
	public void testGetNSubsetColumnCombinationsSupersetOfTopDownNEqualSuperset() {
		// Setup
		ColumnCombinationBitset abcd = new ColumnCombinationBitset(0, 1, 2, 3);
		ColumnCombinationBitset bc = new ColumnCombinationBitset(1, 2);
		
		
		// Execute functionality
		// Check result
		assertEquals(abcd, abcd.getNSubsetColumnCombinationsSupersetOfTopDown(new ColumnCombinationBitset(), 4).get(0));
		assertEquals(bc, abcd.getNSubsetColumnCombinationsSupersetOfTopDown(bc, 2).get(0));
		assertEquals(abcd, abcd.getNSubsetColumnCombinationsSupersetOfBottomUp(new ColumnCombinationBitset(), 4).get(0));
		assertEquals(bc, abcd.getNSubsetColumnCombinationsSupersetOfBottomUp(bc, 2).get(0));
	}
	
	/**
	 * TODO docs
	 */
	@Test
	public void testMinus() {
		// Setup
		ColumnCombinationBitset columnCombination1 = new ColumnCombinationBitset(1, 2, 3, 4);
		ColumnCombinationBitset columnCombination2 = new ColumnCombinationBitset(2, 3);
		// Expected values
		ColumnCombinationBitset expectedColumnCombination = new ColumnCombinationBitset(1, 4);
		
		// Execute functionality
		ColumnCombinationBitset actualColumnCombination = columnCombination1.minus(columnCombination2);
		
		// Check result
		assertEquals(expectedColumnCombination, actualColumnCombination);
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#size()}
	 * 
	 * When setting bits using the setColumns method with contained column indices the correct size should be computed.
	 */
	@Test
	public void testSizeSetColumns() {
		// Setup
		ColumnCombinationBitset columnCombination = new ColumnCombinationBitset(1, 3, 4);
		// Expected values 
		int expectedSize = 3;
		
		// Execute functionality
		// Check result 
		assertEquals(expectedSize, columnCombination.size());
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#getContainedOneColumnCombinations()}
	 * 
	 * {@link ColumnCombinationBitset}s should return all the contained column combinations
	 * of size 1.
	 */
	@Test
	public void testGetContainedOneColumnCombinations() {
		// Setup
		ColumnCombinationBitsetFixture fixture = new ColumnCombinationBitsetFixture();
		ColumnCombinationBitset columnCombination = fixture.getColumnCombination1();
		
		// Execute functionality 
		// Check result
		assertThat(columnCombination.getContainedOneColumnCombinations(),
				IsIterableContainingInAnyOrder.containsInAnyOrder(fixture.getExpectedOneColumnCombinations1()));		
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#size()}
	 * 
	 * When setting bits using the raw bitset setColumns method the correct size should be computed. 
	 */
	@Test
	public void testSizeBitset() {
		// Setup
		long[] columnCombinationSetBits = {0xb}; // 1011
		OpenBitSet columnCombinationBitset = new OpenBitSet(columnCombinationSetBits, 1);		
		ColumnCombinationBitset columnCombination = new ColumnCombinationBitset().setColumns(columnCombinationBitset);
		// Expected values 
		int expectedSize = 3;
		
		// Execute functionality
		// Check result 
		assertEquals(expectedSize, columnCombination.size());
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#union(ColumnCombinationBitset)}
	 * 
	 * Union should return a {@link ColumnCombinationBitset} with all the columns from both column combinations
	 * and no other columns. The original {@link ColumnCombinationBitset}s should remain unchanged.
	 */
	@Test
	public void testUnion() {
		// Setup
		ColumnCombinationBitsetFixture fixture = new ColumnCombinationBitsetFixture();
		ColumnCombinationBitset columnCombination1 = fixture.getColumnCombination1();
		ColumnCombinationBitset columnCombination2 = fixture.getColumnCombination2();
		
		// Execute functionality
		// Check result
		assertEquals(fixture.getExpectedUnionColumnCombination(), columnCombination1.union(columnCombination2));
		assertEquals(fixture.getExpectedUnionColumnCombination(), columnCombination2.union(columnCombination1));
		// Originals should not have changed
		assertEquals(fixture.getColumnCombination1(), columnCombination1);
		assertEquals(fixture.getColumnCombination2(), columnCombination2);
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#intersect(ColumnCombinationBitset)}
	 * 
	 * Intersect should return a {@link ColumnCombinationBitset} with only the columns that are contained in both
	 * combinations. The original {@link ColumnCombinationBitset}s should remain unchanged. 
	 */
	@Test
	public void testIntersection() {
		// Setup
		ColumnCombinationBitsetFixture fixture = new ColumnCombinationBitsetFixture();
		ColumnCombinationBitset columnCombination1 = fixture.getColumnCombination1();
		ColumnCombinationBitset columnCombination2 = fixture.getColumnCombination2();
		
		// Execute functionality 
		// Check result
		assertEquals(fixture.getExpectedIntersectionColumnCombination(), columnCombination1.intersect(columnCombination2));
		assertEquals(fixture.getExpectedIntersectionColumnCombination(), columnCombination2.intersect(columnCombination1));
		// Originals should not have changed
		assertEquals(fixture.getColumnCombination1(), columnCombination1);
		assertEquals(fixture.getColumnCombination2(), columnCombination2);
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#getSetBits()}
	 * 
	 * Returns a list of column indexes contained in the column combination.
	 */
	@Test
	public void testGetSetBits() {
		// Setup
		ColumnCombinationBitsetFixture fixture = new ColumnCombinationBitsetFixture();
		ColumnCombinationBitset columnCombination = fixture.getColumnCombination1();
		
		// Execute functionality
		// Check result 
		assertThat(columnCombination.getSetBits(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(fixture.getExpectedBits1()));
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#getClearedBits(int)}
	 * 
	 * Returns all the cleared bits within the maximum of all the number of columns.
	 */
	@Test
	public void testGetClearedBits() {
		// Setup 
		ColumnCombinationBitsetFixture fixture = new ColumnCombinationBitsetFixture();
		ColumnCombinationBitset columnCombination = fixture.getColumnCombination1();
		
		// Execute functionality
		// Check result
		assertThat(columnCombination.getClearedBits(fixture.getMaxNumberOfColumns()),
                IsIterableContainingInAnyOrder.containsInAnyOrder(fixture.getExpectedClearedBits1()));
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#addColumn(int)}
	 * 
	 * After adding columns the size should be updated and the column's bit be set on the bit set.
	 */
	@Test
	public void testAddColumn() {
		// Setup
		ColumnCombinationBitsetFixture fixture = new ColumnCombinationBitsetFixture();
		ColumnCombinationBitset columnCombination = fixture.getColumnCombination1();
		
		// Execute functionality
		columnCombination.addColumn(256);
		columnCombination.addColumn(2);
		// Check result
		assertEquals(fixture.getExpectedSize1() + 1, columnCombination.size());
		assertTrue(columnCombination.bitset.get(256));
		assertTrue(columnCombination.bitset.get(2));
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#addColumn(int)}
	 * 
	 * When setting the first overflow bit the bit set should be properly expanded and the bit be set.
	 */
	@Test
	public void testAddColumnOverflow() {
		// Setup
		ColumnCombinationBitset columnCombination = new ColumnCombinationBitset();
		
		// Execute functionality
		columnCombination.addColumn(64);
		
		// Check result
		assertEquals(1, columnCombination.size());
		assertTrue(columnCombination.bitset.get(64));
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#removeColumn(int)}
	 * 
	 * After removing a column the size should be updated and the column be removed from the bitset.
	 */
	@Test
	public void testRemoveColumn() {
		// Setup
		ColumnCombinationBitset columnCombination = new ColumnCombinationBitset(0, 2, 3, 4);
		// Expected values
		int expectedSize = 4;
		
		// Execute functionality
		columnCombination.removeColumn(2);
		columnCombination.removeColumn(362);
		// Check result
		assertEquals(expectedSize - 1, columnCombination.size());
		assertFalse(columnCombination.bitset.get(2));
		assertFalse(columnCombination.bitset.get(362));
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#getDirectSupersets(int)}
	 * 
	 * Generates the direct super sets. Supersets are bounded by the maximum number of columns.
	 */
	@Test
	public void testGetDirectSupersetsInt() {
		// Setup
		ColumnCombinationBitsetFixture fixture = new ColumnCombinationBitsetFixture();
		ColumnCombinationBitset columnCombination = fixture.getColumnCombination1();
		
		// Execute functionality
		// Check result
		assertThat(columnCombination.getDirectSupersets(fixture.getMaxNumberOfColumns()),
                IsIterableContainingInAnyOrder.containsInAnyOrder(fixture.getExpectedDirectSupersets1()));
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#getDirectSupersets(ColumnCombinationBitset)}
	 * 
	 * Generates the direct super sets. Supersets are bounded by the maximum superset.
	 */
	@Test
	public void testGetDirectSupersetsColumnCombinationBitset() {
		// Setup
		ColumnCombinationBitsetFixture fixture = new ColumnCombinationBitsetFixture();
		ColumnCombinationBitset columnCombination = fixture.getColumnCombination1();
		
		// Execute functionality
		List<ColumnCombinationBitset> actualDirectSupersets = 
				columnCombination.getDirectSupersets(fixture.getExpectedSupersetOfColumn1());
		
		// Check result
		assertThat(actualDirectSupersets, 
				IsIterableContainingInAnyOrder.containsInAnyOrder(
						fixture.getExpectedDirectSupersetsColumnCombinationBitset()));
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#getDirectSubsets()}
	 * 
	 * Generates the direct subset column combinations.
	 */
	@Test
	public void testGetDirectSubsets() {
		// Setup
		ColumnCombinationBitsetFixture fixture = new ColumnCombinationBitsetFixture();
		ColumnCombinationBitset columnCombination = fixture.getColumnCombination1();
		
		// Execute functionality
		// Check result
		assertThat(columnCombination.getDirectSubsets(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(fixture.getExpectedDirectSubsets1()));
		assertEquals(fixture.getColumnCombination1(), columnCombination);
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#createColumnCombination(String, ImmutableList)}
	 * 
	 * Only the correct identifiers should be found in the {@link ColumnCombination}.
	 */
	@Test
	public void testCreateColumnCombination() {
		// Setup
		ColumnCombinationBitsetFixture fixture = new ColumnCombinationBitsetFixture();
		ColumnCombinationBitset columnCombination = fixture.getColumnCombination1();
		// Expected values
		String expectedRelationName = "relation1";
		ImmutableList<String> expectedColumnNames = ImmutableList.of("column1", "column2", "column3", "column4", "column5");
				
		// Execute functionality
		ColumnCombination actualColumnCombination = columnCombination.createColumnCombination(
				expectedRelationName, expectedColumnNames);
		
		// Check result
		for (Integer setColumnIndex : columnCombination.getSetBits()) {
			assertTrue(actualColumnCombination.getColumnIdentifiers().contains(
					new ColumnIdentifier(expectedRelationName, expectedColumnNames.get(setColumnIndex))));
		}
		assertEquals(columnCombination.size(), actualColumnCombination.getColumnIdentifiers().size());
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#createColumnCombination(String, ImmutableList)}
	 * 
	 * An empty {@link ColumnCombinationBitset} should generate an empty {@link ColumnCombination} with no identifiers.
	 */
	@Test
	public void testCreateColumnCombinationFromEmpty() {
		// Setup
		ColumnCombinationBitset emptyColumnCombinationBitset = new ColumnCombinationBitset();
		
		// Execute functionality
		ColumnCombination actualColumnCombination = emptyColumnCombinationBitset.createColumnCombination(
                "someRelation", ImmutableList.of("someColumn"));
		
		// Check result
		assertEquals(actualColumnCombination, new ColumnCombination());	
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#testBit(int)}
	 * 
	 * Should return true iff the bit at bitIndex is set.
	 */
	@Test 
	public void testTestBit() {
		// Setup
		ColumnCombinationBitset columnCombination = new ColumnCombinationBitset(3, 4, 7);
		
		// Execute functionality
		// Check result
		assertFalse(columnCombination.testBit(0));
		assertFalse(columnCombination.testBit(1));
		assertFalse(columnCombination.testBit(2));
		assertTrue(columnCombination.testBit(3));
		assertTrue(columnCombination.testBit(4));
		assertFalse(columnCombination.testBit(5));
		assertFalse(columnCombination.testBit(6));
		assertTrue(columnCombination.testBit(7));
		assertFalse(columnCombination.testBit(8));
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#setAllBits(int)} 
	 * 
	 * Resets all bits and sets all bits with indeces smaller than dimension.
	 * E.g. dimension 4 generate 111100000...
	 */
	@Test
	public void testSetAllBits() {
		// Setup
		ColumnCombinationBitset columnCombination = new ColumnCombinationBitset(3);
		// Expected values;
		int expectedDimension = 7;
		
		// Execute functionality
		ColumnCombinationBitset actualColumnCombination = columnCombination.setAllBits(expectedDimension);
		
		// Check result
		for (int i = 0; i < expectedDimension; i++) {
			assertTrue(actualColumnCombination.testBit(i));
		}
		assertFalse(actualColumnCombination.testBit(expectedDimension));
		assertEquals(expectedDimension, actualColumnCombination.size());
		assertSame(columnCombination, actualColumnCombination);
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#equals(Object)} and {@link ColumnCombinationBitset#hashCode()}
	 * 
	 * Reproducing the problem a few bugs in lucene caused for our {@link ColumnCombinationBitset}.
	 */
	@Test
	public void testEqualsHashCodeLuceneBug() {
		// Setup
		ColumnCombinationBitset allocatedBitSet = new ColumnCombinationBitset(5, 1000);
		// Expected values
		ColumnCombinationBitset expectedEqualSet = new ColumnCombinationBitset(5);
		
		// Execute functionality
		allocatedBitSet = allocatedBitSet.intersect(expectedEqualSet);
		allocatedBitSet.addColumn(1000);
		allocatedBitSet = new ColumnCombinationBitset(allocatedBitSet);
		
		// Check result
		assertNotEquals(expectedEqualSet, allocatedBitSet);
		assertNotEquals(expectedEqualSet.hashCode(), allocatedBitSet.hashCode());		
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#getAllSubsets()}
	 * 
	 * getAllSubsets should return all subsets including the empty set.
	 */
	@Test
	public void testGetAllSubsets() {
		// Setup
		ColumnCombinationBitsetFixture fixture = new ColumnCombinationBitsetFixture();
		ColumnCombinationBitset columnCombination = fixture.getColumnCombination1();
		
		// Execute functionality
		List<ColumnCombinationBitset> actualSubsets = columnCombination.getAllSubsets();
		
		// Check result
		assertThat(actualSubsets, IsIterableContainingInAnyOrder.containsInAnyOrder(fixture.getExpectedSubsetArray1()));
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#getAllSubsets()}
	 * 
	 * The list of subsets of the empty {@link ColumnCombinationBitset} should be empty.
	 */
	@Test
	public void testGetAllSubsetsEmpty() {
		// Setup
		ColumnCombinationBitset columnCombination = new ColumnCombinationBitset();
		
		// Execute functionality
		assertTrue(columnCombination.getAllSubsets().isEmpty());		
	}
	
	/**
	 * Test method for {@link ColumnCombinationBitset#getAllSubsets()}
	 * 
	 * The list of subsets of a {@link ColumnCombinationBitset} with only one column should 
	 * only contain the empty {@link ColumnCombinationBitset}. 
	 */
	@Test
	public void testGetAllSubsetsOneColumn() {
		// Setup
		ColumnCombinationBitset columnCombination = new ColumnCombinationBitset(5);
		
		// Execute functionality
		assertEquals(new ColumnCombinationBitset(), columnCombination.getAllSubsets().get(0));		
	}
	
}
