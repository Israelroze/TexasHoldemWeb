package API;

import Exceptions.*;
import Move.Move;
import Move.MoveType;
import Player.PlayerType;
import ReturnType.CurrentHandState;
import ReturnType.PlayerStats;
import Card.Card;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public interface  InterfaceAPI {

    //option 1
    public void LoadFromXML(String filename) throws GameStartedException, UnexpectedObjectException, FileNotFoundException, BigSmallMismatchException, PlayerDataMissingException, HandsCountDevideException, WrongFileNameException, HandsCountSmallerException, JAXBException, FileNotXMLException, MinusZeroValueException, BigBiggerThanBuyException, MaxBigMoreThanHalfBuyException, PlayerdIDmismatchException;
    //option2
    public void StartGame();

    //Hand related API
    public void StartNewHand();
    public int GetNumberOfHands();
    public boolean IsCurrentHandFinished();
    public int GetCurrentHandNumber();
    public void Flop();
    public void River();
    public void Turn();

    //Buy API
    public void Buy();
    public int GetMoneyInGame();

    //Bid related API's
    public void StartNewBidCycle() throws NoSufficientMoneyException;
    public boolean IsHumanPlayerFolded();
    public boolean IsCurrentBidCycleFinished();
    public boolean IsCurrentPlayerHuman();
    public boolean IsCurrentPlayerComputer();
    public List<MoveType> GetAllowdedMoves() throws PlayerFoldedException, ChipLessThanPotException;
    public void MoveToNextPlayer();
    public int[] GetAllowdedStakeRange();
    public Move GetAutoMove() throws PlayerFoldedException, ChipLessThanPotException;
    public void SetNewMove(Move move) throws StakeNotInRangeException, PlayerFoldedException, MoveNotAllowdedException, ChipLessThanPotException, NoSufficientMoneyException, PlayerAlreadyBetException;
    public void SetWinner();
    public List<String> GetWinner();
    public PlayerStats GetCurrentPlayerInfo();
    public void CheckBidStatus();
    public boolean IsCurrentPlayerFolded();
    public void AddNewPlayer(String name, PlayerType type, int ID);
    //Statistics related API's
    public List<PlayerStats> GetPlayersInfo();
    public CurrentHandState GetCurrentHandState();
    public boolean IsAnyPlayerOutOfMoney();
    public boolean IsCurrentPlayerNoMoney();


    /////API FOR TARGIL 2

    //public int GetCurrentHandNumber(); // TBD -Check
    public int GetMaxBuys();
    public int GetSmall();
    public int GetBig();
    public int GetPot();
    public int GetTotalNumberOfPlayers();
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
    public List<Card> GetPlayersCards(int id);
    public List<Card> GetCommunityCards();
    public boolean IsCurrentHandOver();
    public void CheckCurrentHandStatus();
    public void PlayerPerformBuy(int id);
    public void PlayerPerformQuitFromGame(int id);
    public boolean IsPlayerExist(int id);
    public void CheckNoActiveHumans();
    public boolean IsOnlyOnePlayerLeft();
    public boolean IsGameOver();

    //for replay
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


    /////API FOR TARGIL 3

    public String LoadFromXML(InputStream fstream) throws JAXBException, UnexpectedObjectException, MaxBigMoreThanHalfBuyException, BigSmallMismatchException, BigBiggerThanBuyException, HandsCountSmallerException, PlayerdIDmismatchException, HandsCountDevideException, MinusZeroValueException, GameStartedException;
    public int GetTotalNumOfPlayers();
    public int GetRegisteredNumOfPlayers();
    public void AddPlayer(String Name,String Type) throws PlayerAlreadyBetException, PlayerdIDmismatchException;
}