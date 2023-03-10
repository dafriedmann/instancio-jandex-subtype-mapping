import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.Index;
import org.jboss.jandex.IndexReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class JandexToInstancioProperties {

    private static Logger log = LoggerFactory.getLogger(JandexToInstancioProperties.class);

    public static void main(String[] args) throws IOException {

        Index index;
        try (FileInputStream input = new FileInputStream("target/classes/META-INF/jandex.idx")) {
            IndexReader reader = new IndexReader(input);
            index = reader.read();
            generateInstancioProperties(index);
        }
        catch (IOException e){
            log.error("Reading jandex index failed. Did you generate the index using the mvn profile?", e);
        }
    }

    private static void generateInstancioProperties(Index index){
        Properties instancioMappingProperties = loadExistingInstancioProperties();

        for (ClassInfo c : index.getKnownClasses()) {
            if (c.isInterface()) {
                Collection<ClassInfo> impls = index.getAllKnownImplementors(c.name());
                if (impls.isEmpty()) {
                    break; // no impl found -> skip
                }
                // Add impl to properties
                Iterator<ClassInfo> implsIterator = impls.iterator();
                String key = "subtype." + c.name();
                String value = implsIterator.next().name().toString(); // only use first
                instancioMappingProperties.putIfAbsent(key, value);
                if(implsIterator.hasNext()){
                    log.warn("Found another impl "+ implsIterator.next().name() +" for " + c.name());
                }
            }
        }
        try {
            Properties sortedInstancioMappingProperties = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            sortedInstancioMappingProperties.putAll(instancioMappingProperties);
            sortedInstancioMappingProperties.store(Files.newOutputStream(Paths.get("src/test/resources/instancio.properties")), null);
        } catch (IOException e) {
            log.error("Failed to write instancio properties. ", e);
        }
        log.info("Instancio properties written to src/test/resources");
    }

    private static Properties loadExistingInstancioProperties() {
        Properties existingInstancioProp = new Properties();
        FileInputStream inputPropertyStream = null;
        try {
            Path instancioPropertiesPath = Paths.get("src/test/resources/instancio.properties");
            if (!Files.exists(instancioPropertiesPath)) {
                return existingInstancioProp; // empty properties
            }
            existingInstancioProp.load(new FileInputStream(instancioPropertiesPath.toString()));
        } catch (FileNotFoundException e) {
            log.error("instancio properties ", e);
        } catch (IOException e) {
            log.error("Failed to load instancio properties ", e);
        }
        return existingInstancioProp;
    }
}
