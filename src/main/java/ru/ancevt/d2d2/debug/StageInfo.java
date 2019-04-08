package ru.ancevt.d2d2.debug;

import ru.ancevt.d2d2.display.Stage;
import ru.ancevt.d2d2.display.text.BitmapFont;
import ru.ancevt.d2d2.display.text.BitmapText;

public class StageInfo extends BitmapText {

	public StageInfo(BitmapFont bitmapFont) {
		super(bitmapFont);
	}
	
	public StageInfo() {
		this(BitmapFont.getDefaultBitmapFont());
	}

	@Override
	public void onEachFrame() {
		setText(getInfo());
		super.onEachFrame();
	}
	
	public final String getInfo() {
		final Stage s = getStage();
		
		final int stageWidth = (int)s.getWidth();
		final int stageHeight = (int)s.getHeight();
		final int scaleMode = s.getScaleMode();
		final int align = s.getAlign();
		
		return 	"Stage " + stageWidth + "x" + stageHeight + "\n" +
				"scaleMode: " + scaleMode + "\n" +
				"align: " + align;
	
		
	}
}
