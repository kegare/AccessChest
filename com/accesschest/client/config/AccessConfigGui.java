package com.accesschest.client.config;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;

import com.accesschest.core.AccessChest;
import com.accesschest.core.Config;
import com.google.common.collect.Lists;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AccessConfigGui extends GuiConfig
{
	public AccessConfigGui(GuiScreen parent)
	{
		super(parent, getConfigElements(), AccessChest.MODID, false, false, I18n.format("accesschest.config", AccessChest.MOD_NAME));
	}

	private static List<IConfigElement> getConfigElements()
	{
		List<IConfigElement> list = Lists.newArrayList();

		for (String category : Config.config.getCategoryNames())
		{
			list.addAll(new ConfigElement(Config.config.getCategory(category)).getChildElements());
		}

		return list;
	}
}