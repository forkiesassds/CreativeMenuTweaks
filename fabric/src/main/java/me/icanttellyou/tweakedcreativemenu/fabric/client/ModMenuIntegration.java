package me.icanttellyou.tweakedcreativemenu.fabric.client;

import me.icanttellyou.tweakedcreativemenu.client.config.ConfigHelper;
import me.icanttellyou.tweakedcreativemenu.client.TweakedCreativeMenuClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuIntegration implements ModMenuApi {
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        FabricLoader loader = FabricLoader.getInstance();
        if (loader.isModLoaded("yet_another_config_lib_v3"))
            return parent -> ConfigHelper.getConfigScreen(parent, loader.getConfigDir(), TweakedCreativeMenuClient.config);

        return null;
    }
}
