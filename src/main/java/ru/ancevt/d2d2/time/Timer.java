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

import java.util.ArrayList;
import java.util.List;

/**
 * Экземпляр данного класса представляет собой классический асинхронный таймер.
 * Ожидание таймера выполняется в параллельном потоке, но функция обратного
 * вызова onTimerTick у его слушателя срабатывает в основном потоке D2D2.
 * 
 * @author ancevt
 * @see TimerListener
 */
public class Timer implements TimerListener {

	/** Состояние таймера "не был запущен" */
	public static final int STATE_IDLE = 0;

	/** Состояние таймера "запущен в данный момент" */
	public static final int STATE_RUN = 1;

	/** Сосатояние таймера "выполнен" */
	public static final int STATE_COMPLETE = 2;
	
	/** Состояние таймера "прерван" */
	public static final int STATE_ABORTED = 3;

	protected static final String STRING_STATE_IDLE = "STATE_IDLE";
	protected static final String STRING_STATE_RUN = "STATE_RUN";
	protected static final String STRING_STATE_COMPLETE = "STATE_COMPLETE";
	protected static final String STRING_STATE_ABORTED = "STATE_ABORTED";

	private static int idCounter;
	private static List<Timer> activeTimers;
	private static boolean noActiveTimers;

	private boolean tick;
	/** Текущее состояние таймера */
	protected int timerState;

	static {
		noActiveTimers = true;
	}

	private TimerListener listener;
	private int delay;
	private final int id;

	private boolean loop;

	private Thread thread;

	/**
	 * Конструктор.
	 * 
	 * @param delay
	 *            задежржка асинхронного ожидания в миллисекундах.
	 */
	public Timer(final int delay) {
		id = idCounter++;
		setDelay(delay);
		setListener(this);
	}
	
	/**
	 * Конструктор.
	 * 
	 * @param delay
	 *            задежржка асинхронного ожидания в миллисекундах.
	 * @param listener
	 *            слушатель событий таймера.
	 */
	public Timer(final int delay, TimerListener listener) {
		this(delay);
		setListener(listener);
	}

	public boolean isStarted() {
		return timerState == STATE_RUN;
	}
	
	/**
	 * Запускает таймер
	 */
	public void start() {
		if (activeTimers == null)
			activeTimers = new ArrayList<Timer>();

		addTimer(this);

		timerState = STATE_RUN;

		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				do {
					try {
						Thread.sleep(delay);
						tick = true;
					} catch (InterruptedException e) {
						break;
					}
				} while (isLoop());

			}
		});
		thread.start();
	}

	/**
	 * Останавливает таймер, прерывая ожидание.
	 */
	public final void stop() {
		timerState = STATE_ABORTED;
		removeTimer(this);
		thread.interrupt();
	}

	/**
	 * Возвращает идентификатор таймера
	 * 
	 * @return уникальный идентификатор таймера
	 */
	public final int getId() {
		return id;
	}

	/**
	 * Возвращает слушателя, реализующего события таймера
	 * 
	 * @return слушатель событий таймера
	 */
	public final TimerListener getListener() {
		return listener;
	}

	/**
	 * Устанавливает слушателя, реализующего события таймера
	 * 
	 * @param listener
	 *            слушатель событий таймера. По умолчанию слушателем является
	 *            сам экземпляр таймера.
	 */
	public final void setListener(TimerListener listener) {
		this.listener = listener;
	}

	/**
	 * Возвращает задержку асинхронного ожидания таймера.
	 * 
	 * @return задержка таймера
	 */
	public final int getDelay() {
		return delay;
	}

	/**
	 * Устанавливает задержку асинхронного ожидания таймера.
	 * 
	 * @param delay
	 *            задержка таймера
	 */
	public final void setDelay(int delay) {
		this.delay = delay;
	}

	private static final void addTimer(final Timer timer) {
		activeTimers.add(timer);
		noActiveTimers = false;
	}

	private static final void removeTimer(final Timer timer) {
		activeTimers.remove(timer);
		if (activeTimers.isEmpty())
			noActiveTimers = true;
	}

	/**
	 * Обработка всех запущенных таймеров. Выполняется в основном потоке D2D2
	 * фреймворком автоматически. Вызов данного метода вручную в любом случае
	 * неоправдан.
	 */
	public static final void handle() {
		
		if (noActiveTimers)
			return;
		

		try {

			final int count = activeTimers.size();
			for (int i = 0; i < count; i++) {
				final Timer timer = activeTimers.get(i);
				if (timer.tick) {
					timer.tick = false;
					if (timer.listener != null)
						timer.apply();

					if (!timer.isLoop()) {
						timer.timerState = STATE_COMPLETE;
						Timer.removeTimer(timer);
					}
				}
			}
		} catch (IndexOutOfBoundsException ex) {
		}
	}

	/**
	 * Выполняет событие onTimerTick в слушателе событий таймера
	 */
	protected void apply() {
		listener.onTimerTick();
	}

	@Override
	public void onTimerTick() {

	}

	/**
	 * Возвращает текущее состояние таймера
	 * 
	 * @return текущее состояние таймера
	 */
	public int getState() {
		return timerState;
	}

	/**
	 * Проверяет, является ли таймер зацикленным
	 * 
	 * @return true - таймер зациклен, иначе - false
	 */
	public boolean isLoop() {
		return loop;
	}

	/**
	 * Включает/выключает зацикливание таймера
	 * 
	 * @param loop
	 *            true - зацикливает, false - выключает зацикливание
	 */
	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	@Override
	public String toString() {
		String stringState = null;

		switch (getState()) {
		case STATE_IDLE:
			stringState = STRING_STATE_IDLE;
			break;
		case STATE_RUN:
			stringState = STRING_STATE_RUN;
			break;
		case STATE_COMPLETE:
			stringState = STRING_STATE_COMPLETE;
			break;
		case STATE_ABORTED:
			stringState = STRING_STATE_ABORTED;
			break;
		}

		return String.format("Timer[%d, delay: %d, looped: %b, state: %s(%d)]", getId(), getDelay(), isLoop(),
				stringState, timerState);
	}
}
