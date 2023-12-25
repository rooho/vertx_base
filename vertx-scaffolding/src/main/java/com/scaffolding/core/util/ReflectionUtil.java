package com.scaffolding.core.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

/**
 * 扫描包
 */
public final class ReflectionUtil {

    public static Reflections getReflections(String packageAddress) {
        List<String> packageAddressList;
        if (packageAddress.contains(",")) {
            packageAddressList = Arrays.asList(packageAddress.split(","));
        } else if (packageAddress.contains(";")) {
            packageAddressList = Arrays.asList(packageAddress.split(";"));
        } else {
            packageAddressList = Collections.singletonList(packageAddress);
        }
        return getReflections(packageAddressList);
    }

    public static Reflections getReflections(List<String> packageAddresses) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        FilterBuilder filterBuilder = new FilterBuilder();
        packageAddresses.forEach(str -> {
            configurationBuilder.addUrls(ClasspathHelper.forPackage(str.trim()));
            filterBuilder.includePackage(str.trim());
        });
        configurationBuilder.filterInputsBy(filterBuilder);
        return new Reflections(configurationBuilder);
    }
}
