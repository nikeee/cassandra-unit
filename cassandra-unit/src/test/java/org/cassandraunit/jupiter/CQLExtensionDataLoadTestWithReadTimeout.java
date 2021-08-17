package org.cassandraunit.jupiter;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 
 * @author Brandon T Johnson
 *
 */
class CQLExtensionDataLoadTestWithReadTimeout {

	private static final int READ_TIMEOUT_VALUE = 15000;

	@RegisterExtension
	static CassandraCQLUnitExtension cqlUnitExtension = new CassandraCQLUnitExtension(new ClassPathCQLDataSet("cql/simple.cql",
			"mykeyspace"), READ_TIMEOUT_VALUE);

	@Test
	void testCQLDataAreInPlace() {
		test();
	}

	@Test
	void sameTestToMakeSureMultipleTestsAreFine() {
		test();
	}

	private void test() {
		ResultSet result = cqlUnitExtension.getSession()
				.execute("select * from testCQLTable WHERE id=1690e8da-5bf8-49e8-9583-4dff8a570737");

		String val = result.iterator().next().getString("value");
		assertEquals("Cql loaded string", val);
	}
}
