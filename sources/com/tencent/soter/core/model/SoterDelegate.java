package com.tencent.soter.core.model;

/* loaded from: classes2.dex */
public class SoterDelegate {
    private static final String TAG = "Soter.SoterDelegate";
    private static volatile ISoterDelegate sSoterDelegateImp = new ISoterDelegate() { // from class: com.tencent.soter.core.model.SoterDelegate.1
        private boolean isTriggeredOOM = false;

        @Override // com.tencent.soter.core.model.SoterDelegate.ISoterDelegate
        public void onTriggeredOOM() {
            SLogger.e(SoterDelegate.TAG, "soter: triggered OOM. using default imp, just record the flag", new Object[0]);
            this.isTriggeredOOM = true;
        }

        @Override // com.tencent.soter.core.model.SoterDelegate.ISoterDelegate
        public boolean isTriggeredOOM() {
            return this.isTriggeredOOM;
        }

        @Override // com.tencent.soter.core.model.SoterDelegate.ISoterDelegate
        public void reset() {
            this.isTriggeredOOM = false;
        }
    };

    public interface ISoterDelegate {
        boolean isTriggeredOOM();

        void onTriggeredOOM();

        void reset();
    }

    public static void setImplement(ISoterDelegate iSoterDelegate) {
        sSoterDelegateImp = iSoterDelegate;
    }

    public static void onTriggerOOM() {
        sSoterDelegateImp.onTriggeredOOM();
    }

    public static boolean isTriggeredOOM() {
        return sSoterDelegateImp.isTriggeredOOM();
    }

    public static void reset() {
        sSoterDelegateImp.reset();
    }
}
