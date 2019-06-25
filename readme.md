### Installation

1. Ensure JDK is installed. 
2. Ensure mvn is installed.
3. `git clone` this project
4. `cd` to your project
5. do `mvn clean install`


### To test:
edit config.properties and set `passwd=./etc/test/passwd` and set `group=./etc/test/group`
do `mvn clean install`

### To run:
do `mvn spring-boot:run`