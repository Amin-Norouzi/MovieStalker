package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.model.movie.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    @Value("#{'${movie.validation.tags}'.split(',')}")
    private Set<String> tags;

    @Value("#{'${movie.validation.formats}'.split(',')}")
    private Set<String> formats;

    public Set<Query> generate(File directory) {
        List<File> files = read(directory);
        return convert(files);
    }

    private List<File> read(File directory) {
        return Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                .filter(File::isFile)
                .filter(this::verify)
                .sorted().toList();
    }

    private Set<Query> convert(List<File> files) {
        Set<Query> queries = new HashSet<>();

        Set<String> names = extract(files);
        names.forEach(name -> queries.add(build(name)));

        return queries;
    }

    private Set<String> extract(List<File> files) {
        Set<String> result = new HashSet<>();
        for (File file : files) {
            String name = file.getName();

            name = replace(formats, name);
            name = replace(tags, name);
            name = replace(name);

            if (supports(name)) {
                result.add(name);
            }
        }

        return result;
    }

    private boolean supports(String value) {
        return value != null && value.isBlank();
    }

    private String replace(Collection<String> characters, String value) {
        for (String character : characters) {
            if (value.contains(character)) {
                value = value.replace(character, "");
            }
        }

        return value;
    }

    private String replace(String value) {
        return Arrays.stream(value.replaceAll("\\.", " ").trim()
                        .replace("_", " ").trim()
                        .replace("-", " ").trim()
                        .replaceAll(" +", " ")
                        .split("\\w\\d{2}\\w\\d{2}"))
                .findFirst().orElse("")
                .replaceAll("(?<=\\d{4}).+", "");
    }

    private Query build(String fileName) {
        StringBuilder builder = new StringBuilder();
        AtomicReference<String> release = new AtomicReference<>("");

        List<String> args = List.of(fileName.split("\\s"));
        args.forEach(arg -> {
            if (StringUtils.isNumeric(arg) && arg.matches("\\d{4}")) {
                release.set(arg);
            } else {
                builder.append(arg).append(" ");
            }
        });

        return Query.builder()
                .title(builder.toString().trim())
                .released(release.get())
                .build();
    }

    private boolean verify(File file) {
        for (String format : formats) {
            if (file.getName().endsWith(format)) {
                return true;
            }
        }
        return false;
    }
}
