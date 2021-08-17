package org.cassandraunit.jupiter;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author Brandon T Johnson
 *
 */
class CQLExtensionDataLoadTestWithFullOptions {

    private static final long STARTUP_TIMEOUT_VALUE = 20000L;
    private static final int READ_TIMEOUT_VALUE = 17000;

    @RegisterExtension
    static CassandraCQLUnitExtension cqlUnitExtension = new CassandraCQLUnitExtension(new ClassPathCQLDataSet("cql/simple.cql",
            "mykeyspace"), EmbeddedCassandraServerHelper.CASSANDRA_RNDPORT_YML_FILE, STARTUP_TIMEOUT_VALUE,
            READ_TIMEOUT_VALUE);

    @Test
    void testNativeDriverAccessToRandomPort() {
        ResultSet result = cqlUnitExtension.getSession()
                .execute("select * from testCQLTable WHERE id=1690e8da-5bf8-49e8-9583-4dff8a570737");

        String val = result.iterator().next().getString("value");
        assertEquals("Cql loaded string", val);
    }
}
