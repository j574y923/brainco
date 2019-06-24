package rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

	public static final String CONFIG_PATH = ".";
	public static final String CONFIG_NAME = "config.properties";

	private String passwdPath = "";
	private String groupPath = "";

	public void read() throws IOException {
		InputStream input = new FileInputStream(CONFIG_PATH + "/" + CONFIG_NAME);

		Properties prop = new Properties();

		prop.load(input);

		passwdPath = prop.getProperty("passwd");
		groupPath = prop.getProperty("group");
	}
	
	public String getPasswdPath() {
		return passwdPath;
	}
	
	public String getGroupPath() {
		return groupPath;
	}
}