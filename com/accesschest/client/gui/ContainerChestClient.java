package com.accesschest.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.accesschest.container.ContainerAbstractChest;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ContainerChestClient extends ContainerAbstractChest
{
	private int chestInventorySize;

	public ContainerChestClient(IInventory playerInventory)
	{
		super(new InventoryBasic("dammyInventoryClient", false, 12 * 8), playerInventory);
		this.chestInventorySize = chestInventory.getSizeInventory();
		this.refreshSlot();
	}

	@Override
	protected void refreshSlotChest()
	{
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 12; j++)
			{
				int index = i * 12 + j;

				if (index < chestInventorySize - scrollIndex * 12)
				{
					addSlotToContainer(new Slot(chestInventory, index, j * 18 + 12, i * 18 + 17));
				}
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		return null;
	}

	@Override
	public int getScrollMax()
	{
		return Math.max(0, (chestInventorySize - 1) / 12 - 7);
	}

	@Override
	public void updateProgressBar(int type, int value)
	{
		switch (type)
		{
			case INFO_TYPE_INVENTORY_SIZE:
				chestInventorySize = value;
				refreshSlot();
				break;
			case INFO_TYPE_SCROLL_INDEX:
				scrollIndex = value;
				break;
		}
	}
}