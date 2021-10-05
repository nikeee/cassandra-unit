package org.cassandraunit.spring;

import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

/**
 * The {@link ContextCustomizer} implementation for starting an embedded cassandra instance via
 * {@link EmbeddedCassandraServerHelper} during Spring Context creation. This approach tends to work best with JUnit 5
 * as the Test Execution Listeners does not work in situations where the junit platform engine is not used (e.g. Cucumber Junit Engine)
 *
 * @author Brandon T Johnson
 */
public class CassandraUnitContextCustomizer implements ContextCustomizer, AfterAllCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraUnitContextCustomizer.class);
    private static boolean initialized = false;

    private final EmbeddedCassandra embeddedCassandra;

    public CassandraUnitContextCustomizer(EmbeddedCassandra embeddedCassandra) {
        this.embeddedCassandra = embeddedCassandra;
    }

    @Override
    public void customizeContext(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
        if (!initialized) {
            Optional.ofNullable(embeddedCassandra.configuration())
                    .ifPresent(yamlFile -> {
                        startServer(embeddedCassandra, yamlFile);
                        initialized = true;
                    });
        }

        CassandraDataSet cassandraDataSet = AnnotationUtils.findAnnotation(mergedConfig.getTestClass(), CassandraDataSet.class);
        if (cassandraDataSet != null) {
            String keyspace = cassandraDataSet.keyspace();
            List<String> dataset = dataSetLocations(context, mergedConfig, cassandraDataSet);
            ListIterator<String> datasetIterator = dataset.listIterator();

            CQLDataLoader cqlDataLoader = new CQLDataLoader(EmbeddedCassandraServerHelper.getSession());
            while (datasetIterator.hasNext()) {
                String next = datasetIterator.next();
                boolean dropAndCreateKeyspace = datasetIterator.previousIndex() == 0;
                cqlDataLoader.load(new ClassPathCQLDataSet(next, dropAndCreateKeyspace, dropAndCreateKeyspace, keyspace));
            }
        }
    }

    private void startServer(EmbeddedCassandra embeddedCassandra, String yamlFile) {
        String tmpDir = embeddedCassandra.tmpDir();
        long timeout = embeddedCassandra.timeout();
        try {
            EmbeddedCassandraServerHelper.startEmbeddedCassandra(yamlFile, tmpDir, timeout);
        } catch (IOException e) {
            LOGGER.error("Error occurred while trying to start embedded cassandra!", e);
        }
    }

    private List<String> dataSetLocations(ConfigurableApplicationContext testContext, MergedContextConfiguration mergedConfig, CassandraDataSet cassandraDataSet) {
        String[] dataset = cassandraDataSet.value();
        if (dataset.length == 0) {
            String alternativePath = alternativePath(mergedConfig.getTestClass(), true, cassandraDataSet.type().name());
            if (testContext.getResource(alternativePath).exists()) {
                dataset = new String[]{alternativePath.replace(ResourceUtils.CLASSPATH_URL_PREFIX + "/", "")};
            } else {
                alternativePath = alternativePath(mergedConfig.getTestClass(), false, cassandraDataSet.type().name());
                if (testContext.getResource(alternativePath).exists()) {
                    dataset = new String[]{alternativePath.replace(ResourceUtils.CLASSPATH_URL_PREFIX + "/", "")};
                } else {
                    LOGGER.info("No dataset will be loaded");
                }
            }
        }
        return Arrays.asList(dataset);
    }

    protected void cleanServer() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }

    protected String alternativePath(Class<?> clazz, boolean includedPackageName, String extension) {
        if (includedPackageName) {
            return ResourceUtils.CLASSPATH_URL_PREFIX + "/" + ClassUtils.convertClassNameToResourcePath(clazz.getName()) + "-dataset" + "." + extension;
        } else {
            return ResourceUtils.CLASSPATH_URL_PREFIX + "/" + clazz.getSimpleName() + "-dataset" + "." + extension;
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        cleanServer();
    }
}
