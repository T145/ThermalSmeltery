package drullkus.thermalsmeltery.plugins;

import java.util.Map;

import mantle.pulsar.pulse.Handler;
import mantle.pulsar.pulse.Pulse;
import mantle.utils.ItemMetaWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import drullkus.thermalsmeltery.config.ModConfig;

@Pulse(id = "TSmelt TE", description = "Thermal Expansion Integration", modsRequired = "ThermalExpansion")
public class PluginThermalExpansion {
	public static void addCrucibleRecipe(int energy, ItemStack input, FluidStack output) {
		if (input == null || output == null) {
			return;
		}

		NBTTagCompound toSend = new NBTTagCompound();

		toSend.setInteger("energy", energy);
		toSend.setTag("input", new NBTTagCompound());
		toSend.setTag("output", new NBTTagCompound());

		input.writeToNBT(toSend.getCompoundTag("input"));
		output.writeToNBT(toSend.getCompoundTag("output"));

		FMLInterModComms.sendMessage("ThermalExpansion", "CrucibleRecipe", toSend);
	}

	@Handler
	public void postInit(FMLPostInitializationEvent event) {
		Map<ItemMetaWrapper, FluidStack> smelteryMap = tconstruct.library.crafting.Smeltery.getSmeltingList();
		Map<ItemMetaWrapper, Integer> tempMap = tconstruct.library.crafting.Smeltery.getTemperatureList();

		for (Map.Entry<ItemMetaWrapper, FluidStack> entry : smelteryMap.entrySet()) {
			ItemStack input = new ItemStack(entry.getKey().item, 1, entry.getKey().meta);
			int energy = tempMap.get(entry.getKey()) * ModConfig.multiplier;
			addCrucibleRecipe(energy, input, entry.getValue());
		}
	}
}