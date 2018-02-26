package GameManager;

import API.Engine;
import API.EngineManager;
import Exceptions.*;
import Game.Game;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GameManager implements EngineManager {

    private Map<String,Engine> gameHash;

    public GameManager(){
        this.gameHash=new HashMap<>();
    }

    @Override
    public void AddGame(InputStream fstream) throws MinusZeroValueException, UnexpectedObjectException, BigSmallMismatchException, HandsCountDevideException, GameStartedException, BigBiggerThanBuyException, MaxBigMoreThanHalfBuyException, HandsCountSmallerException, JAXBException, PlayerdIDmismatchException, GameTitleAllreadyExistException {
        Engine new_game=new Game();
        new_game.LoadFromXML(fstream);

        String id=new_game.GetGameID();

        if(this.IsGameExist(id))
        {
            throw new GameTitleAllreadyExistException();
        }
        else
        {
            this.gameHash.put(id,new_game);
        }
    }

    @Override
    public Engine GetGame(String id) {
        return this.gameHash.get(id);
    }

    @Override
    public boolean IsGameExist(String id) {
        return this.gameHash.containsKey(id);
    }

    @Override
    public void DeleteGame(String id) {
        this.gameHash.remove(id);
    }
}
