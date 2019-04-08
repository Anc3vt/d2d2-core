package ru.ancevt.d2d2.display;

public interface TextField extends IDisplayObject, IColored {
	void setText(String text);
	
	String getText();

	void append(String text);
	
	void append(char c);
}
