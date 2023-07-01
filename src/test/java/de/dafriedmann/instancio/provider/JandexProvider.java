package de.dafriedmann.instancio.provider;

import org.instancio.spi.InstancioServiceProvider;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.Index;
import org.jboss.jandex.IndexReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Implementation of InstancioServiceProvider using a jandex idx.
 */
public class JandexProvider implements InstancioServiceProvider {

    public TypeResolver getTypeResolver() {
        return new JandexIdxTypeResolver();
    }

    /**
     * Custom {@code TypeResolver} implementation.
     * Use compiled Jandex idx to find implementations of interfaces.
     */
    private static class JandexIdxTypeResolver implements TypeResolver {
        private final Map<Class<?>, Class<?>> subtypes = new HashMap<>();
        JandexIdxTypeResolver(){
            generateSubtypeMap(readJandexIdx());
        }
        private Index readJandexIdx() {
            try (FileInputStream input = new FileInputStream("target/classes/META-INF/jandex.idx")) {
                IndexReader reader = new IndexReader(input);
                return reader.read();
            }
            catch (IOException e){
                System.err.println("Reading jandex index failed. Did you generate the index using the mvn profile?");
            }
            return null;
        }

        @Override
        public Class<?> getSubtype(final Class<?> type) {
            return subtypes.get(type);
        }

        private void generateSubtypeMap(Index index) {
            if(index == null){
                // nothing to do
                return;
            }

            List<ClassInfo> interfaces = index.getKnownClasses().stream().filter(ClassInfo::isInterface).toList();
            for (ClassInfo c : interfaces){
                Collection<ClassInfo> impls = index.getAllKnownImplementors(c.name());
                if (impls.isEmpty()) {
                    continue; // no impl found -> skip
                }
                // Add impl subtype map
                Iterator<ClassInfo> implsIterator = impls.iterator();
                try {
                    subtypes.put(Class.forName(c.name().toString()), Class.forName(implsIterator.next().name().toString()));
                } catch (ClassNotFoundException e) {
                    System.err.println("Could not resolve class for " + c.simpleName());
                }

                if(implsIterator.hasNext()){
                    System.out.println("Found another impl "+ implsIterator.next().name() +" for " + c.name());
                }
            }
        }
    }

}

