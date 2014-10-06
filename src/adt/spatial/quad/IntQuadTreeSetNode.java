package adt.spatial.quad;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import adt.util.Geometry;
import adt.util.QuadNode;

final class IntQuadTreeSetNode<E> extends QuadNode<IntQuadTreeSetNode<E>> {
	final WeakReference<IntQuadTreeSet<E>> owner;
	final int level;
	final HashSet<E> elements = new HashSet<E>();
	final int x, y, width, height;

	IntQuadTreeSetNode(IntQuadTreeSet<E> owner, IntQuadTreeSetNode<E> parent,
			int level, int x, int y, int width, int height) {
		super(parent);
		this.owner = new WeakReference<IntQuadTreeSet<E>>(owner);
		this.level = level;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	boolean attach(E o) {
		IntQuadTreeSetNode<E> node = fitNode(o);

		if (node == null) {
			return false;
		}

		if (node.elements.size() < owner.get().threshold) {
			return node.elements.add(o);
		} else {
			if (node.split()) {
				if (Quads.contains(node.x, node.y, node.width / 2,
						node.height / 2, o, owner.get().quad)) {
					if (node.first == null) {
						node.first = new IntQuadTreeSetNode<E>(owner.get(),
								node, node.level + 1, node.x, node.y,
								node.width / 2, node.height / 2);
					}
					node = node.first;
				} else if (Quads.contains(node.x + node.width / 2, node.y,
						node.width / 2, node.height / 2, o, owner.get().quad)) {
					if (node.second == null) {
						node.second = new IntQuadTreeSetNode<E>(owner.get(),
								node, node.level + 1, node.x + node.width / 2,
								node.y, node.width / 2, node.height / 2);
					}
					node = node.second;
				} else if (Quads.contains(node.x, node.y + node.height / 2,
						node.width / 2, node.height / 2, o, owner.get().quad)) {
					if (node.third == null) {
						node.third = new IntQuadTreeSetNode<E>(owner.get(),
								node, node.level + 1, node.x, node.y
										+ node.height / 2, node.width / 2,
								node.height / 2);
					}
					node = node.third;
				} else if (Quads.contains(node.x + node.width / 2, node.y
						+ node.height / 2, node.width / 2, node.height / 2, o,
						owner.get().quad)) {
					if (node.fourth == null) {
						node.fourth = new IntQuadTreeSetNode<E>(owner.get(),
								node, node.level + 1, node.x + node.width / 2,
								node.y + node.height / 2, node.width / 2,
								node.height / 2);
					}
					node = node.fourth;
				}
				return node.elements.add(o);
			}
			return false;
		}
	}

	void clear() {
		first = null;
		second = null;
		third = null;
		fourth = null;
		elements.clear();
	}

	void compact() {
		for (IntQuadTreeSetNode<E> cursor = this; cursor.elements.isEmpty()
				&& !cursor.isRoot() && cursor.isLeaf(); cursor = cursor.parent
				.get()) {
			cursor.unlink();
		}
	}

	boolean contains(E o) {
		final IntQuadTreeSetNode<E> node = fitNode(o);

		if (node == null) {
			return false;
		}

		return node.elements.contains(o);
	}

	int count(E o) {
		return count(owner.get().quad.x(o), owner.get().quad.y(o),
				owner.get().quad.width(o), owner.get().quad.height(o));
	}

	int count(int qx, int qy, int qw, int qh) {
		final IntQuadTreeSetNode<E> node = fitNode(qx, qy, qw, qh);

		if (node == null) {
			return 0;
		}

		int count = 0;

		for (E e : node.elements) {
			if (Quads.contains(qx, qy, qw, qh, e, owner.get().quad)) {
				++count;
			}
		}

		if (node.first != null)
			count += node.first.count(node.first.x, node.first.y,
					node.first.width, node.first.height);
		if (node.second != null)
			count += node.second.count(node.second.x, node.second.y,
					node.second.width, node.second.height);
		if (node.third != null)
			count += node.third.count(node.third.x, node.third.y,
					node.third.width, node.third.height);
		if (node.fourth != null)
			count += node.fourth.count(node.fourth.x, node.fourth.y,
					node.fourth.width, node.fourth.height);

		return count;
	}

	boolean detach(E o) {
		final IntQuadTreeSetNode<E> node = fitNode(o);

		if (node == null) {
			return false;
		}

		if (node.elements.remove(o)) {
			node.compact();
			return true;
		}

		return false;
	}

	void detach(E o, Collection<? super E> results) {
		detach(owner.get().quad.x(o), owner.get().quad.y(o),
				owner.get().quad.width(o), owner.get().quad.height(o), results);
	}

	int detach(int qx, int qy, int qw, int qh) {
		final IntQuadTreeSetNode<E> node = fitNode(qx, qy, qw, qh);

		if (node == null) {
			return 0;
		}

		int count = 0;

		for (Iterator<E> iterator = node.elements.iterator(); iterator
				.hasNext();) {
			final E e = iterator.next();
			if (Quads.contains(qx, qy, qw, qh, e, owner.get().quad)) {
				final int oldSize = node.elements.size();
				iterator.remove();
				if (node.elements.size() == oldSize - 1) {
					++count;
					--owner.get().count;
					node.compact();
				}
			}
		}

		if (node.first != null)
			count += node.first.detach(node.first.x, node.first.y,
					node.first.width, node.first.height);
		if (node.second != null)
			count += node.second.detach(node.second.x, node.second.y,
					node.second.width, node.second.height);
		if (node.third != null)
			count += node.third.detach(node.third.x, node.third.y,
					node.third.width, node.third.height);
		if (node.fourth != null)
			count += node.fourth.detach(node.fourth.x, node.fourth.y,
					node.fourth.width, node.fourth.height);

		return count;
	}

	void detach(int qx, int qy, int qw, int qh, Collection<? super E> results) {
		final IntQuadTreeSetNode<E> node = fitNode(qx, qy, qw, qh);

		if (node == null) {
			return;
		}

		for (Iterator<E> iterator = node.elements.iterator(); iterator
				.hasNext();) {
			final E e = iterator.next();
			if (Quads.contains(qx, qy, qw, qh, e, owner.get().quad)) {
				final int oldSize = node.elements.size();
				iterator.remove();
				if (node.elements.size() == oldSize - 1) {
					results.add(e);
					--owner.get().count;
					node.compact();
				}
			}
		}

		if (node.first != null)
			node.first.detach(node.first.x, node.first.y, node.first.width,
					node.first.height, results);
		if (node.second != null)
			node.second.detach(node.second.x, node.second.y, node.second.width,
					node.second.height, results);
		if (node.third != null)
			node.third.detach(node.third.x, node.third.y, node.third.width,
					node.third.height, results);
		if (node.fourth != null)
			node.fourth.detach(node.fourth.x, node.fourth.y, node.fourth.width,
					node.fourth.height, results);
	}

	boolean fit(int fx, int fy, int fw, int fh) {
		return Geometry.contains(x, y, width, height, fx, fy, fw, fh);
	}

	IntQuadTreeSetNode<E> fitNode(E o) {
		return fitNode(owner.get().quad.x(o), owner.get().quad.y(o),
				owner.get().quad.width(o), owner.get().quad.height(o));
	}

	IntQuadTreeSetNode<E> fitNode(int fx, int fy, int fw, int fh) {
		if (first != null && first.fit(fx, fy, fw, fh)) {
			return first.fitNode(fx, fy, fw, fh);
		}
		if (second != null && second.fit(fx, fy, fw, fh)) {
			return second.fitNode(fx, fy, fw, fh);
		}
		if (third != null && third.fit(fx, fy, fw, fh)) {
			return third.fitNode(fx, fy, fw, fh);
		}
		if (fourth != null && fourth.fit(fx, fy, fw, fh)) {
			return fourth.fitNode(fx, fy, fw, fh);
		}
		if (fit(fx, fy, fw, fh)) {
			return this;
		}
		return null;
	}

	@Override
	protected IntQuadTreeSetNode<E> next() {
		return super.next();
	}

	@Override
	protected IntQuadTreeSetNode<E> previous() {
		return super.previous();
	}

	void query(E o, Collection<? super E> results) {
		query(owner.get().quad.x(o), owner.get().quad.y(o),
				owner.get().quad.width(o), owner.get().quad.height(o), results);
	}

	void query(int qx, int qy, int qw, int qh, Collection<? super E> results) {
		final IntQuadTreeSetNode<E> node = fitNode(qx, qy, qw, qh);

		if (node == null) {
			return;
		}

		for (E e : node.elements) {
			if (Quads.contains(qx, qy, qw, qh, e, owner.get().quad)) {
				results.add(e);
			}
		}

		if (node.first != null)
			node.first.query(node.first.x, node.first.y, node.first.width,
					node.first.height, results);
		if (node.second != null)
			node.second.query(node.second.x, node.second.y, node.second.width,
					node.second.height, results);
		if (node.third != null)
			node.third.query(node.third.x, node.third.y, node.third.width,
					node.third.height, results);
		if (node.fourth != null)
			node.fourth.query(node.fourth.x, node.fourth.y, node.fourth.width,
					node.fourth.height, results);
	}

	boolean split() {
		if (level < owner.get().depth) {
			for (Iterator<E> iterator = elements.iterator(); iterator.hasNext();) {
				final E e = iterator.next();
				if (Quads.contains(x, y, width / 2, height / 2, e,
						owner.get().quad)) {
					if (first == null) {
						first = new IntQuadTreeSetNode<E>(owner.get(), this,
								level + 1, x, y, width / 2, height / 2);
					}
					first.attach(e);
					iterator.remove();
				} else if (Quads.contains(x + width / 2, y, width / 2,
						height / 2, e, owner.get().quad)) {
					if (second == null) {
						second = new IntQuadTreeSetNode<E>(owner.get(), this,
								level + 1, x + width / 2, y, width / 2,
								height / 2);
					}
					second.attach(e);
					iterator.remove();
				} else if (Quads.contains(x, y + height / 2, width / 2,
						height / 2, e, owner.get().quad)) {
					if (third == null) {
						third = new IntQuadTreeSetNode<E>(owner.get(), this,
								level + 1, x, y + height / 2, width / 2,
								height / 2);
					}
					third.attach(e);
					iterator.remove();
				} else if (Quads.contains(x + width / 2, y + height / 2,
						width / 2, height / 2, e, owner.get().quad)) {
					if (fourth == null) {
						fourth = new IntQuadTreeSetNode<E>(owner.get(), this,
								level + 1, x + width / 2, y + height / 2,
								width / 2, height / 2);
					}
					fourth.attach(e);
					iterator.remove();
				}
			}
			return true;
		}
		return false;
	}
}
