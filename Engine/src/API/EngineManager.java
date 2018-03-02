package API;

import Exceptions.*;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.List;

public interface EngineManager {
    ////////////////////////////////////////////
    /////Games
    ////////////////////////////////////////////
    public void AddGame(InputStream fstream,String uploader) throws MinusZeroValueException, UnexpectedObjectException, BigSmallMismatchException, HandsCountDevideException, GameStartedException, BigBiggerThanBuyException, MaxBigMoreThanHalfBuyException, HandsCountSmallerException, JAXBException, PlayerdIDmismatchException, GameTitleAllreadyExistException;
    public Engine GetGame(String id);
    public boolean IsGameExist(String id);
    public void DeleteGame(String id);
    public List<Engine> GetGamesList();
    public void AddPlayerToGame(String game_id,String username) throws UserNameNotProvidedException, GameIDNotProvidedException, PlayerAlreadyInGameException;

    /////////////////////////////////////////////
    /////users
    /////////////////////////////////////////////
    public List<String> GetUserList();
    public boolean IsUserListEmpty();
    public void AddNewUser(String username,String Type) throws PlayerAlreadyExistException;
    public String GetUserType(String username);
    public int GetUserMoney(String username);
    public int GetUserNumOfBuys(String username);
    public int GetUserNumOfWins(String username);
    public String IsPlayerInReadyGame(String username);
}
