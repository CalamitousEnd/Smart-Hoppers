package me.final_cataclysm.smarthoppers;

import me.final_cataclysm.smarthoppers.config.General;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Main.MODID)
public class Main {

    public static final String MODID = "smarthoppers";

    public Main() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, General.SPEC, MODID + "-general.toml");
        System.out.println(MODID + ":registering mod and configs");
    }

    public static void Setup(final FMLCommonSetupEvent e) {
    }
}
