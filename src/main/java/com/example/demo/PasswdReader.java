package com.example.demo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

public class PasswdReader {
	
	private String passwdPath = "";
	private String contents = "";
	private JsonArray contentsJson = null;
	
	public PasswdReader(String passwdPath) {
		this.passwdPath = passwdPath;
	}
	
	public void read() {
		try (InputStream input = new FileInputStream(passwdPath)) {
			BufferedReader buf = new BufferedReader(new InputStreamReader(input));
			String line = buf.readLine();
			StringBuilder sb = new StringBuilder();

			while (line != null) {
				sb.append(line).append("\n");
				line = buf.readLine();
			}
			contents = sb.toString();
			readToJSON(contents);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void readToJSON(String passwdContents) {
		JsonArrayBuilder jsonArrBuilder = Json.createArrayBuilder();
		String[] lines = passwdContents.split("\n");
		for(String line : lines) {
			// initialize json values
			JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
					.add("name", "")
					.add("uid", -1)
					.add("gid", -1)
					.add("comment", "")
					.add("root", "")
					.add("shell", "");
			
			// fill in json values
			String[] values = line.split(":");
			for(int i = 0; i < values.length; i++) {
				switch(i) {
				case 0:
					jsonBuilder.add("name", values[0]);
					break;
				case 2:
					int uid = -1;
					try {
						uid = Integer.parseInt(values[2]);
					} catch (NumberFormatException e) {
						uid = -1;
						e.printStackTrace();
					}
					jsonBuilder.add("uid", uid);
					break;
				case 3:
					int gid = -1;
					try {
						gid = Integer.parseInt(values[3]);
					} catch (NumberFormatException e) {
						gid = -1;
						e.printStackTrace();
					}
					jsonBuilder.add("gid", gid);
					break;
				case 4:
					jsonBuilder.add("comment", values[4]);
					break;
				case 5:
					jsonBuilder.add("root", values[5]);
					break;
				case 6:
					jsonBuilder.add("shell", values[6]);
					break;
				}
			}
			
			jsonArrBuilder.add(jsonBuilder);
		}
		contentsJson = jsonArrBuilder.build();
	}
	
	public String getContents() {
		return contents;
	}
	
	public JsonArray getContentsJson() {
		return contentsJson;
	}
}