package org.cassandraunit.jupiter;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * @author Brandon T Johnson
 */
public abstract class BaseCassandraUnitExtension implements BeforeAllCallback, AfterAllCallback {

	protected String configurationFileName;
	protected long startupTimeoutMillis;
	protected int readTimeoutMillis = 12000;

	public BaseCassandraUnitExtension() {
		this(EmbeddedCassandraServerHelper.DEFAULT_STARTUP_TIMEOUT);
	}

	public BaseCassandraUnitExtension(long startupTimeoutMillis) {
		this.startupTimeoutMillis = startupTimeoutMillis;
	}

	protected abstract void load();

	@Override
	public void beforeAll(ExtensionContext extensionContext) throws Exception {
		/* start an embedded Cassandra */
		if (configurationFileName != null) {
			EmbeddedCassandraServerHelper.startEmbeddedCassandra(configurationFileName, startupTimeoutMillis);
		} else {
			EmbeddedCassandraServerHelper.startEmbeddedCassandra(startupTimeoutMillis);
		}
		/* create structure and load data */
		load();
	}

	@Override
	public void afterAll(ExtensionContext extensionContext) throws Exception {
		EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
	}
}
