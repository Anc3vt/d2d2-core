package ru.ancevt.d2d2.display.text;

public class BitmapCharInfo {

	private char character;
	private short x;
	private short y;
	private short width;
	private short height;
	
	public BitmapCharInfo(final char character, final short x, final short y, final short width, final short height) {
		this.character = character;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public char getCharacter() {
		return character;
	}
	
	public short getX() {
		return x;
	}
	
	public short getY() {
		return y;
	}
	
	public short getWidth() {
		return width;
	}
	
	public short getHeight() {
		return height;
	}

}
