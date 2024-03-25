package ui;

import java.util.HashMap;

public class LoggedInBuilder {

    public static String logoutString =
            """
            Successfully logged out of your account, "%s"
            Currently logged out. To view possible commands, type "help"
            """;


    public static String listGamesString =
            """
            %d. Game Name: %s. Player White: %s. Player Black: %s
            """;

    public static HashMap<Integer, Integer> gameNumberMap;

    public static String helpString =
            """
            Commands:
            < create <GAMENAME>  -- to create a new Chess Game
            < join <GAMENUMBER> <COLOR (white/black) > -- to join a game as a player
            < spectate <GAMENUMBER> <COLOR> -- to join a game as a spectator
            < list -- to list all current Chess Games
            < logout -- to log out of your account
            < help -- to get a list of possible commands
            """;


    public static String getLogoutString(String username) {
        return String.format(logoutString, username);
    }

    public static String getlistGamesString(int gameNumber, String gameName, String whiteUsername, String blackUsername) {
        return String.format(listGamesString, gameNumber, gameName, whiteUsername, blackUsername);
    }


}
