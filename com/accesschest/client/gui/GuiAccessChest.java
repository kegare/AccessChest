package com.accesschest.client.gui;

import invtweaks.api.container.ChestContainer;
import invtweaks.api.container.ContainerSection;
import invtweaks.api.container.ContainerSectionCallback;

import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.accesschest.core.AccessChest;
import com.accesschest.core.Config;
import com.accesschest.network.MessageEject;
import com.accesschest.network.MessageFilter;
import com.accesschest.network.MessageScrollIndex;
import com.accesschest.network.MessageSort;
import com.accesschest.network.MessageStore;
import com.google.common.collect.Maps;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@ChestContainer(rowSize = 12)
@SideOnly(Side.CLIENT)
public class GuiAccessChest extends GuiContainer
{
	private static final ResourceLocation chestGuiTexture = new ResourceLocation("accesschest", "textures/gui/chest.png");

	private static final int GUI_CLEAR_BUTTON_ID = 1;
	private static final int GUI_SORT_BUTTON_ID = 2;
	private static final int GUI_EJECT_BUTTON_ID = 3;
	private static final int GUI_STOREINV_BUTTON_ID = 4;
	private static final int GUI_STOREEQP_BUTTON_ID = 5;

	private static final Map<Integer, Integer> lastScrolledIndex = Maps.newHashMap();

	private final ContainerChestClient container;
	private final int chestId;

	private GuiTextField filterTextField;
	private GuiButton clearButton;
	private GuiButton sortButton;
	private GuiButton ejectButton;
	private GuiButton storeInvButton;
	private GuiButton storeEqpButton;

	private boolean isScrolling;

	public GuiAccessChest(ContainerChestClient container, int id)
	{
		super(container);
		this.container = container;
		this.chestId = id;
		this.xSize = 256;
		this.ySize = 256;
	}

	@Override
	public void initGui()
	{
		Keyboard.enableRepeatEvents(true);

		super.initGui();

		int left = guiLeft + 176;
		int line0 = guiTop + 9 + 156;
		int line1 = guiTop + 9 + 173;
		int line2 = line1 + 19;
		int line3 = line2 + 19;
		int butWidth = 68;
		int butHeight = 20;

		clearButton = new GuiButtonExt(GUI_CLEAR_BUTTON_ID, left, line1, butWidth, butHeight, I18n.format("gui.button.clear"));
		sortButton = new GuiButtonExt(GUI_SORT_BUTTON_ID, left, line2, butWidth, butHeight, I18n.format("gui.button.sort"));
		ejectButton = new GuiButtonExt(GUI_EJECT_BUTTON_ID, left, line3, butWidth, butHeight, I18n.format("gui.button.eject"));
		storeInvButton = new GuiButtonExt(GUI_STOREINV_BUTTON_ID, left, line2, butWidth, butHeight, I18n.format("gui.button.storeInventory"));
		storeEqpButton = new GuiButtonExt(GUI_STOREEQP_BUTTON_ID, left, line3, butWidth, butHeight, I18n.format("gui.button.storeEquipment"));

		updateButtonsDisplay(false);

		buttonList.clear();
		buttonList.add(clearButton);
		buttonList.add(sortButton);
		buttonList.add(ejectButton);
		buttonList.add(storeInvButton);
		buttonList.add(storeEqpButton);

		filterTextField = new GuiTextField(fontRendererObj, left, line0, 68, 16);
		filterTextField.setMaxStringLength(100);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		filterTextField.updateCursorCounter();
	}

	@Override
	public void drawScreen(int x, int y, float par3)
	{
		if (lastScrolledIndex.containsKey(chestId))
		{
			container.setScrollIndex(lastScrolledIndex.remove(chestId));

			AccessChest.network.sendToServer(new MessageScrollIndex(container.getScrollIndex()));
		}

		super.drawScreen(x, y, par3);

		filterTextField.drawTextBox();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseZ)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		mc.getTextureManager().bindTexture(chestGuiTexture);

		drawTexturedModalRect(guiLeft, guiTop + 9, 0, 0, xSize, ySize - 18);

		int k = container.getScrollMax();

		if (k != 0)
		{
			int scroll = (int)((142 - 15) * (double)container.getScrollIndex() / k);
			drawTexturedModalRect(guiLeft + 232, guiTop + 17 + scroll, 2, 239, 12, 15);
		}

		int opacity = 63;

		for (int a = 12 * 8 - 1; a >= container.inventorySlots.size() - 9 * 4; --a)
		{
			int x = a % 12;
			int y = a / 12;
			int xPos = guiLeft + 12 + x * 18;
			int yPos = guiTop + 9 + 8 + y * 18;

			drawGradientRect(xPos, yPos, xPos + 16, yPos + 16, opacity << 24, opacity << 24);
		}
	}

	@Override
	public void handleMouseInput()
	{
		super.handleMouseInput();

		int wheelDiff = MathHelper.clamp_int(Mouse.getDWheel(), -1, 1);

		if (wheelDiff != 0)
		{
			container.setScrollIndex(container.getScrollIndex() - wheelDiff * Config.wheelScrollAmount);

			AccessChest.network.sendToServer(new MessageScrollIndex(container.getScrollIndex()));
		}

		if (isScrolling && Mouse.isButtonDown(0))
		{
			scrollbarDragged(height - Mouse.getEventY() * height / mc.displayHeight - 1);

			AccessChest.network.sendToServer(new MessageScrollIndex(container.getScrollIndex()));
		}
		else
		{
			isScrolling = false;
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int code)
	{
		super.mouseClicked(x, y, code);

		filterTextField.mouseClicked(x, y, code);

		if (guiLeft + 232 <= x && x < guiLeft + 232 + 12 && guiTop + 17 <= y && y < guiTop + 17 + 142)
		{
			isScrolling = true;
		}
	}

	private void scrollbarDragged(int y)
	{
		float f = Math.max(0.0f, Math.min((y - (guiTop + 17) - 15 / 2) / (float)(142 - 15), 1.0f));

		container.setScrollIndex(Math.round(container.getScrollMax() * f));
	}

	@Override
	public void handleKeyboardInput()
	{
		super.handleKeyboardInput();

		updateButtonsDisplay(GuiConfig.isShiftKeyDown());
	}

	private void updateButtonsDisplay(boolean shift)
	{
		sortButton.visible = !shift;
		ejectButton.visible = !shift;
		storeInvButton.visible = shift;
		storeEqpButton.visible = shift;
	}

	@Override
	protected void keyTyped(char c, int code)
	{
		if (filterTextField.isFocused() && code != 1)
		{
			if (code == Keyboard.KEY_RETURN)
			{
				AccessChest.network.sendToServer(new MessageFilter("exact:" + filterTextField.getText()));
			}
			else
			{
				String prev = filterTextField.getText();

				filterTextField.textboxKeyTyped(c, code);

				String text = filterTextField.getText();

				if (!text.equals(prev))
				{
					AccessChest.network.sendToServer(new MessageFilter(text));
				}
			}
		}
		else
		{
			if (code == mc.gameSettings.keyBindChat.getKeyCode() || code == mc.gameSettings.keyBindCommand.getKeyCode())
			{
				filterTextField.setFocused(true);
			}
			else if (code == Keyboard.KEY_F)
			{
				int x = Mouse.getEventX() * width / mc.displayWidth;
				int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;
				Slot slot = getSlotAtPosition(x, y);

				if (slot != null && slot.getHasStack())
				{
					filterTextField.setText(slot.getStack().getDisplayName());

					AccessChest.network.sendToServer(new MessageFilter(filterTextField.getText()));
				}
			}
			else if (code == Keyboard.KEY_UP)
			{
				container.setScrollIndex(container.getScrollIndex() - 8);

				AccessChest.network.sendToServer(new MessageScrollIndex(container.getScrollIndex()));
			}
			else if (code == Keyboard.KEY_DOWN)
			{
				container.setScrollIndex(container.getScrollIndex() + 8);

				AccessChest.network.sendToServer(new MessageScrollIndex(container.getScrollIndex()));
			}
			else
			{
				super.keyTyped(c, code);
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		switch (button.id)
		{
			case GUI_CLEAR_BUTTON_ID:
				filterTextField.setText("");
				AccessChest.network.sendToServer(new MessageFilter(filterTextField.getText()));
				break;
			case GUI_SORT_BUTTON_ID:
				if (Keyboard.isKeyDown(Keyboard.KEY_1))
				{
					AccessChest.network.sendToServer(new MessageSort(1));
				}
				else if (Keyboard.isKeyDown(Keyboard.KEY_2))
				{
					AccessChest.network.sendToServer(new MessageSort(2));
				}
				else if (Keyboard.isKeyDown(Keyboard.KEY_3))
				{
					AccessChest.network.sendToServer(new MessageSort(3));
				}
				else if (Keyboard.isKeyDown(Keyboard.KEY_4))
				{
					AccessChest.network.sendToServer(new MessageSort(4));
				}
				else if (Keyboard.isKeyDown(Keyboard.KEY_5))
				{
					AccessChest.network.sendToServer(new MessageSort(5));
				}
				else if (Keyboard.isKeyDown(Keyboard.KEY_6))
				{
					AccessChest.network.sendToServer(new MessageSort(6));
				}
				else if (Keyboard.isKeyDown(Keyboard.KEY_7))
				{
					AccessChest.network.sendToServer(new MessageSort(7));
				}
				else if (Keyboard.isKeyDown(Keyboard.KEY_8))
				{
					AccessChest.network.sendToServer(new MessageSort(8));
				}
				else if (Keyboard.isKeyDown(Keyboard.KEY_9))
				{
					AccessChest.network.sendToServer(new MessageSort(9));
				}
				else if (Keyboard.isKeyDown(Keyboard.KEY_0))
				{
					AccessChest.network.sendToServer(new MessageSort(0));
				}
				else
				{
					int prev = container.getScrollIndex();

					AccessChest.network.sendToServer(new MessageSort(-1));
					AccessChest.network.sendToServer(new MessageFilter(filterTextField.getText()));

					container.setScrollIndex(prev);
					AccessChest.network.sendToServer(new MessageScrollIndex(container.getScrollIndex()));
				}

				break;
			case GUI_EJECT_BUTTON_ID:
				AccessChest.network.sendToServer(new MessageEject());
				break;
			case GUI_STOREINV_BUTTON_ID:
				AccessChest.network.sendToServer(new MessageStore(0));
				break;
			case GUI_STOREEQP_BUTTON_ID:
				AccessChest.network.sendToServer(new MessageStore(1));
				break;
		}
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();

		if (chestId != AccessChest.GUI_ID_TILEENTITY)
		{
			lastScrolledIndex.put(chestId, container.getScrollIndex());
		}

		Keyboard.enableRepeatEvents(false);
	}

	protected Slot getSlotAtPosition(int x, int y)
	{
		for (int i = 0; i < inventorySlots.inventorySlots.size(); ++i)
		{
			Slot slot = (Slot)inventorySlots.inventorySlots.get(i);

			if (func_146978_c(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, x, y))
			{
				return slot;
			}
		}

		return null;
	}

	@ContainerSectionCallback
	public Map<ContainerSection, List<Slot>> getSlotTypes()
	{
		Map<ContainerSection, List<Slot>> map = Maps.newHashMap();
		List<Slot> slots = container.inventorySlots;
		int size = slots.size();

		map.put(ContainerSection.INVENTORY_HOTBAR, slots.subList(size - 9, size));
		map.put(ContainerSection.INVENTORY_NOT_HOTBAR, slots.subList(size - 36, size - 9));
		map.put(ContainerSection.CHEST, slots.subList(0, size - 36));

		return map;
	}
}