package ru.ancevt.d2d2.common;

import ru.ancevt.d2d2.display.Color;
import ru.ancevt.d2d2.display.DisplayObjectContainer;

public class BorderedRect extends DisplayObjectContainer {
	private static final Color DEFAULT_FILL_COLOR = Color.WHITE;
	private static final Color DEFAULT_BORDER_COLOR = Color.BLACK;

	private static final float DEFAULT_WIDTH = 16;
	private static final float DEFAULT_HEIGHT = 16;

	private final PlainRect borderLeft, borderRight, borderTop, borderBottom;
	private final PlainRect fillRect;
	
	private float borderWidth = 1;
	
	public BorderedRect() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FILL_COLOR, DEFAULT_BORDER_COLOR);
	}

	public BorderedRect(float width, float height) {
		this(width, height, DEFAULT_FILL_COLOR, DEFAULT_BORDER_COLOR);
	}
	
	public BorderedRect(Color fillColor) {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, fillColor, DEFAULT_BORDER_COLOR);
	}

	public BorderedRect(Color fillColor, Color borderColor) {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, fillColor, borderColor);
	}
	
	public BorderedRect(float width, float height, Color fillColor) {
		this(width, height, fillColor, DEFAULT_BORDER_COLOR);
	}

	public BorderedRect(float width, float height, Color fillColor, Color borderColor) {
		borderLeft = new PlainRect();
		borderRight = new PlainRect();
		borderTop = new PlainRect();
		borderBottom = new PlainRect();
		fillRect = new PlainRect();

		borderLeft.setSize(1, 1);
		borderRight.setSize(1, 1);
		borderTop.setSize(1, 1);
		borderBottom.setSize(1, 1);
		fillRect.setSize(1, 1);
		
		add(fillRect);
		add(borderLeft);
		add(borderRight);
		add(borderTop);
		add(borderBottom);
		

		setBorderColor(borderColor);
		setFillColor(fillColor);
		
		setSize(width, height);
	}
	
	public void setWidth(float width) {
		fillRect.setWidth(width);
		borderTop.setWidth(width);
		borderBottom.setWidth(width);
		borderRight.setX(width - borderWidth);
	}
	
	public void setHeight(float height) {
		fillRect.setHeight(height);
		borderLeft.setHeight(height);
		borderRight.setHeight(height);
		borderBottom.setY(height - borderWidth);
	}
	
	public void setSize(float width, float height) {
		setWidth(width);
		setHeight(height);
		
		
	}
	
	@Override
	public float getWidth() {
		return fillRect.getWidth();
	}
	
	@Override
	public float getHeight() {
		return fillRect.getHeight();
	}
	
	public void setFillColor(Color color) {
		if(color == null) {
			if(fillRect.getParent() != null)
				fillRect.removeFromParent();
		} else {
			if(fillRect.getParent() == null)
				add(fillRect, 0);
		}
		
		fillRect.setColor(color);
	}
	
	public Color getFillColor() {
		return fillRect.getColor();
	}
	
	public void setBorderColor(Color color) {
		if(color == null) {
			if(borderLeft.getParent() != null) {
				borderLeft.removeFromParent();
				borderRight.removeFromParent();
				borderTop.removeFromParent();
				borderBottom.removeFromParent();
			}
		} else {
			if(borderLeft.getParent() == null) {
				add(borderLeft);
				add(borderRight);
				add(borderBottom);
				add(borderTop);
			}
		}
		
		borderLeft.setColor(color);
		borderRight.setColor(color);
		borderTop.setColor(color);
		borderBottom.setColor(color);
	}
	
	public Color getBorderColor() {
		return borderLeft.getColor();
	}
	
	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
		borderLeft.setWidth(borderWidth);
		borderRight.setWidth(borderWidth);
		borderTop.setHeight(borderWidth);
		borderBottom.setHeight(borderWidth);
		setSize(getWidth(), getHeight());
	}
	
	public float getBorderWidth() {
		return borderWidth;
	}
}














