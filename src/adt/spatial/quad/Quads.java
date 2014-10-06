package adt.spatial.quad;

import adt.spatial.quad.policy.IntQuad;
import adt.util.Geometry;

public final class Quads {
	public static <T> boolean contains(int x0, int y0, int w0, int h0, T o,
			IntQuad<? super T> quad) {
		return Geometry.contains(x0, y0, w0, h0, quad.x(o), quad.y(o),
				quad.width(o), quad.height(o));
	}

	public static <T> boolean contains(T o, IntQuad<? super T> quad, int x1,
			int y1, int w1, int h1) {
		return Geometry.contains(quad.x(o), quad.y(o), quad.width(o),
				quad.height(o), x1, y1, w1, h1);
	}

	public static <T> boolean contains(T o1, T o2, IntQuad<? super T> quad) {
		return Geometry.contains(quad.x(o1), quad.y(o1), quad.width(o1),
				quad.height(o1), quad.x(o2), quad.y(o2), quad.width(o2),
				quad.height(o2));
	}

	private Quads() {
	}
}
