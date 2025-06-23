package com.chailotl.particular.sushi_bar.owo.config;

import com.chailotl.particular.Main;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;

@Config(name = Main.MOD_ID)
public class ParticularConfig implements ConfigData {

    @ConfigEntry.Category("enabled_effects")
    @ConfigEntry.Gui.TransitiveObject
    public EnabledEffects enabledEffects = new EnabledEffects();

    @ConfigEntry.Category("advanced_settings")
    @ConfigEntry.Gui.TransitiveObject
    public AdvancedSettings advancedSettings = new AdvancedSettings();

    public static class EnabledEffects {
        public boolean waterSplash = true;
        public boolean cascades = true;
        public boolean waterfallSpray = true;
        public boolean fireflies = true;
        public boolean fallingLeaves = true;
        public boolean caveDust = true;
        public boolean chestBubbles = true;
        public boolean soulSandBubbles = true;
        public boolean barrelBubbles = true;
        public boolean poppingBubbles = true;
        public boolean rainRipples = true;
        public boolean waterDripRipples = true;
        public boolean cakeEatingParticles = true;
        public boolean emissiveLavaDrips = true;
    }

    public static class AdvancedSettings {
        @ConfigEntry.Gui.CollapsibleObject
        public FireflySettings fireflySettings = new FireflySettings();

        @ConfigEntry.Gui.CollapsibleObject
        public FallingLeavesSettings fallingLeavesSettings = new FallingLeavesSettings();

        @ConfigEntry.Gui.CollapsibleObject
        public CaveDustSettings caveDustSettings = new CaveDustSettings();
    }

    public static class FireflySettings {
        @Comment("Time when fireflies start appearing (0-23999)")
        @ConfigEntry.BoundedDiscrete(min = 0, max = 23999)
        public int startTime = 12000;

        @Comment("Time when fireflies stop appearing (0-23999)")
        @ConfigEntry.BoundedDiscrete(min = 0, max = 23999)
        public int endTime = 23000;

        public float minTemp = 0.5f;
        public float maxTemp = 0.99f;
        public boolean canSpawnInRain = false;

        @Comment("Daily random frequency modifiers")
        public List<Float> dailyRandom = Arrays.asList(0f, 0f, 0f, 0.33f, 0.66f, 1f);

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @Comment("Grass spawn frequency (as percentage)")
        public int grassPercentage = 17; // 1/6 ≈ 17%

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @Comment("Tall grass spawn frequency (as percentage)")
        public int tallGrassPercentage = 8; // 1/12 ≈ 8%

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @Comment("Flowers spawn frequency (as percentage)")
        public int flowersPercentage = 100;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        @Comment("Tall flowers spawn frequency (as percentage)")
        public int tallFlowersPercentage = 50;
    }

    public static class FallingLeavesSettings {
        @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
        public int spawnChance = 60;

        public boolean spawnRipples = true;
        public boolean layFlatOnGround = true;
        public boolean layFlatRightAngles = false;
    }

    public static class CaveDustSettings {
        @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
        public int spawnChance = 700;

        @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
        public int baseMaxAge = 200;

        @ConfigEntry.ColorPicker
        public int color = 0x808080;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int fadeDuration = 20;

        public float maxAcceleration = 0.03f;

        @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
        public int accelChangeChance = 180;

        @Comment("Biomes to exclude from cave dust spawning")
        public List<String> excludeBiomes = Arrays.asList(
                "minecraft:lush_caves",
                "minecraft:dripstone_caves",
                "minecraft:deep_dark"
        );
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        if (advancedSettings.fireflySettings.minTemp > advancedSettings.fireflySettings.maxTemp) {
            throw new ValidationException("Firefly minimum temperature cannot be greater than maximum temperature");
        }
    }
}