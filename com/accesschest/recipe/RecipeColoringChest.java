package com.accesschest.recipe;

import java.util.ArrayList;
import java.util.Arrays;

import com.accesschest.util.AccessUtils;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

public class RecipeColoringChest extends ShapelessRecipes
{
	private final ItemStack chest;
	private final ItemStack dye;

	public RecipeColoringChest(ItemStack dye, ItemStack itemstack, ItemStack output)
	{
		super(output, new ArrayList(Arrays.asList(new ItemStack[] {dye, itemstack})));
		this.chest = itemstack;
		this.dye = dye;

		AccessUtils.checkIsAbstractChest(itemstack);
	}

	@Override
	public boolean matches(InventoryCrafting inventory, World world)
	{
		boolean existChest = false;
		boolean existDye = false;

		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; ++j)
			{
				ItemStack target = inventory.getStackInRowAndColumn(j, i);

				if (target != null)
				{
					if (AccessUtils.isTheSameGrade(target, chest) && AccessUtils.isOriginal(target) == AccessUtils.isOriginal(chest))
					{
						existChest = true;
					}
					else if (dye.isItemEqual(target))
					{
						existDye = true;
					}
				}
			}
		}

		return existChest && existDye;
	}
}