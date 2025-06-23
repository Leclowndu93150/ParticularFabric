package com.chailotl.particular.sushi_bar.owo.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigManager {
    private static ConfigHolder<ParticularConfig> holder;

    public static void init() {
        holder = AutoConfig.register(ParticularConfig.class, JanksonConfigSerializer::new);

        GuiRegistry registry = AutoConfig.getGuiRegistry(ParticularConfig.class);

        registry.registerPredicateProvider(
                (translationKey, field, config, defaults, guiProvider) -> {
                    if (field.getName().equals("excludeBiomes") && List.class.isAssignableFrom(field.getType())) {
                        return Collections.singletonList(
                                ConfigEntryBuilder.create()
                                        .startStrList(Text.translatable(translationKey), getIdentifierStrings(config))
                                        .setDefaultValue(getIdentifierStrings(defaults))
                                        .setSaveConsumer(strings -> setIdentifierStrings(config, strings))
                                        .setTooltip(Text.translatable(translationKey + ".tooltip"))
                                        .build()
                        );
                    }
                    return Collections.emptyList();
                },
                field -> field.getName().equals("excludeBiomes")
        );
    }

    @SuppressWarnings("unchecked")
    private static List<String> getIdentifierStrings(Object config) {
        try {
            ParticularConfig.CaveDustSettings settings = ((ParticularConfig) config).advancedSettings.caveDustSettings;
            return settings.excludeBiomes;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private static void setIdentifierStrings(Object config, List<String> strings) {
        ParticularConfig.CaveDustSettings settings = ((ParticularConfig) config).advancedSettings.caveDustSettings;
        settings.excludeBiomes = strings.stream()
                .filter(s -> Identifier.tryParse(s) != null)
                .collect(Collectors.toList());
    }

    public static ParticularConfig getConfig() {
        return holder.getConfig();
    }

    public static void save() {
        holder.save();
    }

    public static float getFireflyGrassChance() {
        return getConfig().advancedSettings.fireflySettings.grassPercentage / 100f;
    }

    public static float getFireflyTallGrassChance() {
        return getConfig().advancedSettings.fireflySettings.tallGrassPercentage / 100f;
    }

    public static float getFireflyFlowersChance() {
        return getConfig().advancedSettings.fireflySettings.flowersPercentage / 100f;
    }

    public static float getFireflyTallFlowersChance() {
        return getConfig().advancedSettings.fireflySettings.tallFlowersPercentage / 100f;
    }

    public static List<Identifier> getExcludedBiomes() {
        return getConfig().advancedSettings.caveDustSettings.excludeBiomes.stream()
                .map(Identifier::of)
                .collect(Collectors.toList());
    }
}