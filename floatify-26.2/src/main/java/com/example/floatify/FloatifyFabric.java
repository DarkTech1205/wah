package com.example.floatify;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FloatifyFabric implements ModInitializer {
    public static final String MOD_ID = "floatify";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Floatify (Fabric) loaded - position/velocity truncated to float precision");
    }
}
