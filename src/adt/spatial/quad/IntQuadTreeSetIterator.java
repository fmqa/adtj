package adt.spatial.quad;

import java.util.Iterator;

final class IntQuadTreeSetIterator<E> implements Iterator<E> {
	IntQuadTreeSetIterator(IntQuadTreeSetNode<E> node) {
		prevNode = null;
		this.node = node;
		advance();
	}
	
	void advance() {
		while (node != null && node.elements.isEmpty()) {
			node = node.next();
		}
		if (node != null) {
			cursor = node.elements.iterator();
		}
	}
	
	@Override
	public boolean hasNext() {
		return cursor.hasNext();
	}

	@Override
	public E next() {
		E element = cursor.next();
		prevCursor = cursor;
		prevNode = node;
		if (!cursor.hasNext()) {
			node = node.next();
			advance();
		}
		return element;
	}

	@Override
	public void remove() {
		int count = prevNode.elements.size();
		prevCursor.remove();
		if (prevNode.elements.size() == count - 1) {
			--prevNode.owner.get().count;
		}
	}
	
	IntQuadTreeSetNode<E> prevNode;
	IntQuadTreeSetNode<E> node;
	Iterator<E> prevCursor;
	Iterator<E> cursor;
}
