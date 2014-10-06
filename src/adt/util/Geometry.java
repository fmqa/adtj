package adt.util;

public final class Geometry {
	private Geometry() {
	}

	public static boolean contains(int x0, int y0, int w0, int h0, int x1,
			int y1, int w1, int h1) {
		return x1 >= x0 && y1 >= y0 && x1 + w1 <= x0 + w0 && y1 + h1 <= y0 + h0;
	}
}
