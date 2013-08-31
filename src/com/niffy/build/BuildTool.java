package com.niffy.build;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildTool {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused") 
	private final static Logger log = LoggerFactory.getLogger(BuildTool.class);

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================


	public static void main(String[] args) {
		System.out.println("Started");
		
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		 
        URL[] urls = ((URLClassLoader)cl).getURLs();
 
        for(URL url: urls){
        	System.out.println(url.getFile());
        }
        
		
		
		Properties prop = new Properties();
		try{
			InputStream stream = BuildTool.class.getClassLoader().getResourceAsStream("log4j.properties");
			prop.load(stream);
			System.out.println("Done it!!");
		} catch (IOException ex){
			ex.printStackTrace();
		}
		
		
		
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
