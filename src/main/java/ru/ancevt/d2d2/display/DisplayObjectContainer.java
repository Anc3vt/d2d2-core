package ru.ancevt.d2d2.display;

import java.util.ArrayList;
import java.util.List;

public class DisplayObjectContainer extends DisplayObject implements IDisplayObjectContainer {

	static final int MAX_X = 1048576;
	static final int MAX_Y = 1048576;
	
	final List<DisplayObject> children;
	
	public DisplayObjectContainer() {
		children = new ArrayList<DisplayObject>();
	}
	
	@Override
	public void add(DisplayObject child) {
		child.indexToAdd = -1;
		child.setParent(this);
		child.onAdd(this);

		children.remove(child);
		children.add(child);
		
		if(this.getStage() != null) child.onAddToStage();
	}

	@Override
	public void add(DisplayObject child, int indexAt) {
		child.indexToAdd = indexAt;
		child.setParent(this);
		child.onAdd(this);

		children.remove(child);
		children.add(indexAt, child);
		
		if(this.getStage() != null) child.onAddToStage();
	}

	@Override
	public void add(DisplayObject child, float x, float y) {
		child.setXY(x, y);
		child.indexToAdd = -1;
		child.setParent(this);
		child.onAdd(this);

		children.remove(child);
		children.add(child);
		
		if(this.getStage() != null) child.onAddToStage();
	}

	@Override
	public void add(DisplayObject child, int indexAt, float x, float y) {
		child.setXY(x, y);
		child.indexToAdd = indexAt;
		child.setParent(this);
		child.onAdd(this);

		children.remove(child);
		children.add(indexAt, child);
		
		if(this.getStage() != null) child.onAddToStage();
	}

	@Override
	public void remove(DisplayObject child) {
		final boolean removedFromStage = child.getStage() != null;
		
		child.setParent(null);
		child.onRemove(this);
		
		children.remove(child);
		
		if(removedFromStage) child.onRemoveFromStage();
	}

	@Override
	public int indexOf(DisplayObject child) {
		return children.indexOf(child);
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public DisplayObject getChild(int index) {
		return children.get(index);
	}
	
	public void removeAllChildren() {
		children.clear();
	}

	@Override
	public boolean contains(DisplayObject child) {
		return children.contains(child);
	}
	
	final void actualRemove(DisplayObject child) {
		children.remove(child);
	}
	
	final void actualRemove(int index) {
		children.remove(index);
	}
	
	final void actualAdd(DisplayObject child) {
		children.add(child);
	}
	
	final void actualAdd(DisplayObject child, int indexAt) {
		children.add(indexAt, child);
	}
	
	@Override
	public float getWidth() {
		float min = MAX_X, max = 0;

		for (final DisplayObject child : children) {
			float x = child.getX();
			float xw = x + child.getWidth();

			min = x < min ? x : min;
			max = xw > max ? xw : max;
		}

		return max - min;
	}

	@Override
	public float getHeight() {
		float min = MAX_Y, max = 0;

		for (final DisplayObject child : children) {

			final float y = child.getY();
			final float yh = y + child.getHeight();

			min = y < min ? y : min;
			max = yh > max ? yh : max;
		}

		return max - min;
	}
}


































