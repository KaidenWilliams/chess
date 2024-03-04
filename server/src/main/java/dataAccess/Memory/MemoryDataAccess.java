package dataAccess.Memory;
import dataAccess.IDataAccess;
import dataAccess.IAuthDAO;
import dataAccess.IGameDAO;
import dataAccess.IUserDAO;

public class MemoryDataAccess implements IDataAccess {

    private MemoryAuthDAO authDAO = new MemoryAuthDAO();

    private MemoryGameDAO gameDAO = new MemoryGameDAO();

    private MemoryUserDAO userDAO = new MemoryUserDAO();

    public MemoryAuthDAO getAuthDAO() {
        return authDAO;
    }

    public MemoryGameDAO getGameDAO() {
        return gameDAO;
    }

    public MemoryUserDAO getUserDAO() {
        return userDAO;
    }







}
