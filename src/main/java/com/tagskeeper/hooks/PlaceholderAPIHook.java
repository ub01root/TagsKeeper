package com.tagskeeper.hooks;

import com.tagskeeper.Main;
import com.tagskeeper.models.PlayerTagData;
import com.tagskeeper.models.Tag;
import com.tagskeeper.utils.ColorUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion {
   public @NotNull String getIdentifier() {
      return "tagskeeper";
   }

   public @NotNull String getAuthor() {
      return "DevAdvvy";
   }

   public @NotNull String getVersion() {
      return "1.0";
   }

   public boolean persist() {
      return true;
   }

   public String onPlaceholderRequest(Player player, @NotNull String params) {
      if (player == null) {
         return "";
      } else if (params.equalsIgnoreCase("tag")) {
         PlayerTagData data = Main.getInstance().getPlayerDataManager().get(player.getUniqueId());
         if (data == null) {
            return "";
         } else if (data.getSelectedTag() == null) {
            return "";
         } else {
            Tag tag = Main.getInstance().getTagManager().getTag(data.getSelectedTag());
            return tag == null ? "" : ColorUtil.color(tag.getTag());
         }
      } else {
         return "";
      }
   }
}
