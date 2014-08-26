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
		for (int color = 0; color < 16; ++color)
		{
			for (int grade = 0; grade < 4; ++grade)
			{
				list.add(new ItemStack(item, 1, AccessUtils.getChestId(color, grade)));
			}
		}
	}
}