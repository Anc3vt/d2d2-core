package ru.ancevt.d2d2.display;

public interface IRepeatable {
	void setRepeat(int repeatX, int repeatY);

	void setRepeatX(int value);

	void setRepeatY(int value);

	int getRepeatX();

	int getRepeatY();
}
