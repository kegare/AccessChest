package com.accesschest.repository;

import net.minecraft.item.ItemStack;

public class DataManagerArray implements IDataManager
{
	private final ItemStack[] contents;

	public DataManagerArray()
	{
		this.contents = new ItemStack[13824];
	}

	@Override
	public void setItem(int index, ItemStack itemstack)
	{
		contents[index] = itemstack;
	}

	@Override
	public ItemStack getItem(int index)
	{
		return contents[index];
	}

	@Override
	public int getMaxSize()
	{
		return contents.length;
	}

	@Override
	public ComparatorChest getComparator()
	{
		return new ComparatorChest();
	}
}