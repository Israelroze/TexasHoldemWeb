package GameManager;

import API.Engine;
import API.EngineManager;
import Exceptions.*;
import Game.Game;
import Player.APlayer;
import Player.PlayerType;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class GameManager implements EngineManager {

    private Map<String,Engine> gameHash;
    private Map<String, APlayer> usersHash;

    public GameManager(){
        this.usersHash=new HashMap<>();
        this.gameHash=new HashMap<>();
    }

    @Override
    public void AddGame(InputStream fstream,String uploader) throws MinusZeroValueException, UnexpectedObjectException, BigSmallMismatchException, HandsCountDevideException, GameStartedException, BigBiggerThanBuyException, MaxBigMoreThanHalfBuyException, HandsCountSmallerException, JAXBException, PlayerdIDmismatchException, GameTitleAllreadyExistException {
        Engine new_game=new Game();
        new_game.LoadFromXML(fstream);

        String id=new_game.GetGameID();
        new_game.RegisterUploader(uploader);
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

    @Override
    public List<Engine> GetGamesList() {
        List<Engine> res = new LinkedList<>();

        for (Map.Entry<String, Engine> entry : this.gameHash.entrySet()) {
            String key = entry.getKey();
            res.add(entry.getValue());
        }

        return res;
    }

    @Override
    public void AddPlayerToGame(String game_id, String username) throws UserNameNotProvidedException, GameIDNotProvidedException, PlayerAlreadyInGameException {
        try {
            APlayer player = this.usersHash.get(username);

            try{
                Engine game=this.gameHash.get(game_id);
                game.AddNewPlayer(player);
            }
            catch(NullPointerException e)
            {
                throw new GameIDNotProvidedException();
            }
        }
        catch (NullPointerException e)
        {
            throw new UserNameNotProvidedException();
        }
    }

    @Override
    public List<String> GetUserList() {
        List<String> res=new LinkedList<>();

        for (Map.Entry<String, APlayer> entry : this.usersHash.entrySet()) {
            String key = entry.getKey();
            res.add(key);
        }

        return res;
    }

    @Override
    public boolean IsUserListEmpty() {
        return this.usersHash.isEmpty();
    }

    @Override
    public void AddNewUser(String username, String Type) throws PlayerAlreadyExistException {
        if(this.usersHash.containsKey(username)){
            throw new PlayerAlreadyExistException();
        }
        else {
            if (Type.equals("HUMAN")) {
                this.usersHash.put(username, new APlayer(username, PlayerType.HUMAN,this.usersHash.size()));
            } else {
                this.usersHash.put(username, new APlayer(username, PlayerType.COMPUTER,this.usersHash.size()));
            }
        }
    }

    @Override
    public String GetUserType(String username) {
        PlayerType type=this.usersHash.get(username).GetType();

        if(type==PlayerType.HUMAN)
        {
            return "Human";
        }
        return "Computer";
    }

    @Override
    public int GetUserMoney(String username) {
        return this.usersHash.get(username).GetMoney();
    }

    @Override
    public int GetUserNumOfBuys(String username) {
        return this.usersHash.get(username).GetNumOfBuys();
    }

    @Override
    public int GetUserNumOfWins(String username) {
        return this.usersHash.get(username).GetNumOfWins();
    }

    @Override
    public String IsPlayerInReadyGame(String username) {

        APlayer player=this.usersHash.get(username);

        for (Map.Entry<String, Engine> entry : this.gameHash.entrySet()) {
            if(entry.getValue().IfPlayerExist(player))//if the player registered in the game
            {
                if(entry.getValue().IfEnoughPlayers())
                {
                    if(!entry.getValue().IsGameStarted() ) {
                      return entry.getValue().GetGameID();
                    }
                }
            }
        }
        return null;
    }
}
