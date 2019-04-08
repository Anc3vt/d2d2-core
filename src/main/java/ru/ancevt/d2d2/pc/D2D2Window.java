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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;

import ru.ancevt.d2d2.display.Stage;


/**
 * Экземпляр данного класса является готовым окном, для запуска приложения
 * на платформе PC. Автоматически создает полотно для отрисовки (Canvas).
 * @author ancevt
 * @see CanvasComponent
 */
abstract public class D2D2Window extends JFrame {

	private static final long serialVersionUID = 8485291697794376740L;

	private CanvasComponent canvas;
	
	/**
	 * Конструктор.
	 * @param screenWidth ширина экрана приложения
	 * @param screenHeight высота экрана приложения
	 */
	public D2D2Window(int screenWidth, int screenHeight) {
		setSize(100,100);
		setIconImage(WindowIcon.getWindowIcon());
		
		createCanvas(screenWidth, screenHeight);
		
		pack();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				super.windowClosing(e);
			}
		});
	}
	
	public Stage getStage() {
		return canvas.getStage();
	}
	
	private final void createCanvas(int screenWidth, int screenHeight) {
		canvas = new CanvasComponent() {

			private static final long serialVersionUID = -3293830197827070137L;

			@Override
			public void contextCreated(GL2 gl) {
				super.contextCreated(gl);
				init();
			}
		};
		canvas.setPreferredSize(new Dimension(screenWidth, screenHeight));
		add(canvas);
		canvas.setRendering(true);
		
	}

	abstract public void init();
	
	/**
	 * Возвращает полотно отрисовки графики
	 * @return полотно отрисовки графики
	 */
	public final Component canvas() {
		return canvas;
	}
	
	@Override
	public String toString() {
		return "D2D2Window[]";
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		canvas.requestFocus();
	}
}














































