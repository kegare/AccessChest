package com.accesschest.handler;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.WorldEvent;

import com.accesschest.client.ClientProxy;
import com.accesschest.core.AccessChest;
import com.accesschest.core.Config;
import com.accesschest.item.ItemAccessChest;
import com.accesschest.network.MessageAutoCollect;
import com.accesschest.network.MessageCollectiveTransfer;
import com.accesschest.repository.DataManagerWorldData;
import com.accesschest.repository.RepositoryAccessChest;
import com.accesschest.util.AccessUtils;
import com.google.common.collect.Sets;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AccessEventHooks
{
	public static final AccessEventHooks instance = new AccessEventHooks();

	public static final Set<String> autoCollect = Sets.newHashSet();

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event)
	{
		if (event.modID.equals(AccessChest.MODID))
		{
			Config.syncConfig();
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event)
	{
		Minecraft mc = FMLClientHandler.instance().getClient();
		EntityPlayer player = mc.thePlayer;
		String uuid = player.getGameProfile().getId().toString();
		IChatComponent message = new ChatComponentText("[" + AccessChest.MOD_NAME + "] ");
		IChatComponent component;
		String enabled = I18n.format("gui.button.enabled");
		String disabled = I18n.format("gui.button.disabled");

		if (ClientProxy.collectiveTransferKey.isPressed())
		{
			if (ItemAccessChest.collectiveTransfer.contains(uuid))
			{
				ItemAccessChest.collectiveTransfer.remove(uuid);

				AccessChest.network.sendToServer(new MessageCollectiveTransfer(false));

				component = new ChatComponentTranslation(ClientProxy.collectiveTransferKey.getKeyDescription() + ".toggle", disabled);
				component.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
				message.appendSibling(component);
			}
			else
			{
				ItemAccessChest.collectiveTransfer.add(uuid);

				AccessChest.network.sendToServer(new MessageCollectiveTransfer(true));

				component = new ChatComponentTranslation(ClientProxy.collectiveTransferKey.getKeyDescription() + ".toggle", enabled);
				component.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
				message.appendSibling(component);
			}

			mc.ingameGUI.getChatGUI().printChatMessage(message);
		}
		else if (ClientProxy.autoCollectKey.isPressed())
		{
			if (autoCollect.contains(uuid))
			{
				autoCollect.remove(uuid);

				AccessChest.network.sendToServer(new MessageAutoCollect(false));

				component = new ChatComponentTranslation(ClientProxy.autoCollectKey.getKeyDescription() + ".toggle", disabled);
				component.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
				message.appendSibling(component);
			}
			else
			{
				autoCollect.add(uuid);

				AccessChest.network.sendToServer(new MessageAutoCollect(true));

				component = new ChatComponentTranslation(ClientProxy.autoCollectKey.getKeyDescription() + ".toggle", enabled);
				component.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
				message.appendSibling(component);
			}

			mc.ingameGUI.getChatGUI().printChatMessage(message);
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event)
	{
		if (event.player instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP)event.player;
			String uuid = player.getGameProfile().getId().toString();

			if (player.getEntityData().getBoolean("AC:AutoCollect"))
			{
				autoCollect.add(uuid);

				AccessChest.network.sendTo(new MessageAutoCollect(true), player);
			}
			else
			{
				autoCollect.remove(uuid);

				AccessChest.network.sendTo(new MessageAutoCollect(false), player);
			}
		}
	}

	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event)
	{
		EntityPlayer player = event.entityPlayer;

		if (!autoCollect.contains(player.getGameProfile().getId().toString()))
		{
			return;
		}

		ItemStack item = event.item.getEntityItem();

		if (player.inventory.addItemStackToInventory(item))
		{
			return;
		}

		ItemStack[] inventory = player.inventory.mainInventory;

		for (int i = 0; i < inventory.length; ++i)
		{
			if (inventory[i] != null && inventory[i].getItem() instanceof ItemAccessChest)
			{
				int damage = inventory[i].getItemDamage();
				RepositoryAccessChest repo = new RepositoryAccessChest(player.worldObj, AccessUtils.getColor(damage), AccessUtils.getGrade(damage), AccessUtils.isOriginal(damage));

				repo.storeItem(item);

				if (item.stackSize <= 0)
				{
					break;
				}
			}
		}
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		World world = event.world;

		for (int i = 0; i < 16; ++i)
		{
			String name = "AccessChest" + i;

			if (world.loadItemData(DataManagerWorldData.class, name) == null)
			{
				world.setItemData(name, new DataManagerWorldData(name));
			}
		}
	}
}