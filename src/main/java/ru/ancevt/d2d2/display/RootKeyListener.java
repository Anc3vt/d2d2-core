package ru.ancevt.d2d2.display;

public interface RootKeyListener {
	void keyDown(int keyCode, char keyChar, boolean shift, boolean control, boolean alt);
	void keyUp(int keyCode, char keyChar, boolean shift, boolean control, boolean alt);
}
