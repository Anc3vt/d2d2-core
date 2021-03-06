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
package ru.ancevt.d2d2.time;
/**
 * Интерфейс является слушателем событий многошагового таймера
 * 
 * @author ancevt
 * @see MultistepTimer
 */
public interface MultistepTimerListener extends TimerListener {
	/**
	 * Событие, инициализируемое на каждом шаге многошагового таймера. В качестве аргумента
	 * передается номер шага, начиная с 0
	 * @param step номер выполненного шага начиная с 0
	 */
	void onTimerStep(int step);
}
