package me.icanttellyou.tweakedcreativemenu;

import me.icanttellyou.tweakedcreativemenu.networking.INetworkHelper;
import me.icanttellyou.tweakedcreativemenu.util.DummyItemGroup;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class TweakedCreativeMenu {
    public static final String MOD_ID = "tweaked_creative_menu";
    public static final String MOD_NAME = "Tweaked Creative Menu";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static Function<Identifier, DummyItemGroup> DUMMY_SUPPLIER = null;
    private static INetworkHelper networkHelper = null;

    public static Identifier makeModID(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public static void setNetworkHelper(INetworkHelper helper) {
        if (networkHelper != null)
            throw new RuntimeException("INetworkHelper is already implemented!");

        networkHelper = helper;
    }

    public static INetworkHelper getNetworkHelper() {
        if (networkHelper == null)
            throw new RuntimeException("INetworkHelper not implemented!");

        return networkHelper;
    }
}
