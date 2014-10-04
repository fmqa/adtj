package adt.spatial.quad;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

final class IntQuadTreeSetNode<E> {
	IntQuadTreeSetNode(IntQuadTreeSet<E> owner, IntQuadTreeSetNode<E> parent,
			int level, int x, int y, int width, int height) {
		this.owner = new WeakReference<IntQuadTreeSet<E>>(owner);
		this.parent = new WeakReference<IntQuadTreeSetNode<E>>(parent);
		this.level = level;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	void clear() {
		nw = null;
		ne = null;
		sw = null;
		se = null;
		elements.clear();
	}

	void compact() {
		for (IntQuadTreeSetNode<E> cursor = this; cursor.elements.isEmpty()
				&& cursor.parent.get() != null; cursor = cursor.parent.get()) {
			if (cursor == cursor.parent.get().nw) {
				cursor.parent.get().nw = null;
			} else if (cursor == cursor.parent.get().ne) {
				cursor.parent.get().ne = null;
			} else if (cursor == cursor.parent.get().sw) {
				cursor.parent.get().sw = null;
			} else if (cursor == cursor.parent.get().se) {
				cursor.parent.get().se = null;
			}
			if (cursor.parent.get().nw != null
					|| cursor.parent.get().ne != null
					|| cursor.parent.get().sw != null
					|| cursor.parent.get().se != null) {
				break;
			}
		}
	}

	boolean split() {
		if (nw == null && ne == null && sw == null && se == null
				&& level < owner.get().depth) {
			nw = new IntQuadTreeSetNode<E>(owner.get(), this, level + 1, x, y,
					width / 2, height / 2);
			ne = new IntQuadTreeSetNode<E>(owner.get(), this, level + 1, x
					+ width / 2, y, width / 2, height / 2);
			sw = new IntQuadTreeSetNode<E>(owner.get(), this, level + 1, x, y
					+ height / 2, width / 2, height / 2);
			se = new IntQuadTreeSetNode<E>(owner.get(), this, level + 1, x
					+ width / 2, y + height / 2, width / 2, height / 2);

			for (Iterator<E> iterator = elements.iterator(); iterator.hasNext();) {
				final E e = iterator.next();
				if (nw.fit(e)) {
					nw.attach(e);
					iterator.remove();
				} else if (ne.fit(e)) {
					ne.attach(e);
					iterator.remove();
				} else if (sw.fit(e)) {
					sw.attach(e);
					iterator.remove();
				} else if (se.fit(e)) {
					se.attach(e);
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
		if (nw != null && nw.fit(fx, fy, fw, fh)) {
			return nw.fitNode(fx, fy, fw, fh);
		}
		if (ne != null && ne.fit(fx, fy, fw, fh)) {
			return ne.fitNode(fx, fy, fw, fh);
		}
		if (sw != null && sw.fit(fx, fy, fw, fh)) {
			return sw.fitNode(fx, fy, fw, fh);
		}
		if (se != null && se.fit(fx, fy, fw, fh)) {
			return se.fitNode(fx, fy, fw, fh);
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

		if (node.nw != null)
			node.nw.query(node.nw.x, node.nw.y, node.nw.width, node.nw.height,
					results);
		if (node.ne != null)
			node.ne.query(node.ne.x, node.ne.y, node.ne.width, node.ne.height,
					results);
		if (node.sw != null)
			node.sw.query(node.sw.x, node.sw.y, node.sw.width, node.sw.height,
					results);
		if (node.se != null)
			node.se.query(node.se.x, node.se.y, node.se.width, node.se.height,
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

		if (node.nw != null)
			count += node.nw.detach(node.nw.x, node.nw.y, node.nw.width,
					node.nw.height);
		if (node.ne != null)
			count += node.ne.detach(node.ne.x, node.ne.y, node.ne.width,
					node.ne.height);
		if (node.sw != null)
			count += node.sw.detach(node.sw.x, node.sw.y, node.sw.width,
					node.sw.height);
		if (node.se != null)
			count += node.se.detach(node.se.x, node.se.y, node.se.width,
					node.se.height);

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

		if (node.nw != null)
			node.nw.detach(node.nw.x, node.nw.y, node.nw.width, node.nw.height,
					results);
		if (node.ne != null)
			node.ne.detach(node.ne.x, node.ne.y, node.ne.width, node.ne.height,
					results);
		if (node.sw != null)
			node.sw.detach(node.sw.x, node.sw.y, node.sw.width, node.sw.height,
					results);
		if (node.se != null)
			node.se.detach(node.se.x, node.se.y, node.se.width, node.se.height,
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

		if (node.nw != null)
			count += node.nw.count(node.nw.x, node.nw.y, node.nw.width,
					node.nw.height);
		if (node.ne != null)
			count += node.ne.count(node.ne.x, node.ne.y, node.ne.width,
					node.ne.height);
		if (node.sw != null)
			count += node.sw.count(node.sw.x, node.sw.y, node.sw.width,
					node.sw.height);
		if (node.se != null)
			count += node.se.count(node.se.x, node.se.y, node.se.width,
					node.se.height);

		return count;
	}

	int count(E o) {
		return count(owner.get().quad.x(o), owner.get().quad.y(o),
				owner.get().quad.width(o), owner.get().quad.height(o));
	}

	final WeakReference<IntQuadTreeSet<E>> owner;
	final WeakReference<IntQuadTreeSetNode<E>> parent;
	final int level;
	final HashSet<E> elements = new HashSet<E>();
	final int x, y, width, height;
	IntQuadTreeSetNode<E> nw, ne, sw, se;
}
