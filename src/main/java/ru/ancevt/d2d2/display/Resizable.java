package ru.ancevt.d2d2.display;

public interface Resizable extends IDisplayObject {
	void setSize(float width, float height);
	void setWidth(float value);
	void setHeight(float value);
}
