package com.accesschest.network;

import com.accesschest.container.ContainerChestServer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageSort implements IMessage, IMessageHandler<MessageSort, IMessage>
{
	private int priority;

	public MessageSort() {}

	public MessageSort(int priority)
	{
		this.priority = priority;
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		priority = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		buffer.writeInt(priority);
	}

	@Override
	public IMessage onMessage(MessageSort message, MessageContext ctx)
	{
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;

		if (player.openContainer instanceof ContainerChestServer)
		{
			ContainerChestServer container = (ContainerChestServer)player.openContainer;

			if (message.priority < 0)
			{
				container.sort();
			}
			else
			{
				container.setPriorities(message.priority);
			}
		}

		return null;
	}
}