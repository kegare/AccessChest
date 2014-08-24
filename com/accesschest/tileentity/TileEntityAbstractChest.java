package com.accesschest.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;

import com.accesschest.core.AccessChest;
import com.accesschest.network.MessageAccessChest;
import com.accesschest.repository.Repository;
import com.accesschest.repository.RepositoryDammy;

public abstract class TileEntityAbstractChest extends TileEntity implements IInventory
{
	public float lidAngle;
	public float prevLidAngle;

	protected Repository repo;
	protected int numUsingPlayers;

	private int updateEntityTick;

	public Repository getRepository()
	{
		if (repo == null)
		{
			return new RepositoryDammy(0);
		}

		return repo;
	}

	protected abstract Repository createRepository(int color, int grade, boolean isOriginal);

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		byte color = nbt.getByte("Color");
		byte grade = nbt.getByte("Grade");
		boolean isOriginal = nbt.getBoolean("Original");

		setColorAndGrade(color, grade, isOriginal);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setByte("Color", (byte) (getRepository().getColor() & 0xF));
		nbt.setByte("Grade", (byte) (getRepository().getGrade() & 0xF));
		nbt.setBoolean("Original", getRepository().isOriginal());
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return AccessChest.network.getPacketFrom(new MessageAccessChest(xCoord, yCoord, zCoord, getRepository().getColor(), getRepository().getGrade(), getRepository().isOriginal()));
	}

	public void setColorAndGrade(int color, int grade, boolean isOriginal)
	{
		repo = createRepository(color, grade, isOriginal);
	}

	public int getColor()
	{
		return getRepository().getColor();
	}

	@Override
	public int getSizeInventory()
	{
		return getRepository().getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return getRepository().getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int amount)
	{
		return getRepository().decrStackSize(index, amount);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		return getRepository().getStackInSlotOnClosing(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack)
	{
		getRepository().setInventorySlotContents(slot, itemstack);
	}

	@Override
	public String getInventoryName()
	{
		return getRepository().getInventoryName();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return getRepository().getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord) == this)
		{
			return player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64;
		}

		return false;
	}

	@Override
	public void openInventory()
	{
		getRepository().openInventory();

		++numUsingPlayers;
	}

	@Override
	public void closeInventory()
	{
		getRepository().closeInventory();

		--numUsingPlayers;
	}

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

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (++updateEntityTick % 20 * 4 == 0)
		{
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord), 1, numUsingPlayers);
		}

		prevLidAngle = lidAngle;

		float var1 = 0.1F;
		double var4;

		if (numUsingPlayers > 0 && lidAngle == 0.0F)
		{
			double var2 = xCoord + 0.5D;
			var4 = zCoord + 0.5D;

			worldObj.playSoundEffect(var2, yCoord + 0.5D, var4, "random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F)
		{
			float var8 = lidAngle;

			if (numUsingPlayers > 0)
			{
				lidAngle += var1;
			}
			else
			{
				lidAngle -= var1;
			}

			if (lidAngle > 1.0F)
			{
				lidAngle = 1.0F;
			}

			float var3 = 0.5F;

			if (lidAngle < var3 && var8 >= var3)
			{
				var4 = xCoord + 0.5D;
				double var6 = zCoord + 0.5D;

				worldObj.playSoundEffect(var4, yCoord + 0.5D, var6, "random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (lidAngle < 0.0F)
			{
				lidAngle = 0.0F;
			}
		}
	}

	@Override
	public boolean receiveClientEvent(int par1, int par2)
	{
		if (par1 == 1)
		{
			numUsingPlayers = par2;

			return true;
		}

		return super.receiveClientEvent(par1, par2);
	}

	@Override
	public void invalidate()
	{
		updateContainingBlockInfo();

		super.invalidate();
	}
}