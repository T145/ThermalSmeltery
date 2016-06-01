package us.drullk.thermalsmeltery.network;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cofh.api.tileentity.IRedstoneControl;
import cofh.core.network.PacketCoFHBase;
import cofh.core.network.PacketHandler;
import cofh.lib.gui.container.IAugmentableContainer;

public class PacketThermalSmeltery extends PacketCoFHBase {
	public static enum PacketTypes {
		RS_POWER_UPDATE, RS_CONFIG_UPDATE, TAB_AUGMENT;
	}

	public static void initialize() {
		PacketHandler.instance.registerPacket(PacketThermalSmeltery.class);
	}

	public void handlePacket(EntityPlayer player, boolean server) {
		try {
			byte packetType = getByte();
			int[] coords;
			IRedstoneControl tile;

			switch (PacketThermalSmeltery.PacketTypes.values()[packetType]) {
			case RS_POWER_UPDATE:
				coords = getCoords();
				tile = (IRedstoneControl) player.worldObj.getTileEntity(coords[0], coords[1], coords[2]);
				tile.setPowered(getBool());
				return;
			case RS_CONFIG_UPDATE:
				coords = getCoords();
				tile = (IRedstoneControl) player.worldObj.getTileEntity(coords[0], coords[1], coords[2]);
				tile.setControl(IRedstoneControl.ControlMode.values()[getByte()]);
				return;
			case TAB_AUGMENT:
				if (player.openContainer instanceof IAugmentableContainer) {
					((IAugmentableContainer) player.openContainer).setAugmentLock(getBool());
				}

				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendRSPowerUpdatePacketToClients(IRedstoneControl tile, World world, int x, int y, int z) {
		PacketHandler.sendToAllAround(getPacket(PacketTypes.RS_POWER_UPDATE).addCoords(x, y, z).addBool(tile.isPowered()), world, x, y, z);
	}

	public static void sendRSConfigUpdatePacketToServer(IRedstoneControl tile, int x, int y, int z) {
		PacketHandler.sendToServer(getPacket(PacketTypes.RS_CONFIG_UPDATE).addCoords(x, y, z).addByte(tile.getControl().ordinal()));
	}

	public static void sendTabAugmentPacketToServer(boolean lock) {
		PacketHandler.sendToServer(getPacket(PacketTypes.TAB_AUGMENT).addBool(lock));
	}

	public static PacketCoFHBase getPacket(PacketTypes type) {
		return new PacketThermalSmeltery().addByte(type.ordinal());
	}

	@Override
	public PacketCoFHBase addCoords(int x, int y, int z) {
		addInt(x);
		addByte(y);
		return addInt(z);
	}

	@Override
	public int[] getCoords() {
		return new int[] { getInt(), getUnsignedByte(), getInt() };
	}

	public byte getUnsignedByte() {
		try {
			return (byte) datain.readUnsignedByte();
		} catch (IOException e) {
			return (byte) 0;
		}
	}
}