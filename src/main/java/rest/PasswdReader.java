package rest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

public class PasswdReader {
	
	private String passwdPath = "";
	private String contents = "";
	private JSONArray contentsJson = null;
	
	public PasswdReader(String passwdPath) {
		this.passwdPath = passwdPath;
	}

	public void read() throws IOException {
		InputStream input = new FileInputStream(passwdPath);
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
		JSONArray jsonArray = new JSONArray();
		String[] lines = passwdContents.split("\n");
		for(String line : lines) {
			// initialize json values
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("name", "");
			jsonObj.put("uid", -1);
			jsonObj.put("gid", -1);
			jsonObj.put("comment", "");
			jsonObj.put("root", "");
			jsonObj.put("shell", "");

			// fill in json values
			String[] values = line.split(":");
			for(int i = 0; i < values.length; i++) {
				switch(i) {
				case 0:
					jsonObj.put("name", values[0]);
					break;
				case 2:
					int uid = -1;
					try {
						uid = Integer.parseInt(values[2]);
					} catch (NumberFormatException e) {
						uid = -1;
						e.printStackTrace();
					}
					jsonObj.put("uid", uid);
					break;
				case 3:
					int gid = -1;
					try {
						gid = Integer.parseInt(values[3]);
					} catch (NumberFormatException e) {
						gid = -1;
						e.printStackTrace();
					}
					jsonObj.put("gid", gid);
					break;
				case 4:
					jsonObj.put("comment", values[4]);
					break;
				case 5:
					jsonObj.put("root", values[5]);
					break;
				case 6:
					jsonObj.put("shell", values[6]);
					break;
				}
			}
			
			jsonArray.put(jsonObj);
		}
		contentsJson = jsonArray;
	}
	
	public String getContents() {
		return contents;
	}
	
	public JSONArray getContentsJson() {
		return contentsJson;
	}

	public JSONArray getUsersQuery(String name, String uid, String gid, String comment, String home, String shell) {

		JSONArray jsonArr = contentsJson;
		JSONArray newJsonArr = new JSONArray();
		for (int i = 0; i < jsonArr.length(); i++) {
			Object jsonObj = jsonArr.get(i);
			if (name != null) {
				String jsonName = (String) ((JSONObject) jsonObj).get("name");
				if (!jsonName.equals(name))
					continue;
			}
			if (uid != null) {
				Integer jsonUid = (Integer) ((JSONObject) jsonObj).get("uid");
				if (!jsonUid.toString().equals(uid))
					continue;
			}
			if (gid != null) {
				Integer jsonGid = (Integer) ((JSONObject) jsonObj).get("gid");
				if (!jsonGid.toString().equals(gid))
					continue;
			}
			if (comment != null) {
				String jsonComment = (String) ((JSONObject) jsonObj).get("comment");
				if (!jsonComment.equals(comment))
					continue;
			}
			if (home != null) {
				String jsonHome = (String) ((JSONObject) jsonObj).get("home");
				if (!jsonHome.equals(home))
					continue;
			}
			if (shell != null) {
				String jsonShell = (String) ((JSONObject) jsonObj).get("shell");
				if (!jsonShell.equals(shell))
					continue;
			}

			newJsonArr.put(jsonObj);
		}
		return newJsonArr;
	}
	
	public JSONObject getUsersUid(String uid) {

		JSONArray jsonArr = contentsJson;
		for(int i = 0 ; i < jsonArr.length(); i++) {
			Object jsonObj = jsonArr.get(i);
			Integer jsonUid = (Integer) ((JSONObject)jsonObj).get("uid");
			if (jsonUid.toString().equals(uid))
				return (JSONObject) jsonObj;
		}
		return null;
	}
}