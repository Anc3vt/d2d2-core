package ru.ancevt.d2d2.display;

public interface IDisplayObjectContainer extends IDisplayObject {

	void add(DisplayObject child);

	void add(DisplayObject child, int index);
	
	void add(DisplayObject child, float x, float y);
	
	void add(DisplayObject child, int index, float x, float y);
	
	void remove(DisplayObject child);
	
	int indexOf(DisplayObject child);

	int getChildCount();

	DisplayObject getChild(int index);
	
	boolean contains(DisplayObject child);
}
