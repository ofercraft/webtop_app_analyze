package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import dev.skomlach.biometric.compat.engine.internal.face.oppo.OppoFaceUnlockModule;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes2.dex */
public final class Code128Writer extends OneDimensionalCodeWriter {
    private static final int CODE_CODE_A = 101;
    private static final int CODE_CODE_B = 100;
    private static final int CODE_CODE_C = 99;
    private static final int CODE_FNC_1 = 102;
    private static final int CODE_FNC_2 = 97;
    private static final int CODE_FNC_3 = 96;
    private static final int CODE_FNC_4_A = 101;
    private static final int CODE_FNC_4_B = 100;
    private static final int CODE_START_A = 103;
    private static final int CODE_START_B = 104;
    private static final int CODE_START_C = 105;
    private static final int CODE_STOP = 106;
    private static final char ESCAPE_FNC_1 = 241;
    private static final char ESCAPE_FNC_2 = 242;
    private static final char ESCAPE_FNC_3 = 243;
    private static final char ESCAPE_FNC_4 = 244;

    private enum CType {
        UNCODABLE,
        ONE_DIGIT,
        TWO_DIGITS,
        FNC_1
    }

    @Override // com.google.zxing.oned.OneDimensionalCodeWriter
    protected Collection<BarcodeFormat> getSupportedWriteFormats() {
        return Collections.singleton(BarcodeFormat.CODE_128);
    }

    @Override // com.google.zxing.oned.OneDimensionalCodeWriter
    public boolean[] encode(String str) {
        return encode(str, null);
    }

    @Override // com.google.zxing.oned.OneDimensionalCodeWriter
    public boolean[] encode(String str, Map<EncodeHintType, ?> map) {
        return (map != null && map.containsKey(EncodeHintType.CODE128_COMPACT) && Boolean.parseBoolean(map.get(EncodeHintType.CODE128_COMPACT).toString())) ? new MinimalEncoder(null).encode(str) : encodeFast(str, check(str, map));
    }

    private static int check(String str, Map<EncodeHintType, ?> map) {
        String string;
        if (map != null && map.containsKey(EncodeHintType.FORCE_CODE_SET)) {
            string = map.get(EncodeHintType.FORCE_CODE_SET).toString();
            string.hashCode();
            switch (string) {
                case "A":
                    break;
                case "B":
                    break;
                case "C":
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported code set hint: " + string);
            }
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = str.charAt(i);
            switch (cCharAt) {
                case 241:
                case 242:
                case 243:
                case 244:
                    break;
                default:
                    if (cCharAt > 127) {
                        throw new IllegalArgumentException("Bad character in input: ASCII value=" + ((int) cCharAt));
                    }
                    break;
            }
            /*  JADX ERROR: Method code generation error
                java.lang.NullPointerException: Switch insn not found in header
                	at java.base/java.util.Objects.requireNonNull(Unknown Source)
                	at jadx.core.codegen.RegionGen.makeSwitch(RegionGen.java:246)
                	at jadx.core.dex.regions.SwitchRegion.generate(SwitchRegion.java:84)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:66)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:66)
                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:83)
                	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:195)
                	at jadx.core.dex.regions.loops.LoopRegion.generate(LoopRegion.java:171)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:66)
                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:66)
                	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:298)
                	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:277)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:410)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:335)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:301)
                	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(Unknown Source)
                	at java.base/java.util.ArrayList.forEach(Unknown Source)
                	at java.base/java.util.stream.SortedOps$RefSortingSink.end(Unknown Source)
                	at java.base/java.util.stream.Sink$ChainedReference.end(Unknown Source)
                	at java.base/java.util.stream.ReferencePipeline$7$1FlatMap.end(Unknown Source)
                	at java.base/java.util.stream.AbstractPipeline.copyInto(Unknown Source)
                	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(Unknown Source)
                	at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateSequential(Unknown Source)
                	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(Unknown Source)
                	at java.base/java.util.stream.AbstractPipeline.evaluate(Unknown Source)
                	at java.base/java.util.stream.ReferencePipeline.forEach(Unknown Source)
                	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:297)
                	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:286)
                	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:270)
                	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:161)
                	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:103)
                	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:45)
                	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:34)
                	at jadx.core.codegen.CodeGen.generate(CodeGen.java:22)
                	at jadx.core.ProcessClass.process(ProcessClass.java:79)
                	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
                	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:403)
                	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:391)
                	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:341)
                */
            /*
                Method dump skipped, instructions count: 284
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.oned.Code128Writer.check(java.lang.String, java.util.Map):int");
        }

        private static boolean[] encodeFast(String str, int i) throws NumberFormatException {
            int length = str.length();
            ArrayList arrayList = new ArrayList();
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            int i5 = 1;
            while (i2 < length) {
                int iChooseCode = i == -1 ? chooseCode(str, i2, i4) : i;
                int iCharAt = 100;
                if (iChooseCode == i4) {
                    switch (str.charAt(i2)) {
                        case 241:
                            iCharAt = 102;
                            break;
                        case 242:
                            iCharAt = CODE_FNC_2;
                            break;
                        case 243:
                            iCharAt = CODE_FNC_3;
                            break;
                        case 244:
                            if (i4 == 101) {
                                iCharAt = 101;
                                break;
                            }
                            break;
                        default:
                            if (i4 == 100) {
                                iCharAt = str.charAt(i2) - ' ';
                                break;
                            } else if (i4 == 101) {
                                char cCharAt = str.charAt(i2);
                                iCharAt = cCharAt - ' ';
                                if (iCharAt < 0) {
                                    iCharAt = cCharAt + '@';
                                    break;
                                }
                            } else {
                                int i6 = i2 + 1;
                                if (i6 == length) {
                                    throw new IllegalArgumentException("Bad number of characters for digit only encoding.");
                                }
                                iCharAt = Integer.parseInt(str.substring(i2, i2 + 2));
                                i2 = i6;
                                break;
                            }
                            break;
                    }
                    i2++;
                } else {
                    if (i4 == 0) {
                        iCharAt = iChooseCode != 100 ? iChooseCode != 101 ? CODE_START_C : 103 : 104;
                    } else {
                        iCharAt = iChooseCode;
                    }
                    i4 = iChooseCode;
                }
                arrayList.add(Code128Reader.CODE_PATTERNS[iCharAt]);
                i3 += iCharAt * i5;
                if (i2 != 0) {
                    i5++;
                }
            }
            return produceResult(arrayList, i3);
        }

        static boolean[] produceResult(Collection<int[]> collection, int i) {
            int i2 = i % 103;
            if (i2 < 0) {
                throw new IllegalArgumentException("Unable to compute a valid input checksum");
            }
            collection.add(Code128Reader.CODE_PATTERNS[i2]);
            collection.add(Code128Reader.CODE_PATTERNS[CODE_STOP]);
            int iAppendPattern = 0;
            int i3 = 0;
            for (int[] iArr : collection) {
                for (int i4 : iArr) {
                    i3 += i4;
                }
            }
            boolean[] zArr = new boolean[i3];
            Iterator<int[]> it = collection.iterator();
            while (it.hasNext()) {
                iAppendPattern += appendPattern(zArr, iAppendPattern, it.next(), true);
            }
            return zArr;
        }

        private static CType findCType(CharSequence charSequence, int i) {
            int length = charSequence.length();
            if (i >= length) {
                return CType.UNCODABLE;
            }
            char cCharAt = charSequence.charAt(i);
            if (cCharAt == 241) {
                return CType.FNC_1;
            }
            if (cCharAt < '0' || cCharAt > '9') {
                return CType.UNCODABLE;
            }
            int i2 = i + 1;
            if (i2 >= length) {
                return CType.ONE_DIGIT;
            }
            char cCharAt2 = charSequence.charAt(i2);
            if (cCharAt2 < '0' || cCharAt2 > '9') {
                return CType.ONE_DIGIT;
            }
            return CType.TWO_DIGITS;
        }

        private static int chooseCode(CharSequence charSequence, int i, int i2) {
            CType cTypeFindCType;
            CType cTypeFindCType2;
            char cCharAt;
            CType cTypeFindCType3 = findCType(charSequence, i);
            if (cTypeFindCType3 == CType.ONE_DIGIT) {
                if (i2 == 101) {
                    return OppoFaceUnlockModule.FACE_ACQUIRED_NO_FACE;
                }
                return 100;
            }
            if (cTypeFindCType3 == CType.UNCODABLE) {
                if (i >= charSequence.length() || ((cCharAt = charSequence.charAt(i)) >= ' ' && (i2 != 101 || (cCharAt >= CODE_FNC_3 && (cCharAt < 241 || cCharAt > 244))))) {
                    return 100;
                }
                return OppoFaceUnlockModule.FACE_ACQUIRED_NO_FACE;
            }
            if (i2 == 101 && cTypeFindCType3 == CType.FNC_1) {
                return OppoFaceUnlockModule.FACE_ACQUIRED_NO_FACE;
            }
            if (i2 == CODE_CODE_C) {
                return CODE_CODE_C;
            }
            if (i2 == 100) {
                if (cTypeFindCType3 == CType.FNC_1 || (cTypeFindCType = findCType(charSequence, i + 2)) == CType.UNCODABLE || cTypeFindCType == CType.ONE_DIGIT) {
                    return 100;
                }
                if (cTypeFindCType == CType.FNC_1) {
                    if (findCType(charSequence, i + 3) == CType.TWO_DIGITS) {
                        return CODE_CODE_C;
                    }
                    return 100;
                }
                int i3 = i + 4;
                while (true) {
                    cTypeFindCType2 = findCType(charSequence, i3);
                    if (cTypeFindCType2 != CType.TWO_DIGITS) {
                        break;
                    }
                    i3 += 2;
                }
                if (cTypeFindCType2 == CType.ONE_DIGIT) {
                    return 100;
                }
                return CODE_CODE_C;
            }
            if (cTypeFindCType3 == CType.FNC_1) {
                cTypeFindCType3 = findCType(charSequence, i + 1);
            }
            if (cTypeFindCType3 == CType.TWO_DIGITS) {
                return CODE_CODE_C;
            }
            return 100;
        }

        private static final class MinimalEncoder {
            static final /* synthetic */ boolean $assertionsDisabled = false;
            static final String A = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001fÿ";
            static final String B = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007fÿ";
            private static final int CODE_SHIFT = 98;
            private int[][] memoizedCost;
            private Latch[][] minPath;

            private enum Charset {
                A,
                B,
                C,
                NONE
            }

            private enum Latch {
                A,
                B,
                C,
                SHIFT,
                NONE
            }

            private static boolean isDigit(char c) {
                return c >= '0' && c <= '9';
            }

            private MinimalEncoder() {
            }

            /* synthetic */ MinimalEncoder(AnonymousClass1 anonymousClass1) {
                this();
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* JADX WARN: Removed duplicated region for block: B:30:0x00a2  */
            /* JADX WARN: Removed duplicated region for block: B:36:0x00c1  */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public boolean[] encode(java.lang.String r18) {
                /*
                    Method dump skipped, instructions count: 292
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.oned.Code128Writer.MinimalEncoder.encode(java.lang.String):boolean[]");
            }

            private static void addPattern(Collection<int[]> collection, int i, int[] iArr, int[] iArr2, int i2) {
                collection.add(Code128Reader.CODE_PATTERNS[i]);
                if (i2 != 0) {
                    iArr2[0] = iArr2[0] + 1;
                }
                iArr[0] = iArr[0] + (i * iArr2[0]);
            }

            private boolean canEncode(CharSequence charSequence, Charset charset, int i) {
                int i2;
                char cCharAt = charSequence.charAt(i);
                int i3 = AnonymousClass1.$SwitchMap$com$google$zxing$oned$Code128Writer$MinimalEncoder$Charset[charset.ordinal()];
                if (i3 == 1) {
                    return cCharAt == 241 || cCharAt == 242 || cCharAt == 243 || cCharAt == 244 || A.indexOf(cCharAt) >= 0;
                }
                if (i3 == 2) {
                    return cCharAt == 241 || cCharAt == 242 || cCharAt == 243 || cCharAt == 244 || B.indexOf(cCharAt) >= 0;
                }
                if (i3 != 3) {
                    return false;
                }
                return cCharAt == 241 || ((i2 = i + 1) < charSequence.length() && isDigit(cCharAt) && isDigit(charSequence.charAt(i2)));
            }

            private int encode(CharSequence charSequence, Charset charset, int i) {
                int iEncode;
                int iEncode2;
                int i2 = this.memoizedCost[charset.ordinal()][i];
                if (i2 > 0) {
                    return i2;
                }
                Latch latch = Latch.NONE;
                int i3 = i + 1;
                boolean z = i3 >= charSequence.length();
                Charset[] charsetArr = {Charset.A, Charset.B};
                int i4 = Integer.MAX_VALUE;
                for (int i5 = 0; i5 <= 1; i5++) {
                    if (canEncode(charSequence, charsetArr[i5], i)) {
                        Latch latchValueOf = Latch.NONE;
                        Charset charset2 = charsetArr[i5];
                        if (charset != charset2) {
                            latchValueOf = Latch.valueOf(charset2.toString());
                            iEncode2 = 2;
                        } else {
                            iEncode2 = 1;
                        }
                        if (!z) {
                            iEncode2 += encode(charSequence, charsetArr[i5], i3);
                        }
                        if (iEncode2 < i4) {
                            latch = latchValueOf;
                            i4 = iEncode2;
                        }
                        if (charset == charsetArr[(i5 + 1) % 2]) {
                            Latch latch2 = Latch.SHIFT;
                            int iEncode3 = !z ? encode(charSequence, charset, i3) + 2 : 2;
                            if (iEncode3 < i4) {
                                latch = latch2;
                                i4 = iEncode3;
                            }
                        }
                    }
                }
                if (canEncode(charSequence, Charset.C, i)) {
                    Latch latch3 = Latch.NONE;
                    if (charset != Charset.C) {
                        latch3 = Latch.C;
                        iEncode = 2;
                    } else {
                        iEncode = 1;
                    }
                    int i6 = (charSequence.charAt(i) != 241 ? 2 : 1) + i;
                    if (i6 < charSequence.length()) {
                        iEncode += encode(charSequence, Charset.C, i6);
                    }
                    if (iEncode < i4) {
                        latch = latch3;
                        i4 = iEncode;
                    }
                }
                if (i4 == Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("Bad character in input: ASCII value=" + ((int) charSequence.charAt(i)));
                }
                this.memoizedCost[charset.ordinal()][i] = i4;
                this.minPath[charset.ordinal()][i] = latch;
                return i4;
            }
        }

        /* renamed from: com.google.zxing.oned.Code128Writer$1, reason: invalid class name */
        static /* synthetic */ class AnonymousClass1 {
            static final /* synthetic */ int[] $SwitchMap$com$google$zxing$oned$Code128Writer$MinimalEncoder$Charset;
            static final /* synthetic */ int[] $SwitchMap$com$google$zxing$oned$Code128Writer$MinimalEncoder$Latch;

            static {
                int[] iArr = new int[MinimalEncoder.Charset.values().length];
                $SwitchMap$com$google$zxing$oned$Code128Writer$MinimalEncoder$Charset = iArr;
                try {
                    iArr[MinimalEncoder.Charset.A.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    $SwitchMap$com$google$zxing$oned$Code128Writer$MinimalEncoder$Charset[MinimalEncoder.Charset.B.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                try {
                    $SwitchMap$com$google$zxing$oned$Code128Writer$MinimalEncoder$Charset[MinimalEncoder.Charset.C.ordinal()] = 3;
                } catch (NoSuchFieldError unused3) {
                }
                int[] iArr2 = new int[MinimalEncoder.Latch.values().length];
                $SwitchMap$com$google$zxing$oned$Code128Writer$MinimalEncoder$Latch = iArr2;
                try {
                    iArr2[MinimalEncoder.Latch.A.ordinal()] = 1;
                } catch (NoSuchFieldError unused4) {
                }
                try {
                    $SwitchMap$com$google$zxing$oned$Code128Writer$MinimalEncoder$Latch[MinimalEncoder.Latch.B.ordinal()] = 2;
                } catch (NoSuchFieldError unused5) {
                }
                try {
                    $SwitchMap$com$google$zxing$oned$Code128Writer$MinimalEncoder$Latch[MinimalEncoder.Latch.C.ordinal()] = 3;
                } catch (NoSuchFieldError unused6) {
                }
                try {
                    $SwitchMap$com$google$zxing$oned$Code128Writer$MinimalEncoder$Latch[MinimalEncoder.Latch.SHIFT.ordinal()] = 4;
                } catch (NoSuchFieldError unused7) {
                }
            }
        }
    }
