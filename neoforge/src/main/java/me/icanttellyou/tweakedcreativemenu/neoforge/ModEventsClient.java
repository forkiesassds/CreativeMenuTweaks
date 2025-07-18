package me.icanttellyou.tweakedcreativemenu.neoforge;

import me.icanttellyou.tweakedcreativemenu.TweakedCreativeMenu;
import me.icanttellyou.tweakedcreativemenu.client.config.Config;
import me.icanttellyou.tweakedcreativemenu.client.config.ConfigHelper;
import me.icanttellyou.tweakedcreativemenu.client.TweakedCreativeMenuClient;
import net.minecraft.item.ItemGroup;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.CreativeModeTabRegistry;

@EventBusSubscriber(modid = TweakedCreativeMenu.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventsClient {
    @SubscribeEvent
    public static void onInitialize(FMLClientSetupEvent event) {
        if (FMLLoader.getLoadingModList().getModFileById("yet_another_config_lib_v3") != null) {
            ModLoadingContext.get().registerExtensionPoint(
                    IConfigScreenFactory.class,
                    () -> (client, parent) ->
                            ConfigHelper.getConfigScreen(parent, FMLPaths.CONFIGDIR.get(), TweakedCreativeMenuClient.config)
            );
        }

        TweakedCreativeMenuClient.config = Config.readConfig(FMLPaths.CONFIGDIR.get());
        TweakedCreativeMenuClient.ITEM_GROUP_GETTER = () -> CreativeModeTabRegistry.getSortedCreativeModeTabs()
                .stream().filter(ItemGroup::shouldDisplay).toList();
    }
}
