package ru.ancevt.d2d2.display;

public interface IColored extends IDisplayObject {
	void setColor(Color color);
	void setColor(int rgb);

	Color getColor();
}
