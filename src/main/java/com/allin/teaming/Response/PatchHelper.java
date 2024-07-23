package com.allin.teaming.Response;

import java.lang.reflect.Field;

public class PatchHelper {
    public static <T> void applyPatch(T target, T patch) {
        if (target == null || patch == null) {
            return;
        }

        Field[] fields = patch.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(patch);

                if (value != null) {
                    field.set(target, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error applying patch", e);
            }
        }
    }
}
