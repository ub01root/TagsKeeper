package com.tagskeeper.database;

import com.tagskeeper.Main;
import java.util.UUID;
import java.util.logging.Level;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisStorage implements Storage {
   private JedisPool pool;
   private static final String SELECTED_PREFIX = "tagskeeper:selected:";
   private static final String PURCHASED_PREFIX = "tagskeeper:purchased:";

   public RedisStorage() {
      String host = Main.getInstance().getConfig().getString("storage.redis.host", "localhost");
      int port = Main.getInstance().getConfig().getInt("storage.redis.port", 6379);
      String password = Main.getInstance().getConfig().getString("storage.redis.password", "");
      boolean useSSL = Main.getInstance().getConfig().getBoolean("storage.redis.use-ssl", false);

      JedisPoolConfig poolConfig = new JedisPoolConfig();
      poolConfig.setMaxTotal(10);
      poolConfig.setMinIdle(2);
      poolConfig.setTestOnBorrow(true);

      try {
         if (!password.isEmpty()) {
            this.pool = new JedisPool(poolConfig, host, port, 2000, password, useSSL);
         } else {
            this.pool = new JedisPool(poolConfig, host, port, 2000, useSSL);
         }
         try (Jedis jedis = this.pool.getResource()) {
            jedis.ping();
         }
         Main.getInstance().getLogger().info("Redis storage connected.");
      } catch (Exception e) {
         Main.getInstance().getLogger().log(Level.SEVERE, "Failed to connect to Redis", e);
      }
   }

   public void saveTag(UUID uuid, String tag) {
      try (Jedis jedis = this.pool.getResource()) {
         if (tag == null) {
            jedis.del(SELECTED_PREFIX + uuid);
         } else {
            jedis.set(SELECTED_PREFIX + uuid, tag);
         }
      } catch (Exception e) {
         Main.getInstance().getLogger().log(Level.SEVERE, "Failed to save tag for " + uuid, e);
      }
   }

   public String getTag(UUID uuid) {
      try (Jedis jedis = this.pool.getResource()) {
         return jedis.get(SELECTED_PREFIX + uuid);
      } catch (Exception e) {
         Main.getInstance().getLogger().log(Level.SEVERE, "Failed to get tag for " + uuid, e);
      }
      return null;
   }

   public boolean hasPurchased(UUID uuid, String tagId) {
      try (Jedis jedis = this.pool.getResource()) {
         return jedis.sismember(PURCHASED_PREFIX + uuid, tagId);
      } catch (Exception e) {
         Main.getInstance().getLogger().log(Level.SEVERE, "Failed to check purchase for " + uuid, e);
      }
      return false;
   }

   public void setPurchased(UUID uuid, String tagId) {
      try (Jedis jedis = this.pool.getResource()) {
         jedis.sadd(PURCHASED_PREFIX + uuid, tagId);
      } catch (Exception e) {
         Main.getInstance().getLogger().log(Level.SEVERE, "Failed to set purchase for " + uuid, e);
      }
   }

   public void close() {
      if (this.pool != null && !this.pool.isClosed()) {
         this.pool.close();
      }
   }
}
