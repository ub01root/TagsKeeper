package com.tagskeeper.hooks;

import com.tagskeeper.Main;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyHook {

   private Object economy;
   private boolean enabled;
   private Method hasMethod;
   private Method withdrawMethod;
   private Method formatMethod;

   public boolean setup() {
      if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
         Main.getInstance().getLogger().info("Vault not found, economy features disabled");
         return false;
      }
      try {
         Class<?> econClass = Class.forName("net.milkbowl.vault.economy.Economy");
         RegisteredServiceProvider<?> rsp = Bukkit.getServicesManager().getRegistration(econClass);
         if (rsp == null) {
            Main.getInstance().getLogger().info("No economy provider found, economy features disabled");
            return false;
         }
         economy = rsp.getProvider();
         hasMethod = econClass.getMethod("has", OfflinePlayer.class, double.class);
         withdrawMethod = econClass.getMethod("withdrawPlayer", OfflinePlayer.class, double.class);
         formatMethod = econClass.getMethod("format", double.class);
         enabled = true;
         Main.getInstance().getLogger().info("Hooked into Vault economy");
         return true;
      } catch (Exception e) {
         Main.getInstance().getLogger().warning("Failed to hook Vault: " + e.getMessage());
         return false;
      }
   }

   public boolean isEnabled() {
      return enabled;
   }

   public boolean hasBalance(Player player, double amount) {
      if (!enabled) return false;
      try {
         return (boolean) hasMethod.invoke(economy, player, amount);
      } catch (Exception e) {
         return false;
      }
   }

   public boolean withdraw(Player player, double amount) {
      if (!enabled) return false;
      try {
         Object response = withdrawMethod.invoke(economy, player, amount);
         Method successMethod = response.getClass().getMethod("transactionSuccess");
         return (boolean) successMethod.invoke(response);
      } catch (Exception e) {
         return false;
      }
   }

   public String format(double amount) {
      if (!enabled) return String.valueOf(amount);
      try {
         return (String) formatMethod.invoke(economy, amount);
      } catch (Exception e) {
         return String.valueOf(amount);
      }
   }
}
