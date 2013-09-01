package com.niffy.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildTool {
	// ===========================================================
	// Constants
	// ===========================================================
	private final static Logger log = LoggerFactory.getLogger(BuildTool.class);

	// ==================================================== =======
	// Fields
	// ===========================================================
	public final static String FILE = "file";
	public final static String FILE_OPT = "f";
	public final static String FILE_PATH = "path";
	public final static String FILE_PATH_OPT = "p";
	public final static String BUILD = "build";
	public final static String BUILD_OPT = "b";
	public final static String TYPE = "type";
	public final static String TYPE_OPT = "t";
	public final static String HELP = "help";
	public final static String HELP_OPT = "h";
	public final static String VERSIONER = "versioner";
	public final static String VERSIONER_OPT = "ver";
	public final static String VERSION = "version";
	public final static String VERSION_OPT = "v";

	// ===========================================================
	// Constructors
	// ===========================================================
	public BuildTool() {
		String version = this.getClass().getPackage().getImplementationVersion();
		log.info("Created BuildTool. Using build: {}", version);
	}

	/*
		private static Options createOptions() {
			Options options = new Options();
			Option help = new Option("h", "help", false, "Help!!");
			
			Option app_version = new Option("v", "version", false, "Get build version, which is a date and time");
			Option version = new Option("ver", "versioner", true, "Update a version.properties file");
			version.setArgName("<file> <release type> <inc versionCode>");
			version.setValueSeparator(' ');
			version.setArgs(3);
			options.addOption(help);
			options.addOption(version);
			options.addOption(app_version);
			return options;
		}
		*/

	/**
	 * TODO we could have a mode argument instead?
	 * 
	 * @return
	 */
	private static Options createOptions() {
		Options options = new Options();
		Option help = new Option("h", "help", false, "Help!!");

		Option app_version = new Option(VERSION_OPT, VERSION, false, "Get build version, which is a date and time");
		Option versioner = OptionBuilder.hasArg(false).isRequired(false).withLongOpt(VERSIONER)
				.withDescription("User the versioner").create(VERSIONER_OPT);
		Option file = OptionBuilder.hasArg(true).withArgName("file").isRequired(false).withLongOpt(FILE)
				.withDescription("File with abs path to use").create(FILE_OPT);
		Option file_path = OptionBuilder.hasArg(true).withArgName("path").isRequired(false).withLongOpt(FILE_PATH)
				.withDescription("File with abs path to use").create(FILE_PATH_OPT);
		Option type = OptionBuilder.hasArg(true).withArgName("int").isRequired(false).withLongOpt(TYPE)
				.withDescription("Int of release type").create(TYPE_OPT);
		Option build = OptionBuilder.hasArg(true).withArgName("boolean").isRequired(false).withLongOpt(BUILD)
				.withDescription("Boolean, should versionCode increase?").create(BUILD_OPT);
		options.addOption(help);
		options.addOption(app_version);
		options.addOption(versioner);
		options.addOption(file);
		options.addOption(file_path);
		options.addOption(type);
		options.addOption(build);
		return options;
	}

	private static void showHelp(Options options) {
		HelpFormatter h = new HelpFormatter();
		h.printHelp("help", options);
		System.exit(-1);
	}

	public static void main(String[] args) throws IOException {
		BuildTool tool = new BuildTool();
		Options options = createOptions();

		try {
			CommandLineParser parser = new PosixParser();
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption(HELP_OPT)) {
				showHelp(options);
			} else if (cmd.hasOption(VERSIONER_OPT)) {
				String pPropsFile = cmd.getOptionValue(FILE_OPT);
				String pPropsPath = cmd.getOptionValue(FILE_PATH_OPT);
				String pType = cmd.getOptionValue(TYPE_OPT);
				String pIncCode = cmd.getOptionValue(BUILD_OPT);
				if (pType != null && pPropsFile != null && pIncCode != null) {
					pPropsFile = pPropsFile.trim();
					pPropsPath = pPropsPath.trim();
					pType = pType.trim();
					pIncCode = pIncCode.trim();
					tool.versionControl(pPropsFile, pPropsPath, pType, pIncCode);
				}
			} else if (cmd.hasOption(VERSION_OPT)) {
				log.info("Build: {}", BuildTool.class.getPackage().getImplementationVersion());
			}
		} catch (Exception e) {
			e.printStackTrace();
			showHelp(options);
		}

		/*
		//Check what classpaths we have got
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader)cl).getURLs();
		for(URL url: urls){
			System.out.println(url.getFile());
		}
		*/

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
	 * 
	 * @param pPropsFile
	 *            {@link String} The properties file to load. This is the file
	 *            name
	 * @param pPropsPath
	 *            {@link String} of path to file
	 * @param pType
	 *            {@link String} of release type, this must match the ant build
	 *            tool, ie release, debug or beta
	 * @param pIncCode
	 *            {@link String} of a {@link Boolean} value indicate if the
	 *            versionCode should increment
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public void versionControl(final String pPropsFile, final String pPropsPath, final String pType,
			final String pIncCode) throws IOException, NumberFormatException {
		log.info("Populating version file");
		log.info("properties file: {} path: {}", pPropsFile, pPropsPath);
		log.info("properties type: {} IncCode: {}", pType, pIncCode);

		Properties prop = new Properties();
		try {
			File targetFile = new File(pPropsPath, pPropsFile);
			FileInputStream in = new FileInputStream(targetFile);
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
			if (version_code_inc) {
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

		} catch (IOException ex) {
			log.error("Error reading file: {}", pPropsFile);
			log.error("Error reading file:", ex);
			throw ex;
		} catch (NumberFormatException ex) {
			log.error("Could not parse string to int", ex);
			throw ex;
		}
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
