package ru.ancevt.d2d2.common;

import ru.ancevt.d2d2.display.IRenderer;

public class D2D2 {
	
	private static IRenderer renderer;
	
	public static final void setRenderer(IRenderer renderer) {
		D2D2.renderer = renderer;
	}
	
	public static final IRenderer getRenderer() {
		return renderer;
	}
}
