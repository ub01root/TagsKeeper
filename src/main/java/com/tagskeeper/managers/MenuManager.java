package com.tagskeeper.managers;

import com.tagskeeper.Main;
import com.tagskeeper.models.PlayerTagData;
import com.tagskeeper.models.Tag;
import com.tagskeeper.utils.ColorUtil;
import com.tagskeeper.utils.MessageUtil;
import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

public class MenuManager {
   private static final Map<UUID, Integer> pages = new HashMap();

   public static void open(Player player, int page) {
      if (page < 1) {
         page = 1;
      }
      FileConfiguration menu = Main.getInstance().getConfigManager().getMenu();
      String pagePath = "menus.page-" + page;
      pages.put(player.getUniqueId(), page);
      String title = ColorUtil.color(menu.getString(pagePath + ".title"));
      int size = menu.getInt(pagePath + ".size");
      Inventory inv = Bukkit.createInventory((InventoryHolder)null, size, title);
      if (menu.getBoolean("menus.fill-empty-slots")) {
         Material fillerMat;
         try {
            fillerMat = Material.valueOf(menu.getString("menus.filler.material"));
         } catch (Exception var22) {
            fillerMat = Material.BLACK_STAINED_GLASS_PANE;
         }

         ItemStack filler = new ItemStack(fillerMat);
         ItemMeta fillerMeta = filler.getItemMeta();
         fillerMeta.setDisplayName(ColorUtil.color(menu.getString("menus.filler.name")));
         filler.setItemMeta(fillerMeta);

         for(int i = 0; i < size; ++i) {
            inv.setItem(i, filler);
         }
      }

      PlayerTagData data = Main.getInstance().getPlayerDataManager().get(player.getUniqueId());

      for(Tag tag : Main.getInstance().getTagManager().getTags().values()) {
         if (tag.getPage() == page) {
            boolean unlocked = player.hasPermission(tag.getPermission()) || player.hasPermission("alonsotags.tag." + tag.getId());
            Material material;
            if (!unlocked) {
               material = Material.BARRIER;
            } else {
               try {
                  material = Material.valueOf(tag.getMaterial());
               } catch (Exception var21) {
                  material = Material.NAME_TAG;
               }
            }

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ColorUtil.color(tag.getMenuDisplay()));
            List<String> lore = new ArrayList();

            for(String line : tag.getLore()) {
               String preview = tag.getPreview().replace("{player}", player.getName());
               String status = unlocked ? MessageUtil.get("tag-status.unlocked") : MessageUtil.get("tag-status.locked");
               String selected = tag.getId().equalsIgnoreCase(data.getSelectedTag()) ? MessageUtil.get("tag-selection.selected") : MessageUtil.get("tag-selection.not-selected");
               line = line.replace("%preview%", preview);
               line = line.replace("%status%", status);
               line = line.replace("%selected%", selected);
               lore.add(ColorUtil.color(line));
            }

            meta.setLore(lore);
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            item.setItemMeta(meta);
            inv.setItem(tag.getSlot(), item);
         }
      }

      if (menu.contains(pagePath + ".next-page-slot")) {
         int slot = menu.getInt(pagePath + ".next-page-slot");
         ItemStack next = new ItemStack(Material.ARROW);
         ItemMeta meta = next.getItemMeta();
         meta.setDisplayName(ColorUtil.color("&aSiguiente Página"));
         next.setItemMeta(meta);
         inv.setItem(slot, next);
      }

      if (menu.contains(pagePath + ".previous-page-slot")) {
         int slot = menu.getInt(pagePath + ".previous-page-slot");
         ItemStack back = new ItemStack(Material.ARROW);
         ItemMeta meta = back.getItemMeta();
         meta.setDisplayName(ColorUtil.color("&cPágina Anterior"));
         back.setItemMeta(meta);
         inv.setItem(slot, back);
      }

      if (menu.getBoolean("menus.remove-tag.enabled")) {
         boolean hasTag = data.getSelectedTag() != null;
         String path = hasTag ? "menus.remove-tag.active" : "menus.remove-tag.inactive";

         Material material;
         try {
            material = Material.matchMaterial(menu.getString(path + ".material"));
         } catch (Exception var20) {
            material = Material.BARRIER;
         }

         String base64 = menu.getString(path + ".basehead");
         ItemStack item;
         if (base64 != null && !base64.isEmpty()) {
            item = createBaseHead(base64);
         } else if (material == Material.PLAYER_HEAD) {
            item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skull = (SkullMeta)item.getItemMeta();
            skull.setOwningPlayer(player);
            item.setItemMeta(skull);
         } else {
            item = new ItemStack(material);
         }

         ItemMeta meta = item.getItemMeta();
         meta.setDisplayName(ColorUtil.color(menu.getString(path + ".name")));
         List<String> lore = new ArrayList();
         String currentTag = "&7None";
         if (hasTag) {
            Tag current = Main.getInstance().getTagManager().getTag(data.getSelectedTag());
            if (current != null) {
               currentTag = ColorUtil.color(current.getTag());
            }
         }

         for(String line : menu.getStringList(path + ".lore")) {
            line = line.replace("%current_tag%", currentTag);
            lore.add(ColorUtil.color(line));
         }

         meta.setLore(lore);
         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
         item.setItemMeta(meta);
         if (menu.contains(pagePath + ".remove-tag-slot")) {
            int slot = menu.getInt(pagePath + ".remove-tag-slot");
            inv.setItem(slot, item);
         }
      }

      player.openInventory(inv);
      try {
         player.playSound(player.getLocation(), Sound.valueOf(menu.getString("menus.sounds.open")), 1.0F, 1.0F);
      } catch (IllegalArgumentException e) {
         Main.getInstance().getLogger().warning("Invalid sound in menu config: menus.sounds.open");
      }
   }

   private static ItemStack createBaseHead(String base64) {
      ItemStack item = new ItemStack(Material.PLAYER_HEAD);
      if (base64 != null && !base64.isEmpty()) {
         ItemMeta var3 = item.getItemMeta();
         if (var3 instanceof SkullMeta) {
            SkullMeta meta = (SkullMeta)var3;

            try {
               String decoded = new String(Base64.getDecoder().decode(base64));
               String texture = decoded.split("texture/")[1].split("\"")[0];
               if (!texture.matches("[a-zA-Z0-9_\\-]+")) {
                  return item;
               }
               PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID(), "TagsKeeper");
               PlayerTextures textures = profile.getTextures();
               textures.setSkin(URI.create("http://textures.minecraft.net/texture/" + texture).toURL());
               profile.setTextures(textures);
               meta.setOwnerProfile(profile);
               item.setItemMeta(meta);
            } catch (Exception e) {
               Main.getInstance().getLogger().warning("Failed to create base head: " + e.getMessage());
            }

            return item;
         } else {
            return item;
         }
      } else {
         return item;
      }
   }

   public static int getPage(Player player) {
      return (Integer)pages.getOrDefault(player.getUniqueId(), 1);
   }
}
