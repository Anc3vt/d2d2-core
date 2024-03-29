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
import com.ancevt.d2d2.asset.Assets;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.text.BitmapCharInfo;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.display.texture.ITextureEngine;
import com.ancevt.d2d2.display.texture.Texture;
import com.ancevt.d2d2.display.texture.TextureAtlas;
import com.ancevt.d2d2.display.texture.TextureCell;
import com.filters.GaussianFilter;
import com.filters.LensBlurFilter;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class LWJGLTextureEngine implements ITextureEngine {

    private static boolean texturePreprocessingEnabled = false;
    private final TextureLoadQueue loadQueue;
    private final Queue<TextureAtlas> unloadQueue;
    private final TextureMapping mapping;
    private int textureAtlasIdCounter;

    public LWJGLTextureEngine() {
        texturePreprocessingEnabled = "true".equals(System.getProperties().get("d2d2.experimental.bloom"));

        mapping = new TextureMapping();
        loadQueue = new TextureLoadQueue();
        unloadQueue = new LinkedList<>();
    }

    @Override
    public boolean bind(@NotNull TextureAtlas textureAtlas) {
        if (mapping.ids().containsKey(textureAtlas.getId())) {
            glBindTexture(GL_TEXTURE_2D, mapping.ids().get(textureAtlas.getId()));
            return true;
        }
        return false;
    }

    @Override
    public void enable(TextureAtlas textureAtlas) {
        GL30.glEnable(GL_TEXTURE_2D);
    }

    @Override
    public void disable(TextureAtlas textureAtlas) {
        GL30.glDisable(GL_TEXTURE_2D);
    }

    public TextureAtlas createTextureAtlas(InputStream pngInputStream) {
        try {
            BufferedImage bufferedImage = ImageIO.read(pngInputStream);
            TextureAtlas textureAtlas = createTextureAtlasFromBufferedImage(bufferedImage);
            mapping.images().put(textureAtlas.getId(), bufferedImage);
            return textureAtlas;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public TextureAtlas createTextureAtlas(String assetPath) {
        try {
            InputStream pngInputStream = Assets.getAssetAsStream(assetPath);
            return createTextureAtlasFromBufferedImage(ImageIO.read(pngInputStream));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public TextureAtlas createTextureAtlas(int width, int height, TextureCell @NotNull [] cells) {
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = (Graphics2D) image.getGraphics();

        for (final TextureCell cell : cells) {

            if (cell.isPixel()) {

                final java.awt.Color awtColor
                    = new java.awt.Color(
                    cell.getColor().getR(),
                    cell.getColor().getG(),
                    cell.getColor().getB(),
                    cell.getAlpha()
                );

                g.setColor(awtColor);
                g.drawRect(cell.getX(), cell.getY(), 0, 0);
            } else {

                AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, cell.getAlpha());

                g.setComposite(alphaComposite);

                AffineTransform affineTransform = g.getTransform();
                affineTransform.rotate(Math.toRadians(cell.getRotation()), cell.getX(), cell.getY());
                g.setTransform(affineTransform);

                drawCell(g, cell);

                affineTransform.rotate(Math.toRadians(-cell.getRotation()), cell.getX(), cell.getY());
                g.setTransform(affineTransform);
            }
        }

        final TextureAtlas textureAtlas = createTextureAtlasFromBufferedImage(image);
        mapping.images().put(textureAtlas.getId(), image);
        D2D2.getTextureManager().addTexture("_textureAtlas_" + textureAtlas.getId(), textureAtlas.createTexture());
        return textureAtlas;
    }

    public TextureAtlas createTextureAtlasFromBufferedImage(@NotNull BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        if (texturePreprocessingEnabled) {
            GaussianFilter f = new GaussianFilter(1.5f);
            image = f.filter(image, new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
//            var f2 = new GlowFilter();
//            f2.setAmount(0.05f);
//            image = f2.filter(image, new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
            LensBlurFilter f3 = new LensBlurFilter();
            f3.setBloom(1.25f);
            f3.setRadius(0.75f);
            f3.setSides(5);
            f3.setBloomThreshold(2f);
            try {
                image = f3.filter(image, new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
            } catch (Exception c) {
                System.out.println(c.getMessage());
            }
        }

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                byteBuffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                byteBuffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                byteBuffer.put((byte) (pixel & 0xFF));             // Blue component
                byteBuffer.put((byte) ((pixel >> 24) & 0xFF));     // Alpha component. Only for RGBA
            }
        }

        byteBuffer.flip();

        TextureAtlas textureAtlas = createTextureAtlasFromByteBuffer(byteBuffer, width, height);
        mapping.images().put(textureAtlas.getId(), image);
        return textureAtlas;
    }

    private @NotNull TextureAtlas createTextureAtlasFromByteBuffer(ByteBuffer byteBuffer, int width, int height) {
        TextureAtlas textureAtlas = new TextureAtlas(++textureAtlasIdCounter, width, height);
        loadQueue.putLoad(new TextureLoadQueue.LoadTask(textureAtlas, width, height, byteBuffer));
        return textureAtlas;
    }

    public void loadTextureAtlases() {
        while (loadQueue.hasTasks()) {
            TextureLoadQueue.LoadTask loadTask = loadQueue.poll();

            TextureAtlas textureAtlas = loadTask.getTextureAtlas();
            ByteBuffer byteBuffer = loadTask.getByteBuffer();
            int width = loadTask.getWidth();
            int height = loadTask.getHeight();

            int openGlTextureId = glGenTextures();

            mapping.ids().put(textureAtlas.getId(), openGlTextureId);

            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, openGlTextureId);

            // Tell OpenGL how to unpack the RGBA bytes. Each component pngInputStream 1 byte size
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            // Upload the texture data
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBuffer);
            // Generate Mip Map
            glGenerateMipmap(GL_TEXTURE_2D);
        }
    }

    private void drawCell(@NotNull Graphics2D g, final @NotNull TextureCell cell) {
        int x = cell.getX();
        int y = cell.getY();
        float repeatX = cell.getRepeatX();
        float repeatY = cell.getRepeatY();
        float scaleX = cell.getScaleX();
        float scaleY = cell.getScaleY();

        BufferedImage fullImageRegion = textureRegionToImage(cell.getTexture());

        BufferedImage imageRegion = fullImageRegion;

        float texWidth = cell.getTexture().width();
        float texHeight = cell.getTexture().height();

        int originWidth = fullImageRegion.getWidth(null);
        int originHeight = fullImageRegion.getHeight(null);

        for (int rY = 0; rY < repeatY; rY += 1.0f) {
            for (int rX = 0; rX < repeatX; rX += 1.0f) {
                int w = (int) (originWidth * scaleX);
                int h = (int) (originHeight * scaleY);

                imageRegion = fullImageRegion;

                if (repeatX - rX < 1.0f || repeatY - rY < 1.0f) {
                    float valX = 1.0f;
                    float valY = 1.0f;

                    if (repeatX - rX < 1.0f) {
                        valX = repeatX - rX;
                        w *= valX;
                    }

                    if (repeatY - rY < 1.0f) {
                        valY = repeatY - rY;
                        h *= valY;
                    }


                    imageRegion = textureRegionToImage(
                        cell.getTexture().getSubtexture(
                            0,
                            0,
                            (int) (texWidth * valX),
                            (int) (texHeight * valY)
                        )
                    );


                }

                g.drawImage(imageRegion,
                    (int) (x + texWidth * rX * scaleX),
                    (int) (y + texHeight * rY * scaleY),
                    w,
                    h,
                    null
                );

            }
        }

    }

    @Override
    public void unloadTextureAtlas(@NotNull TextureAtlas textureAtlas) {
        mapping.images().remove(textureAtlas.getId());
        // TODO: repair creating new textures after unloading
        if (textureAtlas.isDisposed()) {
            return;
            //throw new IllegalStateException("Texture atlas is already unloaded " + textureAtlas);
        }

        unloadQueue.add(textureAtlas);
    }

    public void unloadTextureAtlases() {
        while (!unloadQueue.isEmpty()) {
            TextureAtlas textureAtlas = unloadQueue.poll();
            glDeleteTextures(mapping.ids().get(textureAtlas.getId()));
            mapping.ids().remove(textureAtlas.getId());
            mapping.images().remove(textureAtlas.getId());
        }
    }

    @Override
    public TextureAtlas bitmapTextToTextureAtlas(@NotNull BitmapText bitmapText) {
        int width = (int) bitmapText.getWidth();
        int height = (int) bitmapText.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        BitmapTextDrawHelper.draw(bitmapText, bitmapText.getAlpha(), bitmapText.getScaleX(), bitmapText.getScaleY(),
            (atlas, c, letter, drawX, drawY, textureAtlasWidth, textureAtlasHeight, charInfo, scX, scY, textureBleedingFix, vertexBleedingFix) -> {

                if (c != '\n') {
                    int charX = charInfo.x();
                    int charY = charInfo.y();

                    if (charY >= 0) {

                        BufferedImage charImage = textureRegionToImage(
                            atlas, charX, charY, charInfo.width(), charInfo.height()
                        );

                        charImage = copyImage(charImage);

                        Color letterColor = letter.getColor();

                        applyColorFilter(
                            charImage,
                            letterColor.getR(),
                            letterColor.getG(),
                            letterColor.getB()
                        );

                        g.drawImage(charImage, (int)drawX, (int)drawY - charInfo.height(), null);
                    }
                }

            },
            null
        );


        final TextureAtlas textureAtlas = createTextureAtlasFromBufferedImage(image);
        D2D2.getTextureManager().addTexture("_textureAtlas_text_" + textureAtlas.getId(), textureAtlas.createTexture());
        return textureAtlas;


//        String text = bitmapText.getText();
//        float spacing = bitmapText.getSpacing();
//        float lineSpacing = bitmapText.getLineSpacing();
//
//        BitmapFont bitmapFont = bitmapText.getBitmapFont();
//
//        float boundWidth = bitmapText.getWidth() * bitmapText.getAbsoluteScaleX() + bitmapFont.getCharInfo('0').width() * 2;
//        float boundHeight = bitmapText.getHeight() * bitmapText.getAbsoluteScaleY();
//
//        TextureAtlas fontTextureAtlas = bitmapFont.getTextureAtlas();
//
//        int drawX = 0;
//        int drawY = 0;
//
//        if (bitmapText.isMulticolorEnabled()) {
//            BitmapText.ColorTextData colorTextData = bitmapText.getColorTextData();
//
//            for (int i = 0; i < colorTextData.length(); i++) {
//                BitmapText.ColorTextData.Letter letter = colorTextData.getColoredLetter(i);
//                char c = letter.getCharacter();
//                BitmapCharInfo charInfo = bitmapFont.getCharInfo(c);
//
//                if (charInfo == null) continue;
//
//                if (bitmapFont.getCharInfo(c) == null) {
//                    continue;
//                }
//
//                com.ancevt.d2d2.display.Color letterColor = letter.getColor();
//
//                float charWidth = charInfo.width();
//                float charHeight = charInfo.height();
//
//                if (c == '\n' || (boundWidth != 0 && drawX >= boundWidth - charWidth * 5 + 5)) {
//                    drawX = 0;
//                    drawY += (charHeight + lineSpacing);
//
//                    if (boundHeight != 0 && drawY > boundHeight - charHeight) {
//                        break;
//                    }
//                }
//
//                if (c != '\n') {
//                    int charX = charInfo.x();
//                    int charY = charInfo.y();
//
//                    if (charY >= 0) {
//
//                        BufferedImage charImage = textureRegionToImage(
//                            fontTextureAtlas, charX, charY, (int) charWidth, (int) charHeight
//                        );
//
//                        charImage = copyImage(charImage);
//
//                        applyColorFilter(
//                            charImage,
//                            letterColor.getR(),
//                            letterColor.getG(),
//                            letterColor.getB()
//                        );
//
//                        g.drawImage(charImage, drawX, drawY, null);
//
//
//                        try {
//                            ImageIO.write(charImage, "PNG", new File("/home/ancevt/tmp/tmp" + i + ".png"));
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//
//
//                    }
//
//                    drawX += charWidth + spacing;
//                }
//            }
//        } else {
//            for (int i = 0; i < text.length(); i++) {
//                char c = text.charAt(i);
//
//                BitmapCharInfo charInfo = bitmapFont.getCharInfo(c);
//
//                if (charInfo == null) {
//                    continue;
//                }
//
//                int charX = charInfo.x();
//                int charY = charInfo.y();
//
//                float charWidth = charInfo.width();
//                float charHeight = charInfo.height();
//
//                if (c == '\n' || (boundWidth != 0 && drawX >= boundWidth - charWidth * 5)) {
//                    drawX = 0;
//                    drawY += (charHeight + lineSpacing);
//
//                    if (boundHeight != 0 && drawY > boundHeight - charHeight) {
//                        break;
//                    }
//                }
//
//                if (c != '\n') {
//                    if (charY > 0) {
//
//                        BufferedImage charImage = textureRegionToImage(
//                            fontTextureAtlas, charX, charY, (int) charWidth, (int) charHeight
//                        );
//
//                        g.drawImage(charImage, drawX, drawY, null);
//                    }
//                }
//
//                drawX += (charWidth + (c != '\n' ? spacing : 0));
//            }
//        }

    }

    public static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    private static void applyColorFilter(BufferedImage image, int redPercent, int greenPercent, int bluePercent) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);

                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                pixel = (alpha << 24) | (redPercent * red / 255 << 16) | (greenPercent * green / 255 << 8) | (bluePercent * blue / 255);

                image.setRGB(x, y, pixel);
            }
        }
    }

    private BufferedImage textureRegionToImage(@NotNull TextureAtlas textureAtlas, int x, int y, int width, int height) {
        BufferedImage bufferedImage = mapping.images().get(textureAtlas.getId());

//        try {
//            ImageIO.write(bufferedImage, "PNG", new File("/home/ancevt/tmp/tmp.png"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        return bufferedImage.getSubimage(x, y, width, height);
    }

    private BufferedImage textureRegionToImage(@NotNull Texture texture) {
        return textureRegionToImage(
            texture.getTextureAtlas(),
            texture.x(),
            texture.y(),
            texture.width(),
            texture.height()
        );
    }
}
