package rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BrainCorpControllerTests {

	@Test
	public void shouldGetUsers() throws IOException {
		// tell user to use ./etc/passwd and ./etc/group for config
		String passwdPath = "./etc/passwd";
		
		// overwrite passwd with test data
		String testPasswd = "root:x:0:0::/root:/usr/bin/zsh\n" + 
				"bin:x:1:1::/:/sbin/nologin\n" + 
				"daemon:x:2:2::/:/sbin/nologin\n" + 
				"mail:x:8:12::/var/spool/mail:/sbin/nologin\n" + 
				"ftp:x:14:11::/srv/ftp:/sbin/nologin";
		OutputStream out = null;//TODO: write to 
		
		// getUsers()
		InputStream in = null; //TODO: read get request
		
		// verify data
		JsonArray jsonArr = Json.createReader(in).readArray();
	}

	@Test
	public void shouldGetUsersQuery() {

		// tell user to use ./etc/passwd and ./etc/group for config
		String passwdPath = "./etc/passwd";
		
		// overwrite passwd with test data
		String testPasswd = "root:x:0:0::/root:/usr/bin/zsh\n" + 
				"bin:x:1:1::/:/sbin/nologin\n" + 
				"daemon:x:2:2::/:/sbin/nologin\n" + 
				"mail:x:8:12::/var/spool/mail:/sbin/nologin\n" + 
				"ftp:x:14:11::/srv/ftp:/sbin/nologin";
		
		// getUsers()
		InputStream in = null; //TODO: read get request
		
		// verify data
		JsonArray jsonArr = Json.createReader(in).readArray();
	}

	@Test
	public void shouldGetUsersUid() {

		// tell user to use ./etc/passwd and ./etc/group for config
		String passwdPath = "./etc/passwd";
		
		// overwrite passwd with test data
		String testPasswd = "root:x:0:0::/root:/usr/bin/zsh\n" + 
				"bin:x:1:1::/:/sbin/nologin\n" + 
				"daemon:x:2:2::/:/sbin/nologin\n" + 
				"mail:x:8:12::/var/spool/mail:/sbin/nologin\n" + 
				"ftp:x:14:11::/srv/ftp:/sbin/nologin";
		
		// getUsers()
		InputStream in = null; //TODO: read get request
		
		// verify data
		JsonObject jsonObj = Json.createReader(in).readObject();
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
		JsonArray jsonArr = Json.createReader(in).readArray();

	}

	@Test
	public void shouldGetGroups() {

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
		JsonArray jsonArr = Json.createReader(in).readArray();
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
		JsonArray jsonArr = Json.createReader(in).readArray();
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
		JsonObject jsonObj = Json.createReader(in).readObject();
	}

}
