package ru.ancevt.d2d2.display.texture;

import ru.ancevt.d2d2.display.Color;

public class TextureCell {
	public Color color;
	
	public boolean pixel;
	
	public int id,
			   x, 
			   y, 
			   repeatX = 1, 
			   repeatY = 1;
	
	public float scaleX = 1.0f, 
				 scaleY = 1.0f, 
				 alpha = 1.0f, 
				 rotation = 0.0f;
	
	public Texture texture;
}
