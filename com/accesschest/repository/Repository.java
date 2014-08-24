package com.accesschest.repository;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import com.accesschest.util.AccessUtils;

public abstract class Repository implements IInventory
{
	protected final IDataManager data;
	protected int color;
	protected int grade;
	protected boolean isOriginal;

	public Repository(IDataManager data, int color, int grade, boolean isOriginal)
	{
		this.data = data;
		this.color = color;
		this.grade = grade;
		this.isOriginal = isOriginal;
	}

	public int getColor()
	{
		return color;
	}

	public int getGrade()
	{
		return grade;
	}

	public boolean isOriginal()
	{
		return isOriginal;
	}

	@Override
	public int getSizeInventory()
	{
		return (int)(27 * Math.pow(8, grade));
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return data.getItem(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int amount)
	{
		ItemStack itemstack = data.getItem(index);

		if (itemstack == null)
		{
			return null;
		}
		else if (itemstack.stackSize <= amount)
		{
			data.setItem(index, null);

			return itemstack;
		}

		return itemstack.splitStack(amount);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack itemstack)
	{
		data.setItem(index, itemstack);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public void markDirty() {}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}

	public void storeItem(ItemStack itemstack)
	{
		for (int i = 0; i < getSizeInventory(); ++i)
		{
			ItemStack stored = data.getItem(i);

			if (stored == null)
			{
				data.setItem(i, itemstack.splitStack(Math.min(getInventoryStackLimit(), itemstack.stackSize)));
			}
			else if (AccessUtils.canMerge(stored, itemstack))
			{
				int trans = Math.max(0, Math.min(Math.min(stored.getMaxStackSize(), getInventoryStackLimit()) - stored.stackSize, itemstack.stackSize));

				stored.stackSize += trans;
				itemstack.stackSize -= trans;
			}

			if (itemstack.stackSize <= 0)
			{
				return;
			}
		}
	}

	public void sort()
	{
		ItemStack[] contents = new ItemStack[data.getMaxSize()];

		for (int i = 0; i < contents.length; ++i)
		{
			contents[i] = data.getItem(i);
		}

		Arrays.sort(contents, data.getComparator());
		contents = compact(contents);

		for (int i = 0; i < contents.length; ++i)
		{
			data.setItem(i, contents[i]);
		}
	}

	private ItemStack[] compact(ItemStack[] contents)
	{
		int count = 0;
		ItemStack[] compact = new ItemStack[contents.length];

		for (int i = 0; i < contents.length && contents[i] != null; ++i)
		{
			while (contents[i] != null)
			{
				if (compact[count] == null)
				{
					compact[count] = contents[i];
					contents[i] = null;
				}
				else if (AccessUtils.canMerge(compact[count], contents[i]))
				{
					int trans = Math.min(compact[count].getMaxStackSize() - compact[count].stackSize, contents[i].stackSize);

					compact[count].stackSize += trans;
					contents[i].stackSize -= trans;

					if (contents[i].stackSize == 0)
					{
						contents[i] = null;
					}

					if (compact[count].stackSize == compact[count].getMaxStackSize())
					{
						count++;
					}
				}
				else
				{
					count++;
				}
			}
		}

		return compact;
	}

	public int getPriority(int index)
	{
		return data.getComparator().getPriority(getStackInSlot(index));
	}

	public void setPriority(int index, int prior)
	{
		data.getComparator().setPriority(getStackInSlot(index), prior);
	}
}