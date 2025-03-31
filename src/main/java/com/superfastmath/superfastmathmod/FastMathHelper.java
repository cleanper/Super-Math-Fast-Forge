package com.superfastmath.superfastmathmod;

public class FastMathHelper {
    public static float sin(float radians) {
        return MathUtil.sinAndCos(radians)[0];
    }

    public static float cos(float radians) {
        return MathUtil.sinAndCos(radians)[1];
    }
}