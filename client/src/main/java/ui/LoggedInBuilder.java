package ui;

import java.util.HashMap;

public class LoggedInBuilder {

    public static String logoutString =
            """
            Successfully logged out of your account, "%s"
            You are currently logged out
            """;


    public static String listGamesString =
            """
            %d. Game Name: %s. Player White: %s. Player Black: %s
            """;

    public static HashMap<Integer, Integer> gameNumberMap = new HashMap<>();

    public static String createGameString =
            """
            Successfully created game with name: "%s"
            """;


    public static String joinGameString =
            """
            Successfully joined game with number "%d" as color "%s";
            """;

    public static String joinGameErrorString =
            """
            Error, no game found corresponding to number: "%s". Please try again, or enter "list" to see what games exist
            """;

    public static String spectateGameString =
            """
            Successfully joined game with number "%d" as a spectator;
            """;


    public static String helpString =
            """
            Commands:
            < create <GAMENAME>  -- to create a new Chess Game
            < join <GAMENUMBER> <COLOR(white/black)> -- to join a game as a player
            < spectate <GAMENUMBER> -- to join a game as a spectator
            < list -- to list all current Chess Games
            < logout -- to log out of your account
            < help -- to get a list of possible commands
            """;

    public static String defaultString =
            """
            Your input was invalid. The following valid commands are shown below
            
            Commands:
            < create <GAMENAME>  -- to create a new Chess Game
            < join <GAMENUMBER> <COLOR(white/black)> -- to join a game as a player
            < spectate <GAMENUMBER> -- to join a game as a spectator
            < list -- to list all current Chess Games
            < logout -- to log out of your account
            < help -- to get a list of possible commands
            """;


    public static String getLogoutString(String username) {
        return String.format(logoutString, username);
    }

    public static String getListGamesString(int gameNumber, String gameName, String whiteUsername, String blackUsername) {
        return String.format(listGamesString, gameNumber, gameName, whiteUsername, blackUsername);
    }

    public static String getCreateGameString(String gameName) {
        return String.format(createGameString, gameName);
    }

    public static String getJoinGameString(Integer gameNumber, String color) {
        return String.format(joinGameString, gameNumber, color);
    }

    public static String getJoinGameErrorString(String gameNumber) {
        return String.format(joinGameErrorString, gameNumber);
    }

    public static String getSpecateGameString(Integer gameNumber) {
        return String.format(spectateGameString, gameNumber);
    }


}
