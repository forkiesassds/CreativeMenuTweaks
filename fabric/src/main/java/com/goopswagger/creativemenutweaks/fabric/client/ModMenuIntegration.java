package com.goopswagger.creativemenutweaks.fabric.client;

import com.goopswagger.creativemenutweaks.client.ConfigHelper;
import com.goopswagger.creativemenutweaks.client.CreativeMenuTweaksClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuIntegration implements ModMenuApi {
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        FabricLoader loader = FabricLoader.getInstance();
        if (loader.isModLoaded("yet_another_config_lib_v3"))
            return parent -> ConfigHelper.getConfigScreen(parent, loader.getConfigDir(), CreativeMenuTweaksClient.config);

        return null;
    }
}
