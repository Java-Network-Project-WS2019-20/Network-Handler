package networkHandler;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MyLogger {
	private Logger lgr = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private String logfile;
	
	
	public MyLogger() {
		
		LogManager.getLogManager().reset();
		this.lgr.setLevel(Level.ALL);
		this.logfile = "C:\\Users\\boost\\Downloads\\networkHandler.log";
        
	}
	
	
	
	public void startLogging() {
		
		startConsoleLogger();
		startFileLogger();
		
	}
	
	
	
	private void startConsoleLogger() {
		
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(Level.SEVERE);
		lgr.addHandler(ch);
		
	}
	
	
	private void startFileLogger() {
		
		try {
			java.util.logging.FileHandler fh = new java.util.logging.FileHandler(logfile, true);
			fh.setLevel(Level.FINEST);
			lgr.addHandler(fh);
		} catch (IOException e) {
			lgr.log(Level.SEVERE, "File Logger could not open file.", e);
		}
		
	}
	
	
}

