package com.hidebankquantity;

import com.google.inject.Provides;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ScriptID;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.ItemQuantityMode;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import net.runelite.client.util.WildcardMatcher;

@Slf4j
@PluginDescriptor(
	name = "Hide Bank Quantity",
	description = "Hides the quantity numbers on bank items so you can see the item icons",
	tags = {"bank", "quantity", "stack", "count", "hide", "number"}
)
public class HideBankQuantityPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ItemManager itemManager;

	@Inject
	private HideBankQuantityConfig config;

	private List<String> itemPatterns = Collections.emptyList();

	@Override
	protected void startUp()
	{
		parseItemList();
		clientThread.invokeLater(() -> updateBank(true));
	}

	@Override
	protected void shutDown()
	{
		// Put the numbers back if the bank is open when the plugin is turned off
		clientThread.invokeLater(() -> updateBank(false));
	}

	@Subscribe
	public void onScriptPostFired(ScriptPostFired event)
	{
		// The bank rebuilds its item widgets (and resets their quantity mode)
		// every time it opens, scrolls tabs, searches, or items change.
		if (event.getScriptId() == ScriptID.BANKMAIN_BUILD)
		{
			updateBank(true);
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!HideBankQuantityConfig.GROUP.equals(event.getGroup()))
		{
			return;
		}

		parseItemList();
		clientThread.invokeLater(() ->
		{
			// Restore everything first so items removed from the list get their numbers back
			updateBank(false);
			updateBank(true);
		});
	}

	private void updateBank(boolean hide)
	{
		Widget container = client.getWidget(InterfaceID.Bankmain.ITEMS);
		if (container == null)
		{
			return;
		}

		Widget[] items = container.getDynamicChildren();
		if (items == null)
		{
			return;
		}

		for (Widget item : items)
		{
			int itemId = item.getItemId();
			if (itemId <= 0 || item.isHidden())
			{
				continue;
			}

			if (!hide)
			{
				item.setItemQuantityMode(ItemQuantityMode.ALWAYS);
			}
			else if (shouldHide(itemId))
			{
				item.setItemQuantityMode(ItemQuantityMode.NEVER);
			}
		}
	}

	private boolean shouldHide(int itemId)
	{
		if (!config.onlyListedItems())
		{
			return true;
		}

		// canonicalize so placeholders and noted items match their normal name
		String name = itemManager.getItemComposition(itemManager.canonicalize(itemId)).getName();
		for (String pattern : itemPatterns)
		{
			if (WildcardMatcher.matches(pattern, name))
			{
				return true;
			}
		}
		return false;
	}

	private void parseItemList()
	{
		itemPatterns = Text.fromCSV(config.itemList());
	}

	@Provides
	HideBankQuantityConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HideBankQuantityConfig.class);
	}
}
