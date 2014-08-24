package com.accesschest.repository;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class DataManagerWorldData extends WorldSavedData implements IDataManager
{
	private final DataManagerNBT data = new DataManagerNBT();

	public DataManagerWorldData(String tag)
	{
		super(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		data.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		data.writeToNBT(nbt);
	}

	@Override
	public void setItem(int index, ItemStack itemstack)
	{
		data.setItem(index, itemstack);

		markDirty();
	}

	@Override
	public ItemStack getItem(int index)
	{
		return data.getItem(index);
	}

	@Override
	public int getMaxSize()
	{
		return data.getMaxSize();
	}

	@Override
	public ComparatorChest getComparator()
	{
		return data.getComparator();
	}
}