package API;

import Exceptions.*;

import javax.xml.bind.JAXBException;
import java.io.InputStream;

public interface EngineManager {


    //Game
    public void AddGame(InputStream fstream) throws MinusZeroValueException, UnexpectedObjectException, BigSmallMismatchException, HandsCountDevideException, GameStartedException, BigBiggerThanBuyException, MaxBigMoreThanHalfBuyException, HandsCountSmallerException, JAXBException, PlayerdIDmismatchException, GameTitleAllreadyExistException;
    public Engine GetGame(String id);
    public boolean IsGameExist(String id);
    public void DeleteGame(String id);

    //users
    //?

}
