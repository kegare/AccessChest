package com.accesschest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

import com.accesschest.item.ItemAccessChest;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageCollectiveTransfer implements IMessage, IMessageHandler<MessageCollectiveTransfer, IMessage>
{
	private boolean collectiveTransfer;

	public MessageCollectiveTransfer() {}

	public MessageCollectiveTransfer(boolean collectiveTransfer)
	{
		this.collectiveTransfer = collectiveTransfer;
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		collectiveTransfer = buffer.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		buffer.writeBoolean(collectiveTransfer);
	}

	@Override
	public IMessage onMessage(MessageCollectiveTransfer message, MessageContext ctx)
	{
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		String uuid = player.getUniqueID().toString();

		if (message.collectiveTransfer)
		{
			ItemAccessChest.collectiveTransfer.add(uuid);
		}
		else
		{
			ItemAccessChest.collectiveTransfer.remove(uuid);
		}

		return null;
	}
}