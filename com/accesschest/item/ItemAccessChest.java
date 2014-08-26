package com.accesschest.item;

import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

import com.accesschest.core.AccessChest;
import com.accesschest.repository.RepositoryAccessChest;
import com.accesschest.util.AccessUtils;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class ItemAccessChest extends ItemAbstractChest
{
	public static final Set<String> collectiveTransfer = Sets.newHashSet();

	private boolean coping;

	public ItemAccessChest(Block block)
	{
		super(block);

		FMLCommonHandler.instance().bus().register(this);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
	{
		if (!world.isRemote && !player.isSneaking())
		{
			world.playSoundAtEntity(player, "random.click", 0.7F, 1.35F);

			player.openGui(AccessChest.instance, itemstack.getItemDamage(), world, 0, 0, 0);
		}

		return itemstack;
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity entity = world.getTileEntity(x, y, z);

		if (!world.isRemote && entity instanceof IInventory && collectiveTransfer.contains(player.getUniqueID().toString()))
		{
			RepositoryAccessChest repo = new RepositoryAccessChest(world,
				AccessUtils.getColor(itemstack.getItemDamage()), AccessUtils.getGrade(itemstack.getItemDamage()), AccessUtils.isOriginal(itemstack.getItemDamage()));
			IInventory inventory = (IInventory)entity;

			if (entity instanceof TileEntityChest)
			{
				TileEntity chest;

				for (int i = 1; i <= 7; i += 2)
				{
					chest = world.getTileEntity(x + (i / 3 - 1), y, z + (i % 3 - 1));

					if (chest instanceof TileEntityChest)
					{
						inventory = new InventoryLargeChest("", inventory, (TileEntityChest)entity);

						break;
					}
				}
			}

			if (player.isSneaking())
			{
				repo.pourInventory(inventory);
			}
			else
			{
				repo.extractInventory(inventory);
			}
		}

		return super.onItemUseFirst(itemstack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if (player.isSneaking())
		{
			return super.onItemUse(itemstack, player, world, x, y, z, side, hitX, hitY, hitZ);
		}

		return false;
	}

	@Override
	public boolean placeBlockAt(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		if (player.isSneaking() && !collectiveTransfer.contains(player.getUniqueID().toString()))
		{
			return super.placeBlockAt(itemstack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
		}

		return false;
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for (int color = 0; color < 16; ++color)
		{
			for (int grade = 0; grade < 4; ++grade)
			{
				list.add(new ItemStack(item, 1, AccessUtils.getChestId(color, grade, true)));
				list.add(new ItemStack(item, 1, AccessUtils.getChestId(color, grade, false)));
			}
		}
	}

	@Override
	public boolean hasContainerItem()
	{
		return coping;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemstack)
	{
		ItemStack original = itemstack.copy();
		original.stackSize = 1;

		return original;
	}

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemstack)
	{
		return false;
	}

	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event)
	{
		if (event.crafting.getItem() != this)
		{
			return;
		}

		IInventory crafting = event.craftMatrix;
		boolean flag = false;

		for (int i = 0; i < crafting.getSizeInventory(); ++i)
		{
			ItemStack itemstack = crafting.getStackInSlot(i);

			if (itemstack != null && itemstack.isItemEqual(new ItemStack(Blocks.ender_chest)))
			{
				flag = true;

				break;
			}
		}

		coping = flag;
	}
}