package com.accesschest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import com.accesschest.core.AccessChest;
import com.accesschest.handler.AccessEventHooks;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageAutoCollect implements IMessage, IMessageHandler<MessageAutoCollect, IMessage>
{
	private boolean autoCollect;

	public MessageAutoCollect() {}

	public MessageAutoCollect(boolean autoCollect)
	{
		this.autoCollect = autoCollect;
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		autoCollect = buffer.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		buffer.writeBoolean(autoCollect);
	}

	@Override
	public IMessage onMessage(MessageAutoCollect message, MessageContext ctx)
	{
		EntityPlayer player = AccessChest.proxy.getClientEntityPlayer();

		if (ctx.side.isServer())
		{
			player = ctx.getServerHandler().playerEntity;
			player.getEntityData().setBoolean("AC:AutoCollect", message.autoCollect);
		}

		String uuid = player.getGameProfile().getId().toString();

		if (message.autoCollect)
		{
			AccessEventHooks.autoCollect.add(uuid);
		}
		else
		{
			AccessEventHooks.autoCollect.remove(uuid);
		}

		return null;
	}
}