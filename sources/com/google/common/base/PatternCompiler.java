package com.google.common.base;

/* loaded from: classes.dex */
interface PatternCompiler {
    CommonPattern compile(String pattern);

    boolean isPcreLike();
}
