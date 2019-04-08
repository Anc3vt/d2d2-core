package ru.ancevt.d2d2.display;

import ru.ancevt.d2d2.common.NotImplementedException;

public class Stage extends DisplayObjectContainer {

	private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;

	public static final int DEFAULT_STAGE_WIDTH = 800;
	public static final int DEFAULT_STAGE_HEIGHT = 800;

	public static final int ALIGN_BOTTOM = 0x00;
	public static final int ALIGN_BOTTOM_LEFT = 0x01;
	public static final int ALIGN_BOTTOM_RIGHT = 0x02;
	public static final int ALIGN_LEFT = 0x03;
	public static final int ALIGN_RIGHT = 0x04;
	public static final int ALIGN_TOP = 0x05;
	public static final int ALIGN_TOP_LEFT = 0x06;
	public static final int ALIGN_TOP_RIGHT = 0x07;

	public static final int SCALE_MODE_REAL = 0x01;
	public static final int SCALE_MODE_FIT = 0x02;
	public static final int SCALE_MODE_OUTFIT = 0x03;
	public static final int SCALE_MODE_EXTENDED = 0x04;
	public static final int SCALE_MODE_AUTO = 0x05;

	private IRenderer renderer;

	private int align;
	private int scaleMode;
	private int width;
	private int height;
	private int stageWidth;
	private int stageHeight;
	private Color backgorundColor;

	private Root root;

	public Stage(IRenderer renderer) {
		this.renderer = renderer;
		this.renderer.setStage(this);
		setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
		setStageWidth(DEFAULT_STAGE_WIDTH);
		setStageHeight(DEFAULT_STAGE_HEIGHT);
		setScaleMode(SCALE_MODE_REAL);
		setAlign(ALIGN_TOP_LEFT);
	}

	public void onResize(int width, int height) {
		this.width = width;
		this.height = height;

		if (root == null) return;

		final float w = getWidth();
		final float h = getHeight();
		final float sW = getStageWidth();
		final float sH = getStageHeight();
		final float ratio = sW / sH;

		switch (scaleMode) {
			case SCALE_MODE_REAL:
				root.setScale(1f, 1f);
				break;
	
			case SCALE_MODE_EXTENDED:
				root.setScale(w / sW, h / sH);
				break;
	
			case SCALE_MODE_FIT:
				if (w / h < ratio) {
					root.setScaleX(w / sW);
					root.setScaleY(root.getScaleX() / ratio);
				} else {
					root.setScaleY(h / sH);
					root.setScaleX(root.getScaleY() * ratio);
				}
				break;
	
			case SCALE_MODE_OUTFIT:
				if (w / h > ratio) {
					root.setScaleX(w / sW);
					root.setScaleY(root.getScaleX() / ratio);
				} else {
					root.setScaleY(h / sH);
					root.setScaleX(root.getScaleY() * ratio);
				}
				break;
				
			case SCALE_MODE_AUTO:
				
				if (w > sW && h > sH) {
					root.setScale(1f, 1f);
				} else {
					if (w / h < ratio) {
						root.setScaleX(w / sW);
						root.setScaleY(root.getScaleX() / ratio);
					} else {
						root.setScaleY(h / sH);
						root.setScaleX(root.getScaleY() * ratio);
					}
				}
				break;
				
		}
		
			
	}

	public void setBackgroundColor(Color color) {
		backgorundColor = color;
	}

	public void setBackgroundColor(int rgb) {
		setBackgroundColor(new Color(rgb));
	}

	public Color getBackgroundColor() {
		return backgorundColor;
	}

	public void setFrameRate(int frameRate) {
		renderer.setFpsDelay(1000 / frameRate);
	}

	public int getFrameRate() {
		return 1000 / renderer.getFpsDelay();
	}

	public int getScaleMode() {
		return scaleMode;
	}

	public void setScaleMode(int scaleMode) {
		this.scaleMode = scaleMode;
		onResize(width, height);
	}

	public void setAlign(int align) {
		if (align != ALIGN_TOP_LEFT)
			throw new NotImplementedException(
					"the align " + StageInfoUtil.getAlignName(align) + " is not supported yet");

		this.align = align;
		onResize(width, height);
	}

	public int getAlign() {
		return align;
	}

	public void setStageWidth(int value) {
		this.stageWidth = value;
	}

	public float getStageWidth() {
		return stageWidth;
	}

	public void setStageHeight(int value) {
		this.stageHeight = value;
	}

	public float getStageHeight() {
		return stageHeight;
	}

	public void setStageSize(int width, int height) {
		this.stageWidth = width;
		this.stageHeight = height;
	}

	@Override
	public String toString() {
		return String.format("Stage[%dx%d]", (int) getWidth(), (int) getHeight());
	}

	@Override
	public void add(DisplayObject child) {
		throw new NotImplementedException("use setRoot instead");
	}

	@Override
	public void add(DisplayObject child, float x, float y) {
		throw new NotImplementedException("use setRoot instead");
	}

	@Override
	public void add(DisplayObject child, int indexAt) {
		throw new NotImplementedException("use setRoot instead");
	}

	@Override
	public void add(DisplayObject child, int indexAt, float x, float y) {
		throw new NotImplementedException("use setRoot instead");
	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getHeight() {
		return height;
	}

	public Root getRoot() {
		return root;
	}

	public void setRoot(Root root) {
		super.add(root);
		this.root = root;
		root.onAddToStage();
		onResize(width, height);
	}

}
