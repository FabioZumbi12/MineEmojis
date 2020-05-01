package br.net.fabiozumbi12.MinEmojis.Fanciful;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.stream.JsonWriter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a textual component of a message part.
 * This can be used to not only represent string literals in a JSON message,
 * but also to represent localized strings and other text values.
 * <p>Different instances of this class can be created with static constructor methods.</p>
 */
public abstract class TextualComponent implements Cloneable {

    static {
        ConfigurationSerialization.registerClass(TextualComponent.ArbitraryTextTypeComponent.class);
        ConfigurationSerialization.registerClass(TextualComponent.ComplexTextTypeComponent.class);
    }

    static TextualComponent deserialize(Map<String, Object> map) {
        if (map.containsKey("key") && map.size() == 2 && map.containsKey("value")) {
            // Arbitrary text component
            return ArbitraryTextTypeComponent.deserialize(map);
        } else if (map.size() >= 2 && map.containsKey("key") && !map.containsKey("value") /* It contains keys that START WITH value */) {
            // Complex JSON object
            return ComplexTextTypeComponent.deserialize(map);
        }

        return null;
    }

    static boolean isTextKey(String key) {
        return key.equals("translate") || key.equals("text") || key.equals("score") || key.equals("selector");
    }

    static boolean isTranslatableText(TextualComponent component) {
        return component instanceof ComplexTextTypeComponent && component.getKey().equals("translate");
    }

    /**
     * Create a textual component representing a string literal.
     * This is the default type of textual component when a single string literal is given to a method.
     *
     * @param textValue The text which will be represented.
     * @return The text component representing the specified literal text.
     */
    public static TextualComponent rawText(String textValue) {
        return new ArbitraryTextTypeComponent("text", textValue);
    }

    @Override
    public String toString() {
        return getReadableString();
    }

    /**
     * @return The JSON key used to represent text components of this type.
     */
    public abstract String getKey();

    /**
     * @return A readable String
     */
    public abstract String getReadableString();

    /**
     * Clones a textual component instance.
     * The returned object should not reference this textual component instance, but should maintain the same key and value.
     */
    @Override
    public abstract TextualComponent clone() throws CloneNotSupportedException;

    /**
     * Writes the text data represented by this textual component to the specified JSON writer object.
     * A new object within the writer is not started.
     *
     * @param writer The object to which to write the JSON data.
     * @throws IOException If an error occurs while writing to the stream.
     */
    public abstract void writeJson(JsonWriter writer) throws IOException;

    /**
     * Internal class used to represent all types of text components.
     * Exception validating done is on keys and values.
     */
    private static final class ArbitraryTextTypeComponent extends TextualComponent implements ConfigurationSerializable {

        private String _key;
        private String _value;

        public ArbitraryTextTypeComponent(String key, String value) {
            setKey(key);
            setValue(value);
        }

        public static ArbitraryTextTypeComponent deserialize(Map<String, Object> map) {
            return new ArbitraryTextTypeComponent(map.get("key").toString(), map.get("value").toString());
        }

        @Override
        public String getKey() {
            return _key;
        }

        public void setKey(String key) {
            Preconditions.checkArgument(key != null && !key.isEmpty(), "The key must be specified.");
            _key = key;
        }

        public String getValue() {
            return _value;
        }

        public void setValue(String value) {
            Preconditions.checkArgument(value != null, "The value must be specified.");
            _value = value;
        }

        @Override
        public TextualComponent clone() {
            // Since this is a private and final class, we can just reinstantiate this class instead of casting super.clone
            return new ArbitraryTextTypeComponent(getKey(), getValue());
        }

        @Override
        public void writeJson(JsonWriter writer) throws IOException {
            writer.name(getKey()).value(getValue());
        }

        @SuppressWarnings("serial")
        public Map<String, Object> serialize() {
            return new HashMap<String, Object>() {{
                put("key", getKey());
                put("value", getValue());
            }};
        }

        @Override
        public String getReadableString() {
            return getValue();
        }
    }

    /**
     * Internal class used to represent a text component with a nested JSON value.
     * Exception validating done is on keys and values.
     */
    private static final class ComplexTextTypeComponent extends TextualComponent implements ConfigurationSerializable {

        private String _key;
        private Map<String, String> _value;

        public ComplexTextTypeComponent(String key, Map<String, String> values) {
            setKey(key);
            setValue(values);
        }

        public static ComplexTextTypeComponent deserialize(Map<String, Object> map) {
            String key = null;
            Map<String, String> value = new HashMap<>();
            for (Map.Entry<String, Object> valEntry : map.entrySet()) {
                if (valEntry.getKey().equals("key")) {
                    key = (String) valEntry.getValue();
                } else if (valEntry.getKey().startsWith("value.")) {
                    value.put(valEntry.getKey().substring(6) /* Strips out the value prefix */, valEntry.getValue().toString());
                }
            }
            return new ComplexTextTypeComponent(key, value);
        }

        @Override
        public String getKey() {
            return _key;
        }

        public void setKey(String key) {
            Preconditions.checkArgument(key != null && !key.isEmpty(), "The key must be specified.");
            _key = key;
        }

        public Map<String, String> getValue() {
            return _value;
        }

        public void setValue(Map<String, String> value) {
            Preconditions.checkArgument(value != null, "The value must be specified.");
            _value = value;
        }

        @Override
        public TextualComponent clone() {
            // Since this is a private and final class, we can just reinstantiate this class instead of casting super.clone
            return new ComplexTextTypeComponent(getKey(), getValue());
        }

        @Override
        public void writeJson(JsonWriter writer) throws IOException {
            writer.name(getKey());
            writer.beginObject();
            for (Map.Entry<String, String> jsonPair : _value.entrySet()) {
                writer.name(jsonPair.getKey()).value(jsonPair.getValue());
            }
            writer.endObject();
        }

        @SuppressWarnings("serial")
        public Map<String, Object> serialize() {
            return new java.util.HashMap<String, Object>() {{
                put("key", getKey());
                for (Map.Entry<String, String> valEntry : getValue().entrySet()) {
                    put("value." + valEntry.getKey(), valEntry.getValue());
                }
            }};
        }

        @Override
        public String getReadableString() {
            return getKey();
        }
    }
}
