package com.tagskeeper.models;

import java.util.List;

public class Tag {
   private final String id;
   private final String tag;
   private final String menuDisplay;
   private final String preview;
   private final String permission;
   private final String material;
   private final int slot;
   private final int page;
   private final List<String> lore;

   public Tag(String id, String tag, String menuDisplay, String preview, String permission, String material, int slot, int page, List<String> lore) {
      this.id = id;
      this.tag = tag;
      this.menuDisplay = menuDisplay;
      this.preview = preview;
      this.permission = permission;
      this.material = material;
      this.slot = slot;
      this.page = page;
      this.lore = lore;
   }

   public String getId() {
      return this.id;
   }

   public String getTag() {
      return this.tag;
   }

   public String getMenuDisplay() {
      return this.menuDisplay;
   }

   public String getPreview() {
      return this.preview;
   }

   public String getPermission() {
      return this.permission;
   }

   public String getMaterial() {
      return this.material;
   }

   public int getSlot() {
      return this.slot;
   }

   public int getPage() {
      return this.page;
   }

   public List<String> getLore() {
      return this.lore;
   }
}
