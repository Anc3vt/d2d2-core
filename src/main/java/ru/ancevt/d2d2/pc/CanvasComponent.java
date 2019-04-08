package ru.ancevt.d2d2.pc;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLCanvas;

import ru.ancevt.d2d2.common.D2D2;
import ru.ancevt.d2d2.display.Stage;
import ru.ancevt.d2d2.touch.TouchProcessor;

class CanvasComponent extends GLCanvas {

	private static final long serialVersionUID = -8532555873360169509L;
	
	private Stage stage;
	
	private Renderer renderer;
	private boolean renderingNow;
	private Thread renderThread;
	private TextureMirror textureMirror;
	
	public CanvasComponent() {
		textureMirror = new TextureMirror();
		textureMirror.init();
		renderer = new Renderer(this);
		
		D2D2.setRenderer(renderer);
		
		addGLEventListener(renderer);
		
		stage = new Stage(renderer);
		
		setBackground(java.awt.Color.BLACK);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				onScreenTouchDown(e.getX(), e.getY(), 0);
				super.mousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				onScreenTouchUp(e.getX(), e.getY(), 0);
				super.mouseReleased(e);
			}

		});
		
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				onMouseWheel(e.getUnitsToScroll());
			}
		});

		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				onMouseMove(e.getX(), e.getY());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				onScreenDrag(e.getX(), e.getY(), 0);
			}
		});
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(stage.getRoot() == null) return;
				
				stage.getRoot().onKeyDown(
					e.getKeyCode(), 
					e.getKeyChar(),  
					e.isShiftDown(), 
					e.isControlDown(), 
					e.isAltDown()
				);
				stage.getRoot().dispatchKeyDown(
					e.getKeyCode(), 
					e.getKeyChar(),  
					e.isShiftDown(), 
					e.isControlDown(), 
					e.isAltDown()
				);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(stage.getRoot() == null) return;
				
				stage.getRoot().onKeyUp(
					e.getKeyCode(), 
					e.getKeyChar(), 
					e.isShiftDown(), 
					e.isControlDown(), 
					e.isAltDown()
				);
				stage.getRoot().dispatchKeyUp(
					e.getKeyCode(), 
					e.getKeyChar(),  
					e.isShiftDown(), 
					e.isControlDown(), 
					e.isAltDown()
				);
			}
		});
	}
	
	private void onMouseMove(final int x, final int y) {
		if(stage.getRoot() == null) return;
		
		stage.getRoot().onMouseMove(x, y, false);
	}
	
	private void onMouseWheel(final int delta) {
		if(stage.getRoot() == null) return;
		
		stage.getRoot().onMouseWheel(delta);
	}
	
	private void onScreenTouchDown(final int x, final int y, final int pointer) {
		if(stage.getRoot() == null) return;
		
		final float dWidth = getWidth();
		final float dHeight = getHeight();

		final float rWidth = stage.getWidth();
		final float rHeight = stage.getHeight();

		final int tx = (int) (float) (x * (rWidth / dWidth));
		final int ty = (int) (float) (y * (rHeight / dHeight));

		TouchProcessor.getInstance().screenTouch(x, y, pointer, true);
		
		if (stage.getRoot() != null) {
			if(stage.getScaleMode() == Stage.SCALE_MODE_EXTENDED)
				stage.getRoot().onScreenTouchDown(tx, ty, pointer);
			if(stage.getScaleMode() == Stage.SCALE_MODE_REAL)
				stage.getRoot().onScreenTouchDown(x, y, pointer);
		}
	};

	private void onScreenTouchUp(final int x, final int y, final int pointer) {
		if(stage.getRoot() == null) return;
		
		final float dWidth = getWidth();
		final float dHeight = getHeight();

		final float rWidth = stage.getWidth();
		final float rHeight = stage.getHeight();

		final int tx = (int) (float) (x * (rWidth / dWidth));
		final int ty = (int) (float) (y * (rHeight / dHeight));

		TouchProcessor.getInstance().screenTouch(x, y, pointer, false);

		if (stage.getRoot() != null) {
			if(stage.getScaleMode() == Stage.SCALE_MODE_EXTENDED)
				stage.getRoot().onScreenTouchUp(tx, ty, pointer);
			if(stage.getScaleMode() == Stage.SCALE_MODE_REAL)
				stage.getRoot().onScreenTouchUp(x, y, pointer);
		}
	}

	private void onScreenDrag(final int x, final int y, final int pointer) {
		if(stage.getRoot() == null) return;
		
		final float dWidth = getWidth();
		final float dHeight = getHeight();

		final float rWidth = stage.getWidth();
		final float rHeight = stage.getHeight();

		final int tx = (int) (float) (x * (rWidth / dWidth));
		final int ty = (int) (float) (y * (rHeight / dHeight));

		TouchProcessor.getInstance().screenDrag(x, y, pointer);
		
		if(stage.getScaleMode() == Stage.SCALE_MODE_EXTENDED)
			stage.getRoot().onScreenDrag(tx, ty, pointer);
		if(stage.getScaleMode() == Stage.SCALE_MODE_REAL)
			stage.getRoot().onScreenDrag(x, y, pointer);
	

		stage.getRoot().onMouseMove(x, y, true);
	}
	
	
	
	
	public void contextCreated(GL2 gl) {
		
	}
	
	Stage getStage() {
		return stage;
	}
	
	void setRendering(final boolean b) {

		renderingNow = b;

		if (b && renderThread == null) {
			renderThread = new Thread(new Runnable() {
				@Override
				public void run() {

					while (!renderThread.isInterrupted() && renderingNow) {

						if (!renderer.isContextCreated()) {
							continue;
						}

						display();
					}

				}
			});
			renderThread.start();

		}

	}

}
