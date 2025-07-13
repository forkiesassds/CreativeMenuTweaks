package me.icanttellyou.tweakedcreativemenu.client.config;

import me.icanttellyou.tweakedcreativemenu.TweakedCreativeMenu;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.nio.file.Path;

public class ConfigHelper {
    public static Screen getConfigScreen(Screen parent, Path configPath, Config config) {
        return YetAnotherConfigLib.createBuilder()
                .title(getConfigText("title"))
                .category(ConfigCategory.createBuilder()
                        .name(getConfigText("title"))
                        .group(OptionGroup.createBuilder()
                                .option(Option.<Boolean>createBuilder()
                                        .name(getConfigText("tabSounds.name"))
                                        .description(OptionDescription.of(getConfigText("tabSounds.desc")))
                                        .binding(true, () -> config.tabSounds, newVal -> config.tabSounds = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(getConfigText("tabPopout.name"))
                                        .description(OptionDescription.of(getConfigText("tabPopout.desc")))
                                        .binding(true, () -> config.tabPopout, newVal -> config.tabPopout = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Config.TooltipMode>createBuilder()
                                        .name(getConfigText("tooltipMode.name"))
                                        .description(OptionDescription.of(getConfigText("tooltipMode.desc")))
                                        .binding(Config.TooltipMode.ATTACHED, () -> config.tooltipMode, newVal -> config.tooltipMode = newVal)
                                        .controller(option -> EnumControllerBuilder.create(option)
                                                .enumClass(Config.TooltipMode.class).formatValue(Config.TooltipMode::getName))
                                        .build())
                                .build()
                        )
                        .build()
                )
                .save(() -> config.saveConfig(configPath))
                .build().generateScreen(parent);
    }

    private static Text getConfigText(String text) {
        return Text.translatable(TweakedCreativeMenu.MOD_ID + ".config." + text);
    }
}
