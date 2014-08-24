package com.accesschest.repository;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class DataManagerNBT extends DataManagerArray
{
	private final ComparatorChest comparator = new ComparatorChest();

	public DataManagerNBT()
	{
		super();

		for (int i = 0; i < getMaxSize(); ++i)
		{
			setItem(i, null);
		}
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList list = (NBTTagList)nbt.getTag("Items");
		NBTTagCompound tag;

		for (int i = 0; i < getMaxSize(); ++i)
		{
			setItem(i, null);
		}

		for (int i = 0; i < list.tagCount(); ++i)
		{
			tag = list.getCompoundTagAt(i);
			int slot = tag.getInteger("Slot");

			if (0 <= slot && slot < getMaxSize())
			{
				setItem(slot, ItemStack.loadItemStackFromNBT(tag));
			}
		}

		tag = (NBTTagCompound)nbt.getTag("Comparator");

		if (tag != null)
		{
			comparator.readFromNBT(tag);
		}
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		NBTTagCompound tag;

		for (int i = 0; i < getMaxSize(); ++i)
		{
			ItemStack itemstack = getItem(i);

			if (itemstack != null)
			{
				tag = new NBTTagCompound();

				tag.setInteger("Slot", i);
				itemstack.writeToNBT(tag);
				list.appendTag(tag);
			}
		}

		nbt.setTag("Items", list);

		tag = new NBTTagCompound();

		comparator.writeToNBT(tag);

		nbt.setTag("Comparator", tag);
	}

	@Override
	public ComparatorChest getComparator()
	{
		return comparator;
	}
}