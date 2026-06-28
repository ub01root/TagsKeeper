package com.tagskeeper.listeners;

import com.tagskeeper.Main;
import com.tagskeeper.models.PlayerTagData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
   @EventHandler
   public void onJoin(PlayerJoinEvent e) {
      Player player = e.getPlayer();
      String selectedTag = Main.getInstance().getStorage().getTag(player.getUniqueId());
      PlayerTagData data = Main.getInstance().getPlayerDataManager().get(player.getUniqueId());
      data.setSelectedTag(selectedTag);
   }
}
