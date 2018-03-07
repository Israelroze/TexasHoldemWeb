import Exceptions.*;
import Generated.JAXB_Generator;
import Game.Game;
import Player.APlayers;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

/*
public class Tester {
   public static void main(String[] args) throws PlayerDataMissingException {
       String conf_file="C:\\Users\\irozenblit\\Documents\\Private\\Java\\TexasHoldem\\EngineUtils\\Resource\\master.xml";
       Game game=new Game();

       boolean isLoadOK=true;
       //load XML test
       try {
           game.LoadFromFile(conf_file);
       } catch (FileNotFoundException e) {
           System.out.println("File not Exist!");
           isLoadOK=false;
       } catch (FileNotXMLException e) {
           System.out.println("File is not XML!");
           isLoadOK=false;
       } catch (WrongFileNameException e) {
           System.out.println("File name is not right!");
           isLoadOK=false;
       } catch (JAXBException e) {
           System.out.println("JAXB error:"+e.getMessage());
           isLoadOK=false;
       } catch (NullObjectException e) {
           System.out.println("NUlL object:"+e.GetObjectName());
           isLoadOK=false;
       } catch (UnexpectedObjectException e) {
           System.out.println("WRONG object:"+e.GetObjectName());
           isLoadOK=false;
       } catch (HandsCountDevideException e) {
           System.out.println("Hands number not devided between the playesr number!");
           isLoadOK=false;
       } catch (BigSmallMismatchException e) {
           System.out.println("Big value smaller than Small value!");
           isLoadOK=false;
       } catch (HandsCountSmallerException e) {
           System.out.println("Hands number smaller than the number of players!");
           isLoadOK=false;
       } catch (GameStartedException e) {
           System.out.println("Game already started,cant load new xml file!");
       }

       //PLayers allocation and random test
       if(isLoadOK)
       {
           game.LoadPlayers();
           APlayers players=game.GetPlayers();

           System.out.println("Start:");
           players.PrintPlayers();

           players.RandomPlayerSeats();

           System.out.println("\nAfter Random:");
           players.PrintPlayers();

           players.ForwardStates();

           System.out.println("\nDealer:");
           System.out.println(players.GetDealer().GetName());

           System.out.println("\nSmall:");
           System.out.println(players.GetSmallPlayer().GetName());

           System.out.println("\nBig:");
           System.out.println(players.GetBigPlayer().GetName());

           players.ForwardStates();

           System.out.println("\nDealer:");
           System.out.println(players.GetDealer().GetName());

           System.out.println("\nSmall:");
           System.out.println(players.GetSmallPlayer().GetName());

           System.out.println("\nBig:");
           System.out.println(players.GetBigPlayer().GetName());
       }



   }
}
*/
