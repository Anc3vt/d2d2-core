package ru.ancevt.d2d2.test;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import ru.ancevt.d2d2.common.PlainRect;
import ru.ancevt.d2d2.debug.DebugBorder;
import ru.ancevt.d2d2.debug.FPSMeter;
import ru.ancevt.d2d2.debug.TextureManagerInfo;
import ru.ancevt.d2d2.display.DisplayObject;
import ru.ancevt.d2d2.display.Root;
import ru.ancevt.d2d2.display.Stage;
import ru.ancevt.d2d2.display.text.BitmapFont;
import ru.ancevt.d2d2.pc.D2D2Window;
import ru.ancevt.d2d2.test.stuff.BrickWall;
import ru.ancevt.d2d2.touch.TouchButton;

public class Test_Window {
	
	public static void main(String[] args) {
		
		final D2D2Window window = new D2D2Window(800, 600) {

			private static final long serialVersionUID = 3296315089809377966L;

			@Override
			public void init() {
				Test_Window.init(this);
			}
		};
		window.setTitle("D2D2 test (floating)");
		window.setVisible(true);
		window.setAlwaysOnTop(true);
		
		window.setLocation(200,  200);
		
	}
	
	public static final void init(final D2D2Window window) {
		final Stage stage = window.getStage();
		
		try {
			BitmapFont.setDefaultBitmapFont(BitmapFont.loadBitmapFont("consola.bmf"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		stage.setFrameRate(60);
		stage.setScaleMode(Stage.SCALE_MODE_REAL);
		stage.setAlign(Stage.ALIGN_TOP_LEFT);
		stage.setStageSize(800, 600);
	
		final Root root = new Root();
		
		final BrickWall brickWall = new BrickWall(32,16) {
			@Override
			public void onEachFrame() {
				moveX(0.01f);
				super.onEachFrame();
			}
		};
		root.add(brickWall, 128, 100);
		//brickWall.setScale(.2f, .2f);
		
		window.canvas().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.isAltDown()) {
					switch(e.getKeyCode()) {
					case KeyEvent.VK_E:
						stage.setScaleMode(Stage.SCALE_MODE_EXTENDED);
						break;
					case KeyEvent.VK_F:
						stage.setScaleMode(Stage.SCALE_MODE_FIT);
						break;
					case KeyEvent.VK_R:
						stage.setScaleMode(Stage.SCALE_MODE_REAL);
						break;
					}
				}
				
				System.out.println("Key: " + e.getKeyCode() + " " + e.getKeyChar());
				
				super.keyPressed(e);
			}
		});
		
		final PlainRect p = new PlainRect();
		p.setAlpha(0.5f);
		p.setScale(130, 32);
		p.setColor(0x000000);
		root.add(p);
		
		final FPSMeter fpsMeter = new FPSMeter();
		root.add(fpsMeter, 0, 0);
		
		final DisplayObject info = new TextureManagerInfo();
		root.add(info, 0, 24);
		
		final TouchButton button = new TouchButton(200, 200, true) {
			@Override
			public void onTouchDown(int x, int y) {
				System.out.println("Touch down");
				super.onTouchDown(x, y);
			}
		};
		root.add(button);
		root.add(new DebugBorder(button));
		
		
		stage.setRoot(root);
	}
}




































