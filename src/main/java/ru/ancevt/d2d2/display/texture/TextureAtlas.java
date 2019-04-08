package ru.ancevt.d2d2.display.texture;

import ru.ancevt.d2d2.common.IDisposable;

public class TextureAtlas implements IDisposable {
	
	private static int idCounter;
	
	private int id;
	private Object nativeTextureData;
	private Object nativeImageData;
	
	private int width;
	private int height;
	
	TextureAtlas() {
		this.id = ++ idCounter;
	}
	
	public TextureAtlas(Object data, int width, int height) {
		this();
		setUp(data, width, height);
	}
	
	final void setUp(Object data, int width, int height) {
		this.nativeTextureData = data;
		this.width = width;
		this.height = height;
	}
	
	public Texture createTexture() {
		return createTexture(0, 0, getWidth(), getHeight());
	}
	
	public Texture createTexture(int x, int y, int width, int height) {
		return new Texture(this, x, y, width, height);
	}

	public int getId() {
		return id;
	}
	
	public final int getWidth() {
		return width;
	}
	
	public final int getHeight() {
		return height;
	}
	
	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder("TextureAtlas[id: ");
		
		s.append(getId());
		s.append(", ");
		s.append(getWidth() + "x" + getHeight());
		s.append(']');
		
		return s.toString();
	}
	
	public final Object getNativeTextureData() {
		return nativeTextureData;
	}

	@Override
	public void dispose() {
		// TODO: implement disposing 
	}

	public Object getNativeImageData() {
		return nativeImageData;
	}

	public void setNativeImageData(Object nativeImageData) {
		this.nativeImageData = nativeImageData;
	}
	
}
