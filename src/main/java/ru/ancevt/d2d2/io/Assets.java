package ru.ancevt.d2d2.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import ru.ancevt.d2d2.exception.AssetNotFoundException;
import ru.ancevt.d2d2.platform.Platform;

public class Assets {

	public static final boolean hasFile(final String assetFilePath) {
		final ClassLoader classLoader = Assets.class.getClassLoader();
		final URL url = classLoader.getResource(Platform.ASSETS_DIRECTORY_PATH + assetFilePath);
		
		if(url == null) return false;
		
		final File file = new File(url.getFile());
		
		return file.exists(); 
	}
	
	public static final File getFile(final String assetFilePath) {
		final ClassLoader classLoader = Assets.class.getClassLoader();
		final URL url = classLoader.getResource(Platform.ASSETS_DIRECTORY_PATH + assetFilePath);
		
		if(url == null) throw new AssetNotFoundException("asset " + assetFilePath + " not found");
		
		final File file = new File(url.getFile());
		
		if(!file.exists()) throw new AssetNotFoundException("asset " + assetFilePath + " not found");
		
		return file;
	}

	public static final String readFileAsString(final String assetFilePath) throws IOException {
		return readFileAsString(assetFilePath, StandardCharsets.UTF_8.name());
	}

	public static final String readFileAsString(final String assetFilePath, final String charset) throws IOException {
		final StringBuilder stringBuilder = new StringBuilder();
		final BufferedReader bufferedReader = readFile(assetFilePath);

		final String endOfLine = String.format("%n");

		String line;
		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(endOfLine);
		}

		bufferedReader.close();

		return stringBuilder.toString();
	}

	public static final BufferedReader readFile(final File file) throws FileNotFoundException {
		try {
			return readFile(file, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			System.err.println("ERROR: UTF-8 not supported");
			e.printStackTrace();
		}
		return null;
	}

	public static final BufferedReader readFile(final File file, final String charsetName)
			throws UnsupportedEncodingException, FileNotFoundException {
		final BufferedReader result = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));

		return result;
	}

	public static final BufferedReader readFile(final String assetFilePath, final String charsetName)
			throws UnsupportedEncodingException, FileNotFoundException {
		return readFile(getFile(assetFilePath), charsetName);
	}

	public static final BufferedReader readFile(final String assetFilePath) throws FileNotFoundException {
		return readFile(getFile(assetFilePath));
	}
	
	public static final InputStream readFileAsInputStream(String assetFilePath) throws FileNotFoundException {
		return new FileInputStream(assetFilePath);
	}
}
