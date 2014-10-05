package adt.spatial.quad;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import adt.util.QuadNode;

final class IntQuadTreeSetNode<E> extends QuadNode<IntQuadTreeSetNode<E>> {
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

	void clear() {
		first = null;
		second = null;
		third = null;
		fourth = null;
		elements.clear();
	}
	
	@Override
	protected IntQuadTreeSetNode<E> next() {
		return super.next();
	}
	
	@Override
	protected IntQuadTreeSetNode<E> previous() {
		return super.previous();
	}

	void compact() {
		for (IntQuadTreeSetNode<E> cursor = this; cursor.elements.isEmpty()
				&& !cursor.isRoot() && cursor.isLeaf(); cursor = cursor.parent.get()) {
			cursor.unlink();
		}
	}

	boolean split() {
		if (isLeaf() && level < owner.get().depth) {
			first = new IntQuadTreeSetNode<E>(owner.get(), this, level + 1, x, y,
					width / 2, height / 2);
			second = new IntQuadTreeSetNode<E>(owner.get(), this, level + 1, x
					+ width / 2, y, width / 2, height / 2);
			third = new IntQuadTreeSetNode<E>(owner.get(), this, level + 1, x, y
					+ height / 2, width / 2, height / 2);
			fourth = new IntQuadTreeSetNode<E>(owner.get(), this, level + 1, x
					+ width / 2, y + height / 2, width / 2, height / 2);

			for (Iterator<E> iterator = elements.iterator(); iterator.hasNext();) {
				final E e = iterator.next();
				if (first.fit(e)) {
					first.attach(e);
					iterator.remove();
				} else if (second.fit(e)) {
					second.attach(e);
					iterator.remove();
				} else if (third.fit(e)) {
					third.attach(e);
					iterator.remove();
				} else if (fourth.fit(e)) {
					fourth.attach(e);
					iterator.remove();
				}
			}

			return true;
		}
		
		return false;
	}

	boolean fit(int fx, int fy, int fw, int fh) {
		return fx >= x && fy >= y && fx + fw <= x + width
				&& fy + fh <= y + height;
	}

	boolean fit(E o) {
		return fit(owner.get().quad.x(o), owner.get().quad.y(o),
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

	IntQuadTreeSetNode<E> fitNode(E o) {
		return fitNode(owner.get().quad.x(o), owner.get().quad.y(o),
				owner.get().quad.width(o), owner.get().quad.height(o));
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
				node = node.fitNode(o);
				return node.elements.add(o);
			}
			return false;
		}
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

	boolean contains(E o) {
		final IntQuadTreeSetNode<E> node = fitNode(o);

		if (node == null) {
			return false;
		}

		return node.elements.contains(o);
	}

	void query(int qx, int qy, int qw, int qh, Collection<? super E> results) {
		final IntQuadTreeSetNode<E> node = fitNode(qx, qy, qw, qh);

		if (node == null) {
			return;
		}

		for (E e : node.elements) {
			if (owner.get().quad.x(e) >= qx
					&& owner.get().quad.y(e) >= qy
					&& owner.get().quad.x(e) + owner.get().quad.width(e) <= qx
							+ qw
					&& owner.get().quad.y(e) + owner.get().quad.height(e) <= qy
							+ qh) {
				results.add(e);
			}
		}

		if (node.first != null)
			node.first.query(node.first.x, node.first.y, node.first.width, node.first.height,
					results);
		if (node.second != null)
			node.second.query(node.second.x, node.second.y, node.second.width, node.second.height,
					results);
		if (node.third != null)
			node.third.query(node.third.x, node.third.y, node.third.width, node.third.height,
					results);
		if (node.fourth != null)
			node.fourth.query(node.fourth.x, node.fourth.y, node.fourth.width, node.fourth.height,
					results);
	}

	void query(E o, Collection<? super E> results) {
		query(owner.get().quad.x(o), owner.get().quad.y(o),
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
			if (owner.get().quad.x(e) >= qx
					&& owner.get().quad.y(e) >= qy
					&& owner.get().quad.x(e) + owner.get().quad.width(e) <= qx
							+ qw
					&& owner.get().quad.y(e) + owner.get().quad.height(e) <= qy
							+ qh) {
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
			count += node.first.detach(node.first.x, node.first.y, node.first.width,
					node.first.height);
		if (node.second != null)
			count += node.second.detach(node.second.x, node.second.y, node.second.width,
					node.second.height);
		if (node.third != null)
			count += node.third.detach(node.third.x, node.third.y, node.third.width,
					node.third.height);
		if (node.fourth != null)
			count += node.fourth.detach(node.fourth.x, node.fourth.y, node.fourth.width,
					node.fourth.height);

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
			if (owner.get().quad.x(e) >= qx
					&& owner.get().quad.y(e) >= qy
					&& owner.get().quad.x(e) + owner.get().quad.width(e) <= qx
							+ qw
					&& owner.get().quad.y(e) + owner.get().quad.height(e) <= qy
							+ qh) {
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
			node.first.detach(node.first.x, node.first.y, node.first.width, node.first.height,
					results);
		if (node.second != null)
			node.second.detach(node.second.x, node.second.y, node.second.width, node.second.height,
					results);
		if (node.third != null)
			node.third.detach(node.third.x, node.third.y, node.third.width, node.third.height,
					results);
		if (node.fourth != null)
			node.fourth.detach(node.fourth.x, node.fourth.y, node.fourth.width, node.fourth.height,
					results);
	}

	void detach(E o, Collection<? super E> results) {
		detach(owner.get().quad.x(o), owner.get().quad.y(o),
				owner.get().quad.width(o), owner.get().quad.height(o), results);
	}

	int count(int qx, int qy, int qw, int qh) {
		final IntQuadTreeSetNode<E> node = fitNode(qx, qy, qw, qh);

		if (node == null) {
			return 0;
		}

		int count = 0;

		for (E e : node.elements) {
			if (owner.get().quad.x(e) >= qx
					&& owner.get().quad.y(e) >= qy
					&& owner.get().quad.x(e) + owner.get().quad.width(e) <= qx
							+ qw
					&& owner.get().quad.y(e) + owner.get().quad.height(e) <= qy
							+ qh) {
				++count;
			}
		}

		if (node.first != null)
			count += node.first.count(node.first.x, node.first.y, node.first.width,
					node.first.height);
		if (node.second != null)
			count += node.second.count(node.second.x, node.second.y, node.second.width,
					node.second.height);
		if (node.third != null)
			count += node.third.count(node.third.x, node.third.y, node.third.width,
					node.third.height);
		if (node.fourth != null)
			count += node.fourth.count(node.fourth.x, node.fourth.y, node.fourth.width,
					node.fourth.height);

		return count;
	}

	int count(E o) {
		return count(owner.get().quad.x(o), owner.get().quad.y(o),
				owner.get().quad.width(o), owner.get().quad.height(o));
	}

	final WeakReference<IntQuadTreeSet<E>> owner;
	final int level;
	final HashSet<E> elements = new HashSet<E>();
	final int x, y, width, height;
}
