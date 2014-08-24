package com.accesschest.block;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.accesschest.item.ItemAccessChest;
import com.accesschest.item.ItemCompressedChest;
import com.accesschest.recipe.RecipeChest;
import com.accesschest.recipe.RecipeColoringChest;
import com.accesschest.recipe.RecipeCopyAccessChest;
import com.accesschest.tileentity.TileEntityAccessChest;
import com.accesschest.tileentity.TileEntityCompressedChest;
import com.accesschest.util.AccessUtils;

import cpw.mods.fml.common.registry.GameRegistry;

public class AccessBlocks
{
	public static final BlockAccessChest access_chest = new BlockAccessChest("accessChest");
	public static final BlockCompressedChest compressed_chest = new BlockCompressedChest("compressedChest");

	public static void registerBlocks()
	{
		GameRegistry.registerBlock(access_chest, ItemAccessChest.class, "access_chest");
		GameRegistry.registerTileEntity(TileEntityAccessChest.class, "accessChest");

		ItemStack ac0 = new ItemStack(access_chest, 1, AccessUtils.getChestId(15, 0));
		ItemStack ac1 = new ItemStack(access_chest, 1, AccessUtils.getChestId(15, 1));
		ItemStack ac2 = new ItemStack(access_chest, 1, AccessUtils.getChestId(15, 2));
		ItemStack ac3 = new ItemStack(access_chest, 1, AccessUtils.getChestId(15, 3));
		ItemStack ac1c = new ItemStack(access_chest, 1, AccessUtils.getChestId(15, 1, false));
		ItemStack ac2c = new ItemStack(access_chest, 1, AccessUtils.getChestId(15, 2, false));
		ItemStack ac3c = new ItemStack(access_chest, 1, AccessUtils.getChestId(15, 3, false));

		GameRegistry.addShapelessRecipe(ac0, Blocks.chest, Items.ender_pearl);
		GameRegistry.addRecipe(new RecipeChest(new ItemStack(Blocks.lapis_block), ac0, ac1));
		GameRegistry.addRecipe(new RecipeChest(new ItemStack(Blocks.gold_block), ac1, ac2));
		GameRegistry.addRecipe(new RecipeChest(new ItemStack(Blocks.diamond_block), ac2, ac3));
		GameRegistry.addRecipe(new RecipeCopyAccessChest(new ItemStack(Blocks.ender_chest, 1), ac1, ac1c));
		GameRegistry.addRecipe(new RecipeCopyAccessChest(new ItemStack(Blocks.ender_chest, 3), ac2, ac2c));
		GameRegistry.addRecipe(new RecipeCopyAccessChest(new ItemStack(Blocks.ender_chest, 8), ac3, ac3c));

		for (int color = 0; color < 16; ++color)
		{
			for (int grade = 0; grade < 4; ++grade)
			{
				GameRegistry.addRecipe(new RecipeColoringChest(
					new ItemStack(Items.dye, 1, color),
					new ItemStack(access_chest, 1, AccessUtils.getChestId(15, grade)),
					new ItemStack(access_chest, 1, AccessUtils.getChestId(color, grade))
				));
			}
		}

		for (int color = 0; color < 16; ++color)
		{
			for (int grade = 0; grade < 4; ++grade)
			{
				GameRegistry.addRecipe(new RecipeColoringChest(
					new ItemStack(Items.dye, 1, color),
					new ItemStack(access_chest, 1, AccessUtils.getChestId(15, grade, false)),
					new ItemStack(access_chest, 1, AccessUtils.getChestId(color, grade, false))
				));
			}
		}

		GameRegistry.registerBlock(compressed_chest, ItemCompressedChest.class, "compressed_chest");
		GameRegistry.registerTileEntity(TileEntityCompressedChest.class, "compressedChest");

		ac0 = new ItemStack(compressed_chest, 1, AccessUtils.getChestId(15, 0));
		ac1 = new ItemStack(compressed_chest, 1, AccessUtils.getChestId(15, 1));
		ac2 = new ItemStack(compressed_chest, 1, AccessUtils.getChestId(15, 2));
		ac3 = new ItemStack(compressed_chest, 1, AccessUtils.getChestId(15, 3));

		GameRegistry.addShapelessRecipe(ac0, Blocks.chest, Items.diamond);
		GameRegistry.addRecipe(new RecipeChest(new ItemStack(Blocks.redstone_block), ac0, ac1));
		GameRegistry.addRecipe(new RecipeChest(new ItemStack(Blocks.gold_block), ac1, ac2));
		GameRegistry.addRecipe(new RecipeChest(new ItemStack(Blocks.diamond_block), ac2, ac3));

		for (int color = 0; color < 16; ++color)
		{
			for (int grade = 0; grade < 4; ++grade)
			{
				GameRegistry.addRecipe(new RecipeColoringChest(
					new ItemStack(Items.dye, 1, color),
					new ItemStack(compressed_chest, 1, AccessUtils.getChestId(15, grade)),
					new ItemStack(compressed_chest, 1, AccessUtils.getChestId(color, grade))
				));
			}
		}
	}
}