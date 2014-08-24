package com.accesschest.client.particle;

import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityChestFX extends EntityPortalFX
{
	public EntityChestFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
	{
		super(world, x, y, z, motionX, motionY, motionZ);
		float f = this.rand.nextFloat() * 0.6F + 0.4F;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F * f * 0.8F;
	}
}