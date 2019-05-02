/*
 * Copyright © D2D2 2015
 * 
 * Этот файл — часть D2D2. D2D2 - свободная программа: вы можете
 * перераспространять ее и/или изменять ее на условиях Стандартной общественной
 * лицензии GNU в том виде, в каком она была опубликована Фондом свободного
 * программного обеспечения; либо версии 3 лицензии, либо (по вашему выбору)
 * любой более поздней версии.
 * 
 * D2D2 распространяется в надежде, что она будет полезной, но БЕЗО ВСЯКИХ
 * ГАРАНТИЙ; даже без неявной гарантии ТОВАРНОГО ВИДА или ПРИГОДНОСТИ ДЛЯ
 * ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Подробнее см. в Стандартной общественной лицензии GNU.
 */
package ru.ancevt.d2d2.touch;

import ru.ancevt.d2d2.display.DisplayObjectContainer;

/**
 * Экземпляр данного класса представляет собой кнопку прикосновения. Область
 * экрана, с которой пользователь может взаимодействовать путем нажатия на
 * экран. Является контейнером, соответственно предоставляет возможность быть
 * визуально отображаемым экранным объектом. Чтобы область заработала, ее нужно
 * включить методом setEnable
 * 
 * @author ancevt
 *
 */
public class TouchButton extends DisplayObjectContainer {

	private static final int DEFAULT_WIDTH = 1;
	private static final int DEFAULT_HEIGHT = 1;

	private TouchArea touchArea;
	private boolean enabled;
	private boolean dragging;

	/**
	 * Конструктор.
	 * 
	 * @param width  ширина кнопки
	 * @param height высота кнопки
	 */
	public TouchButton(final int width, final int height) {
		touchArea = new TouchArea(0, 0, width, height);
		setName("touchButton" + hashCode());
	}

	/**
	 * Конструктор.
	 * 
	 * @param width   ширина кнопки
	 * @param height  высота кнопки
	 * @param enabled указывает, включена ли кнопка. true - включена, false -
	 *                выключена
	 */
	public TouchButton(final int width, final int height, final boolean enabled) {
		this(width, height);
		setEnabled(enabled);
	}

	/**
	 * Конструктор
	 */
	public TouchButton() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	/**
	 * Конструктор.
	 * 
	 * @param enabled указывает, включена ли кнопка. true - включена, false -
	 *                выключена
	 */
	public TouchButton(final boolean enabled) {
		this();
		setEnabled(enabled);
	}

	@Override
	public float getWidth() {
		return touchArea.width;
	}

	@Override
	public float getHeight() {
		return touchArea.height;
	}

	/**
	 * Задает размеры кнопки
	 * 
	 * @param w ширина кнопки
	 * @param h высота кнопки
	 */
	public void setSize(final int w, final int h) {
		touchArea.setUp(0, 0, w, h);
	}

	public void setSize(final float w, final float h) {
		touchArea.setUp(0, 0, (int) w, (int) h);
	}

	public void setWidth(final float width) {
		touchArea.setUp(0, 0, (int) width, touchArea.height);
	}

	public void setHeight(final float height) {
		touchArea.setUp(0, 0, touchArea.width, (int) height);
	}

	public void setWidth(final int width) {
		touchArea.setUp(0, 0, width, touchArea.height);
	}

	public void setHeight(final int height) {
		touchArea.setUp(0, 0, touchArea.width, height);
	}

	@Override
	public void setX(float value) {
		touchArea.setUp((int) value, touchArea.y, touchArea.width, touchArea.height);
		super.setX(value);
	}

	@Override
	public void setY(float value) {
		touchArea.setUp(touchArea.x, (int) value, touchArea.width, touchArea.height);
		super.setY(value);
	}

	@Override
	public void setXY(float x, float y) {
		setX(x);
		setY(y);
		super.setXY(x, y);
	}

	/**
	 * Событие "пользователь прикоснулся к кнопке".
	 * 
	 * @param x X-координата прикосновения
	 * @param y Y-координата прикосновения
	 */
	public boolean onTouchDown(final int x, final int y) {
		return false;
	}

	/**
	 * Событие "пользователь убрал палец с экрана после нажатия кнопки"
	 * 
	 * @param x      X-координата прикосновения
	 * @param y      Y-координата прикосновения
	 * @param onArea в момент отжатия палец на кнопке - true, иначе - false
	 */
	public boolean onTouchUp(final int x, final int y, final boolean onArea) {
		return false;
	}

	/**
	 * Событие "пользователь ведет пальцем по кнопке"
	 * 
	 * @param x текущая X-координата пальца пользователя
	 * @param y текущая Y-координата пальца пользователя
	 */
	public boolean onTouchDrag(final int x, final int y) {
		return false;
	}

	/**
	 * Возвращает зону прикосновения кнопки
	 * 
	 * @return зона прикосновения кнопки в объектном представлении
	 * @deprecated
	 */
	@Deprecated
	public TouchArea getTouchArea() {
		return touchArea;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append("TouchButton[name: " + getName());
		sb.append(", pos: " + getX() + ", " + getY());
		sb.append(", size: " + getTouchArea().width + "x" + getTouchArea().height);
		sb.append(", enabled: " + isEnabled());
		sb.append("]");

		return sb.toString();
	}

	/**
	 * Проверяет, включена ли кнопка (ожидает ли пользовательского нажатия)
	 * 
	 * @return true - включена, false - выключена
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Включает или выключает кнопку прикосновения
	 * 
	 * @param enabled true - включает, false - выключает
	 */
	@SuppressWarnings("deprecation")
	public void setEnabled(boolean enabled) {
		if (this.enabled == enabled)
			return;

		this.enabled = enabled;

		final TouchProcessor touchProcessor = TouchProcessor.getInstance();

		if (enabled)
			touchProcessor.registerTouchableComponent(this);
		else
			touchProcessor.unregisterTouchableComponent(this);
	}

	public boolean isDragging() {
		return dragging;
	}

	void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

}
