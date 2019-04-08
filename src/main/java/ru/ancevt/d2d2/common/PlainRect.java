package ru.ancevt.d2d2.common;

import ru.ancevt.d2d2.display.Color;
import ru.ancevt.d2d2.display.Sprite;
import ru.ancevt.d2d2.display.texture.Texture;
import ru.ancevt.d2d2.display.texture.TextureAtlas;
import ru.ancevt.d2d2.display.texture.TextureManager;

public class PlainRect extends Sprite {
	
	private static final String FILE_PATH = "1x1.png";

	private static Texture texture;

	private static final Texture get1x1Texture() {
		if(texture != null) return texture;
		
		final TextureManager textureManager = TextureManager.getInstance();
		final TextureAtlas textureAtlas = textureManager.loadTextureAtlas(FILE_PATH);
		return texture = textureAtlas.createTexture();
	}
	
	public PlainRect() {
		super(get1x1Texture());
	}
	
	public PlainRect(float width, float height) {
		this();
		setSize(width, height);
	}
	
	public PlainRect(Color color) {
		this();
		setColor(color);
	}
	
	public PlainRect(float width, float height, Color color) {
		this();
		setSize(width, height);
		setColor(color);
	}
	
	public void setWidth(float width) {
		setScaleX(width);
	}
	
	public void setHeight(float height) {
		setScaleY(height);
	}
	
	public void setSize(float width, float height) {
		setWidth(width);
		setHeight(height);
	}
	
	@Override
	public float getWidth() {
		return getScaleX();
	}
	
	@Override
	public float getHeight() {
		return getScaleY();
	}

}
