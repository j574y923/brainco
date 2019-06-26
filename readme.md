### Installation

1. Ensure JDK is installed. 
2. Ensure mvn is installed.
3. `git clone` this project
4. `cd` to your project
5. do `mvn clean install`

### To test:
1. edit config.properties and set `passwd=./etc/test/passwd` and set `group=./etc/test/group`
2. do `mvn clean install`

### To run:
1. do `mvn spring-boot:run`

### config.properties
1. `passwd` will set the path to the passwd file, the default is /etc/passwd
2. `group` will set the path to the group file, the default is /etc/group

### what the service does
This is a RESTful layer that will parse the passwd and group files found in linux's /etc path and return its information in JSON format. You may call the service with the following calls:
1. `/users` will retrieve all users in passwd
2. `/users/query` will retrieve all users in passwd with matching query criteria
3. `/users/{uid}` will retrieve the user in passwd with uid
4. `/users/{uid}/groups` will retrieve all groups in group that contain the user with uid
5. `/groups` will retrieve all groups in group
6. `/groups/query` will retrieve all groups in group with matching query criteria
7. `/groups/{gid}` will retrieve the group in group with gid

### unit testing
The above calls will be unit tested. Each unit test will make a call then do a JSONAssert on expected and actual json strings.