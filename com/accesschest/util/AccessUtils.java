package com.accesschest.util;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.accesschest.item.ItemAbstractChest;
import com.google.common.collect.Lists;

public class AccessUtils
{
	public static int getChestId(int color, int grade)
	{
		return getChestId(color, grade, true);
	}

	public static int getChestId(int color, int grade, boolean isOriginal)
	{
		return ((isOriginal ? 0 : 1) << 6) + ((grade & 3) << 4) + (color & 0xF);
	}

	public static int getColor(int id)
	{
		return id & 0xF;
	}

	public static int getGrade(int id)
	{
		return (id & 0x30) >> 4;
	}

	public static boolean isOriginal(int id)
	{
		return (id & 0x40) == 0;
	}

	public static boolean isOriginal(ItemStack itemstack)
	{
		return isOriginal(itemstack.getItemDamage());
	}

	public static boolean canMerge(ItemStack is1, ItemStack is2)
	{
		if (is1 == null || is2 == null)
		{
			return false;
		}
		else if (!is1.isItemEqual(is2) || !is1.isStackable() || !ItemStack.areItemStackTagsEqual(is1, is2))
		{
			return false;
		}

		return true;
	}

	public static boolean isTheSameGrade(ItemStack itemstack1, ItemStack itemstack2)
	{
		if (itemstack1 == null || itemstack2 == null)
		{
			return false;
		}
		else if (itemstack1.getItem() instanceof ItemAbstractChest && itemstack2.getItem() instanceof ItemAbstractChest)
		{
			if (itemstack1.getItem() == itemstack2.getItem())
			{
				return getGrade(itemstack1.getItemDamage()) == getGrade(itemstack2.getItemDamage());
			}

			return false;
		}

		return false;
	}

	public static void checkIsAbstractChest(ItemStack itemstack)
	{
		if (!(itemstack.getItem() instanceof ItemAbstractChest))
		{
			throw new IllegalArgumentException("invalid recipe for AccessChest MOD");
		}
	}

	public static List<ItemStack> recipeList(ItemStack original, ItemStack sub)
	{
		List<ItemStack> list = Lists.newArrayList();

		list.add(original);

		ItemStack itemstack = sub.copy();
		itemstack.stackSize = 1;

		for (int i = 0; i < sub.stackSize; ++i)
		{
			list.add(itemstack);
		}

		return list;
	}
}