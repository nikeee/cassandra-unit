package org.cassandraunit.spring;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

import java.util.List;

/**
 * The {@link ContextCustomizerFactory} implementation to produce a
 * {@link org.cassandraunit.utils.EmbeddedCassandraServerHelper} if a {@link EmbeddedCassandra} annotation
 * is present on the test class.
 *
 * @author Brandon T Johnson
 */
public class CassandraUnitContextCustomizerFactory implements ContextCustomizerFactory {

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass,
                                                     List<ContextConfigurationAttributes> configAttributes) {
        EmbeddedCassandra embeddedCassandra =
                AnnotatedElementUtils.findMergedAnnotation(testClass, EmbeddedCassandra.class);
        return embeddedCassandra != null ? new CassandraUnitContextCustomizer(embeddedCassandra) : null;
    }

}