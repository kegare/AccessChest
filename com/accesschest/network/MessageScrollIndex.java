package com.accesschest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

import com.accesschest.container.ContainerChestServer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageScrollIndex implements IMessage, IMessageHandler<MessageScrollIndex, IMessage>
{
	private int index;

	public MessageScrollIndex() {}

	public MessageScrollIndex(int index)
	{
		this.index = index;
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		index = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		buffer.writeInt(index);
	}

	@Override
	public IMessage onMessage(MessageScrollIndex message, MessageContext ctx)
	{
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;

		if (player.openContainer != null && player.openContainer instanceof ContainerChestServer)
		{
			((ContainerChestServer)player.openContainer).setScrollIndex(message.index);
		}

		return null;
	}
}