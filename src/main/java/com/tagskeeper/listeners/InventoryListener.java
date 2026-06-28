package com.tagskeeper.listeners;

import com.tagskeeper.Main;
import com.tagskeeper.managers.MenuManager;
import com.tagskeeper.models.PlayerTagData;
import com.tagskeeper.models.Tag;
import com.tagskeeper.utils.ColorUtil;
import com.tagskeeper.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
   @EventHandler
   public void onClick(InventoryClickEvent e) {
      HumanEntity var3 = e.getWhoClicked();
      if (var3 instanceof Player player) {
         FileConfiguration var14 = Main.getInstance().getConfigManager().getMenu();
         int currentPage = MenuManager.getPage(player);
         String title = ColorUtil.color(var14.getString("menus.page-" + currentPage + ".title"));
         if (e.getView().getTitle().equals(title)) {
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item != null) {
               if (item.getType() != Material.AIR) {
                  if (item.hasItemMeta()) {
                     if (item.getItemMeta().hasDisplayName()) {
                        if (var14.contains("menus.page-" + currentPage + ".next-page-slot")) {
                           int nextSlot = var14.getInt("menus.page-" + currentPage + ".next-page-slot");
                           if (e.getSlot() == nextSlot) {
                              MenuManager.open(player, currentPage + 1);
                              return;
                           }
                        }

                        if (var14.contains("menus.page-" + currentPage + ".previous-page-slot")) {
                           int previousSlot = var14.getInt("menus.page-" + currentPage + ".previous-page-slot");
                           if (e.getSlot() == previousSlot) {
                              MenuManager.open(player, currentPage - 1);
                              return;
                           }
                        }

                        String pagePath = "menus.page-" + currentPage;
                        if (var14.contains(pagePath + ".remove-tag-slot")) {
                           int removeSlot = var14.getInt(pagePath + ".remove-tag-slot");
                           if (e.getSlot() == removeSlot) {
                              PlayerTagData data = Main.getInstance().getPlayerDataManager().get(player.getUniqueId());
                              if (data.getSelectedTag() == null) {
                                 player.sendMessage(MessageUtil.get("no-tag-equipped"));
                                 playSound(player, var14.getString("menus.sounds.denied"));
                                 return;
                              }

                              data.setSelectedTag((String)null);
                              Main.getInstance().getStorage().saveTag(player.getUniqueId(), (String)null);
                              player.sendMessage(MessageUtil.get("tag-removed"));
                              playSound(player, var14.getString("menus.sounds.select"));
                              player.closeInventory();
                              return;
                           }

                           boolean validSlot = Main.getInstance().getTagManager().getTags().values().stream().anyMatch((tagx) -> tagx.getSlot() == e.getSlot() && tagx.getPage() == currentPage);
                           if (!validSlot) {
                              return;
                           }

                           for(Tag tag : Main.getInstance().getTagManager().getTags().values()) {
                              if (e.getSlot() == tag.getSlot() && tag.getPage() == currentPage) {
                                 boolean hasPermission = player.hasPermission(tag.getPermission()) || player.hasPermission("alonsotags.tag." + tag.getId());
                                 if (!hasPermission) {
                                    player.sendMessage(MessageUtil.get("no-permission"));
                                    playSound(player, var14.getString("menus.sounds.denied"));
                                    return;
                                 }

                                 PlayerTagData data = Main.getInstance().getPlayerDataManager().get(player.getUniqueId());
                                 if (tag.getId().equalsIgnoreCase(data.getSelectedTag())) {
                                    player.sendMessage(MessageUtil.get("tag-already-selected"));
                                    playSound(player, var14.getString("menus.sounds.already-selected"));
                                    return;
                                 }

                                 data.setSelectedTag(tag.getId());
                                 Main.getInstance().getStorage().saveTag(player.getUniqueId(), tag.getId());
                                 player.sendMessage(MessageUtil.get("tag-selected").replace("%tag%", ColorUtil.color(tag.getTag())));
                                 playSound(player, var14.getString("menus.sounds.select"));
                                 player.closeInventory();
                                 return;
                              }
                           }
                        }

                     }
                  }
               }
            }
         }
      }
   }

   private void playSound(Player player, String soundName) {
      try {
         player.playSound(player.getLocation(), Sound.valueOf(soundName), 1.0F, 1.0F);
      } catch (IllegalArgumentException e) {
         Main.getInstance().getLogger().warning("Invalid sound in menu config: " + soundName);
      }
   }
}
