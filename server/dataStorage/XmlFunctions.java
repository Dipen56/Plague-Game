package server.dataStorage;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import server.dataStorage.alternates.AltGame;
import server.game.Game;

import javax.xml.bind.Unmarshaller;

public class XmlFunctions {

	/**
	 * Saves an object's state as an XML file.
	 * The object will require an XmlRootElement annotation above the class header.
	 * Any internal fields or methods with the XmlElement annotation will be added recursively to the file.
	 * @param An object to save to the file.
	 */
	public static void saveFile(Object obj){
		try {
			File file = new File("save.xml");
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
	 * Creates a new Game object from an xml file.
	 * @return A Game object.
	 */
	public static AltGame loadFile(){
		AltGame game = null;
		
		try {
			File file = new File("save.xml");		// Gets the xml file
			JAXBContext jaxbContext = JAXBContext.newInstance(AltGame.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();	//The object which converts our file from xml to an object.
			game = (AltGame) jaxbUnmarshaller.unmarshal(file);	//loads the game
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return game;
	}
}
