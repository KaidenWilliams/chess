package dataAccess;

public interface IDataAccess {

    public IAuthDAO getAuthDAO();

    public IGameDAO getGameDAO();

    public IUserDAO getUserDAO();
}
