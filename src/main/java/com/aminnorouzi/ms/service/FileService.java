package com.aminnorouzi.ms.service;

import com.aminnorouzi.ms.model.movie.Query;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@AllArgsConstructor
@Service
public class FileService {

    @Value("#{'${movie.validation.tags}'.split(',')}")
    private Set<String> tags;

    @Value("#{'${movie.validation.formats}'.split(',')}")
    private Set<String> formats;

    public Set<Query> convertFilesToQueries(List<File> files) {
        Set<Query> queries = new HashSet<>();

        Set<String> names = convertFilesToNames(files);
        names.forEach(name -> queries.add(generateQuery(name)));

        return queries;
    }

    public Set<String> convertFilesToNames(List<File> files) {
        Set<String> result = new HashSet<>();
        for (File file : files) {
            String name = file.getName();

            for (String format : formats) {
                name = name.replace(format, "");
            }

            for (String tag : tags) {
                if (name.contains(tag)) {
                    name = name.replace(tag, "");
                }
            }

            name = Arrays.stream(name.replaceAll("\\.", " ").trim()
                            .replace("_", " ").trim()
                            .replace("-", " ").trim()
                            .replaceAll(" +", " ")
                            .split("\\w\\d{2}\\w\\d{2}"))
                    .findFirst().get()
                    .replaceAll("(?<=\\d{4}).+", "");

            result.add(name);
        }

        return result;
    }

    private Query generateQuery(String fileName) {
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
                .release(release.get())
                .build();
    }

    public boolean isValid(File file) {
        for (String format : formats) {
            if (file.getName().endsWith(format)) {
                return true;
            }
        }
        return false;
    }

}
