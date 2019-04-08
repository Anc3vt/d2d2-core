package ru.ancevt.d2d2.display;

public interface IDisplayObjectText extends IColored {
	
	void setText(final String text);
	
	String getText();
	
	boolean isEmpty();
	
	void setLineSpacing(float value);
	
	float getLineSpacing();
	
	void setSpacing(float value);
	
	float getSpacing();
	
	float getBoundWidth();
	
	float getBoundHeight();
	
	void setBoundWidth(float value);
	void setBoundHeight(float value);
	
	void setBounds(float boundWidth, float boundHeight);
}
