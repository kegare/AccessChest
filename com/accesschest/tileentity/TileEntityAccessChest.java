package com.accesschest.tileentity;

import com.accesschest.block.AccessBlocks;
import com.accesschest.repository.Repository;
import com.accesschest.repository.RepositoryAccessChest;

import net.minecraft.world.World;

public class TileEntityAccessChest extends TileEntityAbstractChest
{
	private int color;
	private int grade;
	private boolean isOriginal;

	@Override
	protected Repository createRepository(int color, int grade, boolean isOriginal)
	{
		if (worldObj != null)
		{
			return new RepositoryAccessChest(worldObj, color, grade, isOriginal);
		}

		return null;
	}

	@Override
	public void setColorAndGrade(int color, int grade, boolean isOriginal)
	{
		this.color = color;
		this.grade = grade;
		this.isOriginal = isOriginal;

		super.setColorAndGrade(color, grade, isOriginal);
	}

	@Override
	public void setWorldObj(World world)
	{
		super.setWorldObj(world);
		super.setColorAndGrade(color, grade, isOriginal);
	}

	@Override
	public int getColor()
	{
		return color;
	}

	@Override
	public void openInventory()
	{
		super.openInventory();

		worldObj.addBlockEvent(xCoord, yCoord, zCoord, AccessBlocks.access_chest, 1, numUsingPlayers);
	}

	@Override
	public void closeInventory()
	{
		super.closeInventory();

		worldObj.addBlockEvent(xCoord, yCoord, zCoord, AccessBlocks.access_chest, 1, numUsingPlayers);
	}
}