package com.tagskeeper.managers;

import com.tagskeeper.Main;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
   private FileConfiguration messages;
   private FileConfiguration menu;
   private File messagesFile;
   private File menuFile;

   public ConfigManager() {
      this.load();
   }

   public void load() {
      this.messagesFile = new File(Main.getInstance().getDataFolder(), "messages.yml");
      this.menuFile = new File(Main.getInstance().getDataFolder(), "menu.yml");
      this.messages = YamlConfiguration.loadConfiguration(this.messagesFile);
      this.menu = YamlConfiguration.loadConfiguration(this.menuFile);
   }

   public void reload() {
      this.load();
   }

   public FileConfiguration getMessages() {
      return this.messages;
   }

   public FileConfiguration getMenu() {
      return this.menu;
   }
}
