package ru.ancevt.d2d2.display;

import java.util.LinkedList;
import java.util.List;

import ru.ancevt.d2d2.touch.TouchButton;

public class Root extends DisplayObjectContainer {

	private List<RootKeyListener> keyListeners;
	
	public Root() {
		keyListeners = new LinkedList<RootKeyListener>();
	}
	
	public void onBackPressed() {
		
	}
	
	public void addRootKeyListener(RootKeyListener listener) {
		keyListeners.add(listener);
	}
	
	public void removeRootKeyListener(RootKeyListener listener) {
		keyListeners.remove(listener);
	}

	public void dispatchKeyDown(int keyCode, char keyChar, boolean shift, boolean control, boolean alt) {
		for(final RootKeyListener l : keyListeners)
			l.keyDown(keyCode, keyChar, shift, control, alt);
	}

	public void dispatchKeyUp(int keyCode, char keyChar, boolean shift, boolean control, boolean alt) {
		for(final RootKeyListener l : keyListeners)
			l.keyUp(keyCode, keyChar, shift, control, alt);
	}
	
	public void onMouseMove(final int x, final int y, final boolean drag) {
		
	}
	
	public void onMouseWheel(int delta) {
		
	}
	
	/**
	 * Событие срабатываемое при прикосновении пользователя к экрану. Данный метод
	 * следует переопределять в том случае если нужно обрабатывать прикосновения ко
	 * всему экрану, в противном случае следует использовать кнопки прикосновений
	 * {@link TouchButton}
	 * 
	 * @param x       координата X прикосновения
	 * @param y       координата Y прикосновения
	 * @param pointer идентификатор пальца
	 */
	public void onScreenTouchDown(final int x, final int y, int pointer) {
	}

	/**
	 * Событие срабатываемое при убирания пальца пользователя с экрана. Данный метод
	 * следует переопределять в том случае если нужно обрабатывать прикосновения ко
	 * всему экрану, в противном случае следует использовать кнопки прикосновений
	 * {@link TouchButton}
	 * 
	 * @param x       координата X прикосновения
	 * @param y       координата Y прикосновения
	 * @param pointer идентификатор пальца
	 */
	public void onScreenTouchUp(final int x, final int y, int pointer) {
	}

	/**
	 * Событие срабатываемое при неотрывном ведении пальцем по экрану.
	 * 
	 * @param x       координата X прикосновения
	 * @param y       координата Y прикосновения
	 * @param pointer идентификатор пальца
	 */
	public void onScreenDrag(final int x, final int y, int pointer) {
	}

	/**
	 * Событие срабатываемое при нажатии на клавишу (кнопку мобильного устройства)
	 * 
	 * @param keyCode код нажатой клавиши
	 * @param shift   is shift key down
	 * @param control is control key down
	 * @param alt     is alt key down
	 */
	public void onKeyDown(final int keyCode, final int keyChar, final boolean shift, final boolean control, final boolean alt) {
	}

	/**
	 * Событие срабатываемое при отжатии клавиши (кнопки мобильного устройства)
	 * 
	 * @param keyCode код нажатой клавиши
	 * @param shift   is shift key down
	 * @param control is control key down
	 * @param alt     is alt key down
	 */
	public void onKeyUp(final int keyCode, final int keyChar, final boolean shift, final boolean control, final boolean alt) {
	}

}
