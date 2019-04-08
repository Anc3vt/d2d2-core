package ru.ancevt.d2d2.display.texture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.ancevt.d2d2.io.Assets;

public class TextureManager {
	
	// --------- Singleton stuff: --------------------------------------------
	
	private static TextureManager instance;
	
	public static TextureManager getInstance() {
		return instance == null ? instance = new TextureManager() : instance;
	}
	
	// ------------------------------------------------------------------------
	
	private List<TextureAtlas> textureAtalases;
	private List<Texture> textures;
	private ITextureLink textureLink;
	
	private TextureManager() {
		textures = new ArrayList<Texture>();
		textureAtalases = new ArrayList<TextureAtlas>();
	}
	
	public ITextureLink getTextureLink() {
		return textureLink;
	}

	public void setTextureLink(ITextureLink textureLink) {
		this.textureLink = textureLink;
	}

	public final TextureAtlas loadTextureAtlas(final InputStream pngInputStream) {
		final TextureAtlas result = textureLink.createTextureAtlas(pngInputStream);
		textureAtalases.add(result);
		return result;
	}
	
	public final TextureAtlas loadTextureAtlas(final File imageFile) {
		try {
			return loadTextureAtlas(new FileInputStream(imageFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public final TextureAtlas loadTextureAtlas(final String assetPath) {
		return loadTextureAtlas(Assets.getFile(assetPath));
	}
	
	public final void unloadTextureAtlas(final TextureAtlas textureAtlas) {
		textureLink.unloadTextureAtlas(textureAtlas);
		textureAtalases.remove(textureAtlas);
	}
	
	public final void clear() {
		while(textureAtalases.size() > 0) 
			unloadTextureAtlas(textureAtalases.get(0));
	}
	
	public final int getTextureAtlasCount() {
		return textureAtalases.size();
	}
	
	public final TextureAtlas getTextureAtlas(final int index) {
		return textureAtalases.get(index);
	}
	
	public final void addTexture(final Texture texture) {
		textures.add(texture);
	}

	public final Texture getTexture(final String key) {
		final int textureCount = textures.size();
		for(int i = 0; i < textureCount; i ++) {
			final Texture texture = textures.get(i);
			if(key.equals(texture.getKey())) return texture;
		}
		
		return null;
	}
	
	public final void loadTextureDataInfo() {
		try {
			TextureDataInfoReadHelper.readTextureDataInfoFile();
		} catch (IOException e) {
			System.err.println("The file " + TextureDataInfoReadHelper.FILE_NAME + " not found");
		}
	}
}


















