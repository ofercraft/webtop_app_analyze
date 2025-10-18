package com.google.common.base;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/* loaded from: classes.dex */
public final class MoreObjects {
    public static <T> T firstNonNull(T first, T second) {
        if (first != null) {
            return first;
        }
        if (second != null) {
            return second;
        }
        throw new NullPointerException("Both parameters are null");
    }

    public static ToStringHelper toStringHelper(Object self) {
        return new ToStringHelper(self.getClass().getSimpleName());
    }

    public static ToStringHelper toStringHelper(Class<?> clazz) {
        return new ToStringHelper(clazz.getSimpleName());
    }

    public static ToStringHelper toStringHelper(String className) {
        return new ToStringHelper(className);
    }

    public static final class ToStringHelper {
        private final String className;
        private final ValueHolder holderHead;
        private ValueHolder holderTail;
        private boolean omitEmptyValues;
        private boolean omitNullValues;

        private ToStringHelper(String className) {
            ValueHolder valueHolder = new ValueHolder();
            this.holderHead = valueHolder;
            this.holderTail = valueHolder;
            this.omitNullValues = false;
            this.omitEmptyValues = false;
            this.className = (String) Preconditions.checkNotNull(className);
        }

        public ToStringHelper omitNullValues() {
            this.omitNullValues = true;
            return this;
        }

        public ToStringHelper omitEmptyValues() {
            this.omitEmptyValues = true;
            return this;
        }

        public ToStringHelper add(String name, Object value) {
            return addHolder(name, value);
        }

        public ToStringHelper add(String name, boolean value) {
            return addUnconditionalHolder(name, String.valueOf(value));
        }

        public ToStringHelper add(String name, char value) {
            return addUnconditionalHolder(name, String.valueOf(value));
        }

        public ToStringHelper add(String name, double value) {
            return addUnconditionalHolder(name, String.valueOf(value));
        }

        public ToStringHelper add(String name, float value) {
            return addUnconditionalHolder(name, String.valueOf(value));
        }

        public ToStringHelper add(String name, int value) {
            return addUnconditionalHolder(name, String.valueOf(value));
        }

        public ToStringHelper add(String name, long value) {
            return addUnconditionalHolder(name, String.valueOf(value));
        }

        public ToStringHelper addValue(Object value) {
            return addHolder(value);
        }

        public ToStringHelper addValue(boolean value) {
            return addUnconditionalHolder(String.valueOf(value));
        }

        public ToStringHelper addValue(char value) {
            return addUnconditionalHolder(String.valueOf(value));
        }

        public ToStringHelper addValue(double value) {
            return addUnconditionalHolder(String.valueOf(value));
        }

        public ToStringHelper addValue(float value) {
            return addUnconditionalHolder(String.valueOf(value));
        }

        public ToStringHelper addValue(int value) {
            return addUnconditionalHolder(String.valueOf(value));
        }

        public ToStringHelper addValue(long value) {
            return addUnconditionalHolder(String.valueOf(value));
        }

        private static boolean isEmpty(Object value) {
            if (value instanceof CharSequence) {
                return ((CharSequence) value).length() == 0;
            }
            if (value instanceof Collection) {
                return ((Collection) value).isEmpty();
            }
            if (value instanceof Map) {
                return ((Map) value).isEmpty();
            }
            if (value instanceof Optional) {
                return !((Optional) value).isPresent();
            }
            return value.getClass().isArray() && Array.getLength(value) == 0;
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x0032  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.lang.String toString() {
            /*
                r6 = this;
                boolean r0 = r6.omitNullValues
                boolean r1 = r6.omitEmptyValues
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r3 = 32
                r2.<init>(r3)
                java.lang.String r3 = r6.className
                java.lang.StringBuilder r2 = r2.append(r3)
                r3 = 123(0x7b, float:1.72E-43)
                java.lang.StringBuilder r2 = r2.append(r3)
                com.google.common.base.MoreObjects$ToStringHelper$ValueHolder r6 = r6.holderHead
                com.google.common.base.MoreObjects$ToStringHelper$ValueHolder r6 = r6.next
                java.lang.String r3 = ""
            L1d:
                if (r6 == 0) goto L6a
                java.lang.Object r4 = r6.value
                boolean r5 = r6 instanceof com.google.common.base.MoreObjects.ToStringHelper.UnconditionalValueHolder
                if (r5 != 0) goto L32
                if (r4 != 0) goto L2a
                if (r0 != 0) goto L67
                goto L32
            L2a:
                if (r1 == 0) goto L32
                boolean r5 = isEmpty(r4)
                if (r5 != 0) goto L67
            L32:
                r2.append(r3)
                java.lang.String r3 = r6.name
                if (r3 == 0) goto L44
                java.lang.String r3 = r6.name
                java.lang.StringBuilder r3 = r2.append(r3)
                r5 = 61
                r3.append(r5)
            L44:
                if (r4 == 0) goto L62
                java.lang.Class r3 = r4.getClass()
                boolean r3 = r3.isArray()
                if (r3 == 0) goto L62
                java.lang.Object[] r3 = new java.lang.Object[]{r4}
                java.lang.String r3 = java.util.Arrays.deepToString(r3)
                int r4 = r3.length()
                r5 = 1
                int r4 = r4 - r5
                r2.append(r3, r5, r4)
                goto L65
            L62:
                r2.append(r4)
            L65:
                java.lang.String r3 = ", "
            L67:
                com.google.common.base.MoreObjects$ToStringHelper$ValueHolder r6 = r6.next
                goto L1d
            L6a:
                r6 = 125(0x7d, float:1.75E-43)
                java.lang.StringBuilder r6 = r2.append(r6)
                java.lang.String r6 = r6.toString()
                return r6
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.base.MoreObjects.ToStringHelper.toString():java.lang.String");
        }

        private ValueHolder addHolder() {
            ValueHolder valueHolder = new ValueHolder();
            this.holderTail.next = valueHolder;
            this.holderTail = valueHolder;
            return valueHolder;
        }

        private ToStringHelper addHolder(Object value) {
            addHolder().value = value;
            return this;
        }

        private ToStringHelper addHolder(String name, Object value) {
            ValueHolder valueHolderAddHolder = addHolder();
            valueHolderAddHolder.value = value;
            valueHolderAddHolder.name = (String) Preconditions.checkNotNull(name);
            return this;
        }

        private UnconditionalValueHolder addUnconditionalHolder() {
            UnconditionalValueHolder unconditionalValueHolder = new UnconditionalValueHolder();
            this.holderTail.next = unconditionalValueHolder;
            this.holderTail = unconditionalValueHolder;
            return unconditionalValueHolder;
        }

        private ToStringHelper addUnconditionalHolder(Object value) {
            addUnconditionalHolder().value = value;
            return this;
        }

        private ToStringHelper addUnconditionalHolder(String name, Object value) {
            UnconditionalValueHolder unconditionalValueHolderAddUnconditionalHolder = addUnconditionalHolder();
            unconditionalValueHolderAddUnconditionalHolder.value = value;
            unconditionalValueHolderAddUnconditionalHolder.name = (String) Preconditions.checkNotNull(name);
            return this;
        }

        static class ValueHolder {
            String name;
            ValueHolder next;
            Object value;

            ValueHolder() {
            }
        }

        private static final class UnconditionalValueHolder extends ValueHolder {
            private UnconditionalValueHolder() {
            }
        }
    }

    private MoreObjects() {
    }
}
