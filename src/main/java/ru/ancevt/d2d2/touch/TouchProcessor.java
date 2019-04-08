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

import java.util.ArrayList;
import java.util.List;

/**
 * Процессор прикосновений к экрану.
 * @see TouchButton
 * @author ancevt
 *
 */
public class TouchProcessor {
	
	private static TouchProcessor instance;
	
	/**
	 * Извлекает процессор прикосновений к экрану.
	 * @return процессор прикосновений к экрану
	 */
	public static final TouchProcessor getInstance() {
		if(instance == null) instance = new TouchProcessor();
		return instance;
	}
	
	private List<TouchButton> touchableComponents;
	
	private TouchProcessor() {
		touchableComponents = new ArrayList<TouchButton>();
	}
	
	/**
	 * Зарегистрировать кнопку прикосновений в процессоре прикосновений к экрану.
	 * @param touchButton кнопка прикосновений
	 * @deprecated
	 */
	@Deprecated
	public void registerTouchableComponent(final TouchButton touchButton) {
		if(!touchableComponents.contains(touchButton))
			touchableComponents.add(touchButton);
	}
	
	/**
	 * Исключить кнопку прикосновений из процессора прикосновений к экрану.
	 * @param touchButton
	 * @deprecated
	 */
	@Deprecated
	public final void unregisterTouchableComponent(final TouchButton touchButton) {
		if(touchableComponents.contains(touchButton))
			touchableComponents.remove(touchButton);
	}
	
	/**
	 * Делает все зарегистрированные кнопки прикосновений неактивными (выключает).
	 */
	public final void clear() {
		while(touchableComponents.size() > 0)
			touchableComponents.remove(0);
	}

	/**
	 * Инициирует прикосновения или прекращение прикосновения 
	 * к экрану в заданных координатах заданным пальцем.
	 * @param x X-координата прикосновения
	 * @param y Y-координата прикосновения
	 * @param pointer идентификатор пальца пользователя
	 * @param down true - нажатие, false - пользователь убрал палец после нажатия
	 */
	@SuppressWarnings("deprecation")
	public final void screenTouch(final int x, final int y, final int pointer, final boolean down) {

		final Touch t = Touch.touch(pointer);
		if(t == null) return;
		
		t.setUp(x, y, down);
		
		if(!down && t.getTouchButton() != null) {
			
			final TouchButton c = t.getTouchButton();
			
			if(c.isOnScreen()) {
				
				final float tcX = c.getAbsoluteX();
				final float tcY = c.getAbsoluteY();
				final float tcW = c.getTouchArea().width * c.getAbsoluteScaleX();;
				final float tcH = c.getTouchArea().height * c.getAbsoluteScaleY();
				
				final boolean onArea = x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH;
				
				t.getTouchButton().onTouchUp(x, y, onArea);
				t.getTouchButton().setDragging(false);
				t.setTouchButton(null);
				return;
			}
		}

		for(int i = 0; i < touchableComponents.size(); i ++) {
			final TouchButton c = touchableComponents.get(i);
			
			final float tcX = c.getAbsoluteX();
			final float tcY = c.getAbsoluteY();
			final float tcW = c.getTouchArea().width * c.getAbsoluteScaleX();
			final float tcH = c.getTouchArea().height * c.getAbsoluteScaleY();
		
			if(c.isOnScreen() && x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH) {
				if(down) {
					t.setTouchButton(c);
					t.getTouchButton().setDragging(true);
					c.onTouchDown((int)(x - tcX),(int)(y - tcY));
				}
			}
		}
	}
	
	/**
	 * Инициирует проведение пальца пользователя по экрану.
	 * @param x текущия X-координата пальца пользователя
	 * @param y текущия Y-координата пальца пользователя
	 * @param pointer идентификатор пальца пользователя
	 */
	public final void screenDrag(final int x, final int y, final int pointer) {
		
		for(int i = 0; i < touchableComponents.size(); i ++) {
			final TouchButton c = touchableComponents.get(i);

			final float tcX = c.getAbsoluteX();
			final float tcY = c.getAbsoluteY();
//			final float tcW = c.getTouchArea().width * c.getAbsoluteScaleX();;
//			final float tcH = c.getTouchArea().height * c.getAbsoluteScaleY();
			
			if(c.isOnScreen() && c.isDragging()
//				&& x >= tcX 
//				&& x <= tcX + tcW 
//				&& y >= tcY 
//				&& y <= tcY + tcH
				) {
				
				c.onTouchDrag((int)(x - tcX),(int)(y - tcY));
			} 
		}
	}

	/**
	 * Возвращает список зарегистрированных кнопок 
	 * прикосновения в процессоре прикосновений к экрану.
	 * Отладочный метод.
	 * 
	 * @return список зарегистрированных кнопок прикосновения 
	 * в процессоре прикосновений к экрану в виде строки 
	 */
	public final String getActiveList() {
		final StringBuilder sb = new StringBuilder();
		
		for(TouchButton current : touchableComponents) {
			final String s = current.toString();
			sb.append(s + "\n");
		}
		
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return String.format("TouchProcessor[%d buttons]", touchableComponents.size());
	}
}

class Touch {
	
	private static final int MAX_TOUCHES = 4;
	
	private static Touch[] touches = new Touch[MAX_TOUCHES];
	
	public static final Touch touch(final int pointer) {
		for(int i = 0; i < touches.length; i ++) {
			if(pointer >= MAX_TOUCHES) return null;
			final Touch currentTouch = touches[i];
			if(currentTouch != null && currentTouch.getPointer() == pointer) return currentTouch;
		}
		
		final Touch touch = new Touch(pointer);
		touches[pointer] = touch;
		
		return touch;
	}
	
	private int pointer;
	private int x, y;
	private boolean down;
	private TouchButton component;
	
	private Touch(final int pointer) {
		this.pointer = pointer;
	}

	public final int getPointer() {
		return pointer;
	}
	
	public final void setUp(final int x, final int y, final boolean down) {
		setLocation(x, y);
		setDown(true);
	}

	public final void setLocation(final int x, final int y) {
		setX(x);
		setY(y);
	}
	
	public final int getY() {
		return y;
	}

	public final void setY(int y) {
		this.y = y;
	}

	public final int getX() {
		return x;
	}

	public final void setX(int x) {
		this.x = x;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public final TouchButton getTouchButton() {
		return component;
	}

	public final void setTouchButton(final TouchButton component) {
		this.component = component;
	}
}




















