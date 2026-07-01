package com.tagskeeper;

import com.tagskeeper.commands.ReloadCommand;
import com.tagskeeper.commands.TagsCommand;
import com.tagskeeper.hooks.EconomyHook;
import com.tagskeeper.hooks.PlaceholderAPIHook;
import com.tagskeeper.listeners.InventoryListener;
import com.tagskeeper.listeners.JoinListener;
import com.tagskeeper.managers.ConfigManager;
import com.tagskeeper.managers.PlayerDataManager;
import com.tagskeeper.managers.TagManager;
import com.tagskeeper.database.Storage;
import com.tagskeeper.database.StorageManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
   private static Main instance;
   private TagManager tagManager;
   private PlayerDataManager playerDataManager;
   private Storage storage;
   private StorageManager storageManager;
   private ConfigManager configManager;
   private EconomyHook economyHook;

   public void onEnable() {
      if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
         (new PlaceholderAPIHook()).register();
      }

      instance = this;
      this.saveDefaultConfig();
      this.saveResource("messages.yml", false);
      this.saveResource("tags.yml", false);
      this.saveResource("menu.yml", false);
      this.configManager = new ConfigManager();
      this.storageManager = new StorageManager();
      this.storage = this.storageManager.getStorage();
      this.tagManager = new TagManager();
      this.playerDataManager = new PlayerDataManager();
      this.economyHook = new EconomyHook();
      this.economyHook.setup();
      this.getCommand("tags").setExecutor(new TagsCommand());
      this.getCommand("reloadtags").setExecutor(new ReloadCommand());
      this.getServer().getPluginManager().registerEvents(new JoinListener(), this);
      this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
      this.getLogger().info("┌──────────────────────────────────────┐");
      this.getLogger().info("│          TagsKeeper Enabled          │");
      this.getLogger().info("│                                      │");
      this.getLogger().info("│          Author: @DevAdvvy           │");
      this.getLogger().info("│      GitHub: github.com/DevAdvvy     │");
      this.getLogger().info("└──────────────────────────────────────┘");
   }

   public void onDisable() {
      if (this.storage != null) {
         this.storage.close();
      }
   }

   public static Main getInstance() {
      return instance;
   }

   public TagManager getTagManager() {
      return this.tagManager;
   }

   public PlayerDataManager getPlayerDataManager() {
      return this.playerDataManager;
   }

   public Storage getStorage() {
      return this.storage;
   }

   public StorageManager getStorageManager() {
      return this.storageManager;
   }

   public ConfigManager getConfigManager() {
      return this.configManager;
   }

   public EconomyHook getEconomyHook() {
      return this.economyHook;
   }
}
