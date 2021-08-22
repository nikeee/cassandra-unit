WELCOME to CassandraUnit
========================

*Note that this project was forked from [jsevellec](https://github.com/jsevellec/cassandra-unit) and cassandra 4.x modifications made by [wakingrufus](https://github.com/wakingrufus/cassandra-unit)*

Everything is in the wiki : https://github.com/jsevellec/cassandra-unit/wiki

What is it?
-----------
Like other *Unit projects, CassandraUnit is a Java utility test tool.
It helps you create your Java Application with [Apache Cassandra](http://cassandra.apache.org) Database backend.
CassandraUnit is for Cassandra what DBUnit is for Relational Databases.

CassandraUnit helps you writing isolated JUnit tests in a Test Driven Development style.

Main features :
---------------
- Start an embedded Cassandra.
- Create structure (keyspace and Column Families) and load data from an XML, JSON or YAML DataSet.
- Execute a CQL script.

Where to start :
----------------
You can start by reading the wiki : https://github.com/jsevellec/cassandra-unit/wiki

Alternatives to this library :
----------------
* [Testcontainers](https://www.testcontainers.org/modules/databases/cassandra/) - For applications and organizations that run on containers (e.g. Docker), Testcontainers is the best choice for spinning up and down new Cassandra instances
* [Embedded cassandra by nosan](https://github.com/nosan/embedded-cassandra) embedded cassandra approach that allows users to have fine grained control over starting and stopping instances of Cassandra including the version, where and how the files are stored, etc. Note that this dependency does not contain Cassandra dependencies, but instead, downloads Cassandra binaries from Apache and runs that instead.

Modifications made in this fork :
----------------
* Cassandra 4.0.0 support
CassandraUnit now runs on Cassandra 4.0.0, which works on both Java 8 and Java 11. 

***Special Note for Java 11 Support:***

Due to the way that Cassandra 4.0.0 uses unsafe classes and the introduction of the Java Module System in Java 9, in order to run on Java 11, the following jvm options need to be included when running CassandraUnit:

```text
-Djdk.attach.allowAttachSelf=true
--add-exports java.base/jdk.internal.misc=ALL-UNNAMED
--add-exports java.base/jdk.internal.ref=ALL-UNNAMED
--add-exports java.base/sun.nio.ch=ALL-UNNAMED
--add-exports java.management.rmi/com.sun.jmx.remote.internal.rmi=ALL-UNNAMED
--add-exports java.rmi/sun.rmi.registry=ALL-UNNAMED
--add-exports java.rmi/sun.rmi.server=ALL-UNNAMED
--add-exports java.sql/java.sql=ALL-UNNAMED

--add-opens java.base/java.lang.module=ALL-UNNAMED
--add-opens java.base/jdk.internal.loader=ALL-UNNAMED
--add-opens java.base/jdk.internal.ref=ALL-UNNAMED
--add-opens java.base/jdk.internal.reflect=ALL-UNNAMED
--add-opens java.base/jdk.internal.math=ALL-UNNAMED
--add-opens java.base/jdk.internal.module=ALL-UNNAMED
--add-opens java.base/jdk.internal.util.jar=ALL-UNNAMED
--add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED
```


For example, to include these options during a Maven build, add the following to the `maven-surefire-plugin` (or `maven-failsafe-plugin` if CassandraUnit is being ran using that plugin):
```xml
<build>
  <plugins>
      <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>2.7.2</version>
          <configuration>
              <argLine>-Dfile.encoding=${project.build.sourceEncoding}
                  -Djdk.attach.allowAttachSelf=true
                  --add-exports java.base/jdk.internal.misc=ALL-UNNAMED
                  --add-exports java.base/jdk.internal.ref=ALL-UNNAMED
                  --add-exports java.base/sun.nio.ch=ALL-UNNAMED
                  --add-exports java.management.rmi/com.sun.jmx.remote.internal.rmi=ALL-UNNAMED
                  --add-exports java.rmi/sun.rmi.registry=ALL-UNNAMED
                  --add-exports java.rmi/sun.rmi.server=ALL-UNNAMED
                  --add-exports java.sql/java.sql=ALL-UNNAMED

                  --add-opens java.base/java.lang.module=ALL-UNNAMED
                  --add-opens java.base/jdk.internal.loader=ALL-UNNAMED
                  --add-opens java.base/jdk.internal.ref=ALL-UNNAMED
                  --add-opens java.base/jdk.internal.reflect=ALL-UNNAMED
                  --add-opens java.base/jdk.internal.math=ALL-UNNAMED
                  --add-opens java.base/jdk.internal.module=ALL-UNNAMED
                  --add-opens java.base/jdk.internal.util.jar=ALL-UNNAMED
                  --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED</argLine>
          </configuration>
      </plugin>
  </plugins>
</build>
```

* Dependencies upgrades
  - libthrift 0.14.2
  - Junit Jupiter 5.7.1
  - Junit Vintage 5.7.1
  - JNA 5.8.0
  - Mockito 3.11.2
  - Cassandra Driver 4.11.0
  - Spring Core 5.3.9
 
* JUnit 5 Extension for CassandraUnit
CassandraUnit now provides a JUnit 5 Extension to simplify the use of Cassandra in JUnit 5 test classes.

You can see an example here using the @RegisterExtension annotation (recommended) or use the @ExtendWith annotation: 

```java
import com.datastax.oss.driver.api.core.cql.ResultSet;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CQLExtensionDataLoadTestWithReadTimeout {

	private static final int READ_TIMEOUT_VALUE = 15000;

	@RegisterExtension
	static CassandraCQLUnitExtension cqlUnitExtension = new CassandraCQLUnitExtension(new ClassPathCQLDataSet("cql/simple.cql",
			"mykeyspace"), READ_TIMEOUT_VALUE);

	@Test
	void testCQLDataAreInPlace() {
		test();
	}

	private void test() {
		ResultSet result = cqlUnitExtension.getSession()
				.execute("select * from testCQLTable WHERE id=1690e8da-5bf8-49e8-9583-4dff8a570737");

		String val = result.iterator().next().getString("value");
		assertEquals("Cql loaded string", val);
	}
}
```


License :
---------
This project is licensed under LGPL V3.0 :
http://www.gnu.org/licenses/lgpl-3.0-standalone.html

