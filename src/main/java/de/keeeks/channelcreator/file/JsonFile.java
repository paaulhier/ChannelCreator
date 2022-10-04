package de.keeeks.channelcreator.file;

import com.google.gson.Gson;
import de.keeeks.channelcreator.gson.GsonBuilder;
import lombok.SneakyThrows;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class JsonFile {

    private final Gson gson;
    private final File file;

    @SneakyThrows
    public JsonFile(String folder, String filename) {
        this.gson = GsonBuilder.global();
        this.file = file(folder, filename);
    }

    @SneakyThrows
    public JsonFile(String folder, String filename, Gson gson) {
        this.gson = gson;
        this.file = file(folder, filename);
    }

    @SneakyThrows
    public <T> T loadObject(Class<T> type) {
        return gson.fromJson(Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8), type);
    }

    public <T> T loadObject(Class<T> type, T defaultValue) {
        var loadObject = loadObject(type);
        if (loadObject != null) return loadObject;
        save(defaultValue);
        return defaultValue;
    }

    @SneakyThrows
    public <T> void save(T t) {
        var printWriter = new PrintWriter(file, StandardCharsets.UTF_8);
        gson.toJson(t, t.getClass(), printWriter);
        printWriter.flush();
    }

    @SneakyThrows
    private File file(String folder, String filename) {
        var file = new File(folder, filename + ".json");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            Files.createFile(file.toPath());
        }
        return file;
    }
}