package com.google.common.base;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
final class Platform {
    private static final PatternCompiler patternCompiler = loadPatternCompiler();

    private Platform() {
    }

    static CharMatcher precomputeCharMatcher(CharMatcher matcher) {
        return matcher.precomputedInternal();
    }

    static <T extends Enum<T>> Optional<T> getEnumIfPresent(Class<T> enumClass, String value) {
        WeakReference<? extends Enum<?>> weakReference = Enums.getEnumConstants(enumClass).get(value);
        return weakReference == null ? Optional.absent() : Optional.fromNullable(enumClass.cast(weakReference.get()));
    }

    static String formatCompact4Digits(double value) {
        return String.format(Locale.ROOT, "%.4g", Double.valueOf(value));
    }

    static boolean stringIsNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    static String nullToEmpty(String string) {
        return string == null ? "" : string;
    }

    static String emptyToNull(String string) {
        if (stringIsNullOrEmpty(string)) {
            return null;
        }
        return string;
    }

    static String lenientFormat(String template, Object... args) {
        return Strings.lenientFormat(template, args);
    }

    static String stringValueOf(Object o) {
        return String.valueOf(o);
    }

    static CommonPattern compilePattern(String pattern) {
        Preconditions.checkNotNull(pattern);
        return patternCompiler.compile(pattern);
    }

    static boolean patternCompilerIsPcreLike() {
        return patternCompiler.isPcreLike();
    }

    private static PatternCompiler loadPatternCompiler() {
        return new JdkPatternCompiler();
    }

    private static final class JdkPatternCompiler implements PatternCompiler {
        @Override // com.google.common.base.PatternCompiler
        public boolean isPcreLike() {
            return true;
        }

        private JdkPatternCompiler() {
        }

        @Override // com.google.common.base.PatternCompiler
        public CommonPattern compile(String pattern) {
            return new JdkPattern(Pattern.compile(pattern));
        }
    }
}
