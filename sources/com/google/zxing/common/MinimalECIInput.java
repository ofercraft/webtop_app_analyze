package com.google.zxing.common;

import androidx.core.view.InputDeviceCompat;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class MinimalECIInput implements ECIInput {
    private static final int COST_PER_ECI = 3;
    private final int[] bytes;
    private final int fnc1;

    public MinimalECIInput(String str, Charset charset, int i) {
        this.fnc1 = i;
        ECIEncoderSet eCIEncoderSet = new ECIEncoderSet(str, charset, i);
        if (eCIEncoderSet.length() == 1) {
            this.bytes = new int[str.length()];
            for (int i2 = 0; i2 < this.bytes.length; i2++) {
                char cCharAt = str.charAt(i2);
                int[] iArr = this.bytes;
                if (cCharAt == i) {
                    cCharAt = 1000;
                }
                iArr[i2] = cCharAt;
            }
            return;
        }
        this.bytes = encodeMinimally(str, eCIEncoderSet, i);
    }

    public int getFNC1Character() {
        return this.fnc1;
    }

    @Override // com.google.zxing.common.ECIInput
    public int length() {
        return this.bytes.length;
    }

    @Override // com.google.zxing.common.ECIInput
    public boolean haveNCharacters(int i, int i2) {
        if ((i + i2) - 1 >= this.bytes.length) {
            return false;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            if (isECI(i + i3)) {
                return false;
            }
        }
        return true;
    }

    @Override // com.google.zxing.common.ECIInput
    public char charAt(int i) {
        if (i < 0 || i >= length()) {
            throw new IndexOutOfBoundsException("" + i);
        }
        if (isECI(i)) {
            throw new IllegalArgumentException("value at " + i + " is not a character but an ECI");
        }
        return (char) (isFNC1(i) ? this.fnc1 : this.bytes[i]);
    }

    @Override // com.google.zxing.common.ECIInput
    public CharSequence subSequence(int i, int i2) {
        if (i < 0 || i > i2 || i2 > length()) {
            throw new IndexOutOfBoundsException("" + i);
        }
        StringBuilder sb = new StringBuilder();
        while (i < i2) {
            if (isECI(i)) {
                throw new IllegalArgumentException("value at " + i + " is not a character but an ECI");
            }
            sb.append(charAt(i));
            i++;
        }
        return sb;
    }

    @Override // com.google.zxing.common.ECIInput
    public boolean isECI(int i) {
        if (i < 0 || i >= length()) {
            throw new IndexOutOfBoundsException("" + i);
        }
        int i2 = this.bytes[i];
        return i2 > 255 && i2 <= 999;
    }

    public boolean isFNC1(int i) {
        if (i < 0 || i >= length()) {
            throw new IndexOutOfBoundsException("" + i);
        }
        return this.bytes[i] == 1000;
    }

    @Override // com.google.zxing.common.ECIInput
    public int getECIValue(int i) {
        if (i < 0 || i >= length()) {
            throw new IndexOutOfBoundsException("" + i);
        }
        if (!isECI(i)) {
            throw new IllegalArgumentException("value at " + i + " is not an ECI but a character");
        }
        return this.bytes[i] + InputDeviceCompat.SOURCE_ANY;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            if (isECI(i)) {
                sb.append("ECI(");
                sb.append(getECIValue(i));
                sb.append(')');
            } else if (charAt(i) < 128) {
                sb.append('\'');
                sb.append(charAt(i));
                sb.append('\'');
            } else {
                sb.append((int) charAt(i));
            }
        }
        return sb.toString();
    }

    static void addEdge(InputEdge[][] inputEdgeArr, int i, InputEdge inputEdge) {
        if (inputEdgeArr[i][inputEdge.encoderIndex] == null || inputEdgeArr[i][inputEdge.encoderIndex].cachedTotalSize > inputEdge.cachedTotalSize) {
            inputEdgeArr[i][inputEdge.encoderIndex] = inputEdge;
        }
    }

    static void addEdges(String str, ECIEncoderSet eCIEncoderSet, InputEdge[][] inputEdgeArr, int i, InputEdge inputEdge, int i2) {
        int i3;
        int priorityEncoderIndex;
        ECIEncoderSet eCIEncoderSet2;
        InputEdge inputEdge2;
        int i4;
        char cCharAt = str.charAt(i);
        int length = eCIEncoderSet.length();
        if (eCIEncoderSet.getPriorityEncoderIndex() < 0 || !(cCharAt == i2 || eCIEncoderSet.canEncode(cCharAt, eCIEncoderSet.getPriorityEncoderIndex()))) {
            i3 = length;
            priorityEncoderIndex = 0;
        } else {
            priorityEncoderIndex = eCIEncoderSet.getPriorityEncoderIndex();
            i3 = priorityEncoderIndex + 1;
        }
        int i5 = priorityEncoderIndex;
        while (i5 < i3) {
            if (cCharAt == i2 || eCIEncoderSet.canEncode(cCharAt, i5)) {
                eCIEncoderSet2 = eCIEncoderSet;
                inputEdge2 = inputEdge;
                i4 = i2;
                addEdge(inputEdgeArr, i + 1, new InputEdge(cCharAt, eCIEncoderSet2, i5, inputEdge2, i4));
            } else {
                eCIEncoderSet2 = eCIEncoderSet;
                inputEdge2 = inputEdge;
                i4 = i2;
            }
            i5++;
            eCIEncoderSet = eCIEncoderSet2;
            inputEdge = inputEdge2;
            i2 = i4;
        }
    }

    static int[] encodeMinimally(String str, ECIEncoderSet eCIEncoderSet, int i) {
        int length = str.length();
        InputEdge[][] inputEdgeArr = (InputEdge[][]) Array.newInstance((Class<?>) InputEdge.class, length + 1, eCIEncoderSet.length());
        addEdges(str, eCIEncoderSet, inputEdgeArr, 0, null, i);
        for (int i2 = 1; i2 <= length; i2++) {
            for (int i3 = 0; i3 < eCIEncoderSet.length(); i3++) {
                InputEdge inputEdge = inputEdgeArr[i2][i3];
                if (inputEdge != null && i2 < length) {
                    addEdges(str, eCIEncoderSet, inputEdgeArr, i2, inputEdge, i);
                }
            }
            for (int i4 = 0; i4 < eCIEncoderSet.length(); i4++) {
                inputEdgeArr[i2 - 1][i4] = null;
            }
        }
        int i5 = -1;
        int i6 = Integer.MAX_VALUE;
        for (int i7 = 0; i7 < eCIEncoderSet.length(); i7++) {
            InputEdge inputEdge2 = inputEdgeArr[length][i7];
            if (inputEdge2 != null && inputEdge2.cachedTotalSize < i6) {
                i6 = inputEdge2.cachedTotalSize;
                i5 = i7;
            }
        }
        if (i5 < 0) {
            throw new IllegalStateException("Failed to encode \"" + str + "\"");
        }
        ArrayList arrayList = new ArrayList();
        for (InputEdge inputEdge3 = inputEdgeArr[length][i5]; inputEdge3 != null; inputEdge3 = inputEdge3.previous) {
            if (inputEdge3.isFNC1()) {
                arrayList.add(0, 1000);
            } else {
                byte[] bArrEncode = eCIEncoderSet.encode(inputEdge3.c, inputEdge3.encoderIndex);
                for (int length2 = bArrEncode.length - 1; length2 >= 0; length2--) {
                    arrayList.add(0, Integer.valueOf(bArrEncode[length2] & 255));
                }
            }
            if ((inputEdge3.previous == null ? 0 : inputEdge3.previous.encoderIndex) != inputEdge3.encoderIndex) {
                arrayList.add(0, Integer.valueOf(eCIEncoderSet.getECIValue(inputEdge3.encoderIndex) + 256));
            }
        }
        int size = arrayList.size();
        int[] iArr = new int[size];
        for (int i8 = 0; i8 < size; i8++) {
            iArr[i8] = ((Integer) arrayList.get(i8)).intValue();
        }
        return iArr;
    }

    private static final class InputEdge {
        private final char c;
        private final int cachedTotalSize;
        private final int encoderIndex;
        private final InputEdge previous;

        private InputEdge(char c, ECIEncoderSet eCIEncoderSet, int i, InputEdge inputEdge, int i2) {
            char c2 = c == i2 ? (char) 1000 : c;
            this.c = c2;
            this.encoderIndex = i;
            this.previous = inputEdge;
            int length = c2 == 1000 ? 1 : eCIEncoderSet.encode(c, i).length;
            length = (inputEdge == null ? 0 : inputEdge.encoderIndex) != i ? length + 3 : length;
            this.cachedTotalSize = inputEdge != null ? length + inputEdge.cachedTotalSize : length;
        }

        boolean isFNC1() {
            return this.c == 1000;
        }
    }
}
