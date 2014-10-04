package adt.spatial.quad.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.Test;

import adt.spatial.quad.IntQuadTreeSet;
import adt.spatial.quad.policy.IntQuad;

public class TestIntQuadTreeSet {
	static class ArrayIntQuad implements IntQuad<int[]> {
		@Override
		public int x(int[] x) {
			return x[0];
		}

		@Override
		public int y(int[] x) {
			return x[1];
		}

		@Override
		public int width(int[] x) {
			return x[2];
		}

		@Override
		public int height(int[] x) {
			return x[3];
		}
	}
	
	@Test
	public void testAddE() {
		IntQuadTreeSet<int[]> tree = new IntQuadTreeSet<int[]>(0, 0, 256, 256, new ArrayIntQuad());
		assertTrue(tree.size() == 0);
		tree.add(new int[]{5,5,5,5});
		assertTrue(tree.size() == 1);
		tree.add(new int[]{100,100,5,5});
		assertTrue(tree.size() == 2);
	}
	
	@Test
	public void testRemove() {
		IntQuadTreeSet<int[]> tree = new IntQuadTreeSet<int[]>(0, 0, 256, 256, new ArrayIntQuad());
		assertTrue(tree.size() == 0);
		int[] a = new int[]{5,5,5,5};
		tree.add(a);
		assertTrue(tree.size() == 1);
		int[] b = new int[]{100,100,5,5};
		tree.add(b);
		assertTrue(tree.size() == 2);
		tree.remove(a);
		assertTrue(tree.size() == 1);
		tree.remove(b);
		assertTrue(tree.size() == 0);
	}
	
	@Test
	public void testClear() {
		IntQuadTreeSet<int[]> tree = new IntQuadTreeSet<int[]>(0, 0, 256, 256, new ArrayIntQuad());
		tree.add(new int[]{5,5,5,5});
		tree.add(new int[]{100,100,5,5});
		assertTrue(tree.size() == 2);
		tree.clear();
		assertTrue(tree.size() == 0);
	}
	
	@Test
	public void testAddAll() {
		IntQuadTreeSet<int[]> tree = new IntQuadTreeSet<int[]>(0, 0, 256, 256, new ArrayIntQuad());
		
		ArrayList<int[]> list = new ArrayList<int[]>();
		
		list.add(new int[]{0, 0, 10, 10});
		list.add(new int[]{200, 0, 10, 10});
		list.add(new int[]{0, 200, 10, 10});
		list.add(new int[]{200, 200, 10, 10});
		
		list.add(new int[]{20, 20, 10, 10});
		list.add(new int[]{220, 0, 10, 10});
		list.add(new int[]{0, 220, 10, 10});
		list.add(new int[]{220, 220, 10, 10});
		
		list.add(new int[]{40, 40, 10, 10});
		list.add(new int[]{240, 0, 10, 10});
		list.add(new int[]{0, 240, 10, 10});
		list.add(new int[]{240, 240, 10, 10});
		
		list.add(new int[]{100, 100, 5, 5});
		list.add(new int[]{225, 0, 10, 10});
		list.add(new int[]{0, 225, 10, 10});
		list.add(new int[]{225, 225, 10, 10});
		
		list.add(new int[]{50, 50, 5, 5});
		list.add(new int[]{180, 0, 10, 10});
		list.add(new int[]{0, 180, 10, 10});
		list.add(new int[]{180, 180, 10, 10});
		
		list.add(new int[]{0, 0, 1, 1});
		list.add(new int[]{2, 2, 1, 1});
		list.add(new int[]{4, 4, 1, 1});
		list.add(new int[]{8, 8, 1, 1});
		list.add(new int[]{16, 16, 1, 1});
		list.add(new int[]{32, 32, 1, 1});
		list.add(new int[]{64, 64, 1, 1});
		list.add(new int[]{0, 0, 2, 2});
		list.add(new int[]{0, 0, 4, 4});
		list.add(new int[]{0, 0, 8, 8});
		list.add(new int[]{0, 0, 16, 16});
		list.add(new int[]{0, 0, 32, 32});
		list.add(new int[]{32, 32, 1, 1});
		list.add(new int[]{34, 34, 1, 1});
		list.add(new int[]{36, 36, 1, 1});
		list.add(new int[]{38, 38, 1, 1});
		list.add(new int[]{40, 40, 1, 1});
		list.add(new int[]{42, 42, 1, 1});
		list.add(new int[]{44, 44, 1, 1});
		list.add(new int[]{48, 48, 1, 1});
		list.add(new int[]{50, 50, 1, 1});
		list.add(new int[]{52, 52, 1, 1});
		list.add(new int[]{54, 54, 1, 1});
		list.add(new int[]{58, 58, 1, 1});
		list.add(new int[]{60, 60, 1, 1});
		list.add(new int[]{62, 62, 1, 1});
		list.add(new int[]{64, 64, 1, 1});
		list.add(new int[]{68, 68, 1, 1});
		
		tree.addAll(list);
		assertTrue(tree.size() == list.size());
	}
	
	@Test
	public void testIterator() {
		IntQuadTreeSet<int[]> tree = new IntQuadTreeSet<int[]>(0, 0, 256, 256, new ArrayIntQuad());
		HashSet<int[]> list = new HashSet<int[]>();
		for (int i = 0; i < 32; ++i) {
			for (int j = 0; j < 32; ++j) {
				list.add(new int[]{j*8,i*8,8,8});
			}
		}
		tree.addAll(list);
		
		HashSet<int[]> list2 = new HashSet<int[]>();
		
		for (int[] o : tree) {
			list2.add(o);
		}
		
		assertTrue(list.equals(list2));
	}
	
	@Test
	public void testIteratorRemove() {
		IntQuadTreeSet<int[]> tree = new IntQuadTreeSet<int[]>(0, 0, 256, 256, new ArrayIntQuad());
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (int i = 0; i < 32; ++i) {
			for (int j = 0; j < 32; ++j) {
				list.add(new int[]{j*8,i*8,8,8});
			}
		}
		tree.addAll(list);
		assertTrue(tree.size() == list.size());
		
		for (Iterator<int[]> cursor = tree.iterator(); cursor.hasNext();) {
			cursor.next();
			cursor.remove();
		}
		assertTrue(tree.size() == 0);
	}
	
	@Test
	public void testContains() {
		IntQuadTreeSet<int[]> tree = new IntQuadTreeSet<int[]>(0, 0, 256, 256, new ArrayIntQuad());
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (int i = 0; i < 32; ++i) {
			for (int j = 0; j < 32; ++j) {
				list.add(new int[]{j*8,i*8,8,8});
			}
		}
		for (int[] o : list) {
			tree.add(o);
		}
		for (int[] o : tree) {
			assertTrue(list.contains(o));
		}
	}
	
	@Test
	public void testQuery() {
		IntQuadTreeSet<int[]> tree = new IntQuadTreeSet<int[]>(0, 0, 256, 256, new ArrayIntQuad());
		for (int i = 0; i < 32; ++i) {
			for (int j = 0; j < 32; ++j) {
				tree.add(new int[]{j*8,i*8,8,8});
			}
		}
		ArrayList<int[]> results = new ArrayList<int[]>();
		tree.query(0, 0, 256, 256, results);
		assertTrue(results.containsAll(tree));
		
		results.clear();
		tree.query(0, 0, 128, 128, results);
		for (int[] o : results) {
			assertTrue(o[0] >= 0 && o[1] >= 0 && o[2] <= 128 && o[3] <= 128);
		}
		
		results.clear();
		tree.query(128, 128, 128, 128, results);
		for (int[] o : results) {
			assertTrue(o[0] >= 128 && o[1] >= 128 && o[2] <= 256 && o[3] <= 256);
		}
	}
	
	@Test
	public void testRemoveQuery() {
		IntQuadTreeSet<int[]> tree = new IntQuadTreeSet<int[]>(0, 0, 256, 256, new ArrayIntQuad());
		for (int i = 0; i < 32; ++i) {
			for (int j = 0; j < 32; ++j) {
				tree.add(new int[]{j*8,i*8,8,8});
			}
		}
		int oldSize = tree.size();
		assertTrue(tree.remove(0, 0, 256, 256) == oldSize);
		assertTrue(tree.size() == 0);
		
		for (int i = 0; i < 32; ++i) {
			for (int j = 0; j < 32; ++j) {
				tree.add(new int[]{j*8,i*8,8,8});
			}
		}
		ArrayList<int[]> bottomright = new ArrayList<int[]>();
		tree.query(128, 128, 128, 128, bottomright);
		oldSize = tree.size();
		
		int removed = tree.remove(128, 128, 128, 128);
		assertTrue(tree.size() == oldSize - removed);
		for (int[] o : bottomright) {
			assertTrue(!tree.contains(o));
		}
	}
	
	@Test
	public void testRemoveRetrieveQuery() {
		IntQuadTreeSet<int[]> tree = new IntQuadTreeSet<int[]>(0, 0, 256, 256, new ArrayIntQuad());
		for (int i = 0; i < 32; ++i) {
			for (int j = 0; j < 32; ++j) {
				tree.add(new int[]{j*8,i*8,8,8});
			}
		}
		ArrayList<int[]> bottomright = new ArrayList<int[]>();
		tree.query(128, 128, 128, 128, bottomright);
		ArrayList<int[]> results = new ArrayList<int[]>();
		tree.remove(128, 128, 128, 128, results);
		assertTrue(bottomright.equals(results));
	}
	
	@Test 
	public void testCount() {
		IntQuadTreeSet<int[]> tree = new IntQuadTreeSet<int[]>(0, 0, 256, 256, new ArrayIntQuad());
		for (int i = 0; i < 32; ++i) {
			for (int j = 0; j < 32; ++j) {
				tree.add(new int[]{j*8,i*8,8,8});
			}
		}
		assertTrue(tree.size(0, 0, 256, 256) == tree.size());
		assertTrue(tree.size(128, 128, 128, 128) == 256);
	}
}
