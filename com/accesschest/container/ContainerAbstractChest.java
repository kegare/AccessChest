package com.accesschest.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerAbstractChest extends Container
{
	protected static final int INFO_TYPE_INVENTORY_SIZE = 1;
	protected static final int INFO_TYPE_SCROLL_INDEX = 2;

	protected final IInventory chestInventory;
	protected final IInventory playerInventory;

	protected int scrollIndex;

	public ContainerAbstractChest(IInventory chestInventory, IInventory playerInventory)
	{
		this.chestInventory = chestInventory;
		this.playerInventory = playerInventory;

		chestInventory.openInventory();
	}

	protected void refreshSlot()
	{
		inventorySlots.clear();

		refreshSlotChest();

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(playerInventory, i * 9 + j + 9, j * 18 + 12, i * 18 + 165));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(playerInventory, i, i * 18 + 12, 223));
		}

		while (inventoryItemStacks.size() > 12 * 8 + 9 * 4)
		{
			inventoryItemStacks.remove(inventoryItemStacks.size() - 1);
		}
	}

	protected abstract void refreshSlotChest();

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return chestInventory.isUseableByPlayer(player);
	}

	@Override
	public abstract ItemStack transferStackInSlot(EntityPlayer player, int index);

	public int getScrollIndex()
	{
		return scrollIndex;
	}

	public void setScrollIndex(int index)
	{
		if (index != scrollIndex)
		{
			scrollIndex = Math.max(0, Math.min(index, getScrollMax()));

			refreshSlot();
		}
	}

	public abstract int getScrollMax();

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);

		chestInventory.closeInventory();
	}
}