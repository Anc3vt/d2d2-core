package ru.ancevt.d2d2.display.text;

import ru.ancevt.d2d2.display.Color;
import ru.ancevt.d2d2.display.DisplayObject;
import ru.ancevt.d2d2.display.IColored;
import ru.ancevt.d2d2.display.IDisplayObjectText;

public class BitmapText extends DisplayObject implements IDisplayObjectText, IColored {
	
	private static final String DEFAULT_TEXT = "";
	
	private static final float DEFAULT_BOUND_WIDTH = 512f;
	private static final float DEFAULT_BOUND_HEIGHT = 512f;
	
	private static final Color DEFAULT_COLOR = Color.WHITE; 
	
	private String text;
	private Color color;
	
	private BitmapFont bitmapFont;
	
	private float lineSpacing;
	private float spacing;
	
	private float boundWidth;
	private float boundHeight;
	
	public BitmapText(final BitmapFont bitmapFont, float boundWidth, float boundHeight) {
		setBitmapFont(bitmapFont);
		setColor(DEFAULT_COLOR);
		setBoundWidth(boundWidth);
		setBoundHeight(boundHeight);
		setText(DEFAULT_TEXT);
	}
	
	public BitmapText(final BitmapFont bitmapFont) {
		this(bitmapFont, DEFAULT_BOUND_WIDTH, DEFAULT_BOUND_HEIGHT);
	}
	
	public BitmapText(float boundWidth, float boundHeight) {
		this(BitmapFont.getDefaultBitmapFont(), boundWidth, boundHeight);
	}
	
	public BitmapText() {
		this(BitmapFont.getDefaultBitmapFont(), DEFAULT_BOUND_WIDTH, DEFAULT_BOUND_HEIGHT);
	}
	
	public int getTextWidth() {
		if(getText() == null) return 0;
		
		char chars[] = getText().toCharArray();
		int result = 0;
		
		final BitmapFont font = getBitmapFont();
		
		int max = 0;
		
		for(int i = 0; i < chars.length; i ++) {
			final char c = chars[i];
			
			if(c == '\n' || (getBoundWidth() > 0 && result > getBoundWidth())) result = 0;
			
			BitmapCharInfo info = font.getCharInfo(c);
			if(info == null) continue;
			
			result += info.getWidth() + getSpacing();
			
			if(result > max) max = result;
		}
		
		return (int)(max - getSpacing());
	}
	
	public int getTextHeight() {
		if(getText() == null) return 0;
		
		char chars[] = getText().toCharArray();
		int result = 0;
		
		final BitmapFont font = getBitmapFont();
		
		for(int i = 0; i < chars.length; i ++) {
			final char c = chars[i];
			if(c == '\n' || (getBoundWidth() > 0 && result > getBoundWidth())) {
				result += font.getCharHeight() + getLineSpacing();
			}
		}
		
		return (int)result + font.getCharHeight();
	}
	
	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void setColor(int rgb) {
		setColor(new Color(rgb));
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public boolean isEmpty() {
		return text == null || text.length() == 0;
	}

	public BitmapFont getBitmapFont() {
		return bitmapFont;
	}

	public void setBitmapFont(BitmapFont bitmapFont) {
		this.bitmapFont = bitmapFont;
	}

	@Override
	public void setLineSpacing(float value) {
		this.lineSpacing = value;
	}

	@Override
	public float getLineSpacing() {
		return lineSpacing;
	}

	@Override
	public void setSpacing(float value) {
		this.spacing = value;
	}

	@Override
	public float getSpacing() {
		return spacing;
	}

	@Override
	public float getBoundWidth() {
		return boundWidth;
	}

	@Override
	public float getBoundHeight() {
		return boundHeight;
	}

	@Override
	public void setBoundWidth(float value) {
		boundWidth = value;
	}

	@Override
	public void setBoundHeight(float value) {
		boundHeight = value;
	}
	
	@Override
	public float getWidth() {
		return getBoundWidth();
	}
	
	@Override
	public float getHeight() {
		return getBoundHeight();	
	}
	
	@Override
	public void setBounds(float boundWidth, float boundHeight) {
		setBoundWidth(boundWidth);
		setBoundHeight(boundHeight);
	}
	
	@Override
	public String toString() {
		return "BitmapText[bounds: " + getBoundWidth() + "x" + getBoundHeight() + "]";
	}
	
}

















