package ru.ancevt.d2d2.display.texture;

public class Texture {
	
	private final TextureAtlas textureAtlas;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private String key;
	
	Texture(TextureAtlas textureAtlas, int x, int y, int width, int height) {
		this.textureAtlas = textureAtlas;
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	Texture(TextureAtlas textureAtlas, int x, int y, int width, int height, String key) {
		this(textureAtlas, x, y, width, height);
		this.key = key;
	}
	
	public final Texture getSubtexture(int x, int y, int width, int height) {
		return getTextureAtlas().createTexture(x, y, width, height);
	}
	
	public final TextureAtlas getTextureAtlas() {
		return textureAtlas;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	void setKey(final String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder("Texture[key: \"");
		s.append(key);
		s.append("\", (");
		s.append(x + ", " + y + ", " + width + ", " + height + ")");
		s.append(']');
		
		return s.toString();
	}

}





















