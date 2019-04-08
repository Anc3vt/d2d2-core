package ru.ancevt.d2d2.display;

public interface IDisplayObject {

	long displayObjectId();

	String getName();

	void setName(String value);

	DisplayObjectContainer getParent();

	boolean hasParent();

	void setAlpha(float value);
	
	float getAlpha();
	
	void toAlpha(float value);
	
	void setXY(float x, float y);

	void setX(float value);

	float getX();

	void setY(float value);

	float getY();

	void setScale(float scaleX, float scaleY);

	void setScaleX(float value);

	float getScaleX();

	void setScaleY(float value);

	float getScaleY();

	boolean isOnScreen();

	void setVisible(boolean value);

	boolean isVisible();

	void setRotation(float degrees);

	float getRotation();

	void rotate(float toRotation);

	void moveX(float value);

	void moveY(float value);

	void move(float toX, float toY);

	void toScaleX(float value);

	void toScaleY(float value);

	void toScale(float toX, float toY);

	float getWidth();

	float getHeight();

	float getAbsoluteX();

	float getAbsoluteY();

	float getAbsoluteScaleX();

	float getAbsoluteScaleY();

	float getAbsoluteAlpha();

	float getAbsoluteRotation();

	Stage getStage();

	String toString();
	
	void onEachFrame();
	void onAdd(DisplayObjectContainer addedTo);
	void onRemove(DisplayObjectContainer removedFrom);
	void onAddToStage();
	void onRemoveFromStage();
	void removeFromParent();
}
