package com.tagskeeper.models;

import java.util.UUID;

public class PlayerTagData {
   private final UUID uuid;
   private String selectedTag;

   public PlayerTagData(UUID uuid) {
      this.uuid = uuid;
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public String getSelectedTag() {
      return this.selectedTag;
   }

   public void setSelectedTag(String selectedTag) {
      this.selectedTag = selectedTag;
   }
}
