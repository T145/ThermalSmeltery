package us.drullk.thermalsmeltery.plugins;

import mantle.blocks.BlockUtils;
import mantle.pulsar.pulse.Handler;
import mantle.pulsar.pulse.Pulse;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import tconstruct.TConstruct;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.FluidType;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.Smeltery;
import tconstruct.smeltery.TinkerSmeltery;
import us.drullk.thermalsmeltery.ThermalSmeltery;
import us.drullk.thermalsmeltery.config.TSmeltConfig;
import us.drullk.thermalsmeltery.items.FilledBucket;
import us.drullk.thermalsmeltery.lib.FluidHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Pulse(id = "TSmelt EIO Smeltery", description = "TCon Smeltery Integration for EnderIO", modsRequired = "TConstruct;EnderIO")
public class EnderIOSmeltery {

	public static Item buckets;

	public static Fluid moltenEnergeticFluid;
	public static Fluid moltenVibrantFluid;
	public static Fluid moltenConductiveIronFluid;
	public static Fluid moltenPulsatingIronFluid;
	public static Fluid moltenDarkSteelFluid;

	public static Block moltenEnergetic;
	public static Block moltenVibrant;
	public static Block moltenConductiveIron;
	public static Block moltenPulsatingIron;
	public static Block moltenDarkSteel;

	public static Fluid[] fluids = new Fluid[5];
	public static Block[] fluidBlocks = new Block[5];

	private static FluidStack moltenRedstoneDust;
	private static FluidStack moltenGlowstoneDust;
	private static FluidStack moltenEnder;
	private static FluidStack moltenIronIngot;
	private static FluidStack moltenGoldIngot;
	private static FluidStack moltenSteelIngot;

	private static ItemStack itemSiliconStack;

	@Handler
	public void preInit(FMLPreInitializationEvent event) {
		buckets = new FilledBucket(BlockUtils.getBlockFromItem(buckets));
		GameRegistry.registerItem(buckets, "buckets");

		moltenEnergeticFluid = FluidHelper.registerFluid("EnergeticAlloy");
		moltenEnergetic = moltenEnergeticFluid.getBlock();

		moltenVibrantFluid = FluidHelper.registerFluid("PhasedGold");
		moltenVibrant = moltenVibrantFluid.getBlock();

		moltenConductiveIronFluid = FluidHelper.registerFluid("ConductiveIron");
		moltenConductiveIron = moltenConductiveIronFluid.getBlock();

		moltenPulsatingIronFluid = FluidHelper.registerFluid("PhasedIron");
		moltenPulsatingIron = moltenPulsatingIronFluid.getBlock();

		moltenDarkSteelFluid = FluidHelper.registerFluid("DarkSteel");
		moltenDarkSteel = moltenDarkSteelFluid.getBlock();

		fluids = new Fluid[] { moltenEnergeticFluid, moltenVibrantFluid, moltenConductiveIronFluid, moltenPulsatingIronFluid, moltenDarkSteelFluid };
		fluidBlocks = new Block[] { moltenEnergetic, moltenVibrant, moltenConductiveIron, moltenPulsatingIron, moltenDarkSteel };

		FluidType.registerFluidType("EnergeticAlloy", GameRegistry.findBlock("EnderIO", "blockIngotStorage"), 1, 650, moltenEnergeticFluid, false);
		FluidType.registerFluidType("PhasedGold", GameRegistry.findBlock("EnderIO", "blockIngotStorage"), 2, 750, moltenVibrantFluid, false);
		FluidType.registerFluidType("ConductiveIron", GameRegistry.findBlock("EnderIO", "blockIngotStorage"), 4, 500, moltenConductiveIronFluid, false);
		FluidType.registerFluidType("PhasedIron", GameRegistry.findBlock("EnderIO", "blockIngotStorage"), 5, 500, moltenPulsatingIronFluid, false);
		FluidType.registerFluidType("DarkSteel", GameRegistry.findBlock("EnderIO", "blockIngotStorage"), 6, 850, moltenDarkSteelFluid, false);
	}

	@Handler
	public void init(FMLInitializationEvent event) {
		if (TConstruct.pulsar.isPulseLoaded("Tinkers' Smeltery")) {
			moltenRedstoneDust = new FluidStack(FluidRegistry.getFluid("redstone"), 100);
			moltenGlowstoneDust = new FluidStack(FluidRegistry.getFluid("glowstone"), 250);
			moltenEnder = new FluidStack(FluidRegistry.getFluid("ender"), 250);
			moltenIronIngot = new FluidStack(FluidRegistry.getFluid("iron.molten"), TConstruct.ingotLiquidValue);
			moltenGoldIngot = new FluidStack(FluidRegistry.getFluid("gold.molten"), TConstruct.ingotLiquidValue);
			moltenSteelIngot = new FluidStack(FluidRegistry.getFluid("steel.molten"), TConstruct.ingotLiquidValue);
			itemSiliconStack = new ItemStack(GameRegistry.findItem("EnderIO", "itemMaterial"), 1, 0);

			ItemStack ingotcast = new ItemStack(TinkerSmeltery.metalPattern, 1, 0);
			LiquidCasting tableCasting = TConstructRegistry.getTableCasting();
			LiquidCasting basinCasting = TConstructRegistry.getBasinCasting();

			String[] orePrefix = new String[] { "block", "nugget", "ingot" };
			int[] oreAmounts = new int[] { TConstruct.blockLiquidValue, TConstruct.nuggetLiquidValue, TConstruct.ingotLiquidValue };
			String[] fluidNames = new String[] { "EnergeticAlloy", "PhasedGold", "ConductiveIron", "PhasedIron", "DarkSteel" };

			if (TSmeltConfig.EIOAddMetalCasting) {
				for (int c = 0; c < fluidNames.length; c++) {
					String oredictIngot = "ingot" + fluidNames[c];
					String oredictBlock = "block" + fluidNames[c];

					if (OreDictionary.doesOreNameExist(oredictIngot)) {
						tableCasting.addCastingRecipe(OreDictionary.getOres(oredictIngot).get(0), new FluidStack(fluids[c], TConstruct.ingotLiquidValue), ingotcast, 50);

						ThermalSmeltery.logger.info("Added " + oredictIngot + " to TCon Casting Table");
					} else {
						ThermalSmeltery.logger.info("Skipping registration of casting " + oredictIngot);
					}

					if (OreDictionary.doesOreNameExist(oredictBlock)) {
						basinCasting.addCastingRecipe(OreDictionary.getOres(oredictBlock).get(0), new FluidStack(fluids[c], TConstruct.blockLiquidValue), 150);

						ThermalSmeltery.logger.info("Added " + oredictBlock + " to TCon Casting Basin");
					} else {
						ThermalSmeltery.logger.info("Skipping registration of casting " + oredictBlock);
					}

					for (int i = 0; i < orePrefix.length; i++) {
						if (OreDictionary.doesOreNameExist(orePrefix[i] + fluidNames[c])) {
							Smeltery.addDictionaryMelting(orePrefix[i] + fluidNames[c], tconstruct.library.crafting.FluidType.getFluidType(fluids[c]), 0, oreAmounts[i]);
						}
					}

					tableCasting.addCastingRecipe(new ItemStack(GameRegistry.findItem("ThermalSmeltery", "buckets"), 1, c), new FluidStack(fluids[c], 1000), new ItemStack(Items.bucket, 1, 0), true, 50);
				}
			}

			if (TSmeltConfig.EIOElectricalSteelCasting && Loader.isModLoaded("EnderIO")) {
				tableCasting.addCastingRecipe(new ItemStack(GameRegistry.findItem("EnderIO", "itemAlloy"), 1, 0), moltenSteelIngot, itemSiliconStack, true, 60);
			}

			if (TSmeltConfig.EIOEnergeticAlloyRecipe && Loader.isModLoaded("EnderIO")) {
				Smeltery.addAlloyMixing(new FluidStack(moltenEnergeticFluid, TConstruct.ingotLiquidValue), moltenGoldIngot, moltenRedstoneDust, moltenGlowstoneDust);
			}

			if (TSmeltConfig.EIOVibrantAlloyRecipe && Loader.isModLoaded("EnderIO")) {
				Smeltery.addAlloyMixing(new FluidStack(moltenVibrantFluid, TConstruct.ingotLiquidValue), new FluidStack(moltenEnergeticFluid, TConstruct.ingotLiquidValue), moltenEnder);
			}

			if (TSmeltConfig.EIORedstoneAlloyCasting && Loader.isModLoaded("EnderIO")) {
				tableCasting.addCastingRecipe(new ItemStack(GameRegistry.findItem("EnderIO", "itemAlloy"), 1, 3), moltenRedstoneDust, itemSiliconStack, true, 50);
			}

			if (TSmeltConfig.EIOConductiveIronRecipe && Loader.isModLoaded("EnderIO")) {
				Smeltery.addAlloyMixing(new FluidStack(moltenConductiveIronFluid, TConstruct.ingotLiquidValue), moltenIronIngot, moltenRedstoneDust);
			}

			if (TSmeltConfig.EIOPulsatingIronRecipe && Loader.isModLoaded("EnderIO")) {
				Smeltery.addAlloyMixing(new FluidStack(moltenPulsatingIronFluid, TConstruct.ingotLiquidValue), moltenIronIngot, moltenEnder);
			}

			if (TSmeltConfig.EIODarkSteelRecipe && Loader.isModLoaded("EnderIO")) {
				Smeltery.addAlloyMixing(new FluidStack(moltenDarkSteelFluid, TConstruct.ingotLiquidValue), moltenSteelIngot, new FluidStack(FluidRegistry.getFluid("obsidian.molten"), TConstruct.ingotLiquidValue * 2));
			}

			if (TSmeltConfig.EIOSoulariumCasting && Loader.isModLoaded("EnderIO")) {
				tableCasting.addCastingRecipe(new ItemStack(GameRegistry.findItem("EnderIO", "itemAlloy"), 1, 7), moltenGoldIngot, new ItemStack(Blocks.soul_sand, 1, 0), true, 75);
			}
		} else {
			ThermalSmeltery.logger.warn("Tinker's Smeltery is disabled, Adding EIO alloy mixing and casting disabled.");
		}
	}
}
