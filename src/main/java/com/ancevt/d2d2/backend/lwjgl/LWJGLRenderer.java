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
package com.ancevt.d2d2.backend.lwjgl;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.IContainer;
import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.display.IFramedDisplayObject;
import com.ancevt.d2d2.display.IRenderer;
import com.ancevt.d2d2.display.ISprite;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapCharInfo;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.display.texture.Texture;
import com.ancevt.d2d2.display.texture.TextureAtlas;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.EventPool;
import com.ancevt.d2d2.input.Mouse;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import static java.lang.Math.round;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

public class LWJGLRenderer implements IRenderer {

    private final Stage stage;
    private final LWJGLBackend lwjglBackend;
    boolean smoothMode = false;
    private LWJGLTextureEngine textureEngine;
    private int zOrderCounter;

    public LWJGLRenderer(Stage stage, LWJGLBackend lwjglStarter) {
        this.stage = stage;
        this.lwjglBackend = lwjglStarter;
    }

    @Override
    public void init(long windowId) {
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    @Override
    public void reshape(int width, int height) {
        GL11.glViewport(0, 0, width, height);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluOrtho2D(0, width, height, 0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
    }

    @Override
    public void renderFrame() {
        textureEngine.loadTextureAtlases();

        clear();

        GL11.glLoadIdentity();

        zOrderCounter = 0;

        renderDisplayObject(stage,
            0,
            stage.getX(),
            stage.getY(),
            stage.getScaleX(),
            stage.getScaleY(),
            stage.getRotation(),
            stage.getAlpha());

        IDisplayObject cursor = D2D2.getCursor();
        if (cursor != null) {
            renderDisplayObject(cursor, 0, 0, 0, 1, 1, 0, 1);
        }

        textureEngine.unloadTextureAtlases();

        GLFW.glfwGetCursorPos(lwjglBackend.windowId, mouseX, mouseY);

        Mouse.setXY((int) mouseX[0], (int) mouseY[0]);
    }

    private final double[] mouseX = new double[1];
    private final double[] mouseY = new double[1];

    private void clear() {
        Color backgroundColor = stage.getBackgroundColor();
        float backgroundColorRed = backgroundColor.getR() / 255.0f;
        float backgroundColorGreen = backgroundColor.getG() / 255.0f;
        float backgroundColorBlue = backgroundColor.getB() / 255.0f;
        GL11.glClearColor(backgroundColorRed, backgroundColorGreen, backgroundColorBlue, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    private synchronized void renderDisplayObject(@NotNull IDisplayObject displayObject,
                                                  int level,
                                                  float toX,
                                                  float toY,
                                                  float toScaleX,
                                                  float toScaleY,
                                                  float toRotation,
                                                  float toAlpha) {

        if (!displayObject.isVisible()) return;

        displayObject.dispatchEvent(EventPool.simpleEventSingleton(Event.ENTER_FRAME, displayObject));

        zOrderCounter++;
        displayObject.setAbsoluteZOrderIndex(zOrderCounter);

        float scX = displayObject.getScaleX() * toScaleX;
        float scY = displayObject.getScaleY() * toScaleY;
        float r = displayObject.getRotation() + toRotation;

        float x = toScaleX * displayObject.getX();
        float y = toScaleY * displayObject.getY();

        float a = displayObject.getAlpha() * toAlpha;

        x = round(x);
        y = round(y);

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glRotatef(r, 0, 0, 1);

        if (displayObject instanceof IContainer container) {
            for (int i = 0; i < container.getNumberOfChildren(); i++) {
                renderDisplayObject(container.getChild(i), level + 1, x + toX, y + toY, scX, scY, 0, a);
            }

        } else if (displayObject instanceof ISprite s) {
            renderSprite(s, a, scX, scY, 0);
        } else if (displayObject instanceof BitmapText btx) {
            if (btx.isCacheAsSprite()) {
                renderSprite(btx.cachedSprite(), a, scX, scY, btx.getBitmapFont().getPaddingTop() * scY);
            } else {
                renderBitmapText(btx, a, scX, scY);
            }
        }

        if (displayObject instanceof IFramedDisplayObject f) {
            f.processFrame();
        }

        GL11.glPopMatrix();

        displayObject.onEachFrame();
        displayObject.dispatchEvent(EventPool.simpleEventSingleton(Event.EXIT_FRAME, displayObject));
    }

    private void renderSprite(@NotNull ISprite sprite, float alpha, float scaleX, float scaleY, float paddingTop) {
        Texture texture = sprite.getTexture();

        if (texture == null) return;
        if (texture.getTextureAtlas().isDisposed()) return;

        TextureAtlas textureAtlas = texture.getTextureAtlas();

        //textureParamsHandle();

        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        boolean bindResult = D2D2.getTextureManager().getTextureEngine().bind(textureAtlas);

        if (!bindResult) {
            return;
        }

        D2D2.getTextureManager().getTextureEngine().enable(textureAtlas);

        final Color color = sprite.getColor();

        if (color != null) {
            GL11.glColor4f(
                color.getR() / 255f,
                color.getG() / 255f,
                color.getB() / 255f,
                alpha
            );
        }

        int tX = texture.x();
        int tY = texture.y();
        int tW = texture.width();
        int tH = texture.height();

        float totalW = textureAtlas.getWidth();
        float totalH = textureAtlas.getHeight();

        float x = tX / totalW;
        float y = tY / totalH;
        float w = tW / totalW;
        float h = tH / totalH;

        float repeatX = sprite.getRepeatX();
        float repeatY = sprite.getRepeatY();

        double vertexBleedingFix = sprite.getVertexBleedingFix();
        double textureBleedingFix = sprite.getTextureBleedingFix();

        for (int rY = 0; rY < repeatY; rY++) {
            for (float rX = 0; rX < repeatX; rX++) {
                float px = round(rX * tW * scaleX);
                float py = round(rY * tH * scaleY) + paddingTop;

                double textureTop = y + textureBleedingFix;
                double textureBottom = (h + y) - textureBleedingFix;
                double textureLeft = x + textureBleedingFix;
                double textureRight = (w + x) - textureBleedingFix;

                double vertexTop = py - vertexBleedingFix;
                double vertexBottom = py + tH * scaleY + vertexBleedingFix;
                double vertexLeft = px - vertexBleedingFix;
                double vertexRight = px + tW * scaleX + vertexBleedingFix;// * sprite.getRepeatXF();

                if (repeatX - rX < 1.0) {
                    double val = repeatX - rX;
                    vertexRight = px + tW * val * scaleX + vertexBleedingFix;
                    textureRight *= val;
                }

                if (repeatY - rY < 1.0) {
                    double val = repeatY - rY;
                    vertexBottom = py + tH * val * scaleY + vertexBleedingFix;
                    textureBottom = (h * val + y) - textureBleedingFix;
                }

                GL11.glBegin(GL11.GL_QUADS);

                // L
                GL11.glTexCoord2d(textureLeft, textureBottom);
                GL11.glVertex2d(vertexLeft, vertexBottom);

                // _|
                GL11.glTexCoord2d(textureRight, textureBottom);
                GL11.glVertex2d(vertexRight, vertexBottom);

                // ^|
                GL11.glTexCoord2d(textureRight, textureTop);
                GL11.glVertex2d(vertexRight, vertexTop);

                // Г
                GL11.glTexCoord2d(textureLeft, textureTop);
                GL11.glVertex2d(vertexLeft, vertexTop);

                GL11.glEnd();
            }
        }

        GL11.glDisable(GL_BLEND);
        D2D2.getTextureManager().getTextureEngine().disable(textureAtlas);
    }

    private void renderBitmapText(@NotNull BitmapText bitmapText, float alpha, float scaleX, float scaleY) {
        if (bitmapText.isEmpty()) return;

        Color color = bitmapText.getColor();

        GL11.glColor4f(
            (float) color.getR() / 255f,
            (float) color.getG() / 255f,
            (float) color.getB() / 255f,
            alpha
        );

        BitmapFont bitmapFont = bitmapText.getBitmapFont();
        TextureAtlas textureAtlas = bitmapFont.getTextureAtlas();

        D2D2.getTextureManager().getTextureEngine().enable(textureAtlas);

        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        boolean bindResult = D2D2.getTextureManager().getTextureEngine().bind(textureAtlas);

        if (!bindResult) return;

        GL11.glBegin(GL11.GL_QUADS);

        BitmapTextDrawHelper.draw(bitmapText,
            alpha,
            scaleX,
            scaleY,
            LWJGLRenderer::drawChar,
            LWJGLRenderer::applyColor
        );

        /*
        String text = bitmapText.getText();

        int textureWidth = textureAtlas.getWidth();
        int textureHeight = textureAtlas.getHeight();

        float lineSpacing = bitmapText.getLineSpacing();
        float spacing = bitmapText.getSpacing();

        float boundWidth = bitmapText.getWidth() * scaleX + bitmapFont.getCharInfo('0').width() * 3;
        float boundHeight = bitmapText.getHeight() * scaleY;

        float drawX = 0;
        float drawY = bitmapFont.getPaddingTop() * scaleY;

        double textureBleedingFix = bitmapText.getTextureBleedingFix();
        double vertexBleedingFix = bitmapText.getVertexBleedingFix();

        if (bitmapText.isMulticolorEnabled()) {

            BitmapText.ColorTextData colorTextData = bitmapText.getColorTextData();

            for (int i = 0; i < colorTextData.length(); i++) {
                BitmapText.ColorTextData.Letter letter = colorTextData.getColoredLetter(i);

                char c = letter.getCharacter();

                BitmapCharInfo charInfo = bitmapFont.getCharInfo(c);

                if (charInfo == null) continue;

                Color letterColor = letter.getColor();

                GL11.glColor4f(
                    (float) letterColor.getR() / 255f,
                    (float) letterColor.getG() / 255f,
                    (float) letterColor.getB() / 255f,
                    alpha
                );

                float charWidth = charInfo.width();
                float charHeight = charInfo.height();

                if (c == '\n' || (boundWidth != 0 && drawX >= boundWidth - charWidth * 5)) {
                    drawX = 0;
                    drawY += (charHeight + lineSpacing) * scaleY;

                    if (boundHeight != 0 && drawY > boundHeight - charHeight) {
                        break;
                    }
                }

                drawChar(drawX,
                    (drawY + scaleY * charHeight),
                    textureWidth,
                    textureHeight,
                    charInfo,
                    scaleX,
                    scaleY,
                    textureBleedingFix,
                    vertexBleedingFix);

                drawX += (charWidth + (c != '\n' ? spacing : 0)) * scaleX;
            }

        } else {
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);

                BitmapCharInfo charInfo = bitmapFont.getCharInfo(c);

                if (charInfo == null) {
                    continue;
                }

                float charWidth = charInfo.width();
                float charHeight = charInfo.height();

                if (c == '\n' || (boundWidth != 0 && drawX >= boundWidth - charWidth * 5)) {
                    drawX = 0;
                    drawY += (charHeight + lineSpacing) * scaleY;

                    if (boundHeight != 0 && drawY > boundHeight - charHeight) {
                        break;
                    }
                }

                drawChar(drawX,
                    (drawY + scaleY * charHeight),
                    textureWidth,
                    textureHeight,
                    charInfo,
                    scaleX,
                    scaleY,
                    textureBleedingFix,
                    vertexBleedingFix);

                drawX += (charWidth + (c != '\n' ? spacing : 0)) * scaleX;
            }
        }
        */

        GL11.glEnd();

        GL11.glDisable(GL_BLEND);
        D2D2.getTextureManager().getTextureEngine().disable(textureAtlas);
    }

    private static void applyColor(float r, float g, float b, float a) {
        GL11.glColor4f(r, g, b, a);
    }

    private static float nextHalf(float v) {
        return (float) (Math.ceil(v * 2) / 2);
    }

    private static void drawChar(
        TextureAtlas textureAtlas,
        char c,
        BitmapText.ColorTextData.Letter letter,
        float x,
        float y,
        int textureAtlasWidth,
        int textureAtlasHeight,
        @NotNull BitmapCharInfo charInfo,
        float scX,
        float scY,
        double textureBleedingFix,
        double vertexBleedingFix) {

        //scX = nextHalf(scX);
        scY = nextHalf(scY);

        float charWidth = charInfo.width();
        float charHeight = charInfo.height();

        float xOnTexture = charInfo.x();
        float yOnTexture = charInfo.y() + charHeight;

        float cx = xOnTexture / textureAtlasWidth;
        float cy = -yOnTexture / textureAtlasHeight;
        float cw = charWidth / textureAtlasWidth;
        float ch = -charHeight / textureAtlasHeight;

        GL11.glTexCoord2d(cx, -cy);
        GL11.glVertex2d(x - vertexBleedingFix, y + vertexBleedingFix);

        GL11.glTexCoord2d(cx + cw, -cy);
        GL11.glVertex2d(charWidth * scX + x + vertexBleedingFix, y + vertexBleedingFix);

        GL11.glTexCoord2d(cx + cw, -cy + ch);
        GL11.glVertex2d(charWidth * scX + x + vertexBleedingFix, charHeight * -scY + y - vertexBleedingFix);

        GL11.glTexCoord2d(cx, -cy + ch);
        GL11.glVertex2d(x - vertexBleedingFix, charHeight * -scY + y - vertexBleedingFix);
    }

    public void setLWJGLTextureEngine(LWJGLTextureEngine textureEngine) {
        this.textureEngine = textureEngine;
    }

}
