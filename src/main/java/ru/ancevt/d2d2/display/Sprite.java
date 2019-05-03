package ru.ancevt.d2d2.display;

import ru.ancevt.d2d2.display.texture.Texture;
import ru.ancevt.d2d2.display.texture.TextureManager;

public class Sprite extends DisplayObject implements ISprite {

	public static final Color DEFAULT_COLOR = Color.WHITE;
	
	private int repeatX;
	private int repeatY;
	private Color color;
	private Texture texture;
	
	public Sprite() {
		setColor(DEFAULT_COLOR);
		setRepeat(1, 1);
	}
	
	public Sprite(final String textureKey) {
		this(TextureManager.getInstance().getTexture(textureKey));
	}
	
	public Sprite(Texture texture) {
		this.texture = texture;
		
		setColor(DEFAULT_COLOR);
		setRepeat(1, 1);
	}
	
	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void setColor(int rgb) {
		this.color = new Color(rgb);
	}

	@Override
	public Color getColor() {
		return color;
	}


	@Override
	public void setRepeat(int repeatX, int repeatY) {
		setRepeatX(repeatX);
		setRepeatY(repeatY);
	}

	@Override
	public void setRepeatX(int value) {
		this.repeatX = value;
	}

	@Override
	public void setRepeatY(int value) {
		this.repeatY = value;
	}

	@Override
	public int getRepeatX() {
		return repeatX;
	}

	@Override
	public int getRepeatY() {
		return repeatY;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
	
	public void clearTexture() {
		this.texture = null;
	}

	@Override
	public void setTexture(Texture value) {
		this.texture = value;
	}

	@Override
	public void setTexture(String textureKey) {
		setTexture(TextureManager.getInstance().getTexture(textureKey));
	}
	
	@Override
	public float getWidth() {
		return (texture == null) ? 0 : texture.getWidth();
	}
	
	@Override
	public float getHeight() {
		return (texture == null) ? 0 : texture.getHeight();
	}

	@Override
	public void dispose() {
		// TODO implement disposing
	}

}























