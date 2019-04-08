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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import ru.ancevt.d2d2.io.Assets;

/**
 * Экземпляр данного класса представляет собой звук. Предоставляет возможность
 * воспроизведения, зацикливания, выключения во время проигрывания.
 * 
 * @author ancevt
 *
 */
public class Sound {

	private static boolean enabled = true;

	private boolean loop;

	/**
	 * Воспроизводит заданный звук
	 * 
	 * @param sound
	 */
	@Deprecated
	public static void playSound(final Sound sound) {
		sound.play();
	}

	private String filePath;
	private boolean playing;
	private Player player;

	/**
	 * Конструктор
	 */
	public Sound() {
	}

	/**
	 * Конструктор.
	 * 
	 * @param filePath
	 *            путь к файлу ресурса звука в формате MP3 из каталога ресурсов
	 *            проекта (assets)
	 */
	public Sound(final String filePath) {
		setFilePath(filePath);
	}

	/**
	 * Возвращает путь к файлу ресурса звука в формате MP3 из каталога ресурсов
	 * проекта (assets)
	 * 
	 * @return путь к файлу ресурса звука в формате MP3 из каталога ресурсов
	 *         проекта (assets) в виде строки
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Задает путь к файлу ресурса звука в формате MP3 из каталога ресурсов
	 * проекта (assets)
	 * 
	 * @param filePath
	 *            путь к файлу ресурса звука в формате MP3 из каталога ресурсов
	 *            проекта (assets) в виде строки
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;

		final File file = Assets.getFile(filePath);
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			player = new Player(fis);
			
		} catch (FileNotFoundException | JavaLayerException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Воспроизводит звук
	 */
	public void play() {
		if (!isEnabled() || isPlaying())
			return;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				setPlaying(true);
				try {
					do {
						player.play();
						player.close();

						setFilePath(getFilePath());

					} while (loop && playing);

				} catch (JavaLayerException e) {
					e.printStackTrace();
				}
				setPlaying(false);
			}
		});

		thread.start();
	}

	/**
	 * Проверяет разрешено ли воспроизведение звука в приложении
	 * 
	 * @return true - разрешено, иначе - false
	 */
	public static final boolean isEnabled() {
		return enabled;
	}

	/**
	 * выв Разрешает или запрещает воспроизведение звука в приложении
	 * 
	 * @param enabled
	 *            true - разрешает, false - запрещает
	 */
	public static final void setEnabled(final boolean enabled) {
		Sound.enabled = enabled;
	}

	/**
	 * Проверяет, является ли звук зацикленным
	 * 
	 * @return true - зациклен, иначе - false
	 */
	public boolean isLoop() {
		return loop;
	}

	/**
	 * Включает или выключает зацикливание звука
	 * 
	 * @param loop
	 *            true - включает, false - выключает
	 */
	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	boolean isPlaying() {
		return playing;
	}

	private void setPlaying(boolean playing) {
		this.playing = playing;
	}

	/**
	 * Останавливает воспроизведение звука
	 */
	public void stop() {
		setPlaying(false);
		player.close();

		setFilePath(getFilePath());
	}

	/**
	 * Приостанавливает все звуки. В текущей версии работает только на мобильных
	 * платформах. Данный метод следует вызывать в теле метода onPause
	 * активности.
	 */
	public static final void pause() {

	}

	/**
	 * Восстанавливает воспроизведение приостановленных при вызове метода pause
	 * все звуки. В текущей версии работает только на мобильных платформах.
	 * Данный метод следует вызывать в теле метода onResume активности.
	 */
	public static final void resume() {

	}
	
	@Override
	public String toString() {
		return String.format("Sound[%s]", getFilePath());
	}

}
