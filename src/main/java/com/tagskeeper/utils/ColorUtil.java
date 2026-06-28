package com.tagskeeper.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public class ColorUtil {
   private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

   public static String color(String text) {
      if (text == null) {
         return "";
      } else {
         for(Matcher matcher = HEX_PATTERN.matcher(text); matcher.find(); matcher = HEX_PATTERN.matcher(text)) {
            String color = matcher.group();
            text = text.replace(color, ChatColor.of(color.substring(1)).toString());
         }

         return ChatColor.translateAlternateColorCodes('&', text);
      }
   }
}
