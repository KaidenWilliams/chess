package service;
import dataAccess.*;
import dataAccess.Memory.MemoryAuthDAO;
import dataAccess.Memory.MemoryGameDAO;
import dataAccess.Memory.MemoryUserDAO;

public class ChessService {

    private final IAuthDAO authDAO;
    private final IGameDAO gameDAO;
    private final IUserDAO userDAO;

    public ChessService(){
        this.authDAO = new MemoryAuthDAO();
        this.gameDAO = new MemoryGameDAO();
        this.userDAO= new MemoryUserDAO();
    }


    public registerUser()



}
