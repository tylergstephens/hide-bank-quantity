# Hide Bank Quantity

A RuneLite plugin that hides the quantity numbers drawn on top of items in the bank,
so you can actually see the item icons.

## Settings
- **Only listed items** – off (default): hide the number on every bank item.
  On: only hide it on the items in the list below.
- **Items** – comma-separated item names, `*` wildcards allowed (e.g. `Coins, *rune, Shark`).

## Running it
1. Open this folder in IntelliJ IDEA (Java 11+ JDK).
2. Run `./gradlew run` (or run `HideBankQuantityPluginTest` from `src/test`).
3. Log in following https://github.com/runelite/runelite/wiki/Using-Jagex-Accounts
