package io.iqpizza.starcraft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PropertiesUtils {
    private static final Logger log = LoggerFactory.getLogger(PropertiesUtils.class);
    private static final Map<PropertyVariable, String> PROPERTIES = new EnumMap<>(PropertyVariable.class);

    /* properties 에서 가져올 상수를 정의합니다. */
    public enum PropertyVariable {
        MAP("bot.map.base.path"),
        CSV("bot.csv.path"),
        ;

        private final String key;
        PropertyVariable(String key) {
            this.key = key;
        }
    }

    private PropertiesUtils() {
        throw new IllegalStateException("PropertiesUtils class is util class that cannot create instance!");
    }

    static {
        Properties properties = new Properties();

        try (final InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream("bot.properties")) {
            if (inputStream == null) {
                log.error("Sorry, unable to find bot.properties. Close the application");
                throw new IllegalStateException("Sorry, unable to find bot.properties. Close the application");
            }

            properties.load(inputStream);
            for (PropertyVariable variable : PropertyVariable.values()) {
                PROPERTIES.put(variable, properties.getProperty(variable.key));
            }
        } catch (IOException e) {
            throw new IllegalStateException("IOException occurrs while read bot.properties", e);
        }
    }

    /**
     * Returns unmodifiableMap of properties from 'bot.properties'
     * @return properties
     * @see Collections#unmodifiableMap(Map)
     */
    public static Map<PropertyVariable, String> getProperties() {
        return Collections.unmodifiableMap(PROPERTIES);
    }

    public static String getVariable(PropertyVariable variable) {
        return PROPERTIES.get(variable);
    }

}
