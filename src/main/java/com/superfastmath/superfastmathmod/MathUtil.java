package com.superfastmath.superfastmathmod;

import java.nio.FloatBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MathUtil {
    private static FloatBuffer trigTable;
    private static float radToIndex;
    private static final Map<Float, float[]> cache = new ConcurrentHashMap<>();
    private static final int MAX_CACHE_SIZE = 1000;

    public static void initialize(int tableSize) {
        trigTable = FloatBuffer.allocate(tableSize);
        radToIndex = tableSize / (2.0f * (float) Math.PI);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int j = 0; j < tableSize; j += 100) { // 分块计算
            final int start = j;
            final int end = Math.min(j + 100, tableSize);
            executor.execute(() -> {
                for (int k = start; k < end; k++) {
                    trigTable.put(k, toFloat(Math.sin(k * 2.0 * Math.PI / tableSize)));
                }
            });
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        trigTable.flip();
    }

    public static float[] sinAndCos(float radians) {
        if (trigTable == null) {
            throw new IllegalStateException("MathUtil not initialized!");
        }
        if (cache.size() >= MAX_CACHE_SIZE) {
            float oldestKey = cache.keySet().iterator().next();
            cache.remove(oldestKey);
        }
        if (cache.containsKey(radians)) {
            return cache.get(radians);
        }
        int sinIndex = (int) (radians * radToIndex) & (trigTable.limit() - 1);
        int cosIndex = (sinIndex + trigTable.limit() / 4) % trigTable.limit();
        float sinValue = trigTable.get(sinIndex);
        float cosValue = trigTable.get(cosIndex);
        float[] result = new float[]{sinValue, cosValue};
        cache.put(radians, result);
        return result;
    }

    private static float toFloat(double d) {
        return (float) (Math.round(d * 1.0E8D) / 1.0E8D);
    }

    public static void adjustTableSize(int newTableSize) {
        if (newTableSize != trigTable.capacity()) {
            initialize(newTableSize);
        }
    }
}