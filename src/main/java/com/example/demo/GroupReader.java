package com.example.demo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;

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
					int gid = -1;
					try {
						gid = Integer.parseInt(values[2]);
					} catch (NumberFormatException e) {
						gid = -1;
						e.printStackTrace();
					}
					jsonBuilder.add("gid", gid);
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
	
	public JsonArray getGroupsUsername(String username) {

		JsonArrayBuilder jsonArrBuilder = Json.createArrayBuilder();
		if (!username.equals("")) {
			JsonArray jsonArr = contentsJson;
			for(JsonValue jsonObj : jsonArr) {
				JsonArray jsonMembers = (JsonArray) ((JsonObject)jsonObj).get("members");
				for(JsonValue jsonMember : jsonMembers) {
					if (jsonMember.toString().equals(username)) {
						jsonArrBuilder.add(jsonObj);
					}
				}
			}
		}
		
		return jsonArrBuilder.build();
	}

	public JsonArray getGroupsQuery(String name, String gid, List<String> member) {

		JsonArrayBuilder jsonArrBuilder = Json.createArrayBuilder();
		JsonArray jsonArr = contentsJson;
		for(JsonValue jsonObj : jsonArr) {
			if (name != null) {
				JsonString jsonName = (JsonString) ((JsonObject) jsonObj).get("name");
				String jsonNameTidy = jsonName.toString();
				jsonNameTidy = jsonNameTidy.substring(1, jsonNameTidy.length() - 1);
				if (!jsonNameTidy.equals(name))
					continue;
			}
			if (gid != null) {
				JsonNumber jsonGid = (JsonNumber) ((JsonObject)jsonObj).get("gid");
				if (!jsonGid.toString().equals(gid))
					continue;
			}
			if (member != null) {
				JsonArray jsonMembers = (JsonArray) ((JsonObject)jsonObj).get("members");
				List<String> strMembers = new ArrayList<String>();
				for (int i = 0; i < jsonMembers.size(); i++) {
					strMembers.add(jsonMembers.getString(i));
				}
				if (!strMembers.containsAll(member))
					continue;
			}
			
			jsonArrBuilder.add(jsonObj);
		}
		return jsonArrBuilder.build();
	}
	
	public JsonObject getGroupsGid(String gid) {

		JsonArray jsonArr = contentsJson;
		for (JsonValue jsonObj : jsonArr) {
			JsonNumber jsonGid = (JsonNumber) ((JsonObject) jsonObj).get("gid");
			if (jsonGid.toString().equals(gid))
				return (JsonObject) jsonObj;
		}
		return null;
	}
}