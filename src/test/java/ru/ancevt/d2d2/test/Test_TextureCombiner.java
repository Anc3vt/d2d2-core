package ru.ancevt.d2d2.test;

import java.io.IOException;

import ru.ancevt.d2d2.debug.DebugBorder;
import ru.ancevt.d2d2.debug.FPSMeter;
import ru.ancevt.d2d2.display.Color;
import ru.ancevt.d2d2.display.Root;
import ru.ancevt.d2d2.display.Sprite;
import ru.ancevt.d2d2.display.Stage;
import ru.ancevt.d2d2.display.text.BitmapFont;
import ru.ancevt.d2d2.display.text.BitmapText;
import ru.ancevt.d2d2.display.texture.Texture;
import ru.ancevt.d2d2.display.texture.TextureAtlas;
import ru.ancevt.d2d2.display.texture.TextureCombiner;
import ru.ancevt.d2d2.display.texture.TextureManager;
import ru.ancevt.d2d2.pc.D2D2Window;

public class Test_TextureCombiner {
	public static void main(String[] args) {
		final D2D2Window window = new D2D2Window(800, 640) {

			private static final long serialVersionUID = 3296315089809377966L;

			@Override
			public void init() {
				try {
					BitmapFont.setDefaultBitmapFont(BitmapFont.loadBitmapFont("consola.bmf"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				d2d2Init(this);
			}
			
		};
		window.setLocationByPlatform(true);
		window.setTitle("D2D2 project (floating)");
		window.setVisible(true);
	}

	public static final void d2d2Init(final D2D2Window window) {
		final Stage stage = window.getStage();
		
		stage.setFrameRate(60);
		stage.setScaleMode(Stage.SCALE_MODE_REAL);
		stage.setAlign(Stage.ALIGN_TOP_LEFT);
		stage.setStageSize(800, 640);
		stage.setBackgroundColor(Color.DARK_GRAY);

		final Root root = new Root();
		
		// Entry point
		
		final TextureManager tm = TextureManager.getInstance();
		final Texture texture = tm.getTexture("satellite");
		
		TextureCombiner tc = new TextureCombiner(64*2, 48*2);
		tc.append(texture, 0, 0, 1f, 1f, 1f, 25f, 1, 1);
		tc.append(texture, 64, 0);
		tc.append(texture, 0, 48, 1f, 1f, 1f, 25f, 1, 1);
		tc.append(texture, 64, 48);
		final TextureAtlas resultTextureAtlas = tc.createTextureAtlas();
		final Sprite resultSprite = new Sprite(resultTextureAtlas.createTexture());
		resultSprite.setScale(5, 5);
		resultSprite.setXY(50, 50);
		root.add(resultSprite);
		
		final BitmapText bitmapText = new BitmapText(BitmapFont.getDefaultBitmapFont(), 256, 24);
		bitmapText.setText("Hello i am new texture");
		final Texture bitmapTextTexture = TextureCombiner.bitmapTextToTexture(bitmapText);
		final Sprite bitmapTextSprite = new Sprite(bitmapTextTexture);
		root.add(bitmapTextSprite);
		
		root.add(new DebugBorder(bitmapTextSprite));
		root.add(new DebugBorder(resultSprite));
		
		final FPSMeter fpsMeter = new FPSMeter();
		fpsMeter.setXY(0, 32);
		root.add(fpsMeter);
		stage.setRoot(root);
	}
}
