package com.aletheia.mc.leaderboards.SQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DataHandler
{
  static Connection c = CustomLeaderboard.c;
  
  public static void getTotalPoints(Player sender, String target)
    throws SQLException
  {
    Statement statement = c.createStatement();
    



    ResultSet rs = statement.executeQuery("select TotalPoints from regular_players where PlayerName = '" + target + "';");
    if (rs.next())
    {
      sender.sendMessage("Total amount of points for " + ChatColor.GREEN + target + " is " + ChatColor.GOLD + rs.getInt(1));
      

      return;
    }
    rs = statement.executeQuery("select Rank from prof_players where PlayerName = '" + target + "';");
    if (rs.next())
    {
      sender.sendMessage(ChatColor.GOLD + "(Professional) " + ChatColor.WHITE + "Total amount of points for " + ChatColor.GREEN + target + " is " + ChatColor.GOLD + rs.getInt(1));
      


      return;
    }
    sender.sendMessage(ChatColor.RED + "Player not found / Invalid Arguments");
  }
  
  public static void getOlympusPoints(Player sender, String target)
    throws SQLException
  {
    Statement statement = c.createStatement();
    



    ResultSet rs = statement.executeQuery("select OlympusPoints from regular_players where PlayerName = '" + target + "';");
    if (rs.next())
    {
      sender.sendMessage("Total Olympus Points for " + ChatColor.GREEN + target + " is " + ChatColor.GOLD + rs.getInt(1));
      

      return;
    }
    rs = statement.executeQuery("select OlympusPoints from prof_players where PlayerName = '" + target + "';");
    if (rs.next())
    {
      sender.sendMessage(ChatColor.GOLD + "(Professional) " + ChatColor.WHITE + "Total Olympus Points for " + ChatColor.GREEN + target + " is " + ChatColor.GOLD + rs.getInt(1));
      


      return;
    }
    sender.sendMessage(ChatColor.RED + "Player not found / Invalid Arguments");
  }
  
  public static void getCDPoints(Player sender, String playerName)
    throws SQLException
  {
    Statement statement = c.createStatement();
    



    ResultSet rs = statement.executeQuery("select CDPoints from regular_players where PlayerName = '" + playerName + "';");
    if (rs.next())
    {
      sender.sendMessage("Total CD Points points for " + ChatColor.GREEN + playerName + " is " + ChatColor.GOLD + rs.getInt(1));
      

      return;
    }
    rs = statement.executeQuery("select CDPoints from prof_players where PlayerName = '" + playerName + "';");
    if (rs.next())
    {
      sender.sendMessage(ChatColor.GOLD + "(Professional) " + ChatColor.WHITE + "Total CDPoints for " + ChatColor.GREEN + playerName + " is " + ChatColor.GOLD + rs.getInt(1));
      


      return;
    }
    sender.sendMessage(ChatColor.RED + "Player not found / Invalid Arguments");
  }
  
  public static void getRank(Player sender, String target)
    throws SQLException
  {
    Statement statement = c.createStatement();
    Statement countplayer = c.createStatement();
    

    ResultSet rs = statement.executeQuery("select Rank from regular_players where PlayerName = '" + target + "';");
    if (rs.next())
    {
      ResultSet r = countplayer.executeQuery("SELECT COUNT(*) AS NumberOfPlayers FROM regular_players;");
      r.next();
      int count = r.getInt("NumberOfPlayers");
      r.close();
      

      sender.sendMessage("Player " + ChatColor.RED + target + ChatColor.WHITE + " is rank " + ChatColor.GOLD + rs.getInt(1) + " out of " + ChatColor.GREEN + count + ChatColor.WHITE + " regular players");
      

      return;
    }
    rs = statement.executeQuery("select Rank from prof_players where PlayerName = '" + target + "';");
    if (rs.next())
    {
      ResultSet r = countplayer.executeQuery("SELECT COUNT(*) AS NumberOfPlayers FROM prof_players;");
      r.next();
      int count = r.getInt("NumberOfPlayers");
      r.close();
      

      sender.sendMessage("Player " + ChatColor.RED + target + ChatColor.WHITE + " is rank " + ChatColor.GOLD + rs.getInt(1) + " out of " + ChatColor.GOLD + count + ChatColor.WHITE + " professional players");
      

      return;
    }
    sender.sendMessage(ChatColor.RED + "Player not found");
  }
}