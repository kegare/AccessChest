package com.accesschest.tileentity;

import com.accesschest.block.AccessBlocks;
import com.accesschest.repository.Repository;
import com.accesschest.repository.RepositoryCompressedChest;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityCompressedChest extends TileEntityAbstractChest
{
	@Override
	protected Repository createRepository(int color, int grade, boolean isOriginal)
	{
		return new RepositoryCompressedChest(color, grade);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		((RepositoryCompressedChest)repo).readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		((RepositoryCompressedChest)repo).writeToNBT(nbt);
	}

	@Override
	public void openInventory()
	{
		super.openInventory();

		worldObj.addBlockEvent(xCoord, yCoord, zCoord, AccessBlocks.compressed_chest, 1, numUsingPlayers);
	}

	@Override
	public void closeInventory()
	{
		super.closeInventory();

		worldObj.addBlockEvent(xCoord, yCoord, zCoord, AccessBlocks.compressed_chest, 1, numUsingPlayers);
	}
}