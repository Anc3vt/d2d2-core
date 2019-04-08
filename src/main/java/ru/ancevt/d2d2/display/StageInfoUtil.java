package ru.ancevt.d2d2.display;

public class StageInfoUtil {
	
	public static final String getScaleModeName(final int scaleMode) {
		switch (scaleMode) {
			case Stage.SCALE_MODE_AUTO: 	return "SCALE_MODE_AUTO(" + Stage.SCALE_MODE_AUTO + ")";
			case Stage.SCALE_MODE_EXTENDED: return "SCALE_MODE_EXTENDED(" + Stage.SCALE_MODE_EXTENDED + ")";
			case Stage.SCALE_MODE_FIT: 		return "SCALE_MODE_FIT(" + Stage.SCALE_MODE_FIT + ")";
			case Stage.SCALE_MODE_OUTFIT: 	return "SCALE_MODE_OUTFIT(" + Stage.SCALE_MODE_OUTFIT + ")";
			case Stage.SCALE_MODE_REAL: 	return "SCALE_MODE_REAL(" + Stage.SCALE_MODE_REAL + ")";
			default:
				return "unknown scale mode(" + scaleMode + ")";
		}
	}
	
	public static final String getAlignName(final int align) {
		switch(align) {
			case Stage.ALIGN_BOTTOM      : return "ALIGN_BOTTOM(" + Stage.ALIGN_BOTTOM + ")"; 
			case Stage.ALIGN_BOTTOM_LEFT : return "ALIGN_BOTTOM_LEFT(" + Stage.ALIGN_BOTTOM_LEFT + ")"; 
			case Stage.ALIGN_BOTTOM_RIGHT: return "ALIGN_BOTTOM_RIGHT(" + Stage.ALIGN_BOTTOM_RIGHT + ")"; 
			case Stage.ALIGN_LEFT 		 : return "ALIGN_LEFT(" + Stage.ALIGN_LEFT + ")"; 
			case Stage.ALIGN_RIGHT 	     : return "ALIGN_RIGHT(" + Stage.ALIGN_RIGHT + ")";
			case Stage.ALIGN_TOP	     : return "ALIGN_TOP(" + Stage.ALIGN_TOP + ")"; 
			case Stage.ALIGN_TOP_LEFT 	 : return "ALIGN_TOP_LEFT(" + Stage.ALIGN_TOP_LEFT + ")"; 
			case Stage.ALIGN_TOP_RIGHT	 : return "ALIGN_TOP_RIGHT(" + Stage.ALIGN_TOP_RIGHT + ")"; 
			
			default:
				return "unknown align(" + align + ")";
		}
	}
}