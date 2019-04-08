package ru.ancevt.d2d2.display;

public interface IRenderer {
	void setStage(Stage stage);
	Stage getStage();
	
	void setFpsDelay(int value);
	int getFpsDelay();
	
	Object glProfile();
	
	int getWidth();
	int getHeight();
}
