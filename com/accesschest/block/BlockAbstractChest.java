package com.accesschest.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.accesschest.core.AccessChest;
import com.accesschest.repository.Repository;
import com.accesschest.tileentity.TileEntityAbstractChest;
import com.accesschest.util.AccessUtils;

public abstract class BlockAbstractChest extends BlockContainer
{
	public static int RENDER_ID;

	protected BlockAbstractChest(String name)
	{
		super(Material.rock);
		this.setBlockName(name);
		this.setHardness(10.0F);
		this.setCreativeTab(AccessChest.tabAccessChest);
		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int dist, float distX, float distY, float distZ)
	{
		if (!world.isRemote)
		{
			player.openGui(AccessChest.instance, AccessChest.GUI_ID_TILEENTITY, world, x, y, z);
		}

		return true;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return RENDER_ID;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		blockIcon = iconRegister.registerIcon("iron_block");
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase living, ItemStack itemstack)
	{
		byte direction = 0;

		switch (MathHelper.floor_double(living.rotationYaw * 4.0F / 360.0F + 0.5D) & 3)
		{
			case 0:
				direction = 2;
				break;
			case 1:
				direction = 5;
				break;
			case 2:
				direction = 3;
				break;
			case 3:
				direction = 4;
				break;
		}

		world.setBlockMetadataWithNotify(x, y, z, direction, 2);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
	{
		TileEntityAbstractChest entity = (TileEntityAbstractChest)world.getTileEntity(x, y, z);
		Repository repo = entity.getRepository();
		ItemStack itemstack = new ItemStack(Item.getItemFromBlock(block), 1, AccessUtils.getChestId(repo.getColor(), repo.getGrade(), repo.isOriginal()));

		dropBlockAsItem(world, x, y, z, itemstack);

		super.breakBlock(world, x, y, z, block, metadata);
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		TileEntityAbstractChest entity = (TileEntityAbstractChest)world.getTileEntity(x, y, z);
		Repository repo = entity.getRepository();

		return new ItemStack(this, 1, AccessUtils.getChestId(repo.getColor(), repo.getGrade(), repo.isOriginal()));
	}
}