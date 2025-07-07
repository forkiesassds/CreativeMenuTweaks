package com.goopswagger.creativemenutweaks.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.goopswagger.creativemenutweaks.CreativeMenuTweaks;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.StringIdentifiable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("tab_sounds").forGetter(c -> c.tabSounds),
            Codec.BOOL.fieldOf("tab_popout").forGetter(c -> c.tabPopout),
            StringIdentifiable.createBasicCodec(TooltipMode::values).fieldOf("tooltip_mode").forGetter(c -> c.tooltipMode)
    ).apply(instance, Config::new));

    public boolean tabSounds;
    public boolean tabPopout;
    public TooltipMode tooltipMode;

    public Config() {
        this(true, true, TooltipMode.ATTACHED);
    }

    public Config(boolean tabSounds, boolean tabPopout, TooltipMode tooltipMode) {
        this.tabSounds = tabSounds;
        this.tabPopout = tabPopout;
        this.tooltipMode = tooltipMode;
    }

    public static Config readConfig(Path configDir) {
        Path configFile = configDir.resolve(CreativeMenuTweaks.MOD_ID + ".json");
        try (BufferedReader reader = Files.newBufferedReader(configFile)) {
            return CODEC.decode(JsonOps.INSTANCE, new Gson().fromJson(reader, JsonElement.class))
                    .result()
                    .orElseGet(() -> Pair.of(new Config(), null))
                    .getFirst();
        } catch (IOException e) {
            return new Config();
        }
    }

    public void saveConfig(Path configDir) {
        Path configFile = configDir.resolve(CreativeMenuTweaks.MOD_ID + ".json");

        DataResult<JsonElement> encodedConfig = CODEC.encode(this, JsonOps.INSTANCE, new JsonObject());
        if (encodedConfig.result().isEmpty()) {
            CreativeMenuTweaks.LOGGER.warn("Failed to serialise JSON: {}", encodedConfig);
            return;
        }

        try {
            Files.createDirectories(configDir);
            try (BufferedWriter writer = Files.newBufferedWriter(configFile)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(encodedConfig.result().get(), writer);
            }
        } catch (IOException e) {
            CreativeMenuTweaks.LOGGER.error("Failed to write config file: ", e);
        }
    }

    public enum TooltipMode implements StringIdentifiable, Nameable {
        FLOATING("floating"),
        ATTACHED("attached");

        final String id;

        TooltipMode(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return id;
        }

        @Override
        public Text getName() {
            return Text.translatable(CreativeMenuTweaks.MOD_ID + ".config.tooltipMode." + id);
        }
    }
}
