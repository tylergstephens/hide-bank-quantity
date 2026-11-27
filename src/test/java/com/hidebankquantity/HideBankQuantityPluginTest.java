package com.hidebankquantity;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class HideBankQuantityPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(HideBankQuantityPlugin.class);
		RuneLite.main(args);
	}
}
