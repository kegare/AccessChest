package com.accesschest.core;

import java.io.File;
import java.util.List;

import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;

public class Config
{
	public static Configuration config;

	public static int wheelScrollAmount = 8;
	public static int ejectLimit = 96;

	public static void syncConfig()
	{
		if (config == null)
		{
			File file = new File(Loader.instance().getConfigDir(), AccessChest.MOD_NAME.replaceAll(" ", "") + ".cfg");
			config = new Configuration(file);

			try
			{
				config.load();
			}
			catch (Exception e)
			{
				File dest = new File(file.getParentFile(), file.getName() + ".bak");

				if (dest.exists())
				{
					dest.delete();
				}

				file.renameTo(dest);

				FMLLog.log(Level.ERROR, e, "A critical error occured reading the " + file.getName() + " file, defaults will be used - the invalid file is backed up at " + dest.getName());
			}
		}

		Side side = FMLCommonHandler.instance().getSide();
		String category = Configuration.CATEGORY_GENERAL;
		Property prop;
		List<String> propOrder = Lists.newArrayList();

		if (side.isClient())
		{
			prop = config.get(category, "wheelScrollAmount", wheelScrollAmount);
			prop.setMinValue(1).setMaxValue(Byte.MAX_VALUE).setLanguageKey("accesschest.config." + prop.getName());
			prop.comment = StatCollector.translateToLocal(prop.getLanguageKey() + ".tooltip");
			prop.comment += " [range: " + prop.getMinValue() + " ~ " + prop.getMaxValue() + ", default: " + prop.getDefault() + "]";
			prop.comment += Configuration.NEW_LINE;
			prop.comment += "Note: Client-side only.";
			propOrder.add(prop.getName());
			wheelScrollAmount = MathHelper.clamp_int(prop.getInt(), Integer.valueOf(prop.getMinValue()), Integer.valueOf(prop.getMaxValue()));
		}

		prop = config.get(category, "ejectLimit", ejectLimit);
		prop.setMinValue(1).setMaxValue(Short.MAX_VALUE).setLanguageKey("accesschest.config." + prop.getName());
		prop.comment = StatCollector.translateToLocal(prop.getLanguageKey() + ".tooltip");
		prop.comment += " [range: " + prop.getMinValue() + " ~ " + prop.getMaxValue() + ", default: " + prop.getDefault() + "]";
		prop.comment += Configuration.NEW_LINE;
		prop.comment += "Note: If multiplayer, server-side only.";
		propOrder.add(prop.getName());
		ejectLimit = MathHelper.clamp_int(prop.getInt(), Integer.valueOf(prop.getMinValue()), Integer.valueOf(prop.getMaxValue()));

		config.setCategoryPropertyOrder(category, propOrder);

		if (config.hasChanged())
		{
			config.save();
		}
	}
}