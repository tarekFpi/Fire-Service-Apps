package com.techno71.fireservice;

import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.techno71.fireservice.Circle;

public class CircleAnimation extends Animation {
    private Circle circle;
    private float angle;
    private float endangle;

    public CircleAnimation(Circle circle, float endangle) {
        this.angle = circle.getAngle();
        this.endangle = endangle;
        this.circle = circle;
    }


    protected void applyTransformation(float f, Transformation transformation) {
        this.circle.setAngle(this.angle + ((this.endangle - this.angle) * f));
        this.circle.requestLayout();
    }
}
