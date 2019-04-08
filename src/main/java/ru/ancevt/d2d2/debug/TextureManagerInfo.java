package ru.ancevt.d2d2.debug;

import ru.ancevt.d2d2.display.text.BitmapFont;
import ru.ancevt.d2d2.display.text.BitmapText;
import ru.ancevt.d2d2.display.texture.TextureAtlas;
import ru.ancevt.d2d2.display.texture.TextureManager;

public class TextureManagerInfo extends BitmapText {

	public TextureManagerInfo() {
		this(BitmapFont.getDefaultBitmapFont());
	}
	
	public TextureManagerInfo(BitmapFont bitmapFont) {
		super(bitmapFont);
	}
	
	@Override
	public void onEachFrame() {
		setText(getInfo());
		super.onEachFrame();
	}
	
	public final String getInfo() {
		final TextureManager tm = TextureManager.getInstance();
		final int textureAtlasCount = tm.getTextureAtlasCount();
		final StringBuilder stringBuilder = new StringBuilder("TextureManager: " + textureAtlasCount + "\n");

		for(int i = 0; i < textureAtlasCount; i ++) {
			final TextureAtlas a = tm.getTextureAtlas(i);
			stringBuilder.append(" " + a.toString() + "\n");
		}
		
		return stringBuilder.toString();
	}

}
