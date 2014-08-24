package com.accesschest.network;

import com.accesschest.tileentity.TileEntityAbstractChest;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MessageAccessChest implements IMessage, IMessageHandler<MessageAccessChest, IMessage>
{
	private int x, y, z, color, grade;
	private boolean isOriginal;

	public MessageAccessChest() {}

	public MessageAccessChest(int x, int y, int z, int color, int grade, boolean isOriginal)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
		this.grade = grade;
		this.isOriginal = isOriginal;
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		color = buffer.readInt();
		grade = buffer.readInt();
		isOriginal = buffer.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(color);
		buffer.writeInt(grade);
		buffer.writeBoolean(isOriginal);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IMessage onMessage(MessageAccessChest message, MessageContext ctx)
	{
		Minecraft mc = FMLClientHandler.instance().getClient();
		World world = mc.theWorld;
		TileEntity entity = world.getTileEntity(message.x, message.y, message.z);

		if (entity instanceof TileEntityAbstractChest)
		{
			((TileEntityAbstractChest)entity).setColorAndGrade(message.color, message.grade, message.isOriginal);
		}

		return null;
	}
}