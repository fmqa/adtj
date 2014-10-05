package adt.util.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import adt.util.QuadNode;

public class QuadNodeTest {
	static class SimpleQuadNode extends QuadNode<SimpleQuadNode> {
		public SimpleQuadNode() {
			super();
		}
		
		public SimpleQuadNode(SimpleQuadNode parent) {
			super(parent);
		}
		
		public SimpleQuadNode(SimpleQuadNode parent, SimpleQuadNode first, SimpleQuadNode second, SimpleQuadNode third, SimpleQuadNode fourth) {
			super(parent, first, second, third, fourth);
		}
		
		@Override
		public SimpleQuadNode next() {
			return super.next();
		}
		
		@Override
		public SimpleQuadNode previous() {
			return super.previous();
		}
		
		public void setFirst(SimpleQuadNode first) {
			this.first = first;
		}
		
		public SimpleQuadNode getFirst() {
			return first;
		}
		
		public void setSecond(SimpleQuadNode second) {
			this.second = second;
		}
		
		public SimpleQuadNode getSecond() {
			return second;
		}
		
		public void setThird(SimpleQuadNode third) {
			this.third = third;
		}
		
		public SimpleQuadNode getThird() {
			return third;
		}
		
		public void setFourth(SimpleQuadNode fourth) {
			this.fourth = fourth;
		}
		
		public SimpleQuadNode getFourth() {
			return fourth;
		}
	}

	@Test
	public void testNext() {
		SimpleQuadNode root = new SimpleQuadNode();
		root.setFirst(new SimpleQuadNode(root));
		root.setSecond(new SimpleQuadNode(root));
		root.setThird(new SimpleQuadNode(root));
		root.setFourth(new SimpleQuadNode(root));
		
		SimpleQuadNode cursor = root.next();
		assertTrue(cursor == root.getFirst());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond());
		cursor = cursor.next();
		assertTrue(cursor == root.getThird());
		cursor = cursor.next();
		assertTrue(cursor == root.getFourth());
		assertTrue(cursor.next() == null);
		
		root.getFirst().setFirst(new SimpleQuadNode(root.getFirst()));
		root.getFirst().setSecond(new SimpleQuadNode(root.getFirst()));
		root.getFirst().setThird(new SimpleQuadNode(root.getFirst()));
		root.getFirst().setFourth(new SimpleQuadNode(root.getFirst()));
		
		cursor = root.next();
		assertTrue(cursor == root.getFirst());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getFirst());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getSecond());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getThird());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getFourth());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond());
		cursor = cursor.next();
		assertTrue(cursor == root.getThird());
		cursor = cursor.next();
		assertTrue(cursor == root.getFourth());
		cursor = cursor.next();
		assertTrue(cursor == null);
		
		root.getSecond().setFirst(new SimpleQuadNode(root.getSecond()));
		root.getSecond().setSecond(new SimpleQuadNode(root.getSecond()));
		root.getSecond().setThird(new SimpleQuadNode(root.getSecond()));
		root.getSecond().setFourth(new SimpleQuadNode(root.getSecond()));
		
		cursor = root.next();
		assertTrue(cursor == root.getFirst());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getFirst());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getSecond());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getThird());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getFourth());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond().getFirst());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond().getSecond());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond().getThird());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond().getFourth());
		cursor = cursor.next();
		assertTrue(cursor == root.getThird());
		cursor = cursor.next();
		assertTrue(cursor == root.getFourth());
		cursor = cursor.next();
		assertTrue(cursor == null);
		
		root.getThird().setFirst(new SimpleQuadNode(root.getThird()));
		root.getThird().setSecond(new SimpleQuadNode(root.getThird()));
		root.getThird().setThird(new SimpleQuadNode(root.getThird()));
		root.getThird().setFourth(new SimpleQuadNode(root.getThird()));
		
		cursor = root.next();
		assertTrue(cursor == root.getFirst());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getFirst());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getSecond());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getThird());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getFourth());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond().getFirst());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond().getSecond());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond().getThird());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond().getFourth());
		cursor = cursor.next();
		assertTrue(cursor == root.getThird());
		cursor = cursor.next();
		assertTrue(cursor == root.getThird().getFirst());
		cursor = cursor.next();
		assertTrue(cursor == root.getThird().getSecond());
		cursor = cursor.next();
		assertTrue(cursor == root.getThird().getThird());
		cursor = cursor.next();
		assertTrue(cursor == root.getThird().getFourth());
		cursor = cursor.next();
		assertTrue(cursor == root.getFourth());
		cursor = cursor.next();
		assertTrue(cursor == null);
		
		root.getFourth().setFirst(new SimpleQuadNode(root.getFourth()));
		root.getFourth().setSecond(new SimpleQuadNode(root.getFourth()));
		root.getFourth().setThird(new SimpleQuadNode(root.getFourth()));
		root.getFourth().setFourth(new SimpleQuadNode(root.getFourth()));
		
		cursor = root.next();
		assertTrue(cursor == root.getFirst());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getFirst());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getSecond());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getThird());
		cursor = cursor.next();
		assertTrue(cursor == root.getFirst().getFourth());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond().getFirst());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond().getSecond());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond().getThird());
		cursor = cursor.next();
		assertTrue(cursor == root.getSecond().getFourth());
		cursor = cursor.next();
		assertTrue(cursor == root.getThird());
		cursor = cursor.next();
		assertTrue(cursor == root.getThird().getFirst());
		cursor = cursor.next();
		assertTrue(cursor == root.getThird().getSecond());
		cursor = cursor.next();
		assertTrue(cursor == root.getThird().getThird());
		cursor = cursor.next();
		assertTrue(cursor == root.getThird().getFourth());
		cursor = cursor.next();
		assertTrue(cursor == root.getFourth());
		cursor = cursor.next();
		assertTrue(cursor == root.getFourth().getFirst());
		cursor = cursor.next();
		assertTrue(cursor == root.getFourth().getSecond());
		cursor = cursor.next();
		assertTrue(cursor == root.getFourth().getThird());
		cursor = cursor.next();
		assertTrue(cursor == root.getFourth().getFourth());
		cursor = cursor.next();
		assertTrue(cursor == null);
		
		SimpleQuadNode[] dft = {root, root.getFirst(), 
				root.getFirst().getFirst(), root.getFirst().getSecond(), 
				root.getFirst().getThird(), root.getFirst().getFourth(),
				
				root.getSecond(), root.getSecond().getFirst(), root.getSecond().getSecond(),
				root.getSecond().getThird(), root.getSecond().getFourth(),
				
				root.getThird(), root.getThird().getFirst(), root.getThird().getSecond(),
				root.getThird().getThird(), root.getThird().getFourth(),
				
				root.getFourth(), root.getFourth().getFirst(), root.getFourth().getSecond(),
				root.getFourth().getThird(), root.getFourth().getFourth()
		};
		
		int i = 0;
		for (cursor = root; cursor != null; cursor = cursor.next()) {
			assertTrue(cursor == dft[i++]);
		}
	}

	@Test
	public void testPrevious() {
		SimpleQuadNode root = new SimpleQuadNode();
		root.setFirst(new SimpleQuadNode(root));
		root.setSecond(new SimpleQuadNode(root));
		root.setThird(new SimpleQuadNode(root));
		root.setFourth(new SimpleQuadNode(root));
		
		SimpleQuadNode cursor = root.getFourth();
		cursor = cursor.previous();
		assertTrue(cursor == root.getThird());
		cursor = cursor.previous();
		assertTrue(cursor == root.getSecond());
		cursor = cursor.previous();
		assertTrue(cursor == root.getFirst());
		cursor = cursor.previous();
		assertTrue(cursor == root);
		cursor = cursor.previous();
		assertTrue(cursor == null);
		
		root.getFirst().setFirst(new SimpleQuadNode(root.getFirst()));
		root.getFirst().setSecond(new SimpleQuadNode(root.getFirst()));
		root.getFirst().setThird(new SimpleQuadNode(root.getFirst()));
		root.getFirst().setFourth(new SimpleQuadNode(root.getFirst()));
		
		root.getSecond().setFirst(new SimpleQuadNode(root.getSecond()));
		root.getSecond().setSecond(new SimpleQuadNode(root.getSecond()));
		root.getSecond().setThird(new SimpleQuadNode(root.getSecond()));
		root.getSecond().setFourth(new SimpleQuadNode(root.getSecond()));
		
		root.getThird().setFirst(new SimpleQuadNode(root.getThird()));
		root.getThird().setSecond(new SimpleQuadNode(root.getThird()));
		root.getThird().setThird(new SimpleQuadNode(root.getThird()));
		root.getThird().setFourth(new SimpleQuadNode(root.getThird()));
		
		root.getFourth().setFirst(new SimpleQuadNode(root.getFourth()));
		root.getFourth().setSecond(new SimpleQuadNode(root.getFourth()));
		root.getFourth().setThird(new SimpleQuadNode(root.getFourth()));
		root.getFourth().setFourth(new SimpleQuadNode(root.getFourth()));
		
		SimpleQuadNode[] dft = {root, root.getFirst(), 
				root.getFirst().getFirst(), root.getFirst().getSecond(), 
				root.getFirst().getThird(), root.getFirst().getFourth(),
				
				root.getSecond(), root.getSecond().getFirst(), root.getSecond().getSecond(),
				root.getSecond().getThird(), root.getSecond().getFourth(),
				
				root.getThird(), root.getThird().getFirst(), root.getThird().getSecond(),
				root.getThird().getThird(), root.getThird().getFourth(),
				
				root.getFourth(), root.getFourth().getFirst(), root.getFourth().getSecond(),
				root.getFourth().getThird(), root.getFourth().getFourth()
		};
		
		int i = dft.length;
		for (cursor = root.getFourth().getFourth(); cursor != null; cursor = cursor.previous()) {
			assertTrue(cursor == dft[--i]);
		}
	}

}
