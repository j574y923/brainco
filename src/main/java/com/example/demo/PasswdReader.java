package com.example.demo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class PasswdReader {
	
	private String passwdPath = "";
	private String contents = "";
	private JsonObject contentsJson = null;
	
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
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		String[] lines = passwdContents.split("\n");
		for(String line : lines) {
			String[] values = line.split(":");
			jsonBuilder.add("name", values[0]);
			jsonBuilder.add("uid", values[2]);
			jsonBuilder.add("gid", values[3]);
			jsonBuilder.add("comment", values[4]);
			jsonBuilder.add("root", values[5]);
			jsonBuilder.add("shell", values[6]);
		}
		contentsJson = jsonBuilder.build();
	}
	
	public String getContents() {
		return contents;
	}
	
	public JsonObject getContentsJson() {
		return contentsJson;
	}
}