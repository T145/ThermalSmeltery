package drullkus.thermalsmeltery.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import tconstruct.library.TConstructRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FluidMoltenMetal extends BlockFluidClassic {
	@SideOnly(Side.CLIENT)
	public IIcon stillIcon;

	@SideOnly(Side.CLIENT)
	public IIcon flowIcon;

	String texture;
	boolean alpha;
	boolean overwriteFluidIcons = true;
	private Fluid fluid = null;

	public FluidMoltenMetal(Fluid fluid, Material material, String texture) {
		super(fluid, material);
		this.texture = texture;
		setCreativeTab(TConstructRegistry.blockTab);
	}

	public FluidMoltenMetal(Fluid fluid, Material material, String texture, boolean alpha) {
		this(fluid, material, texture);
		this.alpha = alpha;
	}

	@Override
	public int getRenderBlockPass() {
		return alpha ? 1 : 0;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		stillIcon = iconRegister.registerIcon("thermalsmeltery:liquid/" + texture);
		flowIcon = iconRegister.registerIcon("thermalsmeltery:liquid/" + texture + "_flow");

		if (overwriteFluidIcons) {
			getFluid().setIcons(stillIcon, flowIcon);
		}

		if (getFluid().getBlock() != this && fluid != null) {
			fluid.setIcons(stillIcon, flowIcon);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (side == 0 || side == 1) {
			return stillIcon;
		}

		return flowIcon;
	}

	public void suppressOverwritingFluidIcons() {
		overwriteFluidIcons = false;
	}

	public void setFluid(Fluid fluid) {
		this.fluid = fluid;
	}
}