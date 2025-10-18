package com.google.zxing.client.result;

import com.google.zxing.Result;

/* loaded from: classes2.dex */
public final class WifiResultParser extends ResultParser {
    @Override // com.google.zxing.client.result.ResultParser
    public WifiParsedResult parse(Result result) {
        String strSubstring;
        String strMatchSinglePrefixedField;
        String str;
        boolean z;
        String massagedText = getMassagedText(result);
        if (!massagedText.startsWith("WIFI:") || (strMatchSinglePrefixedField = matchSinglePrefixedField("S:", (strSubstring = massagedText.substring("WIFI:".length())), ';', false)) == null || strMatchSinglePrefixedField.isEmpty()) {
            return null;
        }
        String strMatchSinglePrefixedField2 = matchSinglePrefixedField("P:", strSubstring, ';', false);
        String strMatchSinglePrefixedField3 = matchSinglePrefixedField("T:", strSubstring, ';', false);
        if (strMatchSinglePrefixedField3 == null) {
            strMatchSinglePrefixedField3 = "nopass";
        }
        String str2 = strMatchSinglePrefixedField3;
        String strMatchSinglePrefixedField4 = matchSinglePrefixedField("PH2:", strSubstring, ';', false);
        String strMatchSinglePrefixedField5 = matchSinglePrefixedField("H:", strSubstring, ';', false);
        if (strMatchSinglePrefixedField5 == null) {
            str = strMatchSinglePrefixedField4;
        } else {
            if (strMatchSinglePrefixedField4 != null || "true".equalsIgnoreCase(strMatchSinglePrefixedField5) || "false".equalsIgnoreCase(strMatchSinglePrefixedField5)) {
                str = strMatchSinglePrefixedField4;
                z = Boolean.parseBoolean(strMatchSinglePrefixedField5);
                return new WifiParsedResult(str2, strMatchSinglePrefixedField, strMatchSinglePrefixedField2, z, matchSinglePrefixedField("I:", strSubstring, ';', false), matchSinglePrefixedField("A:", strSubstring, ';', false), matchSinglePrefixedField("E:", strSubstring, ';', false), str);
            }
            str = strMatchSinglePrefixedField5;
        }
        z = false;
        return new WifiParsedResult(str2, strMatchSinglePrefixedField, strMatchSinglePrefixedField2, z, matchSinglePrefixedField("I:", strSubstring, ';', false), matchSinglePrefixedField("A:", strSubstring, ';', false), matchSinglePrefixedField("E:", strSubstring, ';', false), str);
    }
}
