package io.redspace.ironsspellbooks.util;

import java.util.List;

public final class ScrollSchoolModels {
    private static final List<String> SCHOOL_ORDER = List.of(
            "blood",
            "eldritch",
            "ender",
            "evocation",
            "fire",
            "holy",
            "ice",
            "lightning",
            "nature"
    );

    private ScrollSchoolModels() {
    }

    public static List<String> schoolOrder() {
        return SCHOOL_ORDER;
    }

    public static float predicateValueForSchool(String schoolPath) {
        int index = SCHOOL_ORDER.indexOf(schoolPath);
        return index < 0 ? 0f : (index + 1);
    }
}
