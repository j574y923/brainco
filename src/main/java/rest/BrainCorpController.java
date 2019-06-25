package rest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

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
	 * @throws IOException 
	 */
	@RequestMapping("/users")
	public @ResponseBody ResponseEntity<String> getUsers() throws IOException {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		PasswdReader passwdReader = new PasswdReader(configReader.getPasswdPath());
		passwdReader.read();
		return ResponseEntity.ok().body(passwdReader.getContentsJson().toString());
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
	 * @throws IOException 
	 */
	@RequestMapping("/users/query")
	public @ResponseBody ResponseEntity<String> getUsersQuery(@RequestParam(required = false) String name,
			@RequestParam(required = false) String uid, @RequestParam(required = false) String gid,
			@RequestParam(required = false) String comment,
			@RequestParam(required = false) String home,
			@RequestParam(required = false) String shell) throws IOException {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		PasswdReader passwdReader = new PasswdReader(configReader.getPasswdPath());
		passwdReader.read();
		return ResponseEntity.ok().body(passwdReader.getUsersQuery(name, uid, gid, comment, home, shell).toString());
	}

	/**
	 * Returns a single user with <uid>. Return 404 if <uid> is not found.
	 * 
	 * Example Response: {“name”: “dwoodlins”, “uid”: 1001, “gid”: 1001, “comment”:
	 * “”, “home”: “/home/dwoodlins”, “shell”: “/bin/false”}
	 * @throws IOException 
	 */
	@RequestMapping(value = "/users/{uid}")
	public @ResponseBody ResponseEntity<String> getUsersUid(@PathVariable("uid") String uid) throws IOException {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		PasswdReader passwdReader = new PasswdReader(configReader.getPasswdPath());
		passwdReader.read();
		JsonObject jsonObj = passwdReader.getUsersUid(uid);
		return ResponseEntity.ok().body(jsonObj != null ? jsonObj.toString() : "");
    }

	/**
	 * Returns all the groups for a given user.
	 * 
	 * Example Response: [ {“name”: “docker”, “gid”: 1002, “members”: [“dwoodlins”]}
	 * ]
	 * @throws IOException 
	 */
	@RequestMapping(value = "/users/{uid}/groups")
	public @ResponseBody ResponseEntity<String> getGroupsUid(@PathVariable("uid") String uid) throws IOException {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		PasswdReader passwdReader = new PasswdReader(configReader.getPasswdPath());
		passwdReader.read();
		
		// find username
		JsonObject jsonObj = passwdReader.getUsersUid(uid);
		String username = jsonObj.get("name").toString();
		
		// create json array with groups with username
		GroupReader groupReader = new GroupReader(configReader.getGroupPath());
		groupReader.read();
		return ResponseEntity.ok().body(groupReader.getGroupsUsername(username).toString());
    }

	/**
	 * Returns a list of all groups on the system, a defined by /etc/group.
	 *
	 * Example Response: [ {“name”: “_analyticsusers”, “gid”: 250, “members”:
	 * [“_analyticsd’,”_networkd”,”_timed”]}, {“name”: “docker”, “gid”: 1002,
	 * “members”: []}
	 * @throws IOException 
	 */
	@RequestMapping(value = "/groups")
	public @ResponseBody ResponseEntity<String> getGroups() throws IOException {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		GroupReader groupReader = new GroupReader(configReader.getGroupPath());
		groupReader.read();
		return ResponseEntity.ok().body(groupReader.getContentsJson().toString());
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
	 * @throws IOException 
	 */
	@RequestMapping(value = "/groups/query")
	public @ResponseBody ResponseEntity<String> getGroupsQuery(@RequestParam(required = false) String name,
			@RequestParam(required = false) String gid,
			@RequestParam(required = false) List<String> member) throws IOException {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		GroupReader groupReader = new GroupReader(configReader.getGroupPath());
		groupReader.read();
		return ResponseEntity.ok().body(groupReader.getGroupsQuery(name, gid, member).toString());
	}

	/**
	 * Returns a single group with <gid>. Return 404 if <gid> is not found.
	 * 
	 * Example Response: {“name”: “docker”, “gid”: 1002, “members”: [“dwoodlins”]}
	 * @throws IOException 
	 */
	@RequestMapping(value = "/groups/{gid}")
	public @ResponseBody ResponseEntity<String> getGroupsGid(@PathVariable("gid") String gid) throws IOException {
		ConfigReader configReader = new ConfigReader();
		configReader.read();
		GroupReader groupReader = new GroupReader(configReader.getGroupPath());
		groupReader.read();
		JsonObject jsonObj = groupReader.getGroupsGid(gid);
		return ResponseEntity.ok().body(jsonObj != null ? jsonObj.toString() : "");
	}
	
	/**
	 * Captures the exceptions that occur for this controller. Returns them into a
	 * json string with keys errortype (corresponding to exception type) and
	 * stacktrace (the printstacktrace).
	 */
	@ExceptionHandler(Exception.class)
	public @ResponseBody ResponseEntity<String> handlerExceptions(final Exception e) {
		String steStr = "";
		for (StackTraceElement ste : e.getStackTrace()) {
			steStr += ste.toString() + "\n";
		}
		JsonObject jsonObj = Json.createObjectBuilder()
				.add("errortype", e.getClass().toString())
				.add("stacktrace", steStr)
				.build();
		return ResponseEntity.ok().body(jsonObj.toString());
	}
}
