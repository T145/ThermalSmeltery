package us.drullk.thermalsmeltery.plugins;

import java.util.Map;

import mantle.pulsar.pulse.Handler;
import mantle.pulsar.pulse.Pulse;
import mantle.utils.ItemMetaWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.CastingRecipe;
import tconstruct.library.crafting.LiquidCasting;
import us.drullk.thermalsmeltery.ThermalSmeltery;
import us.drullk.thermalsmeltery.config.TSmeltConfig;
import us.drullk.thermalsmeltery.lib.MachineRecipeRegistry;
import us.drullk.thermalsmeltery.lib.TE4Helper;
import us.drullk.thermalsmeltery.network.PacketThermalSmeltery;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(ThermalSmeltery.MODID)
@Pulse(id = "TSmelt TE", description = "Thermal Expansion Integration", modsRequired = "ThermalExpansion")
public class ThermalExpansionSmeltery {
	@Handler
	public void preInit(FMLPostInitializationEvent event) {
		PacketThermalSmeltery.initialize();
	}

	@Handler
	public void postInit(FMLPostInitializationEvent event) {
		Map<ItemMetaWrapper, FluidStack> smelteryMap = tconstruct.library.crafting.Smeltery.getSmeltingList();
		Map<ItemMetaWrapper, Integer> tempMap = tconstruct.library.crafting.Smeltery.getTemperatureList();

		for (Map.Entry<ItemMetaWrapper, FluidStack> entry : smelteryMap.entrySet()) {
			ItemStack input = new ItemStack(entry.getKey().item, 1, entry.getKey().meta);
			int energy = tempMap.get(entry.getKey()) * TSmeltConfig.multiplier;
			TE4Helper.addCrucibleRecipe(energy, input, entry.getValue());
		}

		LiquidCasting tableCasting = TConstructRegistry.getTableCasting();
		for (CastingRecipe recipe : tableCasting.getCastingRecipes()) {
			MachineRecipeRegistry.registerStampingRecipe(tableCasting, recipe);
			MachineRecipeRegistry.registerIngotRecipe(recipe);
		}

		LiquidCasting basinCasting = TConstructRegistry.getBasinCasting();
		for (CastingRecipe recipe : basinCasting.getCastingRecipes()) {
			MachineRecipeRegistry.registerBlockRecipe(recipe);
		}
	}
}
