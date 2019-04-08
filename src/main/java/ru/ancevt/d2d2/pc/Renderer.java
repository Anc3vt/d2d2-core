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
package ru.ancevt.d2d2.pc;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.glu.GLU;

import ru.ancevt.d2d2.display.Color;
import ru.ancevt.d2d2.display.DisplayObject;
import ru.ancevt.d2d2.display.DisplayObjectContainer;
import ru.ancevt.d2d2.display.FrameSet;
import ru.ancevt.d2d2.display.IRenderer;
import ru.ancevt.d2d2.display.Sprite;
import ru.ancevt.d2d2.display.Stage;
import ru.ancevt.d2d2.display.text.BitmapCharInfo;
import ru.ancevt.d2d2.display.text.BitmapFont;
import ru.ancevt.d2d2.display.text.BitmapText;
import ru.ancevt.d2d2.display.texture.Texture;
import ru.ancevt.d2d2.display.texture.TextureAtlas;
import ru.ancevt.d2d2.display.texture.TextureManager;
import ru.ancevt.d2d2.time.Timer;

class Renderer implements GLEventListener, IRenderer {

	private static final boolean ANTIALIASING_ENABLED = false;

	private static final GLU glu = new GLU();

	private static int _tX, _tY, _tW, _tH;
	private static float _totalW, _totalH, _x, _y, _w, _h, _px, _py;

	private static float _repeatX, _repeatY;
	
	static GL2 gl;
	static GLProfile glProfile;

	private CanvasComponent canvas;
	private int fpsDelay;

	private boolean contextCreated;
	
	private float backgroundColorRed;
	private float backgroundColorGreen;
	private float backgroundColorBlue;
	
	private Stage stage;

	Renderer(final CanvasComponent canvas) {
		this.canvas = canvas;
	}

	@Override
	public void init(GLAutoDrawable gLDrawable) {

		gl = gLDrawable.getGL().getGL2();
		glProfile = gLDrawable.getGLProfile();
		
		//gl.glHint(GL2.GL_POINT_SMOOTH_HINT,   GL2.GL_NICEST);
		//gl.glHint(GL2.GL_LINE_SMOOTH_HINT,    GL2.GL_NICEST);
		//gl.glHint(GL2.GL_POLYGON_SMOOTH_HINT, GL2.GL_NICEST);
		
		

		reshape(gLDrawable, 0, 0, canvas.getWidth(), canvas.getHeight());

		if (!isContextCreated()) {
			setContextCreated(true);
			onContextCreated(gl);
		}
	}

	private void onContextCreated(GL2 gl) {
		TextureManager.getInstance().loadTextureDataInfo();
		canvas.contextCreated(gl);
	}

	@Override
	public void display(GLAutoDrawable gLDrawable) {
		final long time1 = System.currentTimeMillis();

		gl = gLDrawable.getGL().getGL2();

		final Stage stage = canvas.getStage();
		
		if (stage != null) {
			backgroundColorRed = stage.getBackgroundColor().getR() / 255.0f;
			backgroundColorGreen = stage.getBackgroundColor().getG() / 255.0f;
			backgroundColorBlue = stage.getBackgroundColor().getB() / 255.0f;
			
			
			gl.glClearColor(backgroundColorRed, backgroundColorGreen, backgroundColorBlue, 1.0f);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		}

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		if (stage == null) return;

		gl.setSwapInterval(1);
		renderDisplayObject(
			stage, 
			gl, 0, 
			stage.getX(), 
			stage.getY(), 
			stage.getScaleX(), 
			stage.getScaleY(),
			stage.getRotation(), 
			stage.getAlpha(), 
			0.0f, 0.0f
		);
		
		dispatchEachFrame(stage);
		Timer.handle();

		final long time2 = System.currentTimeMillis();

		final int stm = (int) (time2 - time1);
		final int limit = fpsDelay;
		
		if (stm < limit) {
			try {
				Thread.sleep(limit - stm);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private final void dispatchEachFrame(final DisplayObjectContainer displayObjectContainer) {
		for(int i = 0; i < displayObjectContainer.getChildCount(); i ++) {
			final DisplayObject displayObject = displayObjectContainer.getChild(i);
			displayObject.onEachFrame();
			
			if(displayObject instanceof DisplayObjectContainer)
				dispatchEachFrame((DisplayObjectContainer)displayObject);
		}
	}

	private void renderDisplayObject(DisplayObject displayObject, final GL2 gl, final int level, final float toX,
			final float toY, final float toScaleX, final float toScaleY, final float toRotation, final float toAlpha,
			final float toRotX, final float toRotY) {

		if (displayObject == null || !displayObject.isVisible())
			return;

		final float scX = displayObject.getScaleX() * toScaleX;
		final float scY = displayObject.getScaleY() * toScaleY;
		final float r = displayObject.getRotation() + toRotation;

		final float x = toScaleX * displayObject.getX();
		final float y = toScaleY * displayObject.getY();

		final float a = displayObject.getAlpha() * toAlpha;

		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0);

		gl.glRotatef(r, 0, 0, 1);

		if (displayObject instanceof DisplayObjectContainer) {
			DisplayObjectContainer container = (DisplayObjectContainer) displayObject;

			for (int i = 0; i < container.getChildCount(); i++) {

				final DisplayObject currentChild = container.getChild(i);
				
				renderDisplayObject(
					currentChild, 
					gl, 
					level + 1, 
					x + toX, 
					y + toY, 
					scX, scY, 
					0, 
					a, 
					displayObject.getX(), 
					displayObject.getY()
				);
			}

		} else

		if (displayObject instanceof Sprite) {
			renderSprite((Sprite) displayObject, gl, a, scX, scY);
		} else

		if (displayObject instanceof BitmapText) {
			final BitmapText bitmapText = (BitmapText) displayObject;

			if (!bitmapText.isEmpty()) {

				final BitmapFont bitmapFont = bitmapText.getBitmapFont();

				final com.jogamp.opengl.util.texture.Texture texture = 
						(com.jogamp.opengl.util.texture.Texture) bitmapFont.getTextureAtlas().getNativeTextureData();
				
				if (texture == null)
					return;
				
				gl.glEnable(GL2.GL_BLEND);
				gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
				
				texture.bind(gl);
				texture.enable(gl);

				textureParamsHandle(gl, texture);
				
				final Color color = bitmapText.getColor();

				gl.glColor4f(
					(float)color.getR()/255f, 
					(float)color.getG()/255f, 
					(float)color.getB()/255f, a);

				final String text = bitmapText.getText();
				
				final int textureWidth = texture.getWidth();
				final int textureHeight = texture.getHeight();

				final float lineSpacing = bitmapText.getLineSpacing();
				final float spacing = bitmapText.getSpacing();

				final float boundWidth = bitmapText.getBoundWidth() * bitmapText.getAbsoluteScaleX();
				final float boundHeight = bitmapText.getBoundHeight()  * bitmapText.getAbsoluteScaleY();

				float drawX = 0, drawY = 0;

				gl.glBegin(GL2.GL_QUADS);

				final int length = text.length();
				for (int i = 0; i < length; i++) {
					final char c = text.charAt(i);
					
					final BitmapCharInfo charInfo = bitmapFont.getCharInfo(c);
					
					if(charInfo == null) continue;
					
					final float charWidth = charInfo.getWidth();
					final float charHeight = charInfo.getHeight();

					if (c == '\n' || (boundWidth != 0 && drawX >= boundWidth - charWidth)) {
						drawX = 0;
						drawY += (charHeight + lineSpacing) * scY;

						if (boundHeight != 0 && drawY > boundHeight) {
							break;
						}
					}

					drawChar(gl, drawX, (drawY + scY * charHeight), textureWidth, textureHeight, charInfo, scX, scY);
					
					drawX += (charWidth + (c != '\n' ? spacing : 0)) * scX;
				}

				gl.glEnd();

				gl.glDisable(GL2.GL_BLEND);
				texture.disable(gl);
			}
		}
		
		if(displayObject instanceof FrameSet) {
			((FrameSet)displayObject).processFrame();
		}

		gl.glPopMatrix();
	}
	
	private final void drawChar(
			final GL2 gl, 
			final float x, 
			final float y, 
			final int textureWidth, 
			final int textureHeight,
			final BitmapCharInfo charInfo,
			float scX,
			float scY) {
		

		final float charWidth = charInfo.getWidth();
		final float charHeight = charInfo.getHeight();
		
		final float xOnTexture = charInfo.getX();
		final float yOnTexture = charInfo.getY() + charHeight;

		final float cx = (float) xOnTexture / textureWidth, 
					cy = (float) yOnTexture / textureHeight,
					cw = (float) charWidth / textureWidth, 
					ch = (float) charHeight / textureHeight;

		gl.glTexCoord2f(cx, -cy);
		gl.glVertex2f(x, y);
		gl.glTexCoord2f(cx + cw, -cy);
		gl.glVertex2f(charWidth * scX + x, y);
		gl.glTexCoord2f(cx + cw, -cy + ch);
		gl.glVertex2f(charWidth * scX + x, charHeight * -scY + y);
		gl.glTexCoord2f(cx, -cy + ch);
		gl.glVertex2f(x, charHeight * -scY + y);
	}
	
	private static final void textureParamsHandle(GL2 gl, com.jogamp.opengl.util.texture.Texture texture) {
		if (ANTIALIASING_ENABLED) {
			texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
			texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
			texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
			texture.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
		} else {
			texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
			texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
			texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
			texture.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
		}
	}

	private final void renderSprite(final Sprite sprite, final GL2 gl, final float alpha, final float scaleX,
			final float scaleY) {
		
		final Texture texture = sprite.getTexture();
		
		if(texture == null) return;
		
		final TextureAtlas textureAtlas = texture.getTextureAtlas();

		final com.jogamp.opengl.util.texture.Texture jTexture = 
				(com.jogamp.opengl.util.texture.Texture) textureAtlas.getNativeTextureData();
		
		if (jTexture == null) return;

		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

		jTexture.bind(gl);
		jTexture.enable(gl);

		textureParamsHandle(gl, jTexture);

		final Color color = sprite.getColor();

		if(color != null) {
			gl.glColor4f(
				(float)color.getR() / 255f,
				(float)color.getG() / 255f,
				(float)color.getB() / 255f,
				alpha
			);
		}
		
		_tX = texture.getX();
		_tY = texture.getY();
		_tW = texture.getWidth();
		_tH = texture.getHeight();

		_totalW = jTexture.getWidth();
		_totalH = jTexture.getHeight();

		_x = _tX / _totalW;
		_y = -_tY / _totalH;
		_w = _tW / _totalW;
		_h = -_tH / _totalH;
		
		_repeatX = sprite.getRepeatX();
		_repeatY = sprite.getRepeatY();

		for (int rY = 0; rY < _repeatY; rY++) {
			for (int rX = 0; rX < _repeatX; rX++) {

				_px = rX * _tW * scaleX;
				_py = rY * _tH * scaleY;

				gl.glBegin(GL2.GL_QUADS);
				gl.glTexCoord2f(_x, _h + _y);
				gl.glVertex2f(_px + 0, _py + _tH * scaleY);
				gl.glTexCoord2f(_w + _x, _h + _y);
				gl.glVertex2f(_px + _tW * scaleX, _py + _tH * scaleY);
				gl.glTexCoord2f(_w + _x, _y);
				gl.glVertex2f(_px + _tW * scaleX, _py + 0);
				gl.glTexCoord2f(_x, _y);
				gl.glVertex2f(_px + 0, _py + 0);
				gl.glEnd();
			}
		}

		gl.glDisable(GL2.GL_BLEND);
		jTexture.disable(gl);
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {

	}

	@Override
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) {
		if(gl == null) return;
		gl = glDrawable.getGL().getGL2();
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		glu.gluOrtho2D(0, width, height, 0);
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		stage.onResize(width, height);
	}

	public GL2 getGL() {
		return gl;
	}

	public boolean isContextCreated() {
		return contextCreated;
	}

	private void setContextCreated(boolean contextCreated) {
		this.contextCreated = contextCreated;
	}

	public int getFpsDelay() {
		return fpsDelay;
	}

	public void setFpsDelay(int fpsDelay) {
		this.fpsDelay = fpsDelay;
	}

	@Override
	public Object glProfile() {
		return glProfile;
	}

	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@Override
	public Stage getStage() {
		return stage;
	}

	@Override
	public int getWidth() {
		return canvas.getWidth();
	}

	@Override
	public int getHeight() {
		return canvas.getHeight();
	}
}
















