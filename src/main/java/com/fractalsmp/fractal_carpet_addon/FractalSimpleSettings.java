package com.fractalsmp.fractal_carpet_addon;

import carpet.settings.Rule;

import static carpet.settings.RuleCategory.EXPERIMENTAL;
import static carpet.settings.RuleCategory.FEATURE;

public class FractalSimpleSettings {
    public static final String FractalSettingCategory = "FractalCarpetAddon";
    @Rule(
            desc = "Toggle for end gateway cooldown",
            category = {FractalSettingCategory, FEATURE}
    )
    public static boolean endGatewayCooldown = true;

    @Rule(
            desc = "Toggle for the main end island structure generation, turns off portal, egg, obsidian pillars, gateways and crystals",
            category = {FractalSettingCategory, FEATURE}
    )
    public static boolean endMainIslandStructureGen = true;

    @Rule(
            desc = "Toggle multithreading of dimension ticks",
            category = {FractalSettingCategory, EXPERIMENTAL, FEATURE}
    )
    public static boolean multithreadedDimensions = false;
}
