package com.hidebankquantity;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(HideBankQuantityConfig.GROUP)
public interface HideBankQuantityConfig extends Config
{
	String GROUP = "hidebankquantity";

	@ConfigItem(
		keyName = "onlyListedItems",
		name = "Only listed items",
		description = "Only hide the quantity on the items listed below. When off, every item in the bank has its quantity hidden.",
		position = 1
	)
	default boolean onlyListedItems()
	{
		return false;
	}

	@ConfigItem(
		keyName = "itemList",
		name = "Items",
		description = "Comma-separated item names to hide the quantity on. Supports * wildcards, e.g. 'Coins, *rune, Shark'",
		position = 2
	)
	default String itemList()
	{
		return "";
	}
}
