package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.common.BorderedRect;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.event.InteractiveEvent;
import com.ancevt.d2d2.interactive.InteractiveManager;
import com.ancevt.d2d2.interactive.InteractiveSprite;

public class Tests_InteractiveSprite {

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));

        InteractiveManager.getInstance().setTabbingEnabled(true);

        Container cont = new Container();

        for(int i = 1; i <= 5; i ++){
            InteractiveSprite interactiveSprite = new InteractiveSprite("satellite");

            final int _i = i;
            interactiveSprite.addEventListener(InteractiveEvent.HOVER, event -> interactiveSprite.setColor(Color.YELLOW));
            interactiveSprite.addEventListener(InteractiveEvent.OUT, event -> interactiveSprite.setColor(Color.WHITE));
            interactiveSprite.addEventListener(InteractiveEvent.DOWN, event -> interactiveSprite.setColor(Color.GREEN));
            interactiveSprite.addEventListener(InteractiveEvent.UP, event -> interactiveSprite.setColor(Color.YELLOW));
            interactiveSprite.addEventListener(InteractiveEvent.FOCUS_IN, event -> interactiveSprite.setAlpha(1f));
            interactiveSprite.addEventListener(InteractiveEvent.FOCUS_OUT, event -> interactiveSprite.setAlpha(_i * 0.1f));
            interactiveSprite.setScale(i, i);
            interactiveSprite.setAlpha(_i * 0.1f);
            interactiveSprite.setTabbingEnabled(true);

            cont.add(interactiveSprite, 50, 50 * i);

            BorderedRect rect = new BorderedRect(
                    interactiveSprite.getWidth() * interactiveSprite.getScaleX(),
                    interactiveSprite.getHeight() * interactiveSprite.getScaleY(),
                    null,
                    Color.WHITE
            );

            rect.setAlpha(interactiveSprite.getAlpha());
            cont.add(rect, interactiveSprite.getX(), interactiveSprite.getY());
        }

        cont.setScale(1.5f, 1.75f);

        stage.add(cont, 20, 30);

        D2D2.loop();
    }
}
