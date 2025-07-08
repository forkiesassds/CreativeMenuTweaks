package com.goopswagger.creativemenutweaks.neoforge;

import com.goopswagger.creativemenutweaks.CreativeMenuTweaks;
import com.goopswagger.creativemenutweaks.client.config.Config;
import com.goopswagger.creativemenutweaks.client.config.ConfigHelper;
import com.goopswagger.creativemenutweaks.client.CreativeMenuTweaksClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = CreativeMenuTweaks.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventsClient {
    @SubscribeEvent
    public static void onInitialize(FMLClientSetupEvent event) {
        if (FMLLoader.getLoadingModList().getModFileById("yet_another_config_lib_v3") != null) {
            ModLoadingContext.get().registerExtensionPoint(
                    IConfigScreenFactory.class,
                    () -> (client, parent) ->
                            ConfigHelper.getConfigScreen(parent, FMLPaths.CONFIGDIR.get(), CreativeMenuTweaksClient.config)
            );
        }

        CreativeMenuTweaksClient.config = Config.readConfig(FMLPaths.CONFIGDIR.get());
    }
}
