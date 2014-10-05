package adt.util;

import java.lang.ref.WeakReference;

public abstract class QuadNode<T extends QuadNode<T>> {
	protected QuadNode() {
		this(null);
	}
	
	protected QuadNode(T parent) {
		this(parent, null, null, null, null);
	}
	
	protected QuadNode(T parent, T first, T second, T third, T fourth) {
		this.parent = new WeakReference<T>(parent);
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}
	
	@SuppressWarnings("unchecked")
	protected T next() {
		if (first != null) {
			return first;
		} else if (second != null) {
			return second;
		} else if (third != null) {
			return third;
		} else if (fourth != null) {
			return fourth;
		} else {
			T cursor = (T) this;
			while (!cursor.isRoot()) {
				if (cursor == cursor.parent.get().first) {
					return cursor.parent.get().second;
				} else if (cursor == cursor.parent.get().second) {
					return cursor.parent.get().third;
				} else if (cursor == cursor.parent.get().third) {
					return cursor.parent.get().fourth;
				} else if (cursor == cursor.parent.get().fourth) {
					cursor = cursor.parent.get();
				} else {
					return null;
				}
			}
			if (!cursor.isRoot()) {
				return cursor;
			} else {
				return null;
			}
		}
	}
	
	protected T previous() {
		if (!isRoot()) {
			T cursor = null;
			boolean cascade = false;
			if (this == parent.get().fourth) {
				cascade = parent.get().third == null; 
				if (!cascade) {
					cursor = parent.get().third;
				}
			} else if (this == parent.get().third || cascade) {
				cascade = parent.get().second == null;
				if (!cascade) {
					cursor = parent.get().second;
				}
			} else if (this == parent.get().second || cascade) {
				cascade = parent.get().first == null;
				if (!cascade) {
					cursor = parent.get().first;
				}
			} else if (this == parent.get().first || cascade) {
				return parent.get();
			}
			boolean internal = true;
			do {
				if (cursor.fourth != null) {
					cursor = cursor.fourth;
				} else if (cursor.third != null) {
					cursor = cursor.third;
				} else if (cursor.second != null) {
					cursor = cursor.second;
				} else if (cursor.first != null) {
					cursor = cursor.first;
				} else {
					internal = false;
				}
			} while (internal);
			return cursor;
		} else {
			return null;
		}
	}
	
	protected boolean unlink() {
		if (!isRoot()) {
			if (this == parent.get().first) {
				parent.get().first = null;
				return true;
			} else if (this == parent.get().second) {
				parent.get().second = null;
				return true;
			} else if (this == parent.get().third) {
				parent.get().third = null;
				return true;
			} else if (this == parent.get().fourth) {
				parent.get().fourth = null;
				return true;
			}
		}
		return false;
	}
	
	protected boolean isLeaf() {
		return first == null && second == null && third == null && fourth == null;
	}
	
	protected boolean isRoot() {
		return parent.get() == null;
	}
	
	protected WeakReference<T> parent;
	protected T first, second, third, fourth;
}
