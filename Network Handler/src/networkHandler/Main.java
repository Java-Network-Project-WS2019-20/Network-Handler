package networkHandler;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

//		if (args.length < 1) {
//			System.out.println("Please provide a file for processing!");
//			return;
//		}

//		String fileName = args[0];
		String fileName = "C:\\Users\\khali\\OneDrive\\Desktop\\gra.xml";
		FileHandler nFileHandler = new FileHandler();
		nFileHandler.setFileURI(fileName);






	}

	
}

