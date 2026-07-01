package com.tagskeeper.commands;

import com.tagskeeper.Main;
import com.tagskeeper.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (!sender.hasPermission("tagskeeper.perms.reload")) {
         sender.sendMessage(MessageUtil.get("no-permission"));
         return true;
      } else {
         Main plugin = Main.getInstance();
         plugin.reloadConfig();
         plugin.getConfigManager().reload();
         plugin.getStorageManager().reload();
         plugin.getTagManager().loadTags();
         sender.sendMessage(MessageUtil.get("reload-success"));
         return true;
      }
   }
}
