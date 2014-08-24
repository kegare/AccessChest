package com.accesschest.recipe;

import com.accesschest.util.AccessUtils;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

public class RecipeCopyAccessChest extends ShapelessRecipes
{
	private final ItemStack original;
	private final ItemStack sub;

	public RecipeCopyAccessChest(ItemStack sub, ItemStack original, ItemStack output)
	{
		super(output, AccessUtils.recipeList(original, sub));
		this.original = original;
		this.sub = sub;

		AccessUtils.checkIsAbstractChest(original);
	}

	@Override
	public boolean matches(InventoryCrafting inventory, World world)
	{
		boolean existOriginal = false;
		int numSub = 0;

		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; ++j)
			{
				ItemStack target = inventory.getStackInRowAndColumn(j, i);

				if (target != null)
				{
					if (AccessUtils.isTheSameGrade(target, original))
					{
						existOriginal = true;
					}
					else if (sub.isItemEqual(target))
					{
						++numSub;
					}
				}
			}
		}

		return existOriginal && sub.stackSize == numSub;
	}
}