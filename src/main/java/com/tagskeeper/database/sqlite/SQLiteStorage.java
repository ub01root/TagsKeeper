package com.tagskeeper.database.sqlite;

import com.tagskeeper.Main;
import com.tagskeeper.database.Storage;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class SQLiteStorage implements Storage {
   private Connection connection;

   public SQLiteStorage() {
      String name = Main.getInstance().getConfig().getString("storage.sqlite.file", "data.db");
      File dir = new File(Main.getInstance().getDataFolder(), "database/SQLite");
      if (!dir.exists()) { dir.mkdirs(); }
      File file = new File(dir, name);
      try {
         Class.forName("org.sqlite.JDBC");
         this.connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
         this.createTables();
         Main.getInstance().getLogger().info("SQLite storage connected.");
      } catch (Exception e) {
         Main.getInstance().getLogger().log(Level.SEVERE, "Failed to connect to SQLite", e);
      }
   }

   private void createTables() throws SQLException {
      String selected = "CREATE TABLE IF NOT EXISTS tagskeeper_selected ("
            + "uuid VARCHAR(36) PRIMARY KEY, tag VARCHAR(255))";
      String purchased = "CREATE TABLE IF NOT EXISTS tagskeeper_purchased ("
            + "uuid VARCHAR(36), tag_id VARCHAR(255), PRIMARY KEY (uuid, tag_id))";
      try (PreparedStatement stmt1 = this.connection.prepareStatement(selected);
           PreparedStatement stmt2 = this.connection.prepareStatement(purchased)) {
         stmt1.execute();
         stmt2.execute();
      }
   }

   public void saveTag(UUID uuid, String tag) {
      String sql = "INSERT OR REPLACE INTO tagskeeper_selected (uuid, tag) VALUES (?, ?)";
      try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
         stmt.setString(1, uuid.toString());
         stmt.setString(2, tag);
         stmt.executeUpdate();
      } catch (SQLException e) {
         Main.getInstance().getLogger().log(Level.SEVERE, "Failed to save tag for " + uuid, e);
      }
   }

   public String getTag(UUID uuid) {
      String sql = "SELECT tag FROM tagskeeper_selected WHERE uuid = ?";
      try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
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
      try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
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
      String sql = "INSERT OR IGNORE INTO tagskeeper_purchased (uuid, tag_id) VALUES (?, ?)";
      try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
         stmt.setString(1, uuid.toString());
         stmt.setString(2, tagId);
         stmt.executeUpdate();
      } catch (SQLException e) {
         Main.getInstance().getLogger().log(Level.SEVERE, "Failed to set purchase for " + uuid, e);
      }
   }

   public void close() {
      try {
         if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
         }
      } catch (SQLException e) {
         Main.getInstance().getLogger().log(Level.WARNING, "Error closing SQLite connection", e);
      }
   }
}
