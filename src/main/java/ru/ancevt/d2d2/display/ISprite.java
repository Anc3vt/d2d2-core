package ru.ancevt.d2d2.display;

import ru.ancevt.d2d2.common.IDisposable;
import ru.ancevt.d2d2.display.texture.Texture;

interface ISprite extends IDisplayObject, IColored, IDisposable, IRepeatable {
	Texture getTexture();

	void setTexture(Texture value);
	void setTexture(String textureKey);
	
}
