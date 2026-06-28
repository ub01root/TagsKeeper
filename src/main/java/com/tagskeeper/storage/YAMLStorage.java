package com.tagskeeper.storage;

import com.tagskeeper.Main;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.configuration.file.YamlConfiguration;

public class YAMLStorage {
   private final File file = new File(Main.getInstance().getDataFolder(), "data.yml");
   private final YamlConfiguration config;

   public YAMLStorage() {
      if (!this.file.exists()) {
         try {
            this.file.createNewFile();
         } catch (IOException e) {
            Main.getInstance().getLogger().log(Level.SEVERE, "Failed to create data.yml", e);
         }
      }

      this.config = YamlConfiguration.loadConfiguration(this.file);
   }

   public void saveTag(UUID uuid, String tag) {
      if (tag == null) {
         this.config.set(uuid.toString(), (Object)null);
      } else {
         this.config.set(uuid.toString(), tag);
      }

      this.save();
   }

   public String getTag(UUID uuid) {
      return this.config.getString(uuid.toString());
   }

   private void save() {
      try {
         this.config.save(this.file);
      } catch (IOException e) {
         Main.getInstance().getLogger().log(Level.SEVERE, "Failed to save data.yml", e);
      }

   }
}
