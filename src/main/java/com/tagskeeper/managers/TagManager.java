package com.tagskeeper.managers;

import com.tagskeeper.Main;
import com.tagskeeper.models.Tag;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class TagManager {
   private final Map<String, Tag> tags = new HashMap();

   public TagManager() {
      this.loadTags();
   }

   public void loadTags() {
      this.tags.clear();
      File file = new File(Main.getInstance().getDataFolder(), "tags.yml");
      YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
      ConfigurationSection section = config.getConfigurationSection("tags");
      if (section != null) {
         for(String id : section.getKeys(false)) {
            String tag = section.getString(id + ".tag");
            String menuDisplay = section.getString(id + ".menu-display");
            String preview = section.getString(id + ".preview");
            String permission = section.getString(id + ".permission");
            String material = section.getString(id + ".material");
            int slot = section.getInt(id + ".slot");
            int page = section.getInt(id + ".page", 1);
            List<String> lore = section.getStringList(id + ".lore");
            this.tags.put(id.toLowerCase(), new Tag(id, tag, menuDisplay, preview, permission, material, slot, page, lore));
         }

      }
   }

   public Map<String, Tag> getTags() {
      return this.tags;
   }

   public Tag getTag(String id) {
      return id == null ? null : (Tag)this.tags.get(id.toLowerCase());
   }
}
