package ru.ancevt.d2d2.test;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import ru.ancevt.d2d2.debug.FPSMeter;
import ru.ancevt.d2d2.display.Color;
import ru.ancevt.d2d2.display.Root;
import ru.ancevt.d2d2.display.Sprite;
import ru.ancevt.d2d2.display.Stage;
import ru.ancevt.d2d2.display.text.BitmapFont;
import ru.ancevt.d2d2.display.text.BitmapText;
import ru.ancevt.d2d2.pc.D2D2Window;

public class Test_CommonTests {
	public static void main(String[] args) {
		final D2D2Window window = new D2D2Window(800, 640) {

			private static final long serialVersionUID = 3296315089809377966L;

			@Override
			public void init() {
				try {
					BitmapFont.setDefaultBitmapFont(BitmapFont.loadBitmapFont("Menlo-Regular.bmf"));
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
		
//		final PlainRect rect = new PlainRect();
//		rect.setSize(800, 640);
//		root.add(rect);
		
		final BitmapText bitmapText = new BitmapText();
		bitmapText.setText("This\nis is is is is\na text...");
		bitmapText.setSpacing(10);
		bitmapText.setLineSpacing(10);
		root.add(bitmapText);
		// bitmapText.setBoundWidth(50);
		bitmapText.setXY(50, 50);
		System.out.println(bitmapText.getTextWidth() + " " + bitmapText.getTextHeight());
		
		final Sprite sat = new Sprite("satellite");
		sat.setScale(5f, 5f);
		sat.setXY(320f, 240f);
		root.add(sat);
		
		final FPSMeter fpsMeter = new FPSMeter();
		fpsMeter.setXY(0, 32);
		root.add(fpsMeter);
		stage.setRoot(root);
		
		window.canvas().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.isAltDown()) {
					switch(e.getKeyCode()) {
						case KeyEvent.VK_E:
							stage.setScaleMode(Stage.SCALE_MODE_EXTENDED);
							break;
						case KeyEvent.VK_R:
							stage.setScaleMode(Stage.SCALE_MODE_REAL);
							break;
						case KeyEvent.VK_F:
							stage.setScaleMode(Stage.SCALE_MODE_FIT);
							break;
						case KeyEvent.VK_O:
							stage.setScaleMode(Stage.SCALE_MODE_OUTFIT);
							break;
						case KeyEvent.VK_A:
							stage.setScaleMode(Stage.SCALE_MODE_AUTO);
							break;
							
					}
				}
				
				System.out.println("Key: " + e.getKeyCode() + " " + e.getKeyChar());
				
				super.keyPressed(e);
			}
		});
	}
}
