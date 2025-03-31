package com.superfastmath.superfastmathmod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.IntValue TABLE_SIZE;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        TABLE_SIZE = builder
                .comment("Size of the precomputed trigonometric table (建议值: 4096)")
                .defineInRange("tableSize", 4096, 256, 65536);

        SPEC = builder.build();
    }
}