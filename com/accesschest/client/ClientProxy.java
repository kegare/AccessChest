package com.accesschest.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import com.accesschest.block.BlockAbstractChest;
import com.accesschest.client.renderer.TileEntityAbstractChestRenderer;
import com.accesschest.core.AccessChest;
import com.accesschest.core.CommonProxy;
import com.accesschest.tileentity.TileEntityAbstractChest;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	public static final KeyBinding collectiveTransferKey = new KeyBinding("accesschest.key.collectiveTransfer", Keyboard.KEY_X, AccessChest.MOD_NAME);
	public static final KeyBinding autoCollectKey = new KeyBinding("accesschest.key.autoCollect", Keyboard.KEY_L, AccessChest.MOD_NAME);

	@Override
	public void registerRenderers()
	{
		TileEntityAbstractChestRenderer renderer = new TileEntityAbstractChestRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAbstractChest.class, renderer);
		BlockAbstractChest.RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(BlockAbstractChest.RENDER_ID, renderer);
	}

	@Override
	public void registerKeyBindings()
	{
		ClientRegistry.registerKeyBinding(collectiveTransferKey);
		ClientRegistry.registerKeyBinding(autoCollectKey);
	}

	@Override
	public EntityPlayer getClientEntityPlayer()
	{
		return FMLClientHandler.instance().getClientPlayerEntity();
	}
}