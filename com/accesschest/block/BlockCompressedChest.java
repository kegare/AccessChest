package com.accesschest.block;

import com.accesschest.tileentity.TileEntityCompressedChest;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCompressedChest extends BlockAbstractChest
{
	public BlockCompressedChest(String name)
	{
		super(name);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityCompressedChest();
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
	{
		TileEntityCompressedChest entity = (TileEntityCompressedChest)world.getTileEntity(x, y, z);

		if (entity != null)
		{
			for (int i = 0; i < entity.getSizeInventory(); ++i)
			{
				ItemStack itemstack = entity.getStackInSlot(i);

				if (itemstack != null)
				{
					EntityItem item = new EntityItem(world, x + 0.5F, y + 0.5F, z + 0.5F, itemstack);
					item.motionY = 0.2F;

					world.spawnEntityInWorld(item);
				}
			}
		}

		super.breakBlock(world, x, y, z, block, metadata);
	}
}