package com.accesschest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

import com.accesschest.container.ContainerChestServer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageStore implements IMessage, IMessageHandler<MessageStore, IMessage>
{
	private int type;

	public MessageStore() {}

	public MessageStore(int type)
	{
		this.type = type;
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		type = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		buffer.writeInt(type);
	}

	@Override
	public IMessage onMessage(MessageStore message, MessageContext ctx)
	{
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;

		if (player.openContainer instanceof ContainerChestServer)
		{
			ContainerChestServer container = (ContainerChestServer)player.openContainer;

			switch (message.type)
			{
				case 0:
					container.storeInventory();
					break;
				case 1:
					container.storeEquipment();
					break;
			}
		}

		return null;
	}
}