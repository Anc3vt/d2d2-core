package ru.ancevt.d2d2.display;

import ru.ancevt.d2d2.exception.ConstantChangeException;

public class Color {
	
	public static final Color BLACK = new Color(0, true);
	public static final Color WHITE = new Color(0xFFFFFF, true);
	public static final Color GREEN = new Color(0x00FF00, true);
	public static final Color BLUE = new Color(0x0000FF, true);
	public static final Color DARK_BLUE = new Color(0x000080, true);
	public static final Color YELLOW = new Color(0xFFFF00, true);
	public static final Color RED = new Color(0xFF0000, true);
	public static final Color DARK_RED = new Color(0x800000, true);
	public static final Color PINK = new Color(0xFF8080, true);
	public static final Color ORANGE = new Color(0xFF8000, true);
	public static final Color MAGENTA = new Color(0xFF00FF, true);
	public static final Color DARK_GREEN = new Color(0x008000, true);
	public static final Color LIGHT_GREEN = new Color(0x80FF80, true);
	public static final Color GRAY = new Color(0x808080, true);
	public static final Color DARK_GRAY = new Color(0x3F3F3F, true);
	public static final Color LIGHT_GRAY = new Color(0xBBBBBB, true);
	
	private int r;
	private int g;
	private int b;
	
	private boolean constant;
	
	public Color(int r, int g, int b, boolean constant) {
		setRGB(r, g, b);
		this.constant = constant;
	}
	
	public Color(int r, int g, int b) {
		this(r, g, b, false);
	}
	
	public Color(int rgb, boolean constant) {
		setValue(rgb);
		this.constant = constant;
	}
	
	public Color(int rgb) {
		this(rgb, false);
	}

	public Color(String hex, boolean constant) {
		setValue(Integer.parseInt(hex, 16));
		this.constant = constant;
	}
	
	public Color(String hex) {
		this(hex, false);
	}
	
	public void setRGB(int r, int g, int b) {
		setR(r);
		setG(g);
		setB(b);
	}
	
	public void setValue(int rgb) {
		setR((rgb >> 16) & 0xFF);
		setG((rgb >> 8)  & 0xFF);
		setB(rgb & 255);
	}
	
	public int getValue() {
		return r << 16 | g << 8 | b;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		constantCheck();
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		constantCheck();
		this.g = g;
	}
	
	public int getB() {
		return b;
	}

	public void setB(int b) {
		constantCheck();
		this.b = b;
	}

	public boolean isConstant() {
		return constant;
	}
	
	private final void constantCheck() {
		if(constant) 
			throw new ConstantChangeException("The color object has constant value");
	}
	
	public static final Color createRandomColor() {
		return new Color((int)(Math.random() * 0xFFFFFF));
	}
	
	public String toHexString() {
		return Integer.toHexString(getValue());
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Color[0x");
		
		final String hex = Integer.toHexString(getValue());
		
		if(hex.length() < 6) sb.append('0');
		
		sb.append(hex);
		
		if(constant) 
			sb.append(", const");
		
		sb.append(']');
		
		return sb.toString();
	}
	
}
