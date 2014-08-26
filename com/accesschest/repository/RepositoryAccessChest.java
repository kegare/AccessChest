package com.accesschest.repository;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.accesschest.block.AccessBlocks;
import com.accesschest.tileentity.TileEntityAccessChest;

public class RepositoryAccessChest extends Repository
{
	public RepositoryAccessChest(World world, int color, int grade, boolean isOriginal)
	{
		super((DataManagerWorldData)world.loadItemData(DataManagerWorldData.class, "AccessChest" + color), color, grade, isOriginal);
	}

	@Override
	public String getInventoryName()
	{
		return AccessBlocks.access_chest.getLocalizedName();
	}

	public void extractInventory(IInventory inventory)
	{
		if (isTheSameColoredAccessChest(inventory))
		{
			return;
		}

		for (int i = 0; i < inventory.getSizeInventory(); ++i)
		{
			ItemStack itemstack = inventory.getStackInSlot(i);

			if (itemstack != null)
			{
				int prev = itemstack.stackSize;

				storeItem(itemstack);

				if (prev == itemstack.stackSize)
				{
					return;
				}

				if (itemstack.stackSize == 0)
				{
					inventory.setInventorySlotContents(i, null);
				}
			}
		}
	}

	public void pourInventory(IInventory inventory)
	{
		if (isTheSameColoredAccessChest(inventory))
		{
			return;
		}

		int index = 0;

		for (int i = 0; i < inventory.getSizeInventory() && i < getSizeInventory(); ++i)
		{
			if (inventory.getStackInSlot(i) == null)
			{
				ItemStack itemstack = null;

				while (itemstack == null)
				{
					if (index >= getSizeInventory())
					{
						return;
					}

					itemstack = getStackInSlot(index);

					setInventorySlotContents(index, null);

					++index;
				}

				inventory.setInventorySlotContents(i, itemstack);
			}
		}
	}

	public boolean isTheSameColoredAccessChest(IInventory inventory)
	{
		if (inventory instanceof TileEntityAccessChest)
		{
			return color == ((TileEntityAccessChest)inventory).getRepository().getColor();
		}
		else if (inventory instanceof RepositoryAccessChest)
		{
			return color == ((RepositoryAccessChest)inventory).getColor();
		}

		return false;
	}
}