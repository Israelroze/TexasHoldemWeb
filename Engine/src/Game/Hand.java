package Game;

import Card.Card;
import Card.Deck;
import Exceptions.*;
import Generated.Structure;
import Player.*;
import Move.*;
import Replay.EventTypes;
import Replay.GameEvent;
import com.rundef.poker.EquityCalculator;
import com.rundef.poker.HandEquity;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Hand {
    //winners
    List<Integer> winners;

    //players data
    private APlayers players;
    private APlayer current_player;

    //cards
    private Card[] community=null;
    private Deck deck;

    //flags
    private boolean is_bets_started;
    private boolean is_bets_finished;
    private boolean is_hand_over;

    //bet stats
    private int higest_stake;
    private int poorest_player_chips;

    //money
    private int pot;

    //bets
    private int bet_num;
    private int big;
    private int small;

    //for replay
    private List<GameEvent> events;
    private int current_event_index;
    private boolean IsReplay=false;

    //ctor
    public Hand(APlayers players, Structure structure) {
        this.players = players;
        this.bet_num = 0;
        this.pot = 0;
        this.big = structure.getBlindes().getBig();
        this.small = structure.getBlindes().getSmall();
        this.deck = new Deck();
        this.winners = new LinkedList<>();
        this.events=new LinkedList<>();
        this.community=null;

        //init player flags
        this.players.InitPlayersNewHand();

        //forward player states
        this.players.ForwardStates();

        //deal cards
        this.DealCards();
    }

    //Private Methods
    private APlayer GetFirstPlayer() {
        if (bet_num == 1) {
            return this.players.GetBigPlayer();
        } else {
            return this.players.GetSmallPlayer();
        }
    }

    private void SetBlinds() throws NoSufficientMoneyException {
        //for small
        APlayer small = this.players.GetSmallPlayer();
        small.DecMoney(this.small);
        //small.setBetPlaceFlag(true);
        small.setStake(this.small);
        this.current_player=small;
        LogHandEvent(new Move(MoveType.BET,this.small));
        this.higest_stake = this.small;


        //for big
        APlayer big = this.players.GetBigPlayer();
        big.DecMoney(this.big);
        big.setBetPlaceFlag(true);
        big.setStake(this.big);
        this.current_player=big;
        LogHandEvent(new Move(MoveType.BET,this.big));
        this.higest_stake = this.big;

        //set pot
        this.pot = this.small + this.big;

        this.current_player = this.players.GetNextPlayer(big);
    }

    private void DealCards() {
        for (APlayer player : this.players.GetPlayers()) {
            player.SetCards(new Card[]{this.deck.PopCard(), this.deck.PopCard()});
        }
    }

    private void InitNotFinishedPlayers() {
        for (APlayer player : this.players.GetPlayers()) {
            if (player.getStake() < this.higest_stake) {
                player.setFoldedFlag(false);
            }
        }
    }

    private boolean IsAllStakesEqual() {
        for (APlayer player : this.players.GetPlayers()) {
            if (player.isPlacedBet() != true) {
                this.InitNotFinishedPlayers();
                return false;
            }
        }
        return true;
    }

    private boolean IsAllPlayersPlacedBet() {
        for (APlayer player : this.players.GetPlayers()) {
            if (!player.isPlacedBet()) {
                return false;
            }
        }
        return true;
    }

    private boolean IsAllFolded() {
        int count = 0;
        for (APlayer player : this.players.GetPlayers()) {
            if (player.isFolded() == false) {
                count++;
            }
        }
        if (count <= 1) return true;
        return false;
    }

    private void InitPlayerFlags() {

        //init players flags
        for (APlayer player : this.players.GetPlayers()) {
            if (!player.isFolded()) {
                player.ClearBidStats();
            }
        }
    }

    private void InitPlayersBetFlag(APlayer Cplayer) {
        //init players flags
        for (APlayer player : this.players.GetPlayers()) {
            if (Cplayer.getId() != player.getId() && !player.isFolded()) {
                player.setBetPlaceFlag(false);
            }
        }
    }

    private void IncPot(int amount) {
        this.pot = this.pot + amount;
    }


    //Public Methods

    //getters
    public int GetPoorestChipsValue() {
        Integer min = null;
        for (APlayer player : this.players.GetPlayers()) {
            if (min == null) {
                min = player.GetMoney()+player.getStake();
            } else {
                if (min > player.GetMoney()) {
                    min = player.GetMoney()+player.getStake();
                }
            }
        }
        return min.intValue();
    }

    public boolean GetIsHandOver(){return this.is_hand_over;}

    public Card[] GetCommunity() {
        return this.community;
    }

    public int[] GetAllowdedStakeRange() {
        if(Game.ENABLE_LOG) System.out.println("Player Type:"+this.current_player.GetType() +" ID:"+this.current_player.getId()+"getting allowded stake range");
        int low=this.higest_stake;
        int high=0;

        //int by_poorest=low+this.GetPoorestChipsValue();
        int by_poorest=this.GetPoorestChipsValue();
        int by_pot=this.pot;

        if(by_poorest<by_pot)
        {
            high=by_poorest;
        }
        else
        {
            high=by_pot;
        }

        if(Game.ENABLE_LOG) System.out.println("Player Type:"+this.current_player.GetType() +" ID:"+this.current_player.getId()+"the range is: low:"+low+" high:"+high+"....");
        return new int[]{low+1, high};
    }

    public List<MoveType> GetAllowdedMoves() throws PlayerFoldedException, ChipLessThanPotException {

        if(Game.ENABLE_LOG) System.out.println("Player Type:"+this.current_player.GetType() +" ID:"+this.current_player.getId()+"Getting  allowded moves...");
        List<MoveType> allowded_moves=new LinkedList<>();
        int[] ranges=this.GetAllowdedStakeRange();

        if(this.current_player.GetIsFoldedFlag())
        {
            throw new PlayerFoldedException();
        }
        if(this.current_player.GetMoney()<(this.higest_stake-this.current_player.getStake())) //not suppose to happen really
        {
            throw new ChipLessThanPotException(this.current_player.GetMoney());
        }

        if(this.IsOneOfPlayersOutOfMoney() && this.higest_stake==this.current_player.getStake()) {//
            if(Game.ENABLE_LOG)System.out.println("FROM HAND: player have no money but its ok, no one raised or bet.");
            allowded_moves.add(MoveType.CHECK);
            allowded_moves.add(MoveType.FOLD);
            if(Game.ENABLE_LOG)System.out.println("Player Type:"+this.current_player.GetType() +" ID:"+this.current_player.getId()+"allowded moves: check,fold");
        }
        else {
            if (this.higest_stake == 0) // no bet placed, the player can Check,Bet,Fold
            {
                allowded_moves.add(MoveType.BET);
                allowded_moves.add(MoveType.CHECK);
                allowded_moves.add(MoveType.FOLD);
                //moves.AddMove(new Move(MoveType.CHECK,0));
                //moves.AddMove(new Move(MoveType.FOLD,0));
                if (Game.ENABLE_LOG) System.out.println("Player Type:" + this.current_player.GetType() + " ID:" + this.current_player.getId() + "allowded moves: bet,check,fold");
            } else {
                if (this.higest_stake >= ranges[1] ) ///If the current table stake is them maximum, only call or fold.
                {
                    if (Game.ENABLE_LOG) System.out.println("FROM HAND: highest stake is the maximum! can go higher");
                    allowded_moves.add(MoveType.CALL);
                    allowded_moves.add(MoveType.FOLD);
                    if (Game.ENABLE_LOG) System.out.println("Player Type:" + this.current_player.GetType() + " ID:" + this.current_player.getId() + "allowded moves: call,fold");

                } else {
                    if (this.current_player.getStake() < this.higest_stake)//player need to Call,Raise or Fold
                    {
                        if (Game.ENABLE_LOG)
                            if (Game.ENABLE_LOG) System.out.println("FROM HAND:current player stake less than highest stake");
                        allowded_moves.add(MoveType.RAISE);
                        allowded_moves.add(MoveType.CALL);
                        allowded_moves.add(MoveType.FOLD);
                        if (Game.ENABLE_LOG)
                            if (Game.ENABLE_LOG) System.out.println("Player Type:" + this.current_player.GetType() + " ID:" + this.current_player.getId() + "allowded moves: raise,call,fold");
                    } else {
                        if (this.current_player.getStake() == this.higest_stake) {
                            if (Game.ENABLE_LOG) System.out.println("FROM HAND:current player stake equal than highest stake");
                            allowded_moves.add(MoveType.RAISE);
                            allowded_moves.add(MoveType.CHECK);
                            allowded_moves.add(MoveType.FOLD);
                            if (Game.ENABLE_LOG) System.out.println("Player Type:" + this.current_player.GetType() + " ID:" + this.current_player.getId() + "allowded moves: raise,check,fold");

                        } else //current player stake bigger than highest stake
                        {
                            if (Game.ENABLE_LOG) System.out.println("FROM HAND:current player stake bigger than highest stake");

                        }
                    }
                }

                if (Game.ENABLE_LOG) System.out.println("FROM HAND: highest stake:" + this.higest_stake + " poorest:" + ranges[1]);
            }
        }
        return allowded_moves;
    }

    public int GetHigestStake() {
        return this.higest_stake;
    }

    public APlayer GetCurrentPlayer() {
        return this.current_player;
    }

    public int GetPot() {
        return this.pot;
    }

    public int GetBetCycleNumber() {
        return this.bet_num;
    }

    public APlayer GetNextPlayer() {
        return this.players.GetNextPlayer(this.current_player);
    }

    private String GetBoardForCalculation() {
        //boards cards to string
        String board="";
        for(Card card: this.community)
        {
            if(card!=null) {
                String type;
                String number;
                type = card.GetSuit().toString().toLowerCase();
                number = card.GetNumber().toString();
                if (number.equals("10")) {
                    number = "T";
                }
                if (number.equals("1")) {
                    number = "A";
                }
                number = number.toLowerCase();
                board += number + type;
            }
        }
        return board;
    }

    private String GetPlayerHand(APlayer player) {
        String str_hand = "";

        for (Card card : player.GetCards()) {
            String type;
            String number;
            type = card.GetSuit().toString().toLowerCase();
            number = card.GetNumber().toString();
            if (number.equals("10")) {
                number = "T";
            }
            if (number.equals("1")) {
                number = "A";
            }
            number = number.toLowerCase();
            str_hand += number + type;
        }

        return str_hand;
    }

    public List<String> GetWinnerNames() {
        List<String> win_names=new LinkedList<>();
        for (Integer index : this.winners) {
            win_names.add(this.players.GetPlayers().get(index).GetName());
        }
        return win_names;
    }

    //setters

    public void setPot(int pot) {
        this.pot = pot;
    }

    public void SetIsBetCycleFinished() {
        if(this.IsAllPlayersPlacedBet()) this.is_bets_finished=true;
    }

    public void StartNewBidCycle() throws NoSufficientMoneyException {
        if(!this.is_hand_over) {
            //increase bet cycle number
            this.bet_num++;

            //init highest stake
            this.higest_stake = 0;

            //init flags
            this.is_bets_started = true;
            this.is_bets_finished = false;

        /*for(APlayer player: this.players.GetPlayers())
        {
            if(player.GetType()==PlayerType.HUMAN)
            {
                if(player.isFolded())
                {
                    this.is_bets_finished=true;
                }
            }
        }*/

            //init players flags
            this.InitPlayerFlags();

          /*  //forward states
            this.players.ForwardStates();*/

            //set first playing player
            this.current_player = this.GetFirstPlayer();

            //blinds
            if (this.bet_num == 1) {
                this.SetBlinds();
            }
        }
    }

    public void SetWinner() {
        List<Integer> winner_indexs=new LinkedList<>();
        EquityCalculator calculator = new EquityCalculator();

        //boards cards to string
        String board=GetBoardForCalculation();

        if(Game.ENABLE_LOG) System.out.println("FROM HAND: building Winner calculation, board string:"+board);
        try {
            calculator.setBoardFromString(board);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int num_of_hands=0;

        //player cards to string
        for(APlayer player:this.players.GetPlayers())
        {
            if(!player.GetIsFoldedFlag()) {
                String str_hand = GetPlayerHand(player);

                if (Game.ENABLE_LOG)
                    System.out.println("FROM HAND: Player ID:" + player.getId() + "building Winner calculation, Hand string:" + str_hand);
                try {
                    com.rundef.poker.Hand hand = com.rundef.poker.Hand.fromString(str_hand);
                    calculator.addHand(hand);
                    winner_indexs.add(this.players.GetPlayers().indexOf(player));
                    num_of_hands++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        //calculate the winner
        try {
            calculator.calculate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < num_of_hands; i++) {
            HandEquity equity=calculator.getHandEquity(i);
            if(equity.getEquity()!=0)//we have a winner
            {
                if(Game.ENABLE_LOG) System.out.println("Adding player in index"+winner_indexs.get(i)+" as winner");
                this.winners.add(winner_indexs.get(i));
            }
            if(Game.ENABLE_LOG) System.out.println("Player ID"+this.players.GetPlayers().get(winner_indexs.get(i)).getId()+"Hand #" + (i+1) + ": Rank: " + calculator.getHandRanking(i) + " Equity: " + calculator.getHandEquity(i));
        }

        //set the number of wins for the players
        this.SetWinnersCounter();

        //devide the pot between the winners
        this.PassPot();



        this.is_hand_over=true;
    }

    public void SetTechincalWinner() {
        int index=0;
        for( APlayer player :this.players.GetPlayers())
        {
            if(player.GetIsFoldedFlag()==false)
            {
                this.winners.add(index);
            }
            index++;
        }

        //set the number of wins for the players
        this.SetWinnersCounter();

        //devide the pot between the winners
        this.PassPot();

        //LogHandEvent(EventTypes.Winner);

        this.is_hand_over=true;

    }

    private void SetWinnersCounter() {
        for(Integer index:this.winners)
        {
            this.players.GetPlayers().get(index).IncWinner();
        }
    }


    //actions

    public void ImplementMove(MoveType move,int stake) throws NoSufficientMoneyException, PlayerFoldedException, ChipLessThanPotException, MoveNotAllowdedException, StakeNotInRangeException, PlayerAlreadyBetException {

        //if(!this.is_hand_over) {
        if (move == null) {
            if (Game.ENABLE_LOG)
                System.out.println("FROM GAME: current player:" + this.current_player.getId() + " cant play any more");
            this.current_player.setBetPlaceFlag(true);
            return;
        }

        if (this.current_player.isPlacedBet()) {
            throw new PlayerAlreadyBetException();
        }

        if (!this.IsMoveAllowded(move)) {
            throw new MoveNotAllowdedException();
        }

        if (move == MoveType.BET /* || move==MoveType.RAISE*/) {
            if (!this.IsStakeInRange(stake)) {
                throw new StakeNotInRangeException();
            }
        }

        int delta;
        switch (move) {
            case BET:
                if (Game.ENABLE_LOG)
                    System.out.println("Player Type:" + this.current_player.GetType() + " ID:" + this.current_player.getId() + "Implementing move... type:bet stake:" + stake);
                this.current_player.DecMoney(stake);
                this.current_player.setStake(stake);
                this.current_player.setBetPlaceFlag(true);
                this.IncPot(stake);
                this.InitPlayersBetFlag(this.current_player);
                LogHandEvent(new Move(move,stake));
                this.current_player = this.players.GetNextPlayer(this.current_player);
                this.higest_stake = stake;
                break;
            case RAISE:
                if (Game.ENABLE_LOG)
                    System.out.println("Player Type:" + this.current_player.GetType() + " ID:" + this.current_player.getId() + "Implementing move... type:raise stake:" + stake);
                delta = stake - this.current_player.getStake();
                this.current_player.DecMoney(delta);
                this.current_player.setStake(stake);
                this.current_player.setBetPlaceFlag(true);
                this.IncPot(delta);
                this.InitPlayersBetFlag(this.current_player);
                LogHandEvent(new Move(move, delta));
                this.current_player = this.players.GetNextPlayer(this.current_player);
                this.higest_stake = stake;
                break;
            case CALL:
                if (Game.ENABLE_LOG)
                    System.out.println("Player Type:" + this.current_player.GetType() + " ID:" + this.current_player.getId() + "Implementing move... type:call stake:" + stake);
                delta = this.higest_stake - this.current_player.getStake();
                this.current_player.DecMoney(delta);
                this.current_player.setStake(this.higest_stake);
                this.current_player.setBetPlaceFlag(true);
                this.IncPot(delta);
                LogHandEvent(new Move(move, delta));
                this.current_player = this.players.GetNextPlayer(this.current_player);
                break;
            case CHECK:
                if (Game.ENABLE_LOG)
                    System.out.println("Player Type:" + this.current_player.GetType() + " ID:" + this.current_player.getId() + "Implementing move... type:check stake:" + stake);
                this.current_player.setBetPlaceFlag(true);
                LogHandEvent(new Move(move, 0));
                this.current_player = this.players.GetNextPlayer(this.current_player);
                break;
            case FOLD:
                if (Game.ENABLE_LOG)
                    System.out.println("Player Type:" + this.current_player.GetType() + " ID:" + this.current_player.getId() + "Implementing move... type:fold stake:" + stake);
                this.current_player.setBetPlaceFlag(true);
                this.current_player.setFoldedFlag(true);
                LogHandEvent(new Move(move, 0));
                this.current_player = this.players.GetNextPlayer(this.current_player);
                break;
        }

        this.SetIsBetCycleFinished();
        //}
    }

    public void Flop() {
        if(!this.is_hand_over) {
            this.community = new Card[5];
            for (int i = 0; i < 3; i++) {
                this.community[i] = this.deck.PopCard();
            }
            LogHandEvent(EventTypes.Flop);
        }
    }

    public void River() {
        if(!this.is_hand_over) {
            this.community[3] = this.deck.PopCard();
            LogHandEvent(EventTypes.River);
        }
    }

    public void Turn() {
        if(!this.is_hand_over){
            this.community[4]=this.deck.PopCard();
            LogHandEvent(EventTypes.Turn);
        }
    }

    public void MoveToNextPlayer()
    {
        this.current_player=this.players.GetNextPlayer(this.current_player);
    }

    private void PassPot() {
        if(this.winners.size()<1)
        {
            if(Game.ENABLE_LOG) System.out.println("Basa");
        }
        else {
            if (this.winners.size() > 1)//we have more than one winner
            {
                int share = this.pot / this.winners.size();
                //int seerit=this.pot%share;
                for (Integer index : this.winners) {
                    this.players.GetPlayers().get(index).AddMoney(share);
                    LogHandWinnerEvent(this.winners.get(index),share);
                }
            } else {//only one
                this.players.GetPlayers().get(this.winners.get(0)).AddMoney(this.pot);
                LogHandWinnerEvent(this.winners.get(0),this.pot);
            }

            this.pot=0;
        }
    }

    private void CalculateWinChance() {
        EquityCalculator calculator = new EquityCalculator();

        //boards cards to string
        if(this.community!=null && this.community.length>0  ) {
            //boards cards to string
            String board = GetBoardForCalculation();

            if (Game.ENABLE_LOG) System.out.println("FROM HAND: building Winner calculation, board string:" + board);
            if(!board.equals("")) {
                try {
                    calculator.setBoardFromString(board);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        int num_of_hands=0;
        //player cards to string
        for(APlayer player:this.players.GetPlayers())
        {
            String str_hand=GetPlayerHand(player);

            if(Game.ENABLE_LOG) System.out.println("FROM HAND: Player ID:"+player.getId()+ "building Winner calculation, Hand string:" + str_hand);
            try {
                com.rundef.poker.Hand hand = com.rundef.poker.Hand.fromString(str_hand);
                calculator.addHand(hand);
                num_of_hands++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //calculate the winner
        try {
            calculator.calculate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //update the chances
        for (int i = 0; i < num_of_hands; i++) {
            HandEquity equity=calculator.getHandEquity(i);
            this.players.GetPlayers().get(i).SetWinChance(equity.toString());
           /* if(equity.getEquity()!=0)//we have a winner
            {
                this.players.GetPlayers().get(i).SetWinChance(String.valueOf(equity.getEquity())+"%");
            }*/
        }
    }

    //checks
    public boolean IsOneOfPlayersOutOfMoney() {
        for(APlayer player:this.players.GetPlayers())
        {
            if(player.GetMoney()==0) return true;
        }
        return false;
    }

    public boolean IsMoveAllowded(MoveType mtype) throws PlayerFoldedException, ChipLessThanPotException {
        for(MoveType move:this.GetAllowdedMoves())
        {
            if(mtype==move)
            {
                return true;
            }
        }
        return false;
    }

    public boolean IsStakeInRange(int stake) {
        int[] range=this.GetAllowdedStakeRange();

        if(stake>=range[0] && stake<=range[1])
        {
            return true;
        }
        return false;
    }

    public boolean IsBetsCycleFinished() {
        return this.is_bets_finished;
    }

    public boolean IsHandOver() {
        return this.is_hand_over;
    }

    public boolean IsOnlyOnePlayerActive(){
        int num_of_active_players=this.players.GetSize();
        for( APlayer player :this.players.GetPlayers())
        {
            if(player.GetIsFoldedFlag()==true)
            {
                num_of_active_players--;
            }
        }

        if(num_of_active_players==1) return true;
        return false;
    }

    public boolean IsNoPlayerActive(){
        int num_of_active_players=this.players.GetSize();
        for( APlayer player :this.players.GetPlayers())
        {
            if(player.GetIsFoldedFlag()==true)
            {
                num_of_active_players--;
            }
        }

        if(num_of_active_players<1) return true;
        return false;
    }

    public void CheckHandStatus() {
        if(this.IsOnlyOnePlayerActive() && !this.is_hand_over)
        {
            this.SetTechincalWinner();
            return;
        }
    }

    public void CheckNoActiveHumanPlayers(){
        int active_humans=0;
        for(APlayer player: this.players.GetPlayers())
        {
            if(player.GetType()==PlayerType.HUMAN && !player.GetIsFoldedFlag())
            {
                active_humans++;
            }
        }

        if(active_humans==0) this.is_hand_over=true;

    }


    //for replay
    private void LogHandEvent(Move move){
        if (Game.ENABLE_LOG)
            System.out.println("FROM HAND: EVENT LOGGED: type: move "+", move type:"+move.GetMoveType()+" move value:"+move.GetValue());
        GameEvent new_event=new GameEvent(EventTypes.Move,move,this.current_player.getId());
        this.events.add(new_event);
        this.current_event_index=this.events.size()-1;
    }

    private void LogHandEvent(EventTypes event){
        GameEvent new_event=new GameEvent(event);

        switch(event){
            case Flop:
                new_event.AddEventCard(this.community[0]);
                new_event.AddEventCard(this.community[1]);
                new_event.AddEventCard(this.community[2]);
                break;
            case River:
                new_event.AddEventCard(this.community[3]);
                break;
            case Turn:
                new_event.AddEventCard(this.community[4]);
                break;
            case Winner:
                for(int index:this.winners)
                {
                    int id=this.players.GetPlayers().get(index).getId();
                    int money=this.players.GetPlayers().get(index).GetMoney();
                    new_event.AddWinner(id,money);
                }
                break;
        }
        this.events.add(new_event);
        this.current_event_index=this.events.size()-1;
    }

    private void LogHandWinnerEvent(int index,int amount){
        GameEvent new_event=new GameEvent(EventTypes.Winner,amount);
        new_event.AddWinner(index,amount);
        this.events.add(new_event);
        this.current_event_index=this.events.size()-1;
    }

    public void RevertToStart(){
        for(int i=0;i<this.events.size();i++)
        {
            this.RevertEvent();
        }
        this.CalculateWinChance();
    }

    public String RevertEvent(){
        if(this.IsReplay) this.current_event_index--;
        if(this.current_event_index<0) {
            return "This is the first event, back option is not available.";
        }
        String Return_msg="";
        if(Game.ENABLE_LOG) System.out.println("FROM HAND REPLAY Revert EVENT: POT:"+this.pot +" event number"+this.current_event_index);
        switch(this.events.get(this.current_event_index).GetEventType()) {
            case Move:
                Move move = this.events.get(this.current_event_index).GetMove();
                APlayer player = this.players.GetPlayer(this.events.get(this.current_event_index).GetPlayerId());
                switch (move.GetMoveType()) {
                    case BET:
                        player.SetMoney(player.GetMoney() + move.GetValue());
                        this.setPot(this.pot - move.GetValue());
                        this.current_player=player;
                        Return_msg=player.GetName() + " placed bet with the value of " + move.GetValue();
                        break;
                    case RAISE:
                        player.SetMoney(player.GetMoney() + move.GetValue());
                        this.setPot(this.pot - move.GetValue());
                        this.current_player=player;
                        if(this.current_event_index>=1)
                        {
                            Return_msg=player.GetName() + " raised by" + (move.GetValue()-this.events.get(this.current_event_index-1).GetMove().GetValue());
                        }
                        else
                        {
                            Return_msg=player.GetName() + " raised by" + move.GetValue();
                        }
                        break;
                    case CALL:
                        player.SetMoney(player.GetMoney() + move.GetValue());
                        this.setPot(this.pot - move.GetValue());
                        this.current_player=player;
                        Return_msg=player.GetName() + " called";
                        break;
                    case CHECK:
                        //player.SetMoney(this.current_player.GetMoney() + move.GetValue());
                        //this.setPot(this.pot - move.GetValue());
                        this.current_player=player;
                        Return_msg=player.GetName() + " checked";
                        break;
                    case FOLD:
                        //player.SetMoney(this.current_player.GetMoney() + move.GetValue());
                        //this.setPot(this.pot - move.GetValue());
                        this.current_player=player;
                        Return_msg=player.GetName() + " folded";
                        break;
                }
                break;
            case Flop:
                if(Game.ENABLE_LOG) System.out.println("FROM HAND REPLAY Revert EVENT: flop");
                this.community[0] = null;
                this.community[1] = null;
                this.community[2] = null;
                this.CalculateWinChance();
                Return_msg=" Flop";
                break;

            case River:
                if(Game.ENABLE_LOG) System.out.println("FROM HAND REPLAY Revert EVENT: river");
                this.community[3] = null;
                Return_msg=" River";
                //this.CalculateWinChance();
                break;

            case Turn:
                if(Game.ENABLE_LOG) System.out.println("FROM HAND REPLAY Revert EVENT: turn");
                this.community[4] = null;
                Return_msg=" Turn";
                //this.CalculateWinChance();
                break;

            case Winner:
                if(Game.ENABLE_LOG) System.out.println("FROM HAND REPLAY Revert EVENT: setting winner");
                List<Integer> winners=this.events.get(this.current_event_index).GetWinners();
                for (int i=0;i<winners.size();i++) {
                    int index=winners.get(i);
                    APlayer winner=this.players.GetPlayers().get(index);
                    int amount = this.events.get(this.current_event_index).GetWinnerMoney(i);
                    Return_msg="Player "+winner.GetName()+" is winner with the amount of "+amount;
                    this.IncPot(amount);
                    try {
                        winner.DecMoney(amount);
                        winner.DecWinner();
                    } catch (NoSufficientMoneyException e) {

                    }
                }
                break;
        }
     /*   if(this.IsReplay){//
            if(this.current_event_index<0) return "This is the first event, back option is not available.";
            this.current_event_index--;
            if(this.current_event_index<0) return "This is the first event, back option is not available.";
        }*/

        if(!this.IsReplay) this.current_event_index--;
        //this.current_event_index--;
        if(Game.ENABLE_LOG) System.out.println("FROM HAND REPLAY Revert EVENT:"+Return_msg);
        return Return_msg;
    }

    public String PerformEvent(){

        if(this.current_event_index>this.events.size()-1) return "This is the last event, forward option not available.";
        if(this.current_event_index<0) this.current_event_index=0;

        String Return_msg="";
        if(Game.ENABLE_LOG) System.out.println("FROM HAND REPLAY Forward EVENT: POT:"+this.pot +" event number"+this.current_event_index);
        switch(this.events.get(this.current_event_index).GetEventType()) {
            case Move:
                Move move = this.events.get(this.current_event_index).GetMove();
                APlayer player = this.players.GetPlayer(this.events.get(this.current_event_index).GetPlayerId());

                switch (move.GetMoveType()) {
                    case BET:
                        try {
                            player.DecMoney(move.GetValue());
                        } catch (NoSufficientMoneyException e) {

                        }
                        this.IncPot(move.GetValue());
                        this.current_player=player;
                        Return_msg=player.GetName() + " placed bet with the value of" + move.GetValue();
                        break;
                    case RAISE:
                        try {
                            player.DecMoney(move.GetValue());
                        } catch (NoSufficientMoneyException e) {

                        }
                        this.IncPot(move.GetValue());
                        this.current_player=player;
                        if(this.current_event_index>=1)
                        {
                            Return_msg=player.GetName() + " raised by" + (move.GetValue()-this.events.get(this.current_event_index-1).GetMove().GetValue());
                        }
                        else
                        {
                            Return_msg=player.GetName() + " raised by" + move.GetValue();
                        }
                        break;
                    case CALL:
                        try {
                            player.DecMoney(move.GetValue());
                        } catch (NoSufficientMoneyException e) {

                        }
                        this.IncPot(move.GetValue());
                        this.current_player=player;
                        Return_msg=player.GetName() + " called";
                        this.current_player=player;
                        break;
                    case CHECK:
                        this.current_player=player;
                        Return_msg=player.GetName() + " checked";
                        break;
                    case FOLD:
                        this.current_player=player;
                        Return_msg=player.GetName() + " folded";
                        break;
                }
                break;
            case Flop:
                if(Game.ENABLE_LOG) System.out.println("FROM HAND REPLAY Forward EVENT: Flop");
                this.community[0] = this.events.get(this.current_event_index).GetCards().get(0);
                this.community[1] = this.events.get(this.current_event_index).GetCards().get(1);
                this.community[2] = this.events.get(this.current_event_index).GetCards().get(2);
                Return_msg=" Flop";
                this.CalculateWinChance();
                break;

            case River:
                if(Game.ENABLE_LOG) System.out.println("FROM HAND REPLAY Forward EVENT: River");
                this.community[3] = this.events.get(this.current_event_index).GetCards().get(0);
                Return_msg=" River";
                this.CalculateWinChance();
                break;

            case Turn:
                if(Game.ENABLE_LOG) System.out.println("FROM HAND REPLAY Forward EVENT: turn");
                this.community[4] = this.events.get(this.current_event_index).GetCards().get(0);
                Return_msg=" Turn";
                this.CalculateWinChance();
                break;
            case Winner:
                if(Game.ENABLE_LOG) System.out.println("FROM HAND REPLAY Forward EVENT: setting winner");
                List<Integer> winners=this.events.get(this.current_event_index).GetWinners();
                for (int i=0;i<winners.size();i++) {
                    int index=winners.get(i);
                    APlayer winner=this.players.GetPlayers().get(index);
                    int amount = this.events.get(this.current_event_index).GetWinnerMoney(i);
                    Return_msg="Player "+winner.GetName()+" is winner with the amount of "+amount;
                    this.setPot(this.pot-amount);
                    winner.IncWinner();
                    winner.SetMoney(winner.GetMoney()+amount);
                }
                break;
        }
        if(this.current_event_index<this.events.size()) this.current_event_index++;

        if(Game.ENABLE_LOG) System.out.println("FROM HAND REPLAY Forward EVENT:"+Return_msg);
        return Return_msg;
    }

    public boolean GetReplayMode()
    {
        return this.IsReplay;
    }

    public int GetCurrentEventNumber(){
        return this.current_event_index;
    }

    public int GetTotalEventsNumber(){
        return this.events.size();
    }

    public void SetReplayMode(boolean state)
    {
        this.IsReplay=state;
    }
}
