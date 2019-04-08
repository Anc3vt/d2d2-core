package ru.ancevt.d2d2.test;

import java.io.IOException;

import ru.ancevt.d2d2.debug.FPSMeter;
import ru.ancevt.d2d2.display.Root;
import ru.ancevt.d2d2.display.Sprite;
import ru.ancevt.d2d2.display.Stage;
import ru.ancevt.d2d2.display.text.BitmapFont;
import ru.ancevt.d2d2.pc.D2D2Window;

public class Test_EntryPointEtalon {
	public static void main(String[] args) {
		final D2D2Window window = new D2D2Window(800, 600) {

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
		stage.setStageSize(800, 600);

		final Root root = new Root();
		
		// Entry point
		
		final Sprite sat = new Sprite("satellite");
		sat.setScale(5f, 5f);
		sat.setXY(320f, 240f);
		root.add(sat);
		
		root.add(new FPSMeter());
		stage.setRoot(root);
	}
}
