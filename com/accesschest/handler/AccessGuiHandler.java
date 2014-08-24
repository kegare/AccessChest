package com.accesschest.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.accesschest.client.gui.ContainerChestClient;
import com.accesschest.client.gui.GuiAccessChest;
import com.accesschest.container.ContainerChestServer;
import com.accesschest.core.AccessChest;
import com.accesschest.repository.Repository;
import com.accesschest.repository.RepositoryAccessChest;
import com.accesschest.tileentity.TileEntityAbstractChest;
import com.accesschest.util.AccessUtils;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AccessGuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (0 <= id && id < 16 * 4 * 2)
		{
			return new ContainerChestServer(new RepositoryAccessChest(world, AccessUtils.getColor(id), AccessUtils.getGrade(id), false), player.inventory);
		}
		else if (id == AccessChest.GUI_ID_TILEENTITY)
		{
			return new ContainerChestServer((TileEntityAbstractChest)world.getTileEntity(x, y, z), player.inventory);
		}

		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (0 <= id && id < 16 * 4 * 2 || id == AccessChest.GUI_ID_TILEENTITY)
		{
			if (id == AccessChest.GUI_ID_TILEENTITY)
			{
				TileEntityAbstractChest entity = (TileEntityAbstractChest)world.getTileEntity(x, y, z);
				Repository repo = entity.getRepository();

				id = AccessUtils.getChestId(repo.getColor(), repo.getGrade(), repo.isOriginal());
			}

			return new GuiAccessChest(new ContainerChestClient(player.inventory), id);
		}

		return null;
	}
}