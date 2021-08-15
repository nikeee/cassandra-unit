package org.cassandraunit.jupiter;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 
 * @author Brandon T Johnson
 *
 */
class CQLExtensionDataLoadTestWithBlankLineEndings {

    @RegisterExtension
    static CassandraCQLUnitExtension cqlUnitExtension = new CassandraCQLUnitExtension(new ClassPathCQLDataSet("cql/statementsWithBlankEndings.cql", "mykeyspace"));

    @Test
	void testCQLDataAreInPlace() {
        assertDoesNotThrow(() -> {
            ResultSet result = cqlUnitExtension.getSession().execute("select * from testCQLTable WHERE id=1690e8da-5bf8-49e8-9583-4dff8a570737");

            String val = result.iterator().next().getString("value");
            assertEquals("Cql loaded string",val);
        });
	}
}
