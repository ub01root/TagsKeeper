package com.tagskeeper.database;

import com.tagskeeper.Main;
import com.tagskeeper.database.yaml.YAMLStorage;
import com.tagskeeper.database.sqlite.SQLiteStorage;

public class StorageManager {
   private Storage storage;

   public StorageManager() {
      String type = Main.getInstance().getConfig().getString("storage.type", "YAML").toUpperCase();
      Main.getInstance().getLogger().info("Loading storage type: " + type);
      switch (type) {
         case "SQLITE":
            this.storage = new SQLiteStorage();
            break;
         case "MYSQL":
            this.storage = new MySQLStorage();
            break;
         case "REDIS":
            this.storage = new RedisStorage();
            break;
         case "YAML":
         default:
            this.storage = new YAMLStorage();
            break;
      }
   }

   public Storage getStorage() {
      return this.storage;
   }

   public void reload() {
      this.storage.close();
      String type = Main.getInstance().getConfig().getString("storage.type", "YAML").toUpperCase();
      Main.getInstance().getLogger().info("Reloading storage type: " + type);
      switch (type) {
         case "SQLITE":
            this.storage = new SQLiteStorage();
            break;
         case "MYSQL":
            this.storage = new MySQLStorage();
            break;
         case "REDIS":
            this.storage = new RedisStorage();
            break;
         case "YAML":
         default:
            this.storage = new YAMLStorage();
            break;
      }
   }
}
