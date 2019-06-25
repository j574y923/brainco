package rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BrainCorpControllerTests {
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@Test
	public void shouldGetUsers() throws Exception {
		// tell user to use ./etc/passwd and ./etc/group for config
		String passwdPath = "./etc/passwd";
		
		// overwrite passwd with test data
		String testPasswd = "root:x:0:0::/root:/usr/bin/zsh\n" + 
				"bin:x:1:1::/:/sbin/nologin\n" + 
				"daemon:x:2:2::/:/sbin/nologin\n" + 
				"mail:x:8:12::/var/spool/mail:/sbin/nologin\n" + 
				"ftp:x:14:11::/srv/ftp:/sbin/nologin";
		PrintWriter writer = new PrintWriter(passwdPath);
		writer.println(testPasswd);
		writer.close();
		
		// getUsers()
		MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		MvcResult result = mvc.perform(get("/users").accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andReturn();
		
		// verify data
		String jsonExpectedStr = "[{\"uid\":0,\"gid\":0,\"shell\":\"/usr/bin/zsh\",\"root\":\"/root\",\"name\":\"root\",\"comment\":\"\"},"
				+ "{\"uid\":1,\"gid\":1,\"shell\":\"/sbin/nologin\",\"root\":\"/\",\"name\":\"bin\",\"comment\":\"\"},"
				+ "{\"uid\":2,\"gid\":2,\"shell\":\"/sbin/nologin\",\"root\":\"/\",\"name\":\"daemon\",\"comment\":\"\"},"
				+ "{\"uid\":8,\"gid\":12,\"shell\":\"/sbin/nologin\",\"root\":\"/var/spool/mail\",\"name\":\"mail\",\"comment\":\"\"},"
				+ "{\"uid\":14,\"gid\":11,\"shell\":\"/sbin/nologin\",\"root\":\"/srv/ftp\",\"name\":\"ftp\",\"comment\":\"\"}]";
		JSONArray jsonArrExpected = new JSONArray(jsonExpectedStr);
		String jsonActualStr = result.getResponse().getContentAsString();
		JSONArray jsonArrActual = new JSONArray(jsonActualStr);
		JSONAssert.assertEquals(jsonArrExpected, jsonArrActual, JSONCompareMode.LENIENT);
	}

	@Test
	public void shouldGetUsersQuery() throws Exception {
		// tell user to use ./etc/passwd and ./etc/group for config
		String passwdPath = "./etc/passwd";
		
		// overwrite passwd with test data
		String testPasswd = "root:x:0:0::/root:/usr/bin/zsh\n" + 
				"bin:x:1:1::/:/sbin/nologin\n" + 
				"daemon:x:2:2::/:/sbin/nologin\n" + 
				"mail:x:8:12::/var/spool/mail:/sbin/nologin\n" + 
				"ftp:x:14:11::/srv/ftp:/sbin/nologin";
		PrintWriter writer = new PrintWriter(passwdPath);
		writer.println(testPasswd);
		writer.close();

		// getUsers()
		MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		//FIXME:
		System.out.println("WTF?2?");
		MvcResult result = mvc.perform(get("/users/query").param("param", "/sbin/nologin")
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andReturn();

		// verify data
		String jsonStrExpected = "[{\"uid\":1,\"gid\":1,\"shell\":\"/sbin/nologin\",\"root\":\"/\",\"name\":\"bin\",\"comment\":\"\"},{\"uid\":2,\"gid\":2,\"shell\":\"/sbin/nologin\",\"root\":\"/\",\"name\":\"daemon\",\"comment\":\"\"},{\"uid\":8,\"gid\":12,\"shell\":\"/sbin/nologin\",\"root\":\"/var/spool/mail\",\"name\":\"mail\",\"comment\":\"\"},{\"uid\":14,\"gid\":11,\"shell\":\"/sbin/nologin\",\"root\":\"/srv/ftp\",\"name\":\"ftp\",\"comment\":\"\"}]";
		JSONArray jsonArrExpected = new JSONArray(jsonStrExpected);
		String jsonStrActual = result.getResponse().getContentAsString();
		System.out.println("WTF??");
		System.out.println(jsonStrActual);
		JSONArray jsonArrActual = new JSONArray(jsonStrActual);
		JSONAssert.assertEquals(jsonArrExpected, jsonArrActual, JSONCompareMode.LENIENT);
	}

	@Test
	public void shouldGetUsersUid() throws Exception {

		// tell user to use ./etc/passwd and ./etc/group for config
		String passwdPath = "./etc/passwd";
		
		// overwrite passwd with test data
		String testPasswd = "root:x:0:0::/root:/usr/bin/zsh\n" + 
				"bin:x:1:1::/:/sbin/nologin\n" + 
				"daemon:x:2:2::/:/sbin/nologin\n" + 
				"mail:x:8:12::/var/spool/mail:/sbin/nologin\n" + 
				"ftp:x:14:11::/srv/ftp:/sbin/nologin";
		PrintWriter writer = new PrintWriter(passwdPath);
		writer.println(testPasswd);
		writer.close();
		
		// getUsers()
		MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		MvcResult result = mvc.perform(get("/users/{uid}", 8)
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andReturn();
		
		// verify data
		String jsonStrExpected = "{\"uid\":8,\"gid\":12,\"shell\":\"/sbin/nologin\",\"root\":\"/var/spool/mail\",\"name\":\"mail\",\"comment\":\"\"}";
		JSONObject jsonArrExpected = new JSONObject(jsonStrExpected);
		String jsonStrActual = result.getResponse().getContentAsString();
		JSONObject jsonArrActual = new JSONObject(jsonStrActual);
		JSONAssert.assertEquals(jsonArrExpected, jsonArrActual, JSONCompareMode.LENIENT);
	}

	@Test
	public void shouldGetGroupsUid() {
		// tell user to use ./etc/passwd and ./etc/group for config
		String passwdPath = "./etc/passwd";
		String groupPath = "./etc/group";
		
		// overwrite passwd AND group files with test data
		String testPasswd = "root:x:0:0::/root:/usr/bin/zsh\n" + 
				"bin:x:1:1::/:/sbin/nologin\n" + 
				"daemon:x:2:2::/:/sbin/nologin\n" + 
				"mail:x:8:12::/var/spool/mail:/sbin/nologin\n" + 
				"ftp:x:14:11::/srv/ftp:/sbin/nologin";
		String testGroup = "root:x:0:root\n" + 
				"sys:x:3:bin\n" + 
				"mem:x:8:\n" + 
				"ftP:x:11:\n" + 
				"mail:x:12:\n" + 
				"log:x:19:\n" + 
				"smmsp:x:25:\n" + 
				"proc:x:26:\n" + 
				"games:x:50:\n" + 
				"lock:x:54:\n" + 
				"network:x:90:\n" + 
				"floppy:x:94:\n" + 
				"scanner:x:96:\n" + 
				"power:x:98:\n" + 
				"adm:x:999:daemon\n" + 
				"wheel:x:998:\n" + 
				"kmem:x:997:\n" + 
				"tty:x:5:\n" + 
				"utmp:x:996:\n" + 
				"audio:x:995:root,user1,user2,user3";
		
		// getUsers()
		InputStream in = null; //TODO: read get request
		
		// verify data
//		JsonArray jsonArr = Json.createReader(in).readArray();
//		assertEquals(1,2);
	}

	@Test
	public void shouldGetGroups() throws Exception {
		// tell user to use ./etc/passwd and ./etc/group for config
		String groupPath = "./etc/group";
		
		// overwrite passwd with test data
		String testGroup = "root:x:0:root\n" + 
				"sys:x:3:bin\n" + 
				"mem:x:8:\n" + 
				"ftp:x:11:\n" + 
				"mail:x:12:\n" + 
				"log:x:19:\n" + 
				"smmsp:x:25:\n" + 
				"proc:x:26:\n" + 
				"games:x:50:\n" + 
				"lock:x:54:\n" + 
				"network:x:90:\n" + 
				"floppy:x:94:\n" + 
				"scanner:x:96:\n" + 
				"power:x:98:\n" + 
				"adm:x:999:daemon\n" + 
				"wheel:x:998:\n" + 
				"kmem:x:997:\n" + 
				"tty:x:5:\n" + 
				"utmp:x:996:\n" + 
				"audio:x:995:root,user1,user2,user3";
		PrintWriter writer = new PrintWriter(groupPath);
		writer.println(testGroup);
		writer.close();

		// getUsers()
		MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		MvcResult result = mvc.perform(get("/groups").accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andReturn();

		// verify data
		String jsonStrExpected = "[{\"gid\":0,\"members\":[\"root\"],\"name\":\"root\"},{\"gid\":3,\"members\":[\"bin\"],\"name\":\"sys\"},"
				+ "{\"gid\":8,\"members\":[],\"name\":\"mem\"},{\"gid\":11,\"members\":[],\"name\":\"ftp\"},"
				+ "{\"gid\":12,\"members\":[],\"name\":\"mail\"},{\"gid\":19,\"members\":[],\"name\":\"log\"},"
				+ "{\"gid\":25,\"members\":[],\"name\":\"smmsp\"},{\"gid\":26,\"members\":[],\"name\":\"proc\"},"
				+ "{\"gid\":50,\"members\":[],\"name\":\"games\"},{\"gid\":54,\"members\":[],\"name\":\"lock\"},"
				+ "{\"gid\":90,\"members\":[],\"name\":\"network\"},{\"gid\":94,\"members\":[],\"name\":\"floppy\"},"
				+ "{\"gid\":96,\"members\":[],\"name\":\"scanner\"},{\"gid\":98,\"members\":[],\"name\":\"power\"},"
				+ "{\"gid\":999,\"members\":[\"daemon\"],\"name\":\"adm\"},{\"gid\":998,\"members\":[],\"name\":\"wheel\"},"
				+ "{\"gid\":997,\"members\":[],\"name\":\"kmem\"},{\"gid\":5,\"members\":[],\"name\":\"tty\"},"
				+ "{\"gid\":996,\"members\":[],\"name\":\"utmp\"},{\"gid\":995,\"members\":[\"root\",\"user1\",\"user2\",\"user3\"],\"name\":\"audio\"}]";
		JSONArray jsonArrExpected = new JSONArray(jsonStrExpected);
		String jsonStrActual = result.getResponse().getContentAsString();
		JSONArray jsonArrActual = new JSONArray(jsonStrActual);
		JSONAssert.assertEquals(jsonArrExpected, jsonArrActual, JSONCompareMode.LENIENT);
	}

	@Test
	public void shouldGetGroupsQuery() {

		// tell user to use ./etc/passwd and ./etc/group for config
		String groupPath = "./etc/group";
		
		// overwrite group with test data
		String testGroup = "root:x:0:root\n" + 
				"sys:x:3:bin\n" + 
				"mem:x:8:\n" + 
				"ftP:x:11:\n" + 
				"mail:x:12:\n" + 
				"log:x:19:\n" + 
				"smmsp:x:25:\n" + 
				"proc:x:26:\n" + 
				"games:x:50:\n" + 
				"lock:x:54:\n" + 
				"network:x:90:\n" + 
				"floppy:x:94:\n" + 
				"scanner:x:96:\n" + 
				"power:x:98:\n" + 
				"adm:x:999:daemon\n" + 
				"wheel:x:998:\n" + 
				"kmem:x:997:\n" + 
				"tty:x:5:\n" + 
				"utmp:x:996:\n" + 
				"audio:x:995:root,user1,user2,user3";
		
		// getUsers()
		InputStream in = null; //TODO: read get request
		
		// verify data
	}

	@Test
	public void shouldGetGroupsGid() {

		// tell user to use ./etc/passwd and ./etc/group for config
		String groupPath = "./etc/group";
		
		// overwrite group with test data
		String testGroup = "root:x:0:root\n" + 
				"sys:x:3:bin\n" + 
				"mem:x:8:\n" + 
				"ftP:x:11:\n" + 
				"mail:x:12:\n" + 
				"log:x:19:\n" + 
				"smmsp:x:25:\n" + 
				"proc:x:26:\n" + 
				"games:x:50:\n" + 
				"lock:x:54:\n" + 
				"network:x:90:\n" + 
				"floppy:x:94:\n" + 
				"scanner:x:96:\n" + 
				"power:x:98:\n" + 
				"adm:x:999:daemon\n" + 
				"wheel:x:998:\n" + 
				"kmem:x:997:\n" + 
				"tty:x:5:\n" + 
				"utmp:x:996:\n" + 
				"audio:x:995:root,user1,user2,user3";
		
		// getUsers()
		InputStream in = null; //TODO: read get request
		
		// verify data
	}

}
