package ru.ancevt.d2d2.pc;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.imageio.ImageIO;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import ru.ancevt.d2d2.display.text.BitmapCharInfo;
import ru.ancevt.d2d2.display.text.BitmapFont;
import ru.ancevt.d2d2.display.text.BitmapText;
import ru.ancevt.d2d2.display.texture.ITextureLink;
import ru.ancevt.d2d2.display.texture.TextureAtlas;
import ru.ancevt.d2d2.display.texture.TextureCell;
import ru.ancevt.d2d2.display.texture.TextureManager;

public class TextureMirror implements ITextureLink { 

	public TextureMirror() {
		
	}
	
	public final void init() {
		final TextureManager tm = TextureManager.getInstance();
		
		try {
			Method method = tm.getClass().getDeclaredMethod("setTextureLink", ITextureLink.class);
			method.setAccessible(true);
			method.invoke(tm, this);
			
		} catch (NoSuchMethodException | 
				SecurityException | 
				IllegalAccessException | 
				IllegalArgumentException | 
				InvocationTargetException e) {
			
			e.printStackTrace();
		}
	}
	
	@Override
	public TextureAtlas createTextureAtlas(InputStream inputStream) {
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		final TextureAtlas result = imageToTextureAtlas(image);
		return result;
	}
	
	// TODO: incapsulate
	public static final TextureAtlas imageToTextureAtlas(final BufferedImage image) {
		final BufferedImage flippedImage = createFlipped(image);
		final TextureData textureData = AWTTextureIO.newTextureData(Renderer.glProfile, flippedImage, false);
		
		final com.jogamp.opengl.util.texture.Texture jTexture = TextureIO.newTexture(textureData);
		final TextureAtlas result = new TextureAtlas(jTexture, jTexture.getWidth(), jTexture.getHeight());

		result.setNativeImageData(image);
		
		return result;
	}

	// TODO: incapsulate
	public static BufferedImage createFlipped(BufferedImage image) {
		AffineTransform at = new AffineTransform();
		at.concatenate(AffineTransform.getScaleInstance(1, -1));
		at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
		return createTransformed(image, at);
	}

	// TODO: incapsulate
	static BufferedImage createTransformed(BufferedImage image, AffineTransform at) {
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.transform(at);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}

	@Override
	public final void unloadTextureAtlas(final TextureAtlas textureAtlas) {
		final Texture jTexture = (Texture) textureAtlas.getNativeTextureData();
		jTexture.destroy(Renderer.gl);
	}

	@Override
	public TextureAtlas createTextureAtlas(int width, int height, TextureCell[] cells) {
		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = (Graphics2D)image.getGraphics();
		
		for(final TextureCell cell : cells) {
			
			if(cell.pixel) {
				
				final java.awt.Color awtColor 
					= new java.awt.Color(
						cell.color.getR(),
						cell.color.getG(),
						cell.color.getB(),
						cell.alpha
				);
				
				g.setColor(awtColor);
				g.drawRect(cell.x, cell.y, 0, 0);
			} else {
			
				AlphaComposite alphaComposite = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, cell.alpha
				);
				
				g.setComposite(alphaComposite);
				
				AffineTransform affineTransform = g.getTransform();
				affineTransform.rotate(Math.toRadians(cell.rotation), cell.x, cell.y);
				g.setTransform(affineTransform);
				
				drawImage(g, cell);
	
				affineTransform.rotate(Math.toRadians(-cell.rotation), cell.x, cell.y);
				g.setTransform(affineTransform);
			}
		}
		
		final TextureAtlas textureAtlas = imageToTextureAtlas(image);
		TextureManager.getInstance().addTexture(textureAtlas.createTexture());
		return textureAtlas;
	}
	
	private static void drawImage(Graphics2D g, final TextureCell cell) {
		final int x = cell.x;
		final int y = cell.y;
		final int repeatX = cell.repeatX;
		final int repeatY = cell.repeatY;
		final float scaleX = cell.scaleX;
		final float scaleY = cell.scaleY;
		
		final BufferedImage imageRegion = textureRegionToImage(cell.texture);

		final int width = cell.texture.getWidth() * repeatX;
		final int height = cell.texture.getHeight() * repeatY;
		
		final int originWidth  = imageRegion.getWidth(null);
		final int originHeight = imageRegion.getHeight(null);
		
		int w = (int)(originWidth * scaleX);
		int h = (int)(originHeight * scaleY);
		
		for(int vert = 0; vert < height; vert += originHeight)
			for(int hor = 0; hor < width; hor += originWidth) {
				
				g.drawImage(imageRegion, 
					(int)(x + hor * scaleX) , 
					(int)(y + vert * scaleY),
					w, h, 
					null
				);
			}
	}
	
	private static final BufferedImage textureRegionToImage(
    		final TextureAtlas textureAtlas, 
    		final int x, 
    		final int y,
    		final int width,
    		final int height) {
    	
		final BufferedImage bufferedImage = (BufferedImage)(textureAtlas.getNativeImageData());
		
    	return bufferedImage.getSubimage(x, y, width, height);
    }
	
	private static final BufferedImage textureRegionToImage(final ru.ancevt.d2d2.display.texture.Texture texture) {
    	final TextureAtlas textureAtlas = texture.getTextureAtlas();
    	
    	final int x = texture.getX();
    	final int y = texture.getY();
    	final int w = texture.getWidth();
    	final int h = texture.getHeight();
    	
    	return textureRegionToImage(textureAtlas, x, y, w, h);
	}
	
	@Override
	public final TextureAtlas bitmapTextToTextureAtlas(final BitmapText bitmapText) {
		final String text = bitmapText.getText();
		final float spacing = bitmapText.getSpacing();
		final float lineSpacing = bitmapText.getLineSpacing();
		
		final BitmapFont font = bitmapText.getBitmapFont();
		
		final float boundWidth = bitmapText.getBoundWidth() * bitmapText.getAbsoluteScaleX();
		final float boundHeight = bitmapText.getBoundHeight()  * bitmapText.getAbsoluteScaleY();

		final TextureAtlas fontTextureAtlas = font.getTextureAtlas();
		
		final int width =  (int)bitmapText.getBoundWidth();
		final int height = (int)bitmapText.getBoundHeight();
		
		final BufferedImage image = new BufferedImage(width, height, 
				BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = image.createGraphics();
		
		int drawX = 0, drawY = 0;
		
		final int textLength = text.length();
		for(int i = 0; i < textLength; i ++) {
			final char c = text.charAt(i);
			final BitmapCharInfo charInfo = font.getCharInfo(c);
			final int pX = charInfo.getX();
			final int pY = charInfo.getY();
			final int pW = charInfo.getWidth();
			final int pH = charInfo.getHeight();
			
			if(font.getCharInfo(c) == null) {
				continue;
			}
			
			final float charWidth = charInfo.getWidth();
			final float charHeight = charInfo.getHeight();
			
			if (c == '\n' || (boundWidth != 0 && drawX >= boundWidth - charWidth)) {
				drawX = 0;
				drawY += (charHeight + lineSpacing);

				if (boundHeight != 0 && drawY > boundHeight) {
					break;
				}
			}
			
			if (c >= 0) {
				final BufferedImage charImage = textureRegionToImage(
					fontTextureAtlas, pX, pY, pW, pH
				);
				
				g.drawImage(charImage, drawX, drawY, null);
				
				drawX += (charWidth + (c != '\n' ? spacing : 0));
			}
		}
		
		final TextureAtlas textureAtlas = TextureMirror.imageToTextureAtlas(image);
		TextureManager.getInstance().addTexture(textureAtlas.createTexture());
		return textureAtlas;
	}
}



















