package GameManager;

import API.Engine;
import API.EngineManager;
import Exceptions.*;
import Game.Game;
import Move.*;
import Player.APlayer;
import Player.PlayerType;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class GameManager implements EngineManager {

    private Map<String,Engine> gameHash;
    private Map<String,Integer> gameReadyList;
    private Map<String, APlayer> usersHash;
    private Map<String,List<String>> chatHash;

    public GameManager(){
        this.usersHash=new HashMap<>();
        this.gameHash=new HashMap<>();
        this.gameReadyList=new HashMap<>();
        this.chatHash=new HashMap<>();
    }

    private synchronized List<String> GetChatHash(String game_id){
        for (Map.Entry<String,List<String>> entry : this.chatHash.entrySet()) {
            String key = entry.getKey();
            if(key.equals(game_id)) return entry.getValue();
        }
        return null;
    }

    @Override
    public synchronized void AddGame(InputStream fstream,String uploader) throws MinusZeroValueException, UnexpectedObjectException, BigSmallMismatchException, HandsCountDevideException, GameStartedException, BigBiggerThanBuyException, MaxBigMoreThanHalfBuyException, HandsCountSmallerException, JAXBException, PlayerdIDmismatchException, GameTitleAllreadyExistException {
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
            this.chatHash.put(id,new LinkedList<>());
        }
    }

    @Override
    public synchronized Engine GetGame(String id) {
        return this.gameHash.get(id);
    }

    @Override
    public synchronized boolean IsGameExist(String id) {
        return this.gameHash.containsKey(id);
    }

    @Override
    public synchronized void DeleteGame(String id) {
        this.gameHash.remove(id);
    }

    @Override
    public synchronized List<Engine> GetGamesList() {
        List<Engine> res = new LinkedList<>();

        for (Map.Entry<String, Engine> entry : this.gameHash.entrySet()) {
            String key = entry.getKey();
            res.add(entry.getValue());
        }

        return res;
    }

    @Override
    public synchronized void AddPlayerToGame(String game_id, String username) throws UserNameNotProvidedException, GameIDNotProvidedException, PlayerAlreadyInGameException {
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
    public synchronized void LeavePlayerGame(String game_id, String username) {
        this.GetGame(game_id).DeletePlayer(username);
    }

    @Override
    public synchronized List<String> GetUserList() {
        List<String> res=new LinkedList<>();

        for (Map.Entry<String, APlayer> entry : this.usersHash.entrySet()) {
            String key = entry.getKey();
            res.add(key);
        }

        return res;
    }

    @Override
    public synchronized boolean IsUserListEmpty() {
        return this.usersHash.isEmpty();
    }

    @Override
    public synchronized void AddNewUser(String username, String Type) throws PlayerAlreadyExistException {
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
    public synchronized void LogoutUser(String username) {
        this.usersHash.remove(username);
    }

    @Override
    public synchronized String GetUserType(String username) {
        PlayerType type=this.usersHash.get(username).GetType();

        if(type==PlayerType.HUMAN)
        {
            return "Human";
        }
        return "Computer";
    }

    @Override
    public synchronized String IsPlayerInReadyGame(String username) {

        APlayer player=this.usersHash.get(username);

        for (Map.Entry<String, Engine> entry : this.gameHash.entrySet()) {
            if(entry.getValue().IfPlayerExist(player))//if the player registered in the game
            {
                if(entry.getValue().IfEnoughPlayers())
                {
                   // if(!entry.getValue().IsGameStarted() ) {
                      return entry.getValue().GetGameID();
                   //  }
                }
            }
        }
        return null;
    }

    @Override
    public synchronized void SetChatMessage(String gase_id, String username, String message) {
        this.GetChatHash(gase_id).add(username+": "+message);
    }

    @Override
    public synchronized List<String> getChatData(String game_id) {
       return this.GetChatHash(game_id);
    }

    @Override
    public synchronized void InitChatData(String game_id) {

        this.GetChatHash(game_id).clear();

    }

    @Override
    public synchronized void AddReadyPlayer(String username, String GameID) {

    }

    @Override
    public synchronized boolean IsAllPlayersReady(String GameID) {
        return false;
    }

    @Override
    public synchronized void InitPlayersReady(String GameID) {

    }

    @Override
    public synchronized void CheckGameStatus(Engine game){
        //if game started
        if(game.IsGameStarted())
        {
            //if game is not over
            if(!game.IsGameOver())
            {
                //if there is started Hand
                if(game.IsCurrentHandStarted() && !game.IsCurrentHandFinished())
                {
                    //check if only one active player, if yes set technical winner
                    game.CheckCurrentHandStatus();

                    //check if only computer players
                    game.CheckNoActiveHumans();

                    //check once again for hand status
                    if(game.IsCurrentHandStarted() && !game.IsCurrentHandFinished())
                    {
                        //check for current bid status, manage loop
                        try {
                            this.MenageCycle(game);
                        } catch (NoSufficientMoneyException e) {
                            //set game over flag to true
                            game.SetGameOver(true);
                            //game is over
                            //throw new GameOverException();
                        }

                        //if every thing is ok, check if current player is computer, if yes, auto move his turn
                        if (game.IsCurrentPlayerFolded())
                            game.MoveToNextPlayer();
                        else {
                            if (game.IsCurrentPlayerComputer()) {
                                try {
                                    Move move = game.GetAutoMove();
                                    game.SetNewMove(move);
                                } catch (PlayerFoldedException e) {
                                    // no way, if deals with it before
                                } catch (ChipLessThanPotException e) {
                                   //throw new FatalGameErrorException("Computer player chips less that pot.");
                                } catch (NoSufficientMoneyException e) {
                                    //set game over flag to true
                                    game.SetGameOver(true);
                                    //game is over
                                    //throw new GameOverException();
                                } catch (MoveNotAllowdedException e) {
                                   //throw new FatalGameErrorException("Computer player move not allowded.");
                                } catch (PlayerAlreadyBetException e) {
                                    //throw new FatalGameErrorException("Computer player allready bet.");
                                } catch (StakeNotInRangeException e) {
                                   // throw new FatalGameErrorException("Computer player stake not in range");
                                }
                            }
                        }
                    }
                    else
                    {
                        //hand is over, before starting new, check if sufficient money among all players
                        if(game.IsAnyPlayerOutOfMoney())
                        {
                            //set game over flag to true
                            game.SetGameOver(true);
                            //game is over
                            //throw new GameOverException();
                        }
                    }
                }
                else
                {
                    //hand is over, before starting new, check if sufficient money among all players
                    if(game.IsAnyPlayerOutOfMoney())
                    {
                        //set game over flag to true
                        game.SetGameOver(true);
                        //game is over
                        //throw new GameOverException();
                    }
                }
            }
            //else
            //{
                //game is over
              //  throw new GameOverException();
            //}
        }





    }

    @Override
    public synchronized void InitGame(Engine game) {
        game.InitGame();

        for(APlayer player: this.usersHash.values())
        {
            player.InitPlayer();
        }
    }

    @Override
    public synchronized void CheckCurrentPlayerStatus(Engine game) {
        if(!game.IsCurrentBidCycleFinished() && game.IsCurrentHandStarted()) {

        }
    }

    @Override
    public synchronized void CheckCurrentHandStatus(Engine game) {
        if(game.IsCurrentHandStarted() && !game.IsCurrentHandFinished()) {
            //checks ans preformes techincal winner
            game.CheckCurrentHandStatus();

            //check if only computer players left
            game.CheckNoActiveHumans();

        }
    }

    @Override
    public synchronized boolean IsYourTurn(Engine game,String username) {
        if (game.IsCurrentHandStarted()) {
            String current_player_name = game.GetPlayerName(game.GetCurrentPlayerID());

            if (current_player_name.equals(username) && !game.IsCurrentPlayerComputer()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized void MenageCycle(Engine game) throws NoSufficientMoneyException {
        if(game.IsCurrentHandStarted() &&!game.IsCurrentHandFinished()) {
            game.CheckBidStatus();
            //bid cycle check
            if (game.IsCurrentBidCycleFinished()) {

                switch (game.GetCurrentBidCycleNum()) {

                    case 1:
                        game.Flop();
                        break;
                    case 2:
                        game.Turn();
                        break;
                    case 3:
                        game.River();
                        break;
                    case 4:
                        game.SetWinner();
                        break;
                }

                if (!game.IsCurrentHandOver()) {
                    game.StartNewBidCycle();
                }
            }
        }
    }

    @Override
    public synchronized void SetNewMove(Engine game,String username,String move,String Value) throws NoSufficientMoneyException, MoveNotAllowdedException, PlayerAlreadyBetException, PlayerFoldedException, ChipLessThanPotException, StakeNotInRangeException {
        // get turn status
        boolean is_your_turn = false;
        if (game.IsCurrentHandStarted() && !game.IsCurrentHandOver()) {
            String current_player_name = game.GetPlayerName(game.GetCurrentPlayerID());

            if (current_player_name.equals(username)) {
                is_your_turn = true;
            } else {
                is_your_turn = false;
            }
        }
        if(is_your_turn)
        {
            int value = Integer.parseInt(Value);
            Move new_move=null;
            switch(move){
                case "CALL":
                    new_move=new Move(MoveType.CALL,value);
                    break;
                case "CHECK":
                    new_move=new Move(MoveType.CHECK,value);
                    break;
                case "FOLD":
                    new_move=new Move(MoveType.FOLD,value);
                    break;
                case "RAISE":
                    new_move=new Move(MoveType.RAISE,value);
                    break;
                case "BET":
                    new_move=new Move(MoveType.BET,value);
                    break;
            }
            game.SetNewMove(new_move);
        }
    }
}
