package com.accesschest.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.accesschest.block.BlockAbstractChest;
import com.accesschest.tileentity.TileEntityAbstractChest;
import com.accesschest.tileentity.TileEntityAccessChest;
import com.accesschest.util.AccessUtils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityAbstractChestRenderer extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler
{
	public static final int[] COLOR_CODES = new int[] {
		0x1e1b1b, 0xb3312c, 0x3b511a, 0x51301a, 0x253192, 0x7b2fbe, 0x287697, 0x9a9a9a, 0x434343, 0xd88198,
		0x41cd34, 0xdecf2a, 0x6689d3, 0xc354cd, 0xeb8844, 0xf0f0f0
	};

	private static final TileEntityAbstractChest tileEntityChestDammy = new TileEntityAccessChest();

	private final ModelChest model = new ModelChest();

	public void renderChest(int direction, int colorNum, float lidAngle, float prevLidAngle, double x, double y, double z, float ticks)
	{
		int color = COLOR_CODES[colorNum];
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;

		bindTexture(new ResourceLocation("accesschest", "textures/entity/chest/chest.png"));

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(r, g, b, 1.0F);
		GL11.glTranslatef((float)x, (float)y + 1.0F, (float)z + 1.0F);
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);

		short rotate = 0;

		switch (direction)
		{
			case 2:
				rotate = 180;
				break;
			case 3:
				rotate = 0;
				break;
			case 4:
				rotate = 90;
				break;
			case 5:
				rotate = -90;
				break;
		}

		GL11.glRotatef(rotate, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		float lidangle = prevLidAngle + (lidAngle - prevLidAngle) * ticks;
		lidangle = 1.0F - lidangle;
		lidangle = 1.0F - lidangle * lidangle * lidangle;

		model.chestLid.rotateAngleX = -(lidangle * 3.141593F / 2.0F);
		model.renderAll();

		GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void renderTileEntityAt(TileEntity raw, double x, double y, double z, float ticks)
	{
		TileEntityAbstractChest entity = (TileEntityAbstractChest)raw;
		int direction = 0;

		if (entity.hasWorldObj())
		{
			direction = entity.getBlockMetadata();
		}

		renderChest(direction, entity.getColor(), entity.lidAngle, entity.prevLidAngle, x, y, z, ticks);
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		tileEntityChestDammy.setColorAndGrade(AccessUtils.getColor(metadata), 0, false);

		TileEntityRendererDispatcher.instance.renderTileEntityAt(tileEntityChestDammy, 0.0D, 0.0D, 0.0D, 0.0F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return BlockAbstractChest.RENDER_ID;
	}
}