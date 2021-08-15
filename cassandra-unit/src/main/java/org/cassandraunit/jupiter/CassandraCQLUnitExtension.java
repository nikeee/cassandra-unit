package org.cassandraunit.jupiter;

import com.datastax.oss.driver.api.core.CqlSession;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.CQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * @author Brandon T Johnson
 */
public class CassandraCQLUnitExtension extends BaseCassandraUnitExtension {

	private final CQLDataSet dataSet;
	private CqlSession session;

	public CassandraCQLUnitExtension(CQLDataSet dataSet) {
		this.dataSet = dataSet;
	}

	public CassandraCQLUnitExtension(CQLDataSet dataSet, int readTimeoutMillis) {
		this.dataSet = dataSet;
		this.readTimeoutMillis = readTimeoutMillis;
	}

	public CassandraCQLUnitExtension(CQLDataSet dataSet, String configurationFileName) {
		this(dataSet);
		this.configurationFileName = configurationFileName;
	}

	public CassandraCQLUnitExtension(CQLDataSet dataSet, String configurationFileName, int readTimeoutMillis) {
		this(dataSet);
		this.configurationFileName = configurationFileName;
		this.readTimeoutMillis = readTimeoutMillis;
	}

	// The former constructors with hostip and port have been removed. Now host+port is directly read out of the provided
	// configurationFile(Name). You may also use EmbeddedCassandraServerHelper.CASSANDRA_RNDPORT_YML_FILE to select
	// random (free) ports for EmbeddedCassandra, such that you can start multiple embedded cassandras on the same host
	// (but not in the same JVM).

	public CassandraCQLUnitExtension(CQLDataSet dataSet, String configurationFileName, long startUpTimeoutMillis) {
		super(startUpTimeoutMillis);
		this.dataSet = dataSet;
		this.configurationFileName = configurationFileName;
	}

	public CassandraCQLUnitExtension(CQLDataSet dataSet, String configurationFileName, long startUpTimeoutMillis, int readTimeoutMillis) {
		super(startUpTimeoutMillis);
		this.dataSet = dataSet;
		this.configurationFileName = configurationFileName;
		this.readTimeoutMillis = readTimeoutMillis;
	}

	@Override
	protected void load() {
		session = EmbeddedCassandraServerHelper.getSession();
		CQLDataLoader dataLoader = new CQLDataLoader(session);
		dataLoader.load(dataSet);
		session = dataLoader.getSession();
	}

	public CqlSession getSession() {
		return session;
	}
}
