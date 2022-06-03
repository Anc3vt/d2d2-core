/**
 * Copyright (C) 2022 the original author or authors.
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
package com.ancevt.d2d2.interactive;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.debug.StarletSpace;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.texture.Texture;
import com.ancevt.d2d2.event.InputEvent;

public class Combined9Sprites extends InteractiveContainer {

    private final Sprite topLeft;
    private final Sprite top;
    private final Sprite topRight;
    private final Sprite left;
    private final Sprite center;
    private final Sprite right;
    private final Sprite bottomLeft;
    private final Sprite bottom;
    private final Sprite bottomRight;

    private boolean repeatsEnabled;

    public Combined9Sprites() {
        topLeft = new Sprite();
        top = new Sprite();
        topRight = new Sprite();
        left = new Sprite();
        center = new Sprite();
        right = new Sprite();
        bottomLeft = new Sprite();
        bottom = new Sprite();
        bottomRight = new Sprite();

        add(topLeft);
        add(top);
        add(topRight);
        add(left);
        add(center);
        add(right);
        add(bottomLeft);
        add(bottom);
        add(bottomRight);

        setEnabled(false);
    }

    public Combined9Sprites(Texture topLeftTexture,
                            Texture topTexture,
                            Texture topRightTexture,
                            Texture leftTexture,
                            Texture centerTexture,
                            Texture rightTexture,
                            Texture bottomLeftTexture,
                            Texture bottomTexture,
                            Texture bottomRightTexture) {
        this();
        setTextures(topLeftTexture, topTexture, topRightTexture,
                leftTexture, centerTexture, rightTexture,
                bottomLeftTexture, bottomTexture, bottomRightTexture);

    }

    public void setRepeatsEnabled(boolean repeatsEnabled) {
        this.repeatsEnabled = repeatsEnabled;
        rebuild();
    }

    public boolean isRepeatsEnabled() {
        return repeatsEnabled;
    }

    public void setTextures(Texture topLeftTexture,
                            Texture topTexture,
                            Texture topRightTexture,
                            Texture leftTexture,
                            Texture centerTexture,
                            Texture rightTexture,
                            Texture bottomLeftTexture,
                            Texture bottomTexture,
                            Texture bottomRightTexture) {

        topLeft.setTexture(topLeftTexture);
        top.setTexture(topTexture);
        topRight.setTexture(topRightTexture);
        left.setTexture(leftTexture);
        center.setTexture(centerTexture);
        right.setTexture(rightTexture);
        bottomLeft.setTexture(bottomLeftTexture);
        bottom.setTexture(bottomTexture);
        bottomRight.setTexture(bottomRightTexture);
        rebuild();
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        rebuild();
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        rebuild();
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        rebuild();
    }

    private void rebuild() {
        topRight.setXY(getWidth() - topRight.getWidth(), 0);
        bottomLeft.setXY(0, getHeight() - bottomLeft.getHeight());
        bottomRight.setXY(getWidth() - bottomRight.getWidth(), getHeight() - bottomRight.getHeight());
        top.setX(topLeft.getWidth());
        bottom.setXY(bottomLeft.getWidth(), getHeight() - bottom.getHeight());
        left.setY(topLeft.getHeight());
        right.setXY(getWidth() - right.getWidth(), topRight.getHeight());
        center.setXY(topLeft.getWidth(), topLeft.getHeight());

        if (repeatsEnabled) {
            int textureLength;
            int targetRepeat;

            textureLength = top.getTexture().width();
            targetRepeat = (int) (getWidth() - topLeft.getWidth() - topRight.getWidth());
            targetRepeat /= textureLength;
            top.setScale(1.0f, 1.0f);
            top.setRepeatX(targetRepeat);

            textureLength = bottom.getTexture().width();
            targetRepeat = (int) (getWidth() - bottomLeft.getWidth() - bottomRight.getWidth());
            targetRepeat /= textureLength;
            bottom.setScale(1.0f, 1.0f);
            bottom.setRepeatX(targetRepeat);

            textureLength = left.getTexture().height();
            targetRepeat = (int) (getHeight() - topLeft.getHeight() - bottomLeft.getHeight());
            targetRepeat /= textureLength;
            left.setScale(1.0f, 1.0f);
            left.setRepeatY(targetRepeat);

            textureLength = right.getTexture().height();
            targetRepeat = (int) (getHeight() - topRight.getHeight() - bottomRight.getHeight());
            targetRepeat /= textureLength;
            right.setScale(1.0f, 1.0f);
            right.setRepeatY(targetRepeat);

            int centerTextureWidth = center.getTexture().width();
            int centerRepeatX = (int) (getWidth() - left.getWidth() - right.getWidth());
            centerRepeatX /= centerTextureWidth;

            int centerTextureHeight = center.getTexture().height();
            int centerREpeatY = (int) (getHeight() - top.getHeight() - bottom.getHeight());
            centerREpeatY /= centerTextureHeight;

            center.setScale(1.0f, 1.0f);
            center.setRepeat(centerRepeatX, centerREpeatY);

        } else {
            float textureLength;
            float targetScale;

            textureLength = top.getTexture().width();
            targetScale = getWidth() - topLeft.getWidth() - topRight.getWidth();
            targetScale /= textureLength;
            top.setRepeat(1, 1);
            top.setScaleX(targetScale);

            textureLength = bottom.getTexture().width();
            targetScale = getWidth() - bottomLeft.getWidth() - bottomRight.getWidth();
            targetScale /= textureLength;
            bottom.setRepeat(1, 1);
            bottom.setScaleX(targetScale);

            textureLength = left.getTexture().height();
            targetScale = getHeight() - topLeft.getHeight() - bottomLeft.getHeight();
            targetScale /= textureLength;
            left.setRepeat(1, 1);
            left.setScaleY(targetScale);

            textureLength = right.getTexture().height();
            targetScale = getHeight() - topRight.getHeight() - bottomRight.getHeight();
            targetScale /= textureLength;
            right.setRepeat(1, 1);
            right.setScaleY(targetScale);

            float centerTextureWidth = center.getTexture().width();
            float centerScaleX = getWidth() - left.getWidth() - right.getWidth();
            centerScaleX /= centerTextureWidth;

            float centerTextureHeight = center.getTexture().height();
            float centerScaleY = getHeight() - top.getHeight() - bottom.getHeight();
            centerScaleY /= centerTextureHeight;
            center.setRepeat(1, 1);
            center.setScale(centerScaleX, centerScaleY);
        }
    }

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        StarletSpace.haveFun();


        var tm = D2D2.getTextureManager();

        Combined9Sprites combined9Sprites = new Combined9Sprites(
                tm.getTexture("d2d2-demo-combined-9-sprites-top-left"),
                tm.getTexture("d2d2-demo-combined-9-sprites-top"),
                tm.getTexture("d2d2-demo-combined-9-sprites-top-right"),
                tm.getTexture("d2d2-demo-combined-9-sprites-left"),
                tm.getTexture("d2d2-demo-combined-9-sprites-center"),
                tm.getTexture("d2d2-demo-combined-9-sprites-right"),
                tm.getTexture("d2d2-demo-combined-9-sprites-bottom-left"),
                tm.getTexture("d2d2-demo-combined-9-sprites-bottom"),
                tm.getTexture("d2d2-demo-combined-9-sprites-bottom-right")
        );

        combined9Sprites.setRepeatsEnabled(false);
        combined9Sprites.setSize(100, 100);

        combined9Sprites.setScale(3,3);

        stage.addEventListener(InputEvent.MOUSE_MOVE, event -> {
            var e = (InputEvent) event;

            combined9Sprites.setSize(
                    (e.getX() - combined9Sprites.getX()) / combined9Sprites.getAbsoluteScaleX(),
                    (e.getY() - combined9Sprites.getY()) / combined9Sprites.getAbsoluteScaleY()
            );
        });

        stage.add(combined9Sprites, 100, 300);

        stage.add(new FpsMeter());
        D2D2.loop();
    }
}


































