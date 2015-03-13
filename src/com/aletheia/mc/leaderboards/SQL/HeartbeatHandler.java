package com.aletheia.mc.leaderboards.SQL;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HeartbeatHandler
{
  static Connection c = CustomLeaderboard.c;
  
  public static void heartBeat()
    throws SQLException
  {
    CustomLeaderboard.plugin.getLogger().info("A heartbeat has begun");
    CustomLeaderboard.plugin.getLogger().info("Checking for professional canidants");
    if (checkForProfessional()) {
      CustomLeaderboard.plugin.getLogger().info("Found canidants.. Setting them accordingly");
    } else {
      CustomLeaderboard.plugin.getLogger().info("No canidants found.. Proceeding with heartbeat");
    }
    CustomLeaderboard.plugin.getLogger().info("Calculating points for both tables");
    calculateProfessionalPoints();
    calculateRegularPoints();
    CustomLeaderboard.plugin.getLogger().info("Beggining rank..");
    CustomLeaderboard.plugin.getLogger().info("Ranking professional players");
    try
    {
      rankProfessionalPlayers();
    }
    catch (SQLException e)
    {
      CustomLeaderboard.plugin.getLogger().info("ERROR RANKING AT prof_players");
      e.printStackTrace();
    }
    CustomLeaderboard.plugin.getLogger().info("Professional players have been ranked");
    CustomLeaderboard.plugin.getLogger().info("Ranking regular players");
    try
    {
      rankRegularPlayers();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      CustomLeaderboard.plugin.getLogger().info("ERROR RANKING AT regular_players");
    }
    CustomLeaderboard.plugin.getLogger().info("Regular players have been ranked");
  }
  
  public static void rankRegularPlayers()
    throws SQLException
  {
    Statement statement = c.createStatement();
    
    statement.executeUpdate("UPDATE   regular_players JOIN     (SELECT    p.playerID, @curRank := @curRank + 1 AS Rank FROM      regular_players p JOIN      (SELECT @curRank := 0) r ORDER BY  p.TotalPoints DESC ) ranks ON (ranks.playerID = regular_players.playerID) SET      regular_players.Rank = ranks.rank;");
  }
  
  public static void rankProfessionalPlayers()
    throws SQLException
  {
    Statement statement = c.createStatement();
    
    statement.executeUpdate("UPDATE   prof_players JOIN     (SELECT    p.playerID, @curRank := @curRank + 1 AS Rank FROM      prof_players p JOIN      (SELECT @curRank := 0) r ORDER BY  p.TotalPoints DESC ) ranks ON (ranks.playerID = prof_players.playerID) SET      prof_players.Rank = ranks.rank;");
  }
  
  public static void calculateRegularPoints()
    throws SQLException
  {
    Statement statement = c.createStatement();
    Statement countstatement = c.createStatement();
    
    ResultSet r = countstatement.executeQuery("SELECT COUNT(*) AS NumberOfPlayers FROM regular_players;");
    r.next();
    for (int i = 0; i < r.getInt(1); i++) {
      statement.executeUpdate("update regular_players set TotalPoints = OlympusPoints + CDPoints where playerID = '" + i + "'");
    }
  }
  
  public static void calculateProfessionalPoints()
    throws SQLException
  {
    Statement statement = c.createStatement();
    Statement countstatement = c.createStatement();
    
    ResultSet r = countstatement.executeQuery("SELECT COUNT(*) AS NumberOfPlayers FROM prof_players;");
    r.next();
    for (int i = 0; i < r.getInt(1); i++) {
      statement.executeUpdate("update prof_players set TotalPoints = OlympusPoints + CDPoints where playerID = '" + i + "'");
    }
  }
  
  public static boolean checkForProfessional()
    throws SQLException
  {
    return true;
  }
}
