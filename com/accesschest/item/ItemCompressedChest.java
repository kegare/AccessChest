package com.accesschest.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.accesschest.util.AccessUtils;

public class ItemCompressedChest extends ItemAbstractChest
{
	public ItemCompressedChest(Block block)
	{
		super(block);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for (int i = 0; i < 16; ++i)
		{
			list.add(new ItemStack(item, 1, AccessUtils.getChestId(i, 0)));
			list.add(new ItemStack(item, 1, AccessUtils.getChestId(i, 1)));
			list.add(new ItemStack(item, 1, AccessUtils.getChestId(i, 2)));
			list.add(new ItemStack(item, 1, AccessUtils.getChestId(i, 3)));
		}
	}
}