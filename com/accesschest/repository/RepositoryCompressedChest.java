package com.accesschest.repository;

import net.minecraft.nbt.NBTTagCompound;

import com.accesschest.block.AccessBlocks;

public class RepositoryCompressedChest extends Repository
{
	public RepositoryCompressedChest(int color, int grade)
	{
		super(new DataManagerNBT(), color, grade, true);
	}

	@Override
	public String getInventoryName()
	{
		return AccessBlocks.compressed_chest.getLocalizedName();
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		((DataManagerNBT)data).readFromNBT(nbt);
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		((DataManagerNBT)data).writeToNBT(nbt);
	}
}