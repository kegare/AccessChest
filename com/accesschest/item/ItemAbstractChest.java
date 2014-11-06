package com.accesschest.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.accesschest.tileentity.TileEntityAbstractChest;
import com.accesschest.util.AccessUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemAbstractChest extends ItemBlock
{
	public ItemAbstractChest(Block block)
	{
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		boolean result = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);

		((TileEntityAbstractChest)world.getTileEntity(x, y, z)).setColorAndGrade(
			AccessUtils.getColor(stack.getItemDamage()), AccessUtils.getGrade(stack.getItemDamage()), AccessUtils.isOriginal(stack.getItemDamage()));

		return result;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean advanced)
	{
		String str = "Class " + AccessUtils.getGrade(itemstack.getItemDamage());

		if (!AccessUtils.isOriginal(itemstack.getItemDamage()))
		{
			str += " (Copy)";
		}

		list.add(EnumChatFormatting.GRAY + str);
	}
}
