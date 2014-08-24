package com.accesschest.container;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.accesschest.core.Config;
import com.accesschest.item.ItemAccessChest;
import com.accesschest.repository.Repository;
import com.accesschest.repository.RepositoryAccessChest;
import com.accesschest.tileentity.TileEntityAbstractChest;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class ContainerChestServer extends ContainerAbstractChest
{
	private int lastInventorySize;
	private int lastScrollIndex;

	private final List<Integer> filter = Lists.newArrayList();

	public ContainerChestServer(IInventory chestInventory, IInventory playerInventory)
	{
		super(chestInventory, playerInventory);
		this.setScrollIndex(0);
		this.setFilter("");
		this.lastInventorySize = -1;
		this.lastScrollIndex = -1;
	}

	@Override
	protected void refreshSlotChest()
	{
		int max = filter.size();

		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 12; j++)
			{
				int index = getDisplayBaseIndex() + i * 12 + j;

				if (index < max)
				{
					addSlotToContainer(new Slot(chestInventory, filter.get(index), j * 18 + 12, i * 18 + 17));
				}
			}
		}
	}

	protected int getDisplayBaseIndex()
	{
		return getScrollIndex() * 12;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack result = null;
		Slot slot = (Slot)inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack = slot.getStack();
			result = itemstack.copy();

			int border = inventorySlots.size() - 36;

			if (index < border)
			{
				if (!mergeItemStack(itemstack, border, inventorySlots.size(), false))
				{
					return null;
				}
			}
			else if (chestInventory instanceof RepositoryAccessChest && itemstack.getItem() instanceof ItemAccessChest || !customMergeItemStack(itemstack))
			{
				return null;
			}

			if (itemstack.stackSize == 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return result;
	}

	private boolean customMergeItemStack(ItemStack itemstack)
	{
		boolean isSuccess = false;
		int max = chestInventory.getSizeInventory();
		ItemStack chestStack;

		if (itemstack.isStackable())
		{
			for (int i = 0; i < max; ++i)
			{
				chestStack = chestInventory.getStackInSlot(i);

				if (chestStack != null && chestStack.getItem() == itemstack.getItem() &&
					(!itemstack.getHasSubtypes() || itemstack.getItemDamage() == chestStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack, chestStack))
				{
					int sum = chestStack.stackSize + itemstack.stackSize;

					if (sum <= chestStack.getMaxStackSize())
					{
						itemstack.stackSize = 0;
						chestStack.stackSize = sum;

						isSuccess = true;
					}
					else if (chestStack.stackSize < itemstack.getMaxStackSize())
					{
						itemstack.stackSize -= itemstack.getMaxStackSize() - chestStack.stackSize;
						chestStack.stackSize = chestStack.getMaxStackSize();

						isSuccess = true;
					}
				}

				if (itemstack.stackSize == 0)
				{
					break;
				}
			}
		}

		if (itemstack.stackSize > 0)
		{
			for (int i = 0; i < max; ++i)
			{
				chestStack = chestInventory.getStackInSlot(i);

				if (chestStack == null)
				{
					chestInventory.setInventorySlotContents(i, itemstack.copy());
					itemstack.stackSize = 0;

					isSuccess = true;

					break;
				}
			}
		}

		return isSuccess;
	}

	@Override
	public int getScrollMax()
	{
		return Math.max(0, (filter.size() - 1) / 12 - 7);
	}

	public void setFilter(String str)
	{
		filter.clear();

		int pri;

		if (Strings.isNullOrEmpty(str))
		{
			for (int index = 0; index < chestInventory.getSizeInventory(); ++index)
			{
				filter.add(index);
			}
		}
		else if ((pri = isPriotityViewCommand(str)) != -1)
		{
			Repository repo = getChestRepository();

			for (int index = 0; index < chestInventory.getSizeInventory(); ++index)
			{
				if (repo.getPriority(index) == pri)
				{
					filter.add(index);
				}
			}
		}
		else
		{
			for (int index = 0; index < chestInventory.getSizeInventory(); ++index)
			{
				if (isMatchFilter(chestInventory.getStackInSlot(index), str))
				{
					filter.add(index);
				}
			}
		}

		setScrollIndex(0);
		refreshSlot();
	}

	private boolean isMatchFilter(ItemStack itemstack, String filter)
	{
		if (itemstack == null)
		{
			return false;
		}

		List<String> list = Lists.newArrayList();
		list.add(itemstack.getDisplayName());
		list.add(itemstack.getItem().getUnlocalizedName());
		list.add(Item.itemRegistry.getNameForObject(itemstack.getItem()) + ":" + itemstack.getItemDamage());

		String exactFilter;

		if ((exactFilter = isExactlyMatchCommand(filter)) != null)
		{
			for (String str : list)
			{
				if (str != null && str.equals(exactFilter))
				{
					return true;
				}
			}
		}
		else
		{
			for (String str : list)
			{
				if (str != null && str.toLowerCase().contains(filter.toLowerCase()))
				{
					return true;
				}
			}
		}

		return false;
	}

	private int isPriotityViewCommand(String str)
	{
		if (str == null || !str.startsWith("pri:"))
		{
			return -1;
		}

		try
		{
			return Integer.valueOf(str.substring(4));
		}
		catch (NumberFormatException e)
		{
			return -1;
		}
	}

	private String isExactlyMatchCommand(String str)
	{
		if (str == null || !str.startsWith("exact:"))
		{
			return null;
		}

		return str.substring(6);
	}

	public void sort()
	{
		getChestRepository().sort();
	}

	public void eject(EntityPlayer player)
	{
		Repository repo = getChestRepository();
		int count = 0;

		for (Iterator<Integer> iterator = filter.iterator(); count < Config.ejectLimit && iterator.hasNext();)
		{
			int index = iterator.next();
			ItemStack target = repo.getStackInSlot(index);

			if (target != null)
			{
				repo.setInventorySlotContents(index, null);

				++count;

				player.entityDropItem(target, 0.0F);
			}

			++index;
		}
	}

	public void storeInventory()
	{
		int max = inventorySlots.size();

		for (int i = max - 9 * 4; i < max - 9; ++i)
		{
			transferStackInSlot(null, i);
		}
	}

	public void storeEquipment()
	{
		int max = inventorySlots.size();

		for (int i = max - 9; i < max; ++i)
		{
			transferStackInSlot(null, i);
		}
	}

	public void setPriorities(int priority)
	{
		Repository repo = getChestRepository();

		for (int i : filter)
		{
			repo.setPriority(i, priority);
		}
	}

	@Override
	public void detectAndSendChanges()
	{
		if (lastScrollIndex != scrollIndex || lastInventorySize != filter.size())
		{
			for (int i = 0; i < crafters.size(); ++i)
			{
				ICrafting crafter = (ICrafting)crafters.get(i);

				crafter.sendProgressBarUpdate(this, INFO_TYPE_SCROLL_INDEX, scrollIndex);
				crafter.sendProgressBarUpdate(this, INFO_TYPE_INVENTORY_SIZE, filter.size());
			}
		}

		lastScrollIndex = scrollIndex;
		lastInventorySize = filter.size();

		super.detectAndSendChanges();
	}

	protected Repository getChestRepository()
	{
		if (chestInventory instanceof Repository)
		{
			return (Repository)chestInventory;
		}
		else if (chestInventory instanceof TileEntityAbstractChest)
		{
			return ((TileEntityAbstractChest)chestInventory).getRepository();
		}
		else
		{
			throw new RuntimeException("unexpected IInventory object in chestInventory");
		}
	}
}