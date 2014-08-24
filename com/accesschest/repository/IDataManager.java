package com.accesschest.repository;

import net.minecraft.item.ItemStack;

public interface IDataManager
{
	public void setItem(int index, ItemStack itemstack);

	public ItemStack getItem(int index);

	public int getMaxSize();

	public ComparatorChest getComparator();
}