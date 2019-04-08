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
package ru.ancevt.d2d2.display.texture;

import java.util.ArrayList;
import java.util.List;

import ru.ancevt.d2d2.display.Color;
import ru.ancevt.d2d2.display.text.BitmapText;

public class TextureCombiner {
	private List<TextureCell> cells;
	private int width, height;
	private int cellIdCounter;
	
	public TextureCombiner(final int width, final int height) {
		this.width = width;
		this.height = height;
	
		cells = new ArrayList<TextureCell>();
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public final int append(
			final Texture texture,
			final int x,
			final int y,
			final float scaleX,
			final float scaleY,
			final float alpha,
			final float rotation,
			final int repeatX,
			final int repeatY) {
		
		final TextureCell cell = new TextureCell();
		cell.x = x;
		cell.y = y;
		cell.texture = texture;
		cell.scaleX = scaleX;
		cell.scaleY = scaleY;
		cell.alpha = alpha;
		cell.rotation = rotation;
		cell.repeatX = repeatX;
		cell.repeatY = repeatY;
		cell.id = cellIdCounter++;
		cells.add(cell);
		
		return cell.id;
	}
	
	public final int append(int x, int y, Color color, float alpha) {
		final TextureCell cell = new TextureCell();
		cell.alpha = alpha;
		cell.pixel = true;
		cell.color = color;
		cell.x = x;
		cell.y = y;
		cell.id = cellIdCounter++;
		cells.add(cell);
		
		return cell.id;
	}
	
	public final int append(int x, int y, Color color) {
		final TextureCell cell = new TextureCell();
		cell.pixel = true;
		cell.color = color;
		cell.x = x;
		cell.y = y;
		cell.id = cellIdCounter++;
		cells.add(cell);
		return cell.id;
	}
	
	public final int append(
			final Texture texture,
			final int x,
			final int y,
			final float scaleX,
			final float scaleY) {
		final TextureCell cell = new TextureCell();
		cell.x = x;
		cell.y = y;
		cell.texture = texture;
		cell.scaleX = scaleX;
		cell.scaleY = scaleY;
		cell.id = cellIdCounter++;
		cells.add(cell);
		return cell.id;
	}
	
	public final int append(
			final Texture texture,
			final int x,
			final int y,
			final int repeatX,
			final int repeatY) {
		final TextureCell cell = new TextureCell();
		cell.x = x;
		cell.y = y;
		cell.texture = texture;
		cell.repeatX = repeatX;
		cell.repeatY = repeatY;
		cell.id = cellIdCounter++;
		cells.add(cell);
		
		return cell.id;
	}

	public final int append(
			final Texture texture,
			final int x,
			final int y) {
		final TextureCell cell = new TextureCell();
		cell.x = x;
		cell.y = y;
		cell.texture = texture;
		cell.id = cellIdCounter++;
		cells.add(cell);
		
		return cell.id;
	}
	
	public final void remove(final int cellId) {
		final int count = cells.size();
		for(int i = 0; i < count; i ++) {
			final TextureCell cell = cells.get(i);
			if(cell.id == cellId) {
				cells.remove(cell);
				return;
			}
		}
	}
	
	public final TextureAtlas createTextureAtlas() {
		return TextureManager.getInstance().getTextureLink().
			createTextureAtlas(width, height, cells.toArray(new TextureCell[] {}));
	}
	
	public static final TextureAtlas bitmapTextToTextureAtlas(final BitmapText bitmapText) {
		return TextureManager.getInstance().getTextureLink().bitmapTextToTextureAtlas(bitmapText);
	}
	
	public static final Texture bitmapTextToTexture(final BitmapText bitmapText) {
		final TextureAtlas textureAtlas = bitmapTextToTextureAtlas(bitmapText);
		if(textureAtlas != null) {
			return textureAtlas.createTexture();
		}
		return null;
	}
}




























