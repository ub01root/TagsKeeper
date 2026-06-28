package com.tagskeeper.utils;

import com.tagskeeper.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class MessageUtil {
   public static String get(String path) {
      FileConfiguration config = Main.getInstance().getConfigManager().getMessages();
      String message = config.getString(path);
      return message == null ? "Message not found: " + path : ColorUtil.color(message.replace("%prefix%", config.getString("prefix")));
   }
}
