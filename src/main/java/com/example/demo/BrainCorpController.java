package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import javax.json.*;

@Controller
public class BrainCorpController {

	@RequestMapping("/users")
	public @ResponseBody String getUsers() {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		PasswdReader passwdReader = new PasswdReader(configReader.getPasswdPath());
		passwdReader.read();
		return passwdReader.getContentsJson().toString();
	}

	@RequestMapping("/users/query")
	public @ResponseBody String getUsersQuery(@RequestParam(required = false) String name,
			@RequestParam(required = false) String uid,
			@RequestParam(required = false) String gid,
			@RequestParam(required = false) String comment,
			@RequestParam(required = false) String home,
			@RequestParam(required = false) String shell) {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		PasswdReader passwdReader = new PasswdReader(configReader.getPasswdPath());
		passwdReader.read();

		JsonArrayBuilder jsonArrBuilder = Json.createArrayBuilder();
		JsonArray jsonArr = passwdReader.getContentsJson();
		for(JsonValue jsonObj : jsonArr) {
			if (name != null) {
				JsonString jsonName = (JsonString) ((JsonObject) jsonObj).get("name");
				String jsonNameTidy = jsonName.toString();
				jsonNameTidy = jsonNameTidy.substring(1, jsonNameTidy.length() - 1);
				if (!jsonNameTidy.equals(name))
					continue;
			}
			if (uid != null) {
				JsonNumber jsonUid = (JsonNumber) ((JsonObject)jsonObj).get("uid");
				if (!jsonUid.toString().equals(uid))
					continue;
			}
			if (gid != null) {
				JsonNumber jsonGid = (JsonNumber) ((JsonObject)jsonObj).get("gid");
				if (!jsonGid.toString().equals(gid))
					continue;
			}
			if (comment != null) {
				JsonString jsonComment = (JsonString) ((JsonObject)jsonObj).get("comment");
				String jsonCommentTidy = jsonComment.toString();
				jsonCommentTidy = jsonCommentTidy.substring(1, jsonCommentTidy.length() - 1);
				if (!jsonCommentTidy.equals(comment))
					continue;
			}
			if (home != null) {
				JsonString jsonHome = (JsonString) ((JsonObject)jsonObj).get("home");
				String jsonHomeTidy = jsonHome.toString();
				jsonHomeTidy = jsonHomeTidy.substring(1, jsonHomeTidy.length() - 1);
				if (!jsonHomeTidy.equals(home))
					continue;
			}
			if (shell != null) {
				JsonString jsonShell = (JsonString) ((JsonObject)jsonObj).get("shell");
				String jsonShellTidy = jsonShell.toString();
				jsonShellTidy = jsonShellTidy.substring(1, jsonShellTidy.length() - 1);
				if (!jsonShellTidy.equals(shell))
					continue;
			}
			
			jsonArrBuilder.add(jsonObj);
		}
		return jsonArrBuilder.build().toString();
	}

	@RequestMapping(value = "users/{uid}")
	public @ResponseBody String getUsersUid(@PathVariable("uid") String uid) {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		PasswdReader passwdReader = new PasswdReader(configReader.getPasswdPath());
		passwdReader.read();
		JsonArray jsonArr = passwdReader.getContentsJson();
		for(JsonValue jsonObj : jsonArr) {
			JsonNumber jsonUid = (JsonNumber) ((JsonObject)jsonObj).get("uid");
			if (jsonUid.toString().equals(uid))
				return jsonObj.toString();
		}
		return "";
    }

	@RequestMapping(value = "users/{uid}/groups")
	public @ResponseBody String getGroupsUid(@PathVariable("uid") String uid) {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		PasswdReader passwdReader = new PasswdReader(configReader.getPasswdPath());
		passwdReader.read();
		
		// find username
		JsonArray jsonArr = passwdReader.getContentsJson();
		String username = "";
		for(JsonValue jsonObj : jsonArr) {
			JsonNumber jsonUid = (JsonNumber) ((JsonObject)jsonObj).get("uid");
			if (jsonUid.toString().equals(uid)) {
				username = ((JsonObject)jsonObj).get("name").toString();
				break;
			}
		}
		
		// create json array with groups with username
		GroupReader groupReader = new GroupReader(configReader.getGroupPath());
		groupReader.read();
		JsonArrayBuilder jsonArrBuilder = Json.createArrayBuilder();
		if (!username.equals("")) {
			jsonArr = groupReader.getContentsJson();
			for(JsonValue jsonObj : jsonArr) {
				JsonArray jsonMembers = (JsonArray) ((JsonObject)jsonObj).get("members");
				for(JsonValue jsonMember : jsonMembers) {
					if (jsonMember.toString().equals(username)) {
						jsonArrBuilder.add(jsonObj);
					}
				}
			}
		}
		return jsonArrBuilder.build().toString();
    }

	@RequestMapping(value = "/groups")
	public @ResponseBody String getGroups() {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		GroupReader groupReader = new GroupReader(configReader.getGroupPath());
		groupReader.read();
		return groupReader.getContentsJson().toString();
    }

	@RequestMapping(value = "/groups/query")
	public @ResponseBody String getGroupsQuery(@RequestParam(required = false) String name,
			@RequestParam(required = false) String gid,
			@RequestParam(required = false) List<String> member) {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		GroupReader groupReader = new GroupReader(configReader.getGroupPath());
		groupReader.read();

		JsonArrayBuilder jsonArrBuilder = Json.createArrayBuilder();
		JsonArray jsonArr = groupReader.getContentsJson();
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
		return jsonArrBuilder.build().toString();
    }
	
	@RequestMapping(value = "groups/{gid}")
	public @ResponseBody String getGroupsGid(@PathVariable("gid") String gid) {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		GroupReader groupReader = new GroupReader(configReader.getGroupPath());
		groupReader.read();
		JsonArray jsonArr = groupReader.getContentsJson();
		for(JsonValue jsonObj : jsonArr) {
			JsonNumber jsonGid = (JsonNumber) ((JsonObject)jsonObj).get("gid");
			if (jsonGid.toString().equals(gid))
				return jsonObj.toString();
		}
		return "";
	}
}
