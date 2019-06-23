package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;


/**
 * This service is a minimal HTTP service that exposes the user and group
 * information on a UNIX-like system that is usually locked away in the UNIX
 * /etc/passwd and /etc/groups files.
 * 
 * This service is read-only but responses should reflect changes made to the
 * underlying passwd and groups files while the service is running.
 * 
 * @author j574y
 *
 */
@Controller
public class BrainCorpController {

	/**
	 * 
	 * Returns a list of all users on the system, as defined in the /etc/passwd
	 * file.
	 * 
	 * Example Response: [ {“name”: “root”, “uid”: 0, “gid”: 0, “comment”: “root”,
	 * “home”: “/root”, “shell”: “/bin/bash”}, {“name”: “dwoodlins”, “uid”: 1001,
	 * “gid”: 1001, “comment”: “”, “home”: “/home/dwoodlins”, “shell”: “/bin/false”}
	 * ]
	 */
	@RequestMapping("/users")
	public @ResponseBody String getUsers() {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		PasswdReader passwdReader = new PasswdReader(configReader.getPasswdPath());
		passwdReader.read();
		return passwdReader.getContentsJson().toString();
	}

	/**
	 * Returns a list of users matching all of the specified query fields. The
	 * bracket notation indicates that any of the following query parameters may be
	 * supplied:
	 * 
	 * @param name
	 * @param uid
	 * @param gid
	 * @param comment
	 * @param home
	 * @param shell
	 * 
	 * Only exact matches are supported. Example Query: GET
	 * /users/query?shell=%2Fbin%2Ffalse Example Response: [ {“name”: “dwoodlins”,
	 * “uid”: 1001, “gid”: 1001, “comment”: “”, “home”: “/home/dwoodlins”, “shell”:
	 * “/bin/false”} ]
	 */
	@RequestMapping("/users/query")
	public @ResponseBody String getUsersQuery(@RequestParam(required = false) String name,
			@RequestParam(required = false) String uid, @RequestParam(required = false) String gid,
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

	/**
	 * Returns a single user with <uid>. Return 404 if <uid> is not found.
	 * 
	 * Example Response: {“name”: “dwoodlins”, “uid”: 1001, “gid”: 1001, “comment”:
	 * “”, “home”: “/home/dwoodlins”, “shell”: “/bin/false”}
	 */
	@RequestMapping(value = "/users/{uid}")
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

	/**
	 * Returns all the groups for a given user.
	 * 
	 * Example Response: [ {“name”: “docker”, “gid”: 1002, “members”: [“dwoodlins”]}
	 * ]
	 */
	@RequestMapping(value = "/users/{uid}/groups")
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

	/**
	 * Returns a list of all groups on the system, a defined by /etc/group.
	 *
	 * Example Response: [ {“name”: “_analyticsusers”, “gid”: 250, “members”:
	 * [“_analyticsd’,”_networkd”,”_timed”]}, {“name”: “docker”, “gid”: 1002,
	 * “members”: []}
	 */
	@RequestMapping(value = "/groups")
	public @ResponseBody String getGroups() {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		GroupReader groupReader = new GroupReader(configReader.getGroupPath());
		groupReader.read();
		return groupReader.getContentsJson().toString();
    }

	/**
	 * Return a list of groups matching all of the specified query fields. The
	 * bracket notation indicates that any of the following query parameters may be
	 * supplied:
	 * 
	 * @param name
	 * @param gid
	 * @param member (repeated)
	 * 
	 * Any group containing all the specified members should be returned, i.e. when
	 * query members are a subset of group members. Example Query: GET
	 * /groups/query?member=_analyticsd&member=_networkd Example Response: [
	 * {“name”: “_analyticsusers”, “gid”: 250, “members”:
	 * [“_analyticsd’,”_networkd”,”_timed”]} ]
	 */
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

	/**
	 * Returns a single group with <gid>. Return 404 if <gid> is not found.
	 * 
	 * Example Response: {“name”: “docker”, “gid”: 1002, “members”: [“dwoodlins”]}
	 */
	@RequestMapping(value = "/groups/{gid}")
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
