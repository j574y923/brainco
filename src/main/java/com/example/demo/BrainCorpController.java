package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
		return "employees request by dept: " + name + " " + uid + " " + gid;
	}

	@RequestMapping(value = "users/{uid}")
	public @ResponseBody String getUsersUid(@PathVariable("uid") String uid) {
		return "TODO" + uid;
    }

	@RequestMapping(value = "users/{uid}/groups")
	public @ResponseBody String getGroupsUid(@PathVariable("uid") String uid) {
		return "TODO" + uid;
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
		return "TODO" + member;
    }
	
	@RequestMapping(value = "groups/{gid}")
	public @ResponseBody String getGroupsGid(@PathVariable("gid") String gid) {
		return "TODO" + gid;
	}
}
