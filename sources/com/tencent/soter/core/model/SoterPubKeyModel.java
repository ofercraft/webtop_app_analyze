package com.tencent.soter.core.model;

import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class SoterPubKeyModel {
    public static final String JSON_KEY_CERTS = "certs";
    public static final String JSON_KEY_COUNTER = "counter";
    public static final String JSON_KEY_CPU_ID = "cpu_id";
    public static final String JSON_KEY_PUBLIC = "pub_key";
    public static final String JSON_KEY_UID = "uid";
    private static final String TAG = "Soter.SoterPubKeyModel";
    private ArrayList<String> certs;
    private long counter;
    private String cpu_id;
    private String pub_key_in_x509;
    private String rawJson;
    private String signature;
    private int uid;

    public String toString() {
        return "SoterPubKeyModel{counter=" + this.counter + ", uid=" + this.uid + ", cpu_id='" + this.cpu_id + "', pub_key_in_x509='" + this.pub_key_in_x509 + "', rawJson='" + this.rawJson + "', signature='" + this.signature + "'}";
    }

    public SoterPubKeyModel(long j, int i, String str, String str2, String str3) {
        this.rawJson = "";
        this.certs = null;
        this.counter = j;
        this.uid = i;
        this.cpu_id = str;
        this.pub_key_in_x509 = str2;
        this.signature = str3;
    }

    public SoterPubKeyModel(String str, String str2) throws JSONException {
        this.counter = -1L;
        this.uid = -1;
        this.cpu_id = "";
        this.pub_key_in_x509 = "";
        this.rawJson = "";
        this.certs = null;
        this.signature = "";
        setRawJson(str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has(JSON_KEY_CERTS)) {
                JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray(JSON_KEY_CERTS);
                if (jSONArrayOptJSONArray.length() < 2) {
                    SLogger.e(TAG, "certificates train not enough", new Object[0]);
                }
                SLogger.i(TAG, "certs size: [%d]", Integer.valueOf(jSONArrayOptJSONArray.length()));
                this.certs = new ArrayList<>();
                for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                    this.certs.add(jSONArrayOptJSONArray.getString(i));
                }
                loadDeviceInfo((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(this.certs.get(0).getBytes())));
                jSONObject.put("cpu_id", this.cpu_id);
                jSONObject.put("uid", this.uid);
                jSONObject.put("counter", this.counter);
                setRawJson(jSONObject.toString());
            } else {
                this.counter = jSONObject.optLong("counter");
                this.uid = jSONObject.optInt("uid");
                this.cpu_id = jSONObject.optString("cpu_id");
                this.pub_key_in_x509 = jSONObject.optString(JSON_KEY_PUBLIC);
            }
        } catch (Exception unused) {
            SLogger.e(TAG, "soter: pub key model failed", new Object[0]);
        }
        this.signature = str2;
    }

    public SoterPubKeyModel(Certificate[] certificateArr) throws JSONException {
        this.counter = -1L;
        this.uid = -1;
        this.cpu_id = "";
        this.pub_key_in_x509 = "";
        this.rawJson = "";
        this.certs = null;
        this.signature = "";
        if (certificateArr != null) {
            try {
                ArrayList<String> arrayList = new ArrayList<>();
                JSONArray jSONArray = new JSONArray();
                for (int i = 0; i < certificateArr.length; i++) {
                    Certificate certificate = certificateArr[i];
                    Base64.encodeToString(certificate.getEncoded(), 2);
                    String str = CertUtil.format(certificate);
                    if (i == 0) {
                        loadDeviceInfo((X509Certificate) certificate);
                    }
                    jSONArray.put(str);
                    arrayList.add(str);
                }
                this.certs = arrayList;
                JSONObject jSONObject = new JSONObject();
                jSONObject.put(JSON_KEY_CERTS, jSONArray);
                jSONObject.put("cpu_id", this.cpu_id);
                jSONObject.put("uid", this.uid);
                jSONObject.put("counter", this.counter);
                setRawJson(jSONObject.toString());
            } catch (Exception unused) {
                SLogger.e(TAG, "soter: pub key model failed", new Object[0]);
            }
        }
    }

    private void loadDeviceInfo(X509Certificate x509Certificate) {
        try {
            CertUtil.extractAttestationSequence(x509Certificate, this);
        } catch (Exception e) {
            SLogger.printErrStackTrace(TAG, e, "soter: loadDeviceInfo from attestationCert failed");
        }
    }

    public void setCounter(long j) {
        this.counter = j;
    }

    public void setUid(int i) {
        this.uid = i;
    }

    public void setCpu_id(String str) {
        this.cpu_id = str;
    }

    public void setPub_key_in_x509(String str) {
        this.pub_key_in_x509 = str;
    }

    public void setSignature(String str) {
        this.signature = str;
    }

    public long getCounter() {
        return this.counter;
    }

    public int getUid() {
        return this.uid;
    }

    public String getCpu_id() {
        return this.cpu_id;
    }

    public String getPub_key_in_x509() {
        return this.pub_key_in_x509;
    }

    public String getSignature() {
        return this.signature;
    }

    public String getRawJson() {
        return this.rawJson;
    }

    public void setRawJson(String str) {
        this.rawJson = str;
    }
}
