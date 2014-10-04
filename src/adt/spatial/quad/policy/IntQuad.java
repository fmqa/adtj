package adt.spatial.quad.policy;

public interface IntQuad<T> {
	int x(T x);
	int y(T x);
	int width(T x);
	int height(T x);
}
