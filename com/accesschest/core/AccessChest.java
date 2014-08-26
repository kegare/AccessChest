package com.accesschest.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

import com.accesschest.block.AccessBlocks;
import com.accesschest.handler.AccessEventHooks;
import com.accesschest.handler.AccessGuiHandler;
import com.accesschest.item.ItemAbstractChest;
import com.accesschest.network.MessageAccessChest;
import com.accesschest.network.MessageAutoCollect;
import com.accesschest.network.MessageCollectiveTransfer;
import com.accesschest.network.MessageEject;
import com.accesschest.network.MessageFilter;
import com.accesschest.network.MessageScrollIndex;
import com.accesschest.network.MessageSort;
import com.accesschest.network.MessageStore;
import com.accesschest.util.AccessUtils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod
(
	modid = AccessChest.MODID,
	guiFactory = AccessChest.MOD_PACKAGE + "client.config.AccessGuiFactory"
)
public class AccessChest
{
	public static final String
	MODID = "accesschest",
	MOD_NAME = "Access Chest+",
	MOD_PACKAGE = "com.accesschest.";

	public static final int
	GUI_ID_TILEENTITY = -1,
	DEFAULT_PRIORITY = 7;

	@Instance(MODID)
	public static AccessChest instance;

	@SidedProxy(clientSide = MOD_PACKAGE + "client.ClientProxy", serverSide = MOD_PACKAGE + "core.CommonProxy")
	public static CommonProxy proxy;

	public static final SimpleNetworkWrapper network = new SimpleNetworkWrapper(MODID);

	public static CreativeTabs tabAccessChest = new CreativeTabs(MOD_NAME)
	{
		@SideOnly(Side.CLIENT)
		@Override
		public String getTranslatedTabLabel()
		{
			return getTabLabel();
		}

		@SideOnly(Side.CLIENT)
		@Override
		public Item getTabIconItem()
		{
			if (Config.accessChest)
			{
				return Item.getItemFromBlock(AccessBlocks.access_chest);
			}
			else if (Config.compressedChest)
			{
				return Item.getItemFromBlock(AccessBlocks.compressed_chest);
			}

			return Item.getItemFromBlock(Blocks.chest);
		}

		@SideOnly(Side.CLIENT)
		@Override
		public int func_151243_f()
		{
			return getTabIconItem() instanceof ItemAbstractChest ? AccessUtils.getChestId(15, 0) : 0;
		}
	};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Config.syncConfig();

		AccessBlocks.registerBlocks();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		byte id = 0;

		AccessChest.network.registerMessage(MessageScrollIndex.class, MessageScrollIndex.class, id++, Side.SERVER);
		AccessChest.network.registerMessage(MessageFilter.class, MessageFilter.class, id++, Side.SERVER);
		AccessChest.network.registerMessage(MessageSort.class, MessageSort.class, id++, Side.SERVER);
		AccessChest.network.registerMessage(MessageStore.class, MessageStore.class, id++, Side.SERVER);
		AccessChest.network.registerMessage(MessageEject.class, MessageEject.class, id++, Side.SERVER);
		AccessChest.network.registerMessage(MessageCollectiveTransfer.class, MessageCollectiveTransfer.class, id++, Side.SERVER);
		AccessChest.network.registerMessage(MessageAutoCollect.class, MessageAutoCollect.class, id++, Side.CLIENT);
		AccessChest.network.registerMessage(MessageAutoCollect.class, MessageAutoCollect.class, id++, Side.SERVER);
		AccessChest.network.registerMessage(MessageAccessChest.class, MessageAccessChest.class, id++, Side.CLIENT);

		proxy.registerRenderers();
		proxy.registerKeyBindings();

		FMLCommonHandler.instance().bus().register(AccessEventHooks.instance);

		MinecraftForge.EVENT_BUS.register(AccessEventHooks.instance);

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new AccessGuiHandler());
	}
}