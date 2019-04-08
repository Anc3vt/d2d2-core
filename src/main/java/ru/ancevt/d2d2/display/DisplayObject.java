package ru.ancevt.d2d2.display;

abstract public class DisplayObject implements IDisplayObject {

	private static final String NAME_PREFIX = "_displayObject";
	
	private static long displayObjectIdCounter;
	
	private long displayObjectId;
	private String name;
	private DisplayObjectContainer parent;

	private float alpha; 
	
	private float x;
	private float y;
	
	private float scaleX;
	private float scaleY;
	
	private float rotation;
	
	private boolean visible;
	
	int indexToAdd = -1;
	
	public DisplayObject() {
		displayObjectId = displayObjectIdCounter++;
		name = NAME_PREFIX + displayObjectId;
		visible = true;
		
		scaleX =
		scaleY = 1.0f;
		
		alpha = 1.0f;
	}
	
	@Override
	public long displayObjectId() {
		return displayObjectId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String value) {
		this.name = value;
	}

	final void setParent(final DisplayObjectContainer container) {
		this.parent = container;
	}
	
	@Override
	public DisplayObjectContainer getParent() {
		return parent;
	}

	@Override
	public boolean hasParent() {
		return parent != null;
	}

	@Override
	public void setXY(float x, float y) {
		setX(x);
		setY(y);
	}

	@Override
	public void setX(float value) {
		this.x = value;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public void setY(float value) {
		this.y = value;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public void setScale(float scaleX, float scaleY) {
		setScaleX(scaleX);
		setScaleY(scaleY);
	}

	@Override
	public void setScaleX(float value) {
		this.scaleX = value;
	}

	@Override
	public float getScaleX() {
		return scaleX;
	}

	@Override
	public void setScaleY(float value) {
		this.scaleY = value;
	}

	@Override
	public float getScaleY() {
		return scaleY;
	}

	@Override
	public boolean isOnScreen() {
		IDisplayObjectContainer parent = getParent();
		
		while(parent != null) {
			parent = parent.getParent();
			if(parent instanceof Stage) return true;
		}
		
		return false;
	}

	@Override
	public void setVisible(boolean value) {
		this.visible = value;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setRotation(float degrees) {
		rotation = degrees;
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	@Override
	public void rotate(float toRotation) {
		rotation += toRotation;
	}

	@Override
	public void moveX(float value) {
		setX(getX() + value);
	}

	@Override
	public void moveY(float value) {
		setY(getY() + value);
	}

	@Override
	public void move(float toX, float toY) {
		moveX(toX);
		moveY(toY);
	}

	@Override
	public void toScaleX(float value) {
		setScaleX(getScaleX() * value);
	}

	@Override
	public void toScaleY(float value) {
		setScaleY(getScaleY() * value);
	}

	@Override
	public void toScale(float toX, float toY) {
		toScaleX(toX);
		toScaleY(toY);
	}

	@Override
	public float getWidth() {
		return 0;
	}

	@Override
	public float getHeight() {
		return 0;
	}

	@Override
	public float getAbsoluteX() {
		return DisplayObjectAbsoluteComputer.getAbsoluteX(this);
	}

	@Override
	public float getAbsoluteY() {
		return DisplayObjectAbsoluteComputer.getAbsoluteY(this);
	}

	@Override
	public float getAbsoluteScaleX() {
		return DisplayObjectAbsoluteComputer.getAbsoluteScaleX(this);
	}

	@Override
	public float getAbsoluteScaleY() {
		return DisplayObjectAbsoluteComputer.getAbsoluteScaleY(this);
	}

	@Override
	public float getAbsoluteAlpha() {
		return DisplayObjectAbsoluteComputer.getAbsoluteAlpha(this);
	}

	@Override
	public float getAbsoluteRotation() {
		return DisplayObjectAbsoluteComputer.getAbsoluteRotation(this);
	}

	@Override
	public Stage getStage() {
		
		//TODO: optimize, needs to cache to private field (cache)
		
		IDisplayObjectContainer parent = getParent();
		
		if(parent instanceof Stage) return (Stage)parent;
		
		while(parent != null) {
			parent = parent.getParent();
			if(parent instanceof Stage) return (Stage)parent;
		}
		
		return null;
	}
	
	@Override
	public final void removeFromParent() {
		if(getParent() != null) {
			getParent().remove(this);
		}
	}

	@Override
	public void setAlpha(float value) {
		this.alpha = value;
	}

	@Override
	public float getAlpha() {
		return alpha;
	}

	@Override
	public void toAlpha(float value) {
		alpha *= value;
	}
	
	public Root getRoot() {
		DisplayObjectContainer parent = getParent();
		while(parent != null) {
			if(parent instanceof Root) return (Root)parent;
			parent = parent.getParent();
		}
		return null;
	}
	
	// callbacks -----------------------------------
	
	@Override
	public void onEachFrame() {};
	
	@Override
	public void onAdd(DisplayObjectContainer addedTo) {}

	@Override
	public void onRemove(DisplayObjectContainer removedFrom) {}
	
	@Override
	public void onAddToStage() {}
	
	@Override
	public void onRemoveFromStage() {}
}

















