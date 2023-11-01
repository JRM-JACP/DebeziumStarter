# Debezium SpringBoot Starter
> This example is a prototype version of Debezium starter

## Setup

### Preparation and installing dependency
- Clone this repository
- Go to the folder with pom.xml(debezium-spring-boot-starter)
- Run the command
```bash
mvn clean install
```
to install dependency to the local maven repository(.m2/repository)
- Check the dependency in the .m2/repository/org/jacp folder
- Connect the debezium-spring-boot-starter to your application(in pom.xml)
  <dependency>
  <groupId>org.jacp</groupId>
  <artifactId>debezium-spring-boot-starter</artifactId>
  <version>1.0-SNAPSHOT</version>
  </dependency>
### Application running

Run your application and enjoy!