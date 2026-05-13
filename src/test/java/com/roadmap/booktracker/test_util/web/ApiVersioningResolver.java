package com.roadmap.booktracker.test_util.web;

import com.roadmap.booktracker.controller.annotation.ApiVersion;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class ApiVersioningResolver {
    public static String resolve(Class<?> controllerClass, String methodName, String path) {
        String version = Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(m -> m.getName().equals(methodName))
                .map(m -> AnnotatedElementUtils.findMergedAnnotation(m, ApiVersion.class))
                .filter(Objects::nonNull)
                .map(ApiVersion::value)
                .findFirst()
                .orElseGet(() -> Optional.ofNullable(
                        AnnotatedElementUtils.findMergedAnnotation(controllerClass, ApiVersion.class)
                ).map(ApiVersion::value).orElse(null));

        return version != null ? "/api/" + version + path : path;
    }
}
