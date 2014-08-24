package com.accesschest.recipe;

import com.accesschest.util.AccessUtils;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;

public class RecipeChest extends ShapedRecipes
{
	private final ItemStack center;
	private final ItemStack round;

	public RecipeChest(ItemStack center, ItemStack round, ItemStack output)
	{
		super(3, 3, new ItemStack[] {round, round, round, round, center, round, round, round, round}, output);
		this.center = center;
		this.round = round;

		if (round.getItem() != Item.getItemFromBlock(Blocks.chest))
		{
			AccessUtils.checkIsAbstractChest(round);
		}
	}

	@Override
	public boolean matches(InventoryCrafting inventory, World world)
	{
		boolean exist = false;

		for (int i = 0; i < 9; ++i)
		{
			ItemStack target = inventory.getStackInSlot(i);

			if (i == 4)
			{
				if (target == null || !center.isItemEqual(target))
				{
					return false;
				}
			}
			else
			{
				if (target == null || !round.isItemEqual(target) && !AccessUtils.isTheSameGrade(round, target) || !AccessUtils.isOriginal(target))
				{
					return false;
				}
			}

			if (target != null)
			{
				exist = true;
			}
		}

		return exist;
	}
}