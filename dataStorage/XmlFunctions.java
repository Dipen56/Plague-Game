package dataStorage;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import dataStorage.adapters.GameAdapter;
import dataStorage.adapters.InitialGameAdapter;
import server.game.Game;

public class XmlFunctions {

	/**
	 * Saves an object's state as an XML file.
	 * The object will require an XmlRootElement annotation above the class header.
	 * Any internal fields or methods with the XmlElement annotation will be added to the file.
	 * Relies upon the saveProcess method.
	 * @param obj		An object to save to the file.
	 * @param prefix	A string which determines the prefix of the save file.
	 */
	public static void saveFile(Object obj, String prefix){
	    if(obj instanceof Game){
	        obj = new GameAdapter((Game)obj);
	    }
		saveProcess(obj, new File(prefix+"_save.xml"));
	}

	/**
	 * Saves an object as an XML file
	 * @param obj 	The object to save.
	 * @param file	The file path of the save file.
	 */
	private static void saveProcess(Object obj, File file){
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());	//Provides the client's entry point to the JAXB API
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// Output is printing as  tree. Note that printing happens regardless of this line.
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(obj, file);
			jaxbMarshaller.marshal(obj, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Creates a new Game object from an XML file.
	 * @return A Game object.
	 */
	public static Game loadFile(String prefix){
		GameAdapter gameAdapter = null;

		try {
			File file = new File(prefix+"_save.xml");		// Gets the xml file
			JAXBContext jaxbContext = JAXBContext.newInstance(GameAdapter.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();	//The object which converts our file from xml to an object.
			gameAdapter = (GameAdapter) jaxbUnmarshaller.unmarshal(file);	//loads the game
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		Game game = gameAdapter.getOriginal();	//Builds the game from the adapter class.
		return game;
	}

	/**
	 * Saves an XML version of a Game object, except that the XML file will not contain any players.
	 * This file contains its own saving
	 * @param game	A Game object.
	 */
	public static void saveInitialFile(Game game){
		InitialGameAdapter adapter = new InitialGameAdapter(game);
		saveProcess(adapter, new File("initialGame.xml"));
	}

	/**
	 * Creates a new InitialGameAdapter object from an xml file.
	 * This is used to load the map at the start of a fresh game.
	 * The InitialGameAdapter builds a Game object.
	 * @return A Game object.
	 */
	public static Game loadInitialFile(){
		InitialGameAdapter gameAdapter = null;
		try {
			File file = new File("initialGame.xml");		// Gets the xml file
			JAXBContext jaxbContext = JAXBContext.newInstance(InitialGameAdapter.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();	//The object which converts our file from xml to an object.
			gameAdapter = (InitialGameAdapter) jaxbUnmarshaller.unmarshal(file);	//loads the game
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		Game game = gameAdapter.getOriginal();
		return game;
	}

	/**
	 * Returns true if the save file with the name exists.
	 * The point of this method is to abstract any details regarding files from the game logic.
	 * @param string	The filepath of the file to check.
	 * @return True if there is a save file with the name provided.
	 */
	public static boolean saveExists(String string){
		File file = new File(string+"_save.xml");
		return file.exists();
	}


	public static void main(String[] args){
		File file = new File("save.xml");
		if(!file.exists()){
			System.out.println("Yo the file is not there");
		}
	}

}
