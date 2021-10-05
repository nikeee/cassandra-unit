package org.cassandraunit.spring;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Brandon T Johnson
 */
@ExtendWith(SpringExtension.class)
@EmbeddedCassandra
@CassandraDataSet
class CassandraStartAndLoadWithSpringExtensionTest {

  @Test
  void should_work() {
    test();
  }

  @Test
  void should_work_twice() {
    test();
  }

  private void test() {
    CqlSession session = EmbeddedCassandraServerHelper.getSession();
    ResultSet result = session.execute("select * from testCQLTable WHERE id=1690e8da-5bf8-49e8-9583-4dff8a570737");
    String val = result.iterator().next().getString("value");
    Assertions.assertEquals("Cql loaded string", val);
  }

}
