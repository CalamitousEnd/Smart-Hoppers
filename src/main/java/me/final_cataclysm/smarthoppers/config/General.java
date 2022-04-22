package me.final_cataclysm.smarthoppers.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class General {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.ConfigValue<Integer> TransferCooldown;
    public static ForgeConfigSpec.ConfigValue<Integer> InsertExtractCooldown;

    public static ForgeConfigSpec.ConfigValue<Integer> TransferCooldown2;
    public static ForgeConfigSpec.ConfigValue<Integer> InsertExtractCooldown2;

    static {
        BUILDER.push("minecraft:hopper");
        BUILDER.comment("These settings apply to all hoppers that extend the HopperBlockEntity class");

        TransferCooldown = BUILDER.defineInRange("Transfer Cooldown", Defaults.TransferCooldown, 0, 99999999);
        InsertExtractCooldown = BUILDER.defineInRange("Insert & Extract Cooldown", Defaults.InsertExtractCooldown, 0, 99999999);

        BUILDER.pop();
    }

    static {
        BUILDER.push("uppers:upper");
        BUILDER.comment("If the Uppers mod isn't loaded, these settings won't do anything.");

        TransferCooldown2 = BUILDER.defineInRange("Transfer Cooldown", Defaults.TransferCooldown, 0, 99999999);
        InsertExtractCooldown2 = BUILDER.defineInRange("Insert & Extract Cooldown", Defaults.InsertExtractCooldown, 0, 99999999);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}

