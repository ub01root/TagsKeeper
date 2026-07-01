package com.tagskeeper.database;

import java.util.UUID;

public interface Storage {
    void saveTag(UUID uuid, String tag);
    String getTag(UUID uuid);
    boolean hasPurchased(UUID uuid, String tagId);
    void setPurchased(UUID uuid, String tagId);
    void close();
}
