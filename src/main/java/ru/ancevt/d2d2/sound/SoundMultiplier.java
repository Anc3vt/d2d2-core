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
package ru.ancevt.d2d2.sound;

/**
 * Данный класс является средством для "разможноежния" экземпляров класса Sound
 * и служит решением проблемы невозможности воспроизводить один звук многократно
 * одновременно в рамках одного экземпляра класса Sound. Звук, проигрываемый при
 * помощи Sound не может быть повторно воспроизведен до того момента, пока до
 * конца не воспроизведется после предыдущего вызова метода play() Данный класс
 * (SoundMultiplier) создает несколько экземпляров класса Sound и "прокручивает"
 * их при вызове метода next(). Каждый вызов метода next() возвращает следующий
 * экземпляр класса Sound, и таким образом звук может воспроизводиться в тот
 * момент когда он уже воспроизводится ("наслаиваться").
 * 
 * @author ancevt
 * @see Sound
 */
public class SoundMultiplier {
	public static final int DEFAULT_BUFFER_SIZE = 4;

	private Sound[] sounds;
	private int currentIndex;

	/**
	 * Конструктор.
	 * 
	 * @param filePath
	 *            путь к файлу ресурса звука в формате MP3 из каталога ресурсов
	 *            проекта (assets)
	 */
	public SoundMultiplier(final String filePath) {
		this(filePath, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * Конструктор.
	 * 
	 * @param filePath
	 *            путь к файлу ресурса звука в формате MP3 из каталога ресурсов
	 *            проекта (assets)
	 * @param count
	 *            максимальное количество экземпляров класса Sound. Данный звук
	 *            может быть воспроизведен это число раз одновременно.
	 */
	public SoundMultiplier(final String filePath, final int count) {
		sounds = new Sound[count];
		for (int i = 0; i < sounds.length; i++)
			sounds[i] = new Sound(filePath);
	}

	/**
	 * Возвращает следующий экземпляр класса Sound для воспроизведения.
	 * 
	 * @return следующий экземпляр класса Sound для воспроизведения.
	 */
	public Sound next() {
		final Sound sound = sounds[currentIndex];

		currentIndex++;
		if (currentIndex >= sounds.length) {
			currentIndex = 0;
		}

		return sound;
	}

	@Override
	public String toString() {
		return String.format("SoundMultiplier[%s, size: %d]", sounds[0].getFilePath(), sounds.length);
	}
}
