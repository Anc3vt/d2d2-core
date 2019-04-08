package ru.ancevt.d2d2.test;

import ru.ancevt.d2d2.display.Color;

public class Test_Color {

	public static void main(String[] args) {
		final Color color = new Color(0xFFF00, false);
		
		color.setR(0xF8);
		
		System.out.println(color);

	}

}
