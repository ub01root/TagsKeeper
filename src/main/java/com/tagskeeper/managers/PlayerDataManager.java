package com.tagskeeper.managers;

import com.tagskeeper.models.PlayerTagData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
   private final Map<UUID, PlayerTagData> cache = new HashMap();

   public PlayerTagData get(UUID uuid) {
      return (PlayerTagData)this.cache.computeIfAbsent(uuid, PlayerTagData::new);
   }
}
