package API;

import Exceptions.*;
import Move.Move;
import Move.MoveType;
import Player.APlayer;
import Player.PlayerType;
import ReturnType.CurrentHandState;
import ReturnType.PlayerStats;
import Card.Card;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public interface Engine {
    ////////////////////////////////////////////////
    ///// OLD
    ////////////////////////////////////////////////
    //Statistics related API's
    public PlayerStats GetCurrentPlayerInfo();
    public List<PlayerStats> GetPlayersInfo();
    public CurrentHandState GetCurrentHandState();
    public boolean IsAnyPlayerOutOfMoney();
    public boolean IsCurrentPlayerNoMoney();


    ////////////////////////////////////////////////
    ///// Game
    ////////////////////////////////////////////////
    public void LoadFromXML(String filename) throws GameStartedException, UnexpectedObjectException, FileNotFoundException, BigSmallMismatchException, PlayerDataMissingException, HandsCountDevideException, WrongFileNameException, HandsCountSmallerException, JAXBException, FileNotXMLException, MinusZeroValueException, BigBiggerThanBuyException, MaxBigMoreThanHalfBuyException, PlayerdIDmismatchException;
    public String LoadFromXML(InputStream fstream) throws JAXBException, UnexpectedObjectException, MaxBigMoreThanHalfBuyException, BigSmallMismatchException, BigBiggerThanBuyException, HandsCountSmallerException, PlayerdIDmismatchException, HandsCountDevideException, MinusZeroValueException, GameStartedException;
    public void StartGame();
    public boolean IsGameOver();
    public boolean IsGameStarted();
    public int GetCurrentHandNumber();
    public int GetSmall();
    public int GetBig();
    public int GetMaxBuys();
    public int GetTotalNumberOfPlayers(); ///OLD one don't use it
    public void AddNewPlayer(String name, PlayerType type, int ID);
    public void AddNewPlayer(APlayer player) throws PlayerAlreadyInGameException;
    public int GetMoneyInGame();
    public int GetTotalNumOfPlayers();
    public int GetRegisteredNumOfPlayers();
    public int GetNumberOfHands();
    public String GetGameID();
    public void RegisterUploader(String username);
    public String GetUploaderName();
    public int GetBuy();

    ////////////////////////////////////////////////
    ///// Hand
    ////////////////////////////////////////////////
    public void StartNewHand() throws HandAlreadyStartedException;
    public void Flop();
    public void River();
    public void Turn();
    public int GetPot();
    public List<Card> GetCommunityCards();
    public boolean IsCurrentHandOver();
    public void PlayerPerformQuitFromGame(int id);

    public void CheckCurrentHandStatus();
    public void CheckNoActiveHumans();
    public boolean IsPlayerExist(int id);
    public boolean IsOnlyOnePlayerLeft();
    public boolean IsCurrentHandFinished();
    public boolean IsCurrentHandStarted();
    public boolean IsCurrentBidStarted();
    public boolean IsCurrentBidFinished();


    ////////////////////////////////////////////////
    ///// Bid Cycle
    ////////////////////////////////////////////////
    public void StartNewBidCycle() throws NoSufficientMoneyException;
    public List<MoveType> GetAllowdedMoves() throws PlayerFoldedException, ChipLessThanPotException;
    public void MoveToNextPlayer();
    public int[] GetAllowdedStakeRange();
    public Move GetAutoMove() throws PlayerFoldedException, ChipLessThanPotException;
    public void SetNewMove(Move move) throws StakeNotInRangeException, PlayerFoldedException, MoveNotAllowdedException, ChipLessThanPotException, NoSufficientMoneyException, PlayerAlreadyBetException;
    public void SetWinner();
    public List<String> GetWinner();
    public void CheckBidStatus();
    public int GetCurrentBidCycleNum();

    public boolean IsHumanPlayerFolded();
    public boolean IsCurrentBidCycleFinished();
    public boolean IsCurrentPlayerHuman();
    public boolean IsCurrentPlayerComputer();
    public boolean IsCurrentPlayerFolded();

    ////////////////////////////////////////////////
    ///// Player
    ////////////////////////////////////////////////
    public void AddPlayer(String Name,String Type) throws PlayerAlreadyBetException, PlayerdIDmismatchException;
    public List<Card> GetPlayersCards(int id);
    public void PlayerPerformBuy(int id);
    public int GetFirstPlayerID();
    public int GetCurrentPlayerID();
    public int GetNextPlayerID(int id);
    public int GetPlayerPot(int id);
    public int GetPlayerNumOfWins(int id);
    public boolean GetPlayerIsDealer(int id);
    public boolean GetPlayerIsBig(int id);
    public boolean GetPlayerIsSmall(int id);
    public boolean GetPlayerIsHuman(int id);
    public boolean GetPlayerIsFolded(int id);
    public int GetPlayerNumOfBuy(int id);
    public String GetPlayerName(int id);
    public void Buy();

    ////////////////////////////////////////////////
    ///// Replay
    ////////////////////////////////////////////////
    public void ReverseHandToStart();
    public String GetPreviousEvent();
    public String GetNextEvent();
    public String GetPlayerWinChance(int id);
    public void SetReplayMode(boolean state);
    public boolean IsReplayMode();
    public int GetCurrentEventNumber();
    public int GetTotalEventsNumber();
    public boolean IsFirstHand();
    public void SetGameOver(boolean is_game_over);
    public int GetAllowdedHandNumber();
    public boolean IfEnoughPlayers();
    public boolean IfPlayerExist(APlayer player);


    ////////////////////////////////////////////////
    /////
    ////////////////////////////////////////////////
    public void AddReadyPlayer();
    public void InitReadyPlayers();
    public boolean IsPlayersReady();
}