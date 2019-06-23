package com.example.demo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class GroupReader {
	
	private String groupPath = "";
	private String contents = "";
	private JsonArray contentsJson = null;
	
	public GroupReader(String passwdPath) {
		this.groupPath = passwdPath;
	}
	
	public void read() {
		try (InputStream input = new FileInputStream(groupPath)) {
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
					.add("gid", -1)
					.add("members", Json.createArrayBuilder());
			
			// fill in json values
			String[] values = line.split(":");
			for(int i = 0; i < values.length; i++) {
				switch(i) {
				case 0:
					jsonBuilder.add("name", values[0]);
					break;
				case 2:
					jsonBuilder.add("gid", values[2]);
					break;
				case 3:
					JsonArrayBuilder jsonMembersArrBuilder = Json.createArrayBuilder();
					String[] valuesMembers = values[3].split(",");
					for(String valuesMember : valuesMembers ) {
						jsonMembersArrBuilder.add(valuesMember);
					}
					jsonBuilder.add("members", jsonMembersArrBuilder);
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