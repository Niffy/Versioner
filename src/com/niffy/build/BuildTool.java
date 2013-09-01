package com.niffy.build;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
		String version = this.getClass().getPackage().getImplementationVersion();
		log.info("Created BuildTool. Using build: {}", version);
	}

	public static void main(String[] args) {		
		/*
		//Check what classpaths we have got
		ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)cl).getURLs();
        for(URL url: urls){
        	System.out.println(url.getFile());
        }
        */
		
		/*
		//Loop arguments
		final int count = args.length;
		for (int i = 0; i < count; i++) {
			log.info("Args: {} {}", i, args[i]);
		}
		*/
		BuildTool tool = new BuildTool();
		if(args.length != 0){
			if(args[0].equalsIgnoreCase("version")){
				tool.versionControl(args[1], args[2], args[3]);
			}
		}
		
		/*
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
	 * @param pPropsFile {@link String} The properties file to load. This has to be the absolute path
	 * @param pType {@link String} of release type, this must match the ant build tool, ie release, debug or beta
	 * @param pIncCode {@link String} of a {@link Boolean} value indicate if the versionCode should increment
	 */
	public void versionControl(final String pPropsFile, final String pType, final String pIncCode){
		log.info("Populating version file");
		log.info("properties file: {} type: {} IncCode: {}", pPropsFile, pType, pIncCode);
		Properties prop = new Properties();
		try{
			FileInputStream in = new FileInputStream(pPropsFile);
			prop.load(in);
			in.close();
			
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
			
			FileOutputStream out = new FileOutputStream(pPropsFile);
			prop.store(out, null);
			out.close();
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
