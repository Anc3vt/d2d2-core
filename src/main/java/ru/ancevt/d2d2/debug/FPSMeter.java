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
package ru.ancevt.d2d2.debug;

import java.io.IOException;

import ru.ancevt.d2d2.display.Color;
import ru.ancevt.d2d2.display.text.BitmapFont;
import ru.ancevt.d2d2.display.text.BitmapText;

/**
 * Визуальный измеритель частоты проигрывания кадров в секунду. (FPS)
 * 
 * @author ancevt
 *
 */
public class FPSMeter extends BitmapText {

	private long time1 = System.currentTimeMillis();
	private long time2;
	private int frameCounter;
	private int actualFramesPerSeconds;

	/**
	 * Конструктор.
	 * 
	 * @param font
	 *            точечный шрифт для отображения FPS
	 */
	public FPSMeter(BitmapFont font) {
		super(font);
		setName("fpsMeter");
	}

	/**
	 * Конструктор. В качестве точечного шрифта для отображения используется
	 * точечный шрифт по умолчанию.
	 * @throws IOException 
	 */
	public FPSMeter() {
		this(BitmapFont.getDefaultBitmapFont());
	}
	
	public final int getFramesPerSecond() {
		return actualFramesPerSeconds;
	}

	@Override
	public void onEachFrame() {

		frameCounter++;
		time2 = System.currentTimeMillis();
		if (time2 - time1 >= 1000) {
			time1 = System.currentTimeMillis();

			setText("FPS: " + frameCounter);
			actualFramesPerSeconds = frameCounter;

			if (frameCounter > 40)
				setColor(Color.GREEN);
			else if (frameCounter >= 30 && frameCounter < 40)
				setColor(Color.YELLOW);
			else if (frameCounter < 30)
				setColor(Color.RED);

			frameCounter = 0;
		}

		super.onEachFrame();
	}
	
	@Override
	public String toString() {
		return "FPSMeter[]";
	}
}

