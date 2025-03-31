package com.superfastmath.superfastmathmod;

import com.superfastmath.superfastmathmod.config.Config;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("superfastmath")
public class SuperFastMath {
    public static final String MOD_ID = "superfastmath";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public SuperFastMath() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        MathUtil.initialize(Config.TABLE_SIZE.get());
        LOGGER.info("SuperFastMath initialized (Forge) | Table Size: {}", Config.TABLE_SIZE.get());
    }
}