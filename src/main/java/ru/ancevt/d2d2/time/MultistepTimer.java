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
 * Экземпляр данного класса является многошаговым асинхронным таймером.
 * Принимает целочесленный массив задержек. Таймер последовательно
 * инициализирует событие onTimerStep(int step) слушателя, пепедавая в метод
 * номер шага (начиная с 0) столько раз, сколько в исходном массиве задержек
 * элементов. Задержки между вызовами события слушателя берутся из исходного
 * массива задержек поочередно. Не смотря на то, что ожидание таймера
 * производится асинхронно в параллельном потоке, вызов событий слушателей
 * выполняется в основном потоке D2D2.
 * 
 * @author ancevt
 * @see Timer
 * @see MultistepTimerListener
 */
public class MultistepTimer extends Timer implements MultistepTimerListener {

	private int[] steps;
	private int currentStep;
	private boolean multistepLoop;

	/**
	 * Конструктор.
	 * 
	 * @param steps
	 *            массив задержек для каждого шага таймера. Из данного массива
	 *            поочередно берутся значения и используются в качестве задержки
	 *            для каждого шага.
	 */
	public MultistepTimer(int[] steps) {
		super(steps[0]);
		setSteps(steps);
	}

	/**
	 * Конструктор.
	 * 
	 * @param steps
	 *            массив задержек для каждого шага таймера. Из данного массива
	 *            поочередно берутся значения и используются в качестве задержки
	 *            для каждого шага.
	 * @param listener
	 *            слушатель событий многошагового таймера
	 */
	public MultistepTimer(int[] steps, MultistepTimerListener listener) {
		super(steps[0], listener);
		setSteps(steps);
	}

	@Override
	protected void apply() {

		try {
			timerState = STATE_RUN;
			multistepApply();
			currentStep++;
			setDelay(steps[currentStep]);
			start();
		} catch (IndexOutOfBoundsException ex) {
			if (currentStep == steps.length && isMultistepLoop()) {
				currentStep = 0;
				setDelay(steps[currentStep]);
				start();
			}
		}

		getListener().onTimerTick();
	}

	private void multistepApply() {
		((MultistepTimerListener) getListener()).onTimerStep(currentStep);
	}

	/**
	 * Возвращает массив задержек между шагами многошагового таймера.
	 * 
	 * @return массив задержек, используемый многошаговым таймером.
	 */
	public int[] getSteps() {
		return steps;
	}

	/**
	 * Задает массив задержек между шагами многошагового таймера.
	 * 
	 * @param steps
	 *            массив задержек
	 */
	public void setSteps(int[] steps) {
		setDelay(steps[0]);
		this.steps = steps;
	}

	/**
	 * Возвращает номер текущего шага многошагового таймера.
	 * 
	 * @return номер текущего шага начиная с 0
	 */
	public int getCurrentStep() {
		return currentStep;
	}

	/**
	 * Устанавливает текущий шаг многошагового таймера.
	 * 
	 * @param currentStep
	 *            номер шага начиная с 0
	 */
	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}

	@Override
	@Deprecated
	public void setLoop(boolean loop) {
		super.setLoop(false);
	}

	@Override
	@Deprecated
	public boolean isLoop() {
		return false;
	}

	@Override
	public void onTimerStep(int step) {
	}

	/**
	 * Проверяет зациклен ли многошаговой таймер.
	 * 
	 * @return true - многошаговой таймер зациклен, иначе - false
	 */
	public boolean isMultistepLoop() {
		return multistepLoop;
	}

	/**
	 * Включает/выключает зацикленность многошагового таймера
	 * 
	 * @param multistepLoop
	 *            true - включает зацикленность, false - выключает
	 */
	public void setMultistepLoop(boolean multistepLoop) {
		this.multistepLoop = multistepLoop;
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

		return String.format("Timer[%d, steps: %d, multistepLooped: %b, state: %s(%d)]", getId(), steps.length,
				isMultistepLoop(), stringState, getState());
	}
}
