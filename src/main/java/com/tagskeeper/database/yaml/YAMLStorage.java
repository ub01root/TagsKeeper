package com.tagskeeper.database.yaml;

import com.tagskeeper.Main;
import com.tagskeeper.database.Storage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.configuration.file.YamlConfiguration;

public class YAMLStorage implements Storage {
   private final File file;
   private final YamlConfiguration config;

   public YAMLStorage() {
      File dir = new File(Main.getInstance().getDataFolder(), "database/YAML");
      if (!dir.exists()) { dir.mkdirs(); }
      this.file = new File(dir, "data.yml");
      if (!this.file.exists()) {
         try { this.file.createNewFile(); }
         catch (IOException e) { Main.getInstance().getLogger().log(Level.SEVERE, "Failed to create data.yml", e); }
      }
      this.config = YamlConfiguration.loadConfiguration(this.file);
   }

   public void saveTag(UUID uuid, String tag) {
      if (tag == null) { this.config.set(uuid.toString(), null); }
      else { this.config.set(uuid.toString(), tag); }
      this.save();
   }

   public String getTag(UUID uuid) { return this.config.getString(uuid.toString()); }

   public boolean hasPurchased(UUID uuid, String tagId) {
      return this.config.getBoolean("purchased." + uuid.toString() + "." + tagId, false);
   }

   public void setPurchased(UUID uuid, String tagId) {
      this.config.set("purchased." + uuid.toString() + "." + tagId, true);
      this.save();
   }

   public void close() {}

   private void save() {
      try { this.config.save(this.file); }
      catch (IOException e) { Main.getInstance().getLogger().log(Level.SEVERE, "Failed to save data.yml", e); }
   }
}
