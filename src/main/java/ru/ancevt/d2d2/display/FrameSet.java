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
package ru.ancevt.d2d2.display;

/**
 * Экземпляр данного класса представляет собой анимацию. Фактически это
 * контейнер, автоматический переключающий появления экранных объектов с
 * заданным интервалом и в строгой последовательности. Воспроизведение анимации
 * регулируется.
 * 
 * @author ancevt
 *
 */
public class FrameSet extends DisplayObjectContainer {

	public static final int DEFAULT_SLOWING = 5;

	private DisplayObject[] frames;

	private boolean playing;
	private boolean loop;
	private int slowing;
	private int slowingCounter;
	private int currentFrameNum;
	private DisplayObject currentFrame;

	/**
	 * Конструктор. Принимает массив экранных объектов-кадров для воспроизведения
	 * анимации.
	 * 
	 * @param frames массив экранных объектов кадров для воспроизведения
	 */
	public FrameSet(final DisplayObject[] frames) {
		this();
		setFrames(frames);
	}

	/**
	 * Конструктор.
	 */
	public FrameSet() {
		frames = new DisplayObject[0];
		slowingCounter = 0;
		setLoop(false);
		stop();
		setSlowing(DEFAULT_SLOWING);
		setName("frameSet" + displayObjectId());
	}

	/**
	 * Задает массив с кадрами (экранными объектами) для воспроизведения анимации.
	 * 
	 * @param frames массив с кадрами (экранными объектами) для воспроизведения
	 *               анимации
	 */
	public void setFrames(DisplayObject[] frames) {
		this.frames = frames;
		if (frames.length > 0)
			goToFrame(0);
	}

	/**
	 * Возвращает массив с кадрами (экранными объектами) для воспроизводимыми в
	 * анимации.
	 * 
	 * @return массив с кадрами (экранными объектами) для воспроизводимыми в
	 *         анимации
	 */
	public DisplayObject[] getFrames() {
		return frames;
	}

	public void processFrame() {
		if (!playing)
			return;

		slowingCounter++;
		if (slowingCounter >= slowing) {
			slowingCounter = 0;
			nextFrame();
		}
		
	}

	/**
	 * Включает или выключает закциливание анимации. Если зацикливание отключено то
	 * анимация проигрывается до последнего кадра.
	 * 
	 * @param b true - включает зацикливание, false - отключает
	 */
	public void setLoop(boolean b) {
		this.loop = b;
	}

	/**
	 * Проверяет, зациклена ли анимация.
	 * 
	 * @return true - анимация зациклена, иначе - false
	 */
	public boolean isLoop() {
		return loop;
	}

	/**
	 * Устанавливает замедление анимации. Фактически, при воспроизведении анимации
	 * каждый кадр будет повторяться заданное число раз. Визуально это выглядит как
	 * замедление.
	 * 
	 * @param slowing замедление анимации
	 */
	public void setSlowing(int slowing) {
		this.slowing = slowing;
	}

	/**
	 * Возвращает замедление анимации.
	 * 
	 * @return замедление анимации
	 */
	public int getSlowing() {
		return slowing;
	}

	/**
	 * Включает следующий кадр
	 */
	public void nextFrame() {
		goToFrame(++currentFrameNum);
		if (currentFrameNum >= frames.length) {
		}
		drawCurrentFrame();
	}

	/**
	 * Включает предыдущий кадр
	 */
	public void prevFrame() {
		currentFrameNum--;
		if (currentFrameNum < 0)
			currentFrameNum = 0;
		drawCurrentFrame();
	}

	/**
	 * Включает заданный кадр по его номеру. Отсчет кадров начинается с 0
	 * 
	 * @param frameNumber номер кадра, к которому следует перейти
	 */
	public void goToFrame(int frameNumber) {
		currentFrameNum = frameNumber;
		
		if (currentFrameNum >= frames.length) {
			if (loop) {
				currentFrameNum = 0;
				play();
			} else {
				currentFrameNum--;
				stop();
				onAnimationComplete();
			}
		}

		drawCurrentFrame();
	}

	/**
	 * Включает заданный кадр..
	 * 
	 * @param frame экранный-объект кадр к которому следует перейти
	 */
	public void goToFrame(final DisplayObject frame) {
		for (int i = 0; i < frames.length; i++) {
			if (frames[i] == frame) {
				goToFrame(i);
				return;
			}
		}
	}

	private void drawCurrentFrame() {
		if(currentFrame != null && currentFrame.getParent() != null) {
			currentFrame.removeFromParent();
		}
		
		currentFrame = getFrame(currentFrameNum);
		if (currentFrame != null) {
			this.add(currentFrame);
		}
	}

	/**
	 * Вставляет кадр в конец анимации. Осторожно; слишком частое обращение к
	 * данному методу приводит к неоправданному расходу оперативной памяти,
	 * поскольку вызов данного метода создает новый массив кадров. Предпочтительней
	 * задавать требуемый набор кадров сразу, при инициализации экземпляра данного
	 * класса.
	 * 
	 * @param frame вставляемый в конец кадр
	 */
	public void insertFrame(final DisplayObject frame) {

		final boolean empty = frames.length == 0;

		final DisplayObject[] newFrames = new DisplayObject[frames.length + 1];

		for (int i = 0; i < frames.length; i++) {
			newFrames[i] = frames[i];
		}

		newFrames[newFrames.length - 1] = frame;

		frames = newFrames;

		if (empty) {
			goToFrame(0);
		}
	}

	/**
	 * Проверяет, проигрывается ли анимация в данный момент
	 * 
	 * @return true - проигрывается, иначе - false
	 */
	public boolean isPlaying() {
		return playing;
	}

	/**
	 * Воспроизвести анимацию
	 */
	public void play() {
		playing = true;
	}

	/**
	 * Приостановить анимацию
	 */
	public void stop() {
		playing = false;
	}

	/**
	 * Возвращает экранный объект-кадр по номеру кадра. Отсчет кадров ведется с 0
	 * 
	 * @param frameIndex номер кадра.
	 * @return требуемый кадр
	 */
	public DisplayObject getFrame(final int frameIndex) {
		if (frameIndex >= frames.length)
			return null;
		return frames[frameIndex];
	}

	/**
	 * Возвращает количество кадров в анимации
	 * 
	 * @return количество кадров в анимации
	 */
	public int getFramesCount() {
		return frames.length;
	}

	/**
	 * Возвращает номер текущего кадра в анимации
	 * 
	 * @return номер текущего кадра в анимации
	 */
	public int getCurrentFrameNum() {
		return currentFrameNum;
	}

	/**
	 * Возвращает текущий кадр
	 * 
	 * @return текущий кадр
	 */
	public DisplayObject getCurrentFrame() {
		return frames[currentFrameNum];
	}

	/**
	 * Очищает анимацию
	 */
	public final void clearFrames() {
		for (int i = 0; i < frames.length; i++)
			frames[i] = null;
	}

	/**
	 * Событие, выполняемое при окончании проигрывания анимации
	 */
	public void onAnimationComplete() {
	}

	/**
	 * Возвращает список кадров в виде строки. Отладочный метод.
	 * 
	 * @return текущий список кадров в виде строки
	 */
	public final String getFrameListString() {
		final StringBuilder sb = new StringBuilder();

		for (DisplayObject frame : frames) {
			sb.append(frame + "\n");
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		return String.format("FrameSet[name: %s, frames: %d, playing: %b, loop: %b]", getName(), getFramesCount(),
				isPlaying(), loop);
	}
}
