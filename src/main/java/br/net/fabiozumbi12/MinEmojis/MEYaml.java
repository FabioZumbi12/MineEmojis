package br.net.fabiozumbi12.MinEmojis;

import com.google.common.io.Files;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MEYaml extends YamlConfiguration {

    public void Yaml() {
    }

    @Override
    public void load(File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        load(new FileInputStream(file));
    }

    public void load(InputStream stream) throws IOException, InvalidConfigurationException {
        Validate.notNull(stream, "Stream cannot be null");

        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader input = new BufferedReader(reader)) {
            String line;
            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        }

        loadFromString(builder.toString());
    }

    @Override
    public void save(File file) throws IOException {
        Validate.notNull(file, "File cannot be null");

        Files.createParentDirs(file);
        String data = saveToString();

        FileOutputStream stream = new FileOutputStream(file);
        try (OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {
            writer.write(data);
        }
    }

}