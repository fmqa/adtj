package adt.spatial.quad;

import java.util.Iterator;

final class IntQuadTreeSetIterator<E> implements Iterator<E> {
	IntQuadTreeSetIterator(IntQuadTreeSetNode<E> node) {
		this.node = node;
		moveToNextNonEmptyNode();
		cursor = this.node.elements.iterator();
	}
	
	void moveToNextNode() {
		if (!visited && node.nw != null) node = node.nw;
		else if (!visited && node.ne != null) node = node.ne;
		else if (!visited && node.sw != null) node = node.sw;
		else if (!visited && node.se != null) node = node.se;
		else if (node.parent.get() != null) {
			if (visited) {
				visited = false;
			}
			if (node == node.parent.get().nw) node = node.parent.get().ne;
			else if (node == node.parent.get().ne) node = node.parent.get().sw;
			else if (node == node.parent.get().sw) node = node.parent.get().se;
			else if (node == node.parent.get().se) {
				node = node.parent.get();
				visited = true;
				moveToNextNode();
			}
		}
	}
	
	boolean ended() {
		return node.parent.get() == null && visited;
	}
	
	void moveToNextNonEmptyNode() {
		while ((node.elements.isEmpty() || visited) && !ended()) {
			moveToNextNode();
		}
	}
	
	@Override
	public boolean hasNext() {
		return cursor.hasNext() && !ended();
	}

	@Override
	public E next() {
		final E last = cursor.next();
		lastcursor = cursor;
		if (!cursor.hasNext() && !ended()) {
			visited = true;
			lastnode = node;
			moveToNextNonEmptyNode();
			lastcursor = cursor;
			cursor = node.elements.iterator();
		}
		return last;
	}

	@Override
	public void remove() {
		if (lastcursor != cursor) {
			int oldSize = lastnode.elements.size();
			lastcursor.remove();
			if (lastnode.elements.size() == oldSize - 1) {
				--node.owner.get().count;
				lastnode.compact();
			}
		} else {
			int oldSize = node.elements.size();
			cursor.remove();
			if (node.elements.size() == oldSize - 1) {
				--node.owner.get().count;
				node.compact();
			}
		}
	}
	
	IntQuadTreeSetNode<E> node;
	IntQuadTreeSetNode<E> lastnode;
	Iterator<E> cursor;
	Iterator<E> lastcursor;
	boolean visited;
}
