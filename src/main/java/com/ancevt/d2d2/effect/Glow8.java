/**
 * Copyright (C) 2024 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ancevt.d2d2.effect;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.StarletSpace;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.display.IColored;
import com.ancevt.d2d2.display.ISprite;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapFontManager;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.display.text.StandardBitmapFonts;

public class Glow8 extends Container {

    private final IColored[] elements;

    public Glow8(IColored source, Color color, float distance, float alpha, float offsetX, float offsetY) {
        float[][] coords = {
                {0.0f, -1.0f},
                {1.0f, -1.0f},
                {1.0f, 0.0f},
                {1.0f, 1.0f},
                {0.0f, 1.0f},
                {-1.0f, 1.0f},
                {-1.0f, 0.0f},
                {-1.0f, -1.0f}
        };

        elements = new IColored[8];
        for (int i = 0; i < coords.length; i++) {
            float[] currentCoords = coords[i];

            IColored clone;

            if (source instanceof BitmapText bitmapText) {
                clone = bitmapText.cloneBitmapText();
            } else if (source instanceof ISprite sprite) {
                clone = sprite.cloneSprite();
            } else {
                throw new UnsupportedOperationException("Can't glow8 display object type: " + source.getClass().getName());
            }

            clone.setXY(currentCoords[0] * distance, currentCoords[1] * distance);
            clone.move(offsetX, offsetY);
            clone.setColor(color);
            clone.setAlpha(alpha);
            elements[i] = clone;

            add(clone);
        }

    }


    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        StarletSpace.haveFun();
        stage.setBackgroundColor(Color.WHITE);

        BitmapText bitmapText = new BitmapText(BitmapFontManager.getInstance().loadBitmapFont(StandardBitmapFonts.OPEN_SANS_28));
        bitmapText.setAutosize(true);

        bitmapText.setText("#<FFFF00>This <FFFFFF>is a text i have no imagination");
        bitmapText.setMulticolorEnabled(true);
        bitmapText.setCacheAsSprite(true);
        System.out.println(bitmapText.getWidth());

        Glow8 glow8 = new Glow8(bitmapText, Color.BLACK, 1, 1f, 1, 1);

        stage.add(glow8, 100, 250);
        stage.add(bitmapText, 100, 250);


        D2D2.loop();
    }
}
