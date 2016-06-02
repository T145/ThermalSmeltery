package drullkus.thermalsmeltery;

import mantle.pulsar.config.ForgeCFG;
import mantle.pulsar.control.PulseManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import drullkus.thermalsmeltery.config.ModConfig;
import drullkus.thermalsmeltery.plugins.PluginEnderIO;
import drullkus.thermalsmeltery.plugins.PluginTConstruct;
import drullkus.thermalsmeltery.plugins.PluginThermalExpansion;

@Mod(modid = ThermalSmeltery.MODID, name = ThermalSmeltery.NAME, version = ThermalSmeltery.VERSION, dependencies = ThermalSmeltery.DEPENDENCIES)
public class ThermalSmeltery {
	public static final String MODID = "ThermalSmeltery";
	public static final String NAME = "Thermal Smeltery";
	public static final String VERSION = "$version";
	public static final String DEPENDENCIES = "after:ExtraTiC;after:BigReactors;required-after:TConstruct;required-after:ThermalExpansion";

	public static final Logger logger = LogManager.getLogger(MODID);

	@Instance(MODID)
	public static ThermalSmeltery instance = new ThermalSmeltery();

	@Metadata
	private ModMetadata metadata;

	public static PulseManager pulsar = new PulseManager(MODID, new ForgeCFG("TSmeltModules", "Modules: Disabling these will disable a chunk of the mod"));

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModConfig.initProps(event);

		pulsar.registerPulse(new PluginThermalExpansion());
		pulsar.registerPulse(new PluginTConstruct());
		pulsar.registerPulse(new PluginEnderIO());

		pulsar.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		pulsar.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		pulsar.postInit(event);
	}
}