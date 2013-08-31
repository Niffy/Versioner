package com.niffy.build;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildTool {
	// ===========================================================
	// Constants
	// ===========================================================
	private final static Logger log = LoggerFactory.getLogger(BuildTool.class);

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	public BuildTool(){
		
	}

	public static void main(String[] args) {
		BuildTool tool = new BuildTool();
		log.info("Started BuildTool");
		if(args.length != 0){
			if(args[0] == "version"){
				tool.versionControl(args[1], args[2], args[3]);
			}
		}
		/*
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
		*/
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
	/**
	 * Increment and set values in the version file
	 * @param pPropsFile {@link String} The properties file to load
	 * @param pType {@link String} of release type, this must match the ant build tool
	 * @param pIncCode {@link String} of a {@link Boolean} value indicate if the versionCode should increment
	 */
	public void versionControl(final String pPropsFile, final String pType, final String pIncCode){
		Properties prop = new Properties();
		try{
			InputStream stream = BuildTool.class.getClassLoader().getResourceAsStream(pPropsFile);
			prop.load(stream);
			
			int version_build = 00;
			int version_type = 99;
			int version_code = 02;
			boolean version_code_inc = false;
			
			version_build = Integer.parseInt(prop.getProperty("Version.Build", "0"));
			version_code = Integer.parseInt(prop.getProperty("Version.Code", "0"));
			version_code_inc = Boolean.parseBoolean(pIncCode);
			version_type = Integer.parseInt(pType);
			log.info("Set the type attribute to: {}", version_type);
			version_build++;
			log.info("Incremented the build attribute");
			if(version_code_inc){
				version_code++;
				log.info("Incremented the versionCode attribute");
			}
			prop.setProperty("Version.Build", Integer.toString(version_build));
			prop.setProperty("Version.Code", Integer.toString(version_code));
			prop.setProperty("Version.Type", Integer.toString(version_type));
			log.info("Wrote new attributes to file");
			
		} catch (IOException ex){
			log.error("Error reading file: {}", pPropsFile);
			log.error("Error reading file:", ex);
		} catch (NumberFormatException ex){
			log.error("Could not parse string to int", ex);
		}
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
