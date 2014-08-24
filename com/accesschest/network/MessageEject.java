package com.accesschest.network;

import com.accesschest.container.ContainerChestServer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageEject implements IMessage, IMessageHandler<MessageEject, IMessage>
{
	@Override
	public void fromBytes(ByteBuf buffer) {}

	@Override
	public void toBytes(ByteBuf buffer) {}

	@Override
	public IMessage onMessage(MessageEject message, MessageContext ctx)
	{
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;

		if (player.openContainer instanceof ContainerChestServer)
		{
			((ContainerChestServer)player.openContainer).eject(player);
		}

		return null;
	}
}