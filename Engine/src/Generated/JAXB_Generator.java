package Generated;

import Exceptions.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class JAXB_Generator {

    private final static String PACKAGE_NAME = "Generated";
    private JAXBContext generator;
    private String conf_file;
    private GameDescriptor container;
    private File file;
    private InputStream file_stream;

    public JAXB_Generator(String file_name) throws JAXBException, FileNotFoundException, WrongFileNameException, FileNotXMLException {

        //validate xml extension
        String[] path_parts=file_name.split("\\.");
        int len=path_parts.length;
        if (path_parts.length<2){ throw new WrongFileNameException(); }
        if(!path_parts[path_parts.length-1].equals("xml")){ throw new FileNotXMLException(); }

        //validate file exist
        file=new File((file_name));
        if(!file.exists()){throw new FileNotFoundException();}

        //create input stream
        file_stream=new FileInputStream(file);

        //create JAXB Instance
        generator = JAXBContext.newInstance(PACKAGE_NAME);
    }

    public JAXB_Generator(InputStream fstream) throws JAXBException{

        //create input stream
        this.file_stream=fstream;

        //create JAXB Instance
        this.generator = JAXBContext.newInstance(PACKAGE_NAME);
    }

    public void GenerateFromXML() throws JAXBException, NullObjectException, UnexpectedObjectException {
        Unmarshaller u = this.generator.createUnmarshaller();
        container = (GameDescriptor) u.unmarshal(this.file_stream);
        if(container==null){throw new NullObjectException("GameDescriptor");}
        if(!container.getClass().getSimpleName().equals("GameDescriptor")){throw new UnexpectedObjectException("GameDescriptor");}
    }



    public void ValidateXMLData() throws BigSmallMismatchException, HandsCountSmallerException, HandsCountDevideException, NullObjectException, PlayerdIDmismatchException {

        try {
            container.getStructure();
        }
        catch (NullPointerException e){
            throw  new NullObjectException("Structure");
        }
        try {
            container.getStructure().getBlindes();
        }
        catch (NullPointerException e){
            throw  new NullObjectException("Blindes");
        }



        try {
            container.getPlayers();
        }
        catch (NullPointerException e){
            throw  new NullObjectException("Players");
        }
        try {
            container.getPlayers().getPlayer();
        }
        catch (NullPointerException e){
            throw  new NullObjectException("Players List");
        }



        int big=container.getStructure().getBlindes().getBig();
        int small=container.getStructure().getBlindes().getSmall();
        int num_of_players;
        if(container.getPlayers().getPlayer()!=null)
        {
            num_of_players=container.getPlayers().getPlayer().size();
        }
        else {
            num_of_players = 4;
        }
        int num_of_hands=container.structure.getHandsCount();

        if(big<=small){
            throw new BigSmallMismatchException();
        }

        if(num_of_hands<num_of_players){
            throw new HandsCountSmallerException();
        }
        else
        {
            if(num_of_hands%num_of_players!=0){
                throw new HandsCountDevideException();
            }
        }
    }

    public GameDescriptor getContainer()
    {
        return this.container;
    }
}
