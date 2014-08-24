package com.accesschest.block;

import java.util.Random;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.accesschest.client.particle.EntityChestFX;
import com.accesschest.tileentity.TileEntityAccessChest;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAccessChest extends BlockAbstractChest
{
	public BlockAccessChest(String name)
	{
		super(name);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return new TileEntityAccessChest();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random)
	{
		for (int i = 0; i < 3; ++i)
		{
			int var1 = random.nextInt(2) * 2 - 1;
			int var2 = random.nextInt(2) * 2 - 1;
			double ptX = x + 0.5D + 0.25D * var1;
			double ptY = y + random.nextFloat();
			double ptZ = z + 0.5D + 0.25D * var2;
			double motionX = random.nextFloat() * 1.0F * var1;
			double motionY = (random.nextFloat() - 0.5D) * 0.125D;
			double motionZ = random.nextFloat() * 1.0F * var2;
			EntityFX particle = new EntityChestFX(world, ptX, ptY, ptZ, motionX, motionY, motionZ);

			FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
		}
	}
}