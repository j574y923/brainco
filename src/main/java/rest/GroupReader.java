package rest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class GroupReader {
	
	private String groupPath = "";
	private String contents = "";
	private JSONArray contentsJson = null;
	
	public GroupReader(String groupPath) {
		this.groupPath = groupPath;
	}
	
	public void read() throws IOException {
		InputStream input = new FileInputStream(groupPath);
		BufferedReader buf = new BufferedReader(new InputStreamReader(input));
		String line = buf.readLine();
		StringBuilder sb = new StringBuilder();

		while (line != null) {
			sb.append(line).append("\n");
			line = buf.readLine();
		}
		buf.close();
		contents = sb.toString();
		readToJSON(contents);
	}
	
	private void readToJSON(String passwdContents) {
		JSONArray jsonArr = new JSONArray();
		String[] lines = passwdContents.split("\n");
		for(String line : lines) {
			// initialize json values
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("name", "");
			jsonObj.put("gid", -1);
			jsonObj.put("members", new JSONArray());
			
			// fill in json values
			String[] values = line.split(":");
			for(int i = 0; i < values.length; i++) {
				switch(i) {
				case 0:
					jsonObj.put("name", values[0]);
					break;
				case 2:
					int gid = -1;
					try {
						gid = Integer.parseInt(values[2]);
					} catch (NumberFormatException e) {
						gid = -1;
						e.printStackTrace();
					}
					jsonObj.put("gid", gid);
					break;
				case 3:
					JSONArray jsonMembers = new JSONArray();
					String[] valuesMembers = values[3].split(",");
					for(String valuesMember : valuesMembers ) {
						jsonMembers.put(valuesMember);
					}
					jsonObj.put("members", jsonMembers);
					break;
				}
			}
			
			jsonArr.put(jsonObj);
		}
		contentsJson = jsonArr;
	}
	
	public String getContents() {
		return contents;
	}
	
	public JSONArray getContentsJson() {
		return contentsJson;
	}
	
	public JSONArray getGroupsUsername(String username) {

		JSONArray newJsonArr = new JSONArray();
		if (!username.equals("")) {
			JSONArray jsonArr = contentsJson;
			for(Object jsonObj : jsonArr) {
				JSONArray jsonMembers = (JSONArray) ((JSONObject)jsonObj).get("members");
				for(Object jsonMember : jsonMembers) {
					if (jsonMember.toString().equals(username)) {
						newJsonArr.put(jsonObj);
					}
				}
			}
		}
		
		return newJsonArr;
	}

	public JSONArray getGroupsQuery(String name, String gid, List<String> member) {

		JSONArray jsonArr = contentsJson;
		JSONArray newJsonArr = new JSONArray();
		for(Object jsonObj : jsonArr) {
			if (name != null) {
				String jsonName = (String) ((JSONObject) jsonObj).get("name");
				if (!jsonName.equals(name))
					continue;
			}
			if (gid != null) {
				Integer jsonGid = (Integer) ((JSONObject)jsonObj).get("gid");
				if (!jsonGid.toString().equals(gid))
					continue;
			}
			if (member != null) {
				JSONArray jsonMembers = (JSONArray) ((JSONObject)jsonObj).get("members");
				List<String> strMembers = new ArrayList<String>();
				for (int i = 0; i < jsonMembers.length(); i++) {
					strMembers.add(jsonMembers.getString(i));
				}
				if (!strMembers.containsAll(member))
					continue;
			}
			
			newJsonArr.put(jsonObj);
		}
		return newJsonArr;
	}
	
	public JSONObject getGroupsGid(String gid) {

		JSONArray jsonArr = contentsJson;
		for (Object jsonObj : jsonArr) {
			Integer jsonGid = (Integer) ((JSONObject) jsonObj).get("gid");
			if (jsonGid.toString().equals(gid))
				return (JSONObject) jsonObj;
		}
		return null;
	}
}