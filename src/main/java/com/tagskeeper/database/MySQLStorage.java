package com.tagskeeper.database;

import com.tagskeeper.Main;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class MySQLStorage implements Storage {
   private HikariDataSource dataSource;

   public MySQLStorage() {
      String host = Main.getInstance().getConfig().getString("storage.mysql.host", "localhost");
      int port = Main.getInstance().getConfig().getInt("storage.mysql.port", 3306);
      String database = Main.getInstance().getConfig().getString("storage.mysql.database", "tagskeeper");
      String username = Main.getInstance().getConfig().getString("storage.mysql.username", "root");
      String password = Main.getInstance().getConfig().getString("storage.mysql.password", "");
      int poolSize = Main.getInstance().getConfig().getInt("storage.mysql.pool-size", 10);

      HikariConfig config = new HikariConfig();
      config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC");
      config.setUsername(username);
      config.setPassword(password);
      config.setMaximumPoolSize(poolSize);
      config.setMinimumIdle(2);
      config.setConnectionTimeout(5000);
      config.setLeakDetectionThreshold(10000);
      config.setDriverClassName("com.mysql.cj.jdbc.Driver");

      try {
         this.dataSource = new HikariDataSource(config);
         this.createTables();
         Main.getInstance().getLogger().info("MySQL storage connected.");
      } catch (Exception e) {
         Main.getInstance().getLogger().log(Level.SEVERE, "Failed to connect to MySQL", e);
      }
   }

   private void createTables() throws SQLException {
      String selected = "CREATE TABLE IF NOT EXISTS tagskeeper_selected ("
            + "uuid VARCHAR(36) PRIMARY KEY, tag VARCHAR(255)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
      String purchased = "CREATE TABLE IF NOT EXISTS tagskeeper_purchased ("
            + "uuid VARCHAR(36), tag_id VARCHAR(255), PRIMARY KEY (uuid, tag_id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
      try (Connection conn = this.dataSource.getConnection();
           PreparedStatement stmt1 = conn.prepareStatement(selected);
           PreparedStatement stmt2 = conn.prepareStatement(purchased)) {
         stmt1.execute();
         stmt2.execute();
      }
   }

   private Connection getConnection() throws SQLException {
      return this.dataSource.getConnection();
   }

   public void saveTag(UUID uuid, String tag) {
      String sql = "INSERT INTO tagskeeper_selected (uuid, tag) VALUES (?, ?) "
            + "ON DUPLICATE KEY UPDATE tag = ?";
      try (Connection conn = getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
         stmt.setString(1, uuid.toString());
         stmt.setString(2, tag);
         stmt.setString(3, tag);
         stmt.executeUpdate();
      } catch (SQLException e) {
         Main.getInstance().getLogger().log(Level.SEVERE, "Failed to save tag for " + uuid, e);
      }
   }

   public String getTag(UUID uuid) {
      String sql = "SELECT tag FROM tagskeeper_selected WHERE uuid = ?";
      try (Connection conn = getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
         stmt.setString(1, uuid.toString());
         try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getString("tag");
         }
      } catch (SQLException e) {
         Main.getInstance().getLogger().log(Level.SEVERE, "Failed to get tag for " + uuid, e);
      }
      return null;
   }

   public boolean hasPurchased(UUID uuid, String tagId) {
      String sql = "SELECT 1 FROM tagskeeper_purchased WHERE uuid = ? AND tag_id = ?";
      try (Connection conn = getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
         stmt.setString(1, uuid.toString());
         stmt.setString(2, tagId);
         try (ResultSet rs = stmt.executeQuery()) {
            return rs.next();
         }
      } catch (SQLException e) {
         Main.getInstance().getLogger().log(Level.SEVERE, "Failed to check purchase for " + uuid, e);
      }
      return false;
   }

   public void setPurchased(UUID uuid, String tagId) {
      String sql = "INSERT IGNORE INTO tagskeeper_purchased (uuid, tag_id) VALUES (?, ?)";
      try (Connection conn = getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
         stmt.setString(1, uuid.toString());
         stmt.setString(2, tagId);
         stmt.executeUpdate();
      } catch (SQLException e) {
         Main.getInstance().getLogger().log(Level.SEVERE, "Failed to set purchase for " + uuid, e);
      }
   }

   public void close() {
      if (this.dataSource != null && !this.dataSource.isClosed()) {
         this.dataSource.close();
      }
   }
}
