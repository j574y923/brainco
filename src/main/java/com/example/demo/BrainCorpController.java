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
		//TODO:
		return "{\"lol\":\"TODO!!\"}";
	}

	@RequestMapping("/users/query")
	public @ResponseBody String getUsersQuery(@RequestParam(required = false) String name,
			@RequestParam(required = false) int uid,
			@RequestParam(required = false) int gid,
			@RequestParam(required = false) String comment,
			@RequestParam(required = false) String home,
			@RequestParam(required = false) String shell) {
		return "employees request by dept: " + name + " " + uid + " " + gid;
	}

	@RequestMapping(value = "users/{uid}")
	public @ResponseBody String getUsersUid(@PathVariable("uid") int uid) {
		return "TODO" + uid;
    }

	@RequestMapping(value = "users/{uid}/groups")
	public @ResponseBody String getGroupsUid(@PathVariable("uid") int uid) {
		return "TODO" + uid;
    }

	@RequestMapping(value = "/groups")
	public @ResponseBody String getGroups() {
		return "TODO";
    }

	@RequestMapping(value = "/groups/query")
	public @ResponseBody String getGroupsQuery(@RequestParam(required = false) String name,
			@RequestParam(required = false) int gid,
			@RequestParam(required = false) List<String> member) {
		return "TODO" + member;
    }
	
	@RequestMapping(value = "groups/{gid}")
	public @ResponseBody String getGroupsGid(@PathVariable("gid") int gid) {
		return "TODO" + gid;
    }
}
