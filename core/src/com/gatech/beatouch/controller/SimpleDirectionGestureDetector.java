package com.gatech.beatouch.controller;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;

public class SimpleDirectionGestureDetector extends GestureDetector {
    public interface DirectionListener {
        void onLeft();

        void onRight();


    }

    public SimpleDirectionGestureDetector(DirectionListener directionListener) {
        super(new DirectionGestureListener(directionListener));
    }

    private static class DirectionGestureListener extends GestureAdapter {
        DirectionListener directionListener;

        public DirectionGestureListener(DirectionListener directionListener) {
            this.directionListener = directionListener;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {

            if (velocityX > 0) {
                directionListener.onRight();
            } else {
                directionListener.onLeft();
            }

            return super.fling(velocityX, velocityY, button);
        }

    }

}
