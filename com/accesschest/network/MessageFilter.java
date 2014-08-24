package com.accesschest.network;

import com.accesschest.container.ContainerChestServer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageFilter implements IMessage, IMessageHandler<MessageFilter, IMessage>
{
	private String filter;

	public MessageFilter() {}

	public MessageFilter(String filter)
	{
		this.filter = filter;
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		filter = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		ByteBufUtils.writeUTF8String(buffer, filter);
	}

	@Override
	public IMessage onMessage(MessageFilter message, MessageContext ctx)
	{
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;

		if (player.openContainer instanceof ContainerChestServer)
		{
			((ContainerChestServer)player.openContainer).setFilter(message.filter);
		}

		return null;
	}
}