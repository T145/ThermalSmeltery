package us.drullk.thermalsmeltery;

import mantle.pulsar.config.ForgeCFG;
import mantle.pulsar.control.PulseManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import us.drullk.thermalsmeltery.config.TSmeltConfig;
import us.drullk.thermalsmeltery.plugins.EnderIOSmeltery;
import us.drullk.thermalsmeltery.plugins.ThermalExpansionSmeltery;
import us.drullk.thermalsmeltery.plugins.TinkersSmeltery;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ThermalSmeltery.MODID, name = ThermalSmeltery.NAME, dependencies = ThermalSmeltery.DEPENDENCIES)
public class ThermalSmeltery {
	public static final String MODID = "ThermalSmeltery";
	public static final String NAME = "Thermal Smeltery";
	public static final String DEPENDENCIES = "after:BigReactors;required-after:TConstruct;after:ThermalExpansion";

	public static final Logger logger = LogManager.getLogger(MODID);

	@Instance(MODID)
	public static ThermalSmeltery instance = new ThermalSmeltery();

	public static PulseManager pulsar = new PulseManager(MODID, new ForgeCFG("TSmeltModules", "Modules: Disabling these will disable a chunk of the mod"));

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		TSmeltConfig.initProps(event.getModConfigurationDirectory());

		pulsar.registerPulse(new ThermalExpansionSmeltery());
		pulsar.registerPulse(new TinkersSmeltery());
		pulsar.registerPulse(new EnderIOSmeltery());
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