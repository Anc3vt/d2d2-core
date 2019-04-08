package ru.ancevt.d2d2.test.stuff;

import ru.ancevt.d2d2.display.DisplayObjectContainer;
import ru.ancevt.d2d2.display.Sprite;

public class BrickWall extends DisplayObjectContainer {
	
	private static final String BRICK0 = "brick0";
	private static final String BRICK1 = "brick1";
	
	public BrickWall(int columns, int rows) {

		final int COLUMNS = columns;
		final int ROWS = rows;
		
		for(int y = 0; y < ROWS; y ++) {
			for(int x = 0; x < COLUMNS; x ++) {

				final String textureKey = 
					Math.random() < 0.5 ? BRICK0 : BRICK1;
				
				final Sprite s = new Sprite(textureKey);
				
				if(y < ROWS / 3) 
					s.setColor(0xFFFFFF);
				
				else if(y >= ROWS / 3 && y < ROWS - ROWS / 3) 
					s.setColor(0x0000FF);
				
				else 
					s.setColor(0xFF0000);
				
				
				
				s.setXY(x * s.getWidth(), y * s.getHeight());
				
				add(s);
			}
		}
	}
}
