package adt.spatial.quad;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

import adt.spatial.quad.policy.IntQuad;

public class IntQuadTreeSet<E> extends AbstractSet<E> {
	final IntQuad<? super E> quad;
	final int threshold;
	final int depth;
	final IntQuadTreeSetNode<E> root;
	int count;

	public IntQuadTreeSet(E o, IntQuad<? super E> quad) {
		this(o, quad, 16);
	}

	public IntQuadTreeSet(E o, IntQuad<? super E> quad, int threshold) {
		this(o, quad, threshold, 128);
	}

	public IntQuadTreeSet(E o, IntQuad<? super E> quad, int threshold, int depth) {
		this(quad.x(o), quad.y(o), quad.width(o), quad.height(o), quad,
				threshold, depth);
	}

	public IntQuadTreeSet(int x, int y, int width, int height,
			IntQuad<? super E> quad) {
		this(x, y, width, height, quad, 16);
	}

	public IntQuadTreeSet(int x, int y, int width, int height,
			IntQuad<? super E> quad, int threshold) {
		this(x, y, width, height, quad, threshold, 128);
	}

	public IntQuadTreeSet(int x, int y, int width, int height,
			IntQuad<? super E> quad, int threshold, int depth) {
		this.quad = quad;
		this.threshold = threshold;
		this.depth = depth;
		this.root = new IntQuadTreeSetNode<E>(this, null, 0, x, y, width,
				height);
	}

	@Override
	public boolean add(E o) {
		if (root.attach(o)) {
			++count;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void clear() {
		root.clear();
		count = 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {
		return root.contains((E) o);
	}

	@Override
	public Iterator<E> iterator() {
		return new IntQuadTreeSetIterator<E>(root);
	}

	public void query(int x, int y, int width, int height,
			Collection<? super E> results) {
		root.query(x, y, width, height, results);
	}

	public void remove(E o, Collection<? super E> results) {
		root.detach(o, results);
	}

	public int remove(int x, int y, int width, int height) {
		return root.detach(x, y, width, height);
	}

	public void remove(int x, int y, int width, int height,
			Collection<? super E> results) {
		root.detach(x, y, width, height, results);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		if (root.detach((E) o)) {
			--count;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean modified = false;
		for (Object o : c) {
			modified |= remove(o);
		}
		return modified;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean modified = false;
		for (Object o : c) {
			if (!contains(o)) {
				modified |= remove(o);
			}
		}
		return modified;
	}

	@Override
	public int size() {
		return count;
	}

	public int size(E o) {
		return root.count(o);
	}

	public int size(int x, int y, int width, int height) {
		return root.count(x, y, width, height);
	}
}
