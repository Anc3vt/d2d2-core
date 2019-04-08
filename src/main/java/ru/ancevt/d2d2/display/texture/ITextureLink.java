package ru.ancevt.d2d2.display.texture;

import java.io.InputStream;

import ru.ancevt.d2d2.display.text.BitmapText;

public interface ITextureLink {
	TextureAtlas createTextureAtlas(InputStream pngInputStream);
	TextureAtlas createTextureAtlas(int width, int height, TextureCell[] cells);
	void unloadTextureAtlas(TextureAtlas textureAtlas);
	TextureAtlas bitmapTextToTextureAtlas(BitmapText bitmapText);
}
