package ru.ancevt.d2d2.display.text;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import ru.ancevt.d2d2.display.texture.TextureAtlas;
import ru.ancevt.d2d2.display.texture.TextureManager;
import ru.ancevt.d2d2.exception.NoDefaultFontSpecifiedException;
import ru.ancevt.d2d2.io.Assets;

public class BitmapFont {
	
	private static final int MAX_CHARS = 2048;

	private static BitmapFont defaultBitmapFont;
	private static final String BITMAP_FONTS_DIR_NAME = "bitmapfonts/";

	private final BitmapCharInfo[] charInfos;
	private final TextureAtlas textureAtlas;
	private final boolean monospace;
	
	
	private BitmapFont(
			final TextureAtlas textureAtlas, 
			final BitmapCharInfo[] charInfos, 
			final boolean monospace) { // TODO: implement monospace (flag) mechanism 
		
		this.textureAtlas = textureAtlas;
		this.charInfos = charInfos;
		this.monospace = monospace;
	}
	
	public final BitmapCharInfo getCharInfo(char c) {
		if(c < 0 || c >= MAX_CHARS) return null;
		
		return charInfos[c];
	}
	
	public final int getCharHeight() {
		return charInfos['0'].getHeight();
	}

	public TextureAtlas getTextureAtlas() {
		return textureAtlas;
	}
	
	public boolean isMonospace() {
		return monospace;
	}
	
	// ------------------- Loading stuff: ---------------------------------------------
	
	public static final void setDefaultBitmapFont(final BitmapFont bitmapFont) {
		defaultBitmapFont = bitmapFont;
	}
	
	public static final BitmapFont getDefaultBitmapFont() {
		if(defaultBitmapFont == null) throw new NoDefaultFontSpecifiedException(null);
		return defaultBitmapFont;
	}
	
	public static final void loadDefaultBitmapFont(String assetPath) throws IOException {
		BitmapFont.setDefaultBitmapFont(BitmapFont.loadBitmapFont(assetPath));
	}
	
	
	public static final BitmapFont loadBitmapFont(final String bmfAssetPath) throws IOException {
		return loadBitmapFont(Assets.getFile(BITMAP_FONTS_DIR_NAME + bmfAssetPath));
	}
	
	public static final BitmapFont loadBitmapFont(final File bmfFile) throws IOException {
		final DataInputStream dataInputStream = new DataInputStream(new FileInputStream(bmfFile));
		
		final BitmapCharInfo[] charInfos = new BitmapCharInfo[MAX_CHARS];
		
		int metaSize = dataInputStream.readUnsignedShort();
		
		while(metaSize > 0) {
			final char character = dataInputStream.readChar();
			final short x 	     = dataInputStream.readShort();
			final short y        = dataInputStream.readShort();
			final short width    = dataInputStream.readShort();
			final short height   = dataInputStream.readShort();
			
			final BitmapCharInfo bitmapCharInfo = new BitmapCharInfo(character, x, y, width, height);
			
			charInfos[character] = bitmapCharInfo;
			
			metaSize -= Character.BYTES;
			metaSize -= Short.BYTES;
			metaSize -= Short.BYTES;
			metaSize -= Short.BYTES;
			metaSize -= Short.BYTES;
		}
		
		final TextureManager textureManager = TextureManager.getInstance();
		final TextureAtlas textureAtlas = textureManager.loadTextureAtlas(dataInputStream);
		dataInputStream.close();

		final BitmapFont bitmapFont = new BitmapFont(textureAtlas, charInfos, false); 
		return bitmapFont;
	}
}

















