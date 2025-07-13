package me.icanttellyou.tweakedcreativemenu.neoforge;

import me.icanttellyou.tweakedcreativemenu.TweakedCreativeMenu;
import me.icanttellyou.tweakedcreativemenu.client.TweakedCreativeMenuClient;
import net.minecraft.client.MinecraftClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = TweakedCreativeMenu.MOD_ID, value = Dist.CLIENT)
public class GameEventsClient {
    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        TweakedCreativeMenuClient.onDisconnect(MinecraftClient.getInstance());
    }
}
