package com.google.common.util.concurrent;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.AbstractFutureState;
import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import sun.misc.Unsafe;

/* loaded from: classes2.dex */
abstract class AbstractFutureState<V> extends InternalFutureFailureAccess implements ListenableFuture<V> {
    private static final AtomicHelper ATOMIC_HELPER;
    static final boolean GENERATE_CANCELLATION_CAUSES;
    private static final long SPIN_THRESHOLD_NANOS = 1000;
    volatile AbstractFuture.Listener listenersField;
    volatile Object valueField;
    volatile Waiter waitersField;
    static final Object NULL = new Object();
    static final LazyLogger log = new LazyLogger(AbstractFuture.class);

    final boolean casListeners(AbstractFuture.Listener expect, AbstractFuture.Listener update) {
        return ATOMIC_HELPER.casListeners(this, expect, update);
    }

    final AbstractFuture.Listener gasListeners(AbstractFuture.Listener update) {
        return ATOMIC_HELPER.gasListeners(this, update);
    }

    static boolean casValue(AbstractFutureState<?> future, Object expect, Object update) {
        return ATOMIC_HELPER.casValue(future, expect, update);
    }

    final Object value() {
        return this.valueField;
    }

    final AbstractFuture.Listener listeners() {
        return this.listenersField;
    }

    final void releaseWaiters() {
        for (Waiter waiterGasWaiters = gasWaiters(Waiter.TOMBSTONE); waiterGasWaiters != null; waiterGasWaiters = waiterGasWaiters.next) {
            waiterGasWaiters.unpark();
        }
    }

    final V blockingGet(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        long nanos = timeUnit.toNanos(j);
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object obj = this.valueField;
        if ((obj != null) & AbstractFuture.notInstanceOfDelegatingToFuture(obj)) {
            return (V) AbstractFuture.getDoneValue(obj);
        }
        long jNanoTime = nanos > 0 ? System.nanoTime() + nanos : 0L;
        if (nanos >= SPIN_THRESHOLD_NANOS) {
            Waiter waiter = this.waitersField;
            if (waiter != Waiter.TOMBSTONE) {
                Waiter waiter2 = new Waiter();
                do {
                    waiter2.setNext(waiter);
                    if (casWaiters(waiter, waiter2)) {
                        do {
                            OverflowAvoidingLockSupport.parkNanos(this, nanos);
                            if (Thread.interrupted()) {
                                removeWaiter(waiter2);
                                throw new InterruptedException();
                            }
                            Object obj2 = this.valueField;
                            if ((obj2 != null) & AbstractFuture.notInstanceOfDelegatingToFuture(obj2)) {
                                return (V) AbstractFuture.getDoneValue(obj2);
                            }
                            nanos = jNanoTime - System.nanoTime();
                        } while (nanos >= SPIN_THRESHOLD_NANOS);
                        removeWaiter(waiter2);
                    } else {
                        waiter = this.waitersField;
                    }
                } while (waiter != Waiter.TOMBSTONE);
            }
            return (V) AbstractFuture.getDoneValue(Objects.requireNonNull(this.valueField));
        }
        while (nanos > 0) {
            Object obj3 = this.valueField;
            if ((obj3 != null) & AbstractFuture.notInstanceOfDelegatingToFuture(obj3)) {
                return (V) AbstractFuture.getDoneValue(obj3);
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            nanos = jNanoTime - System.nanoTime();
        }
        String string = toString();
        String lowerCase = timeUnit.toString().toLowerCase(Locale.ROOT);
        String str = "Waited " + j + " " + timeUnit.toString().toLowerCase(Locale.ROOT);
        if (nanos + SPIN_THRESHOLD_NANOS < 0) {
            String str2 = str + " (plus ";
            long j2 = -nanos;
            long jConvert = timeUnit.convert(j2, TimeUnit.NANOSECONDS);
            long nanos2 = j2 - timeUnit.toNanos(jConvert);
            boolean z = jConvert == 0 || nanos2 > SPIN_THRESHOLD_NANOS;
            if (jConvert > 0) {
                String str3 = str2 + jConvert + " " + lowerCase;
                if (z) {
                    str3 = str3 + ",";
                }
                str2 = str3 + " ";
            }
            if (z) {
                str2 = str2 + nanos2 + " nanoseconds ";
            }
            str = str2 + "delay)";
        }
        if (isDone()) {
            throw new TimeoutException(str + " but future completed as timeout expired");
        }
        throw new TimeoutException(str + " for " + string);
    }

    final V blockingGet() throws ExecutionException, InterruptedException {
        Object obj;
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object obj2 = this.valueField;
        if ((obj2 != null) & AbstractFuture.notInstanceOfDelegatingToFuture(obj2)) {
            return (V) AbstractFuture.getDoneValue(obj2);
        }
        Waiter waiter = this.waitersField;
        if (waiter != Waiter.TOMBSTONE) {
            Waiter waiter2 = new Waiter();
            do {
                waiter2.setNext(waiter);
                if (casWaiters(waiter, waiter2)) {
                    do {
                        LockSupport.park(this);
                        if (Thread.interrupted()) {
                            removeWaiter(waiter2);
                            throw new InterruptedException();
                        }
                        obj = this.valueField;
                    } while (!((obj != null) & AbstractFuture.notInstanceOfDelegatingToFuture(obj)));
                    return (V) AbstractFuture.getDoneValue(obj);
                }
                waiter = this.waitersField;
            } while (waiter != Waiter.TOMBSTONE);
        }
        return (V) AbstractFuture.getDoneValue(Objects.requireNonNull(this.valueField));
    }

    AbstractFutureState() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10, types: [java.util.logging.Logger] */
    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.common.util.concurrent.AbstractFutureState$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r1v6 */
    static {
        boolean z;
        AtomicHelper synchronizedHelper;
        Throwable th;
        AtomicHelper atomicReferenceFieldUpdaterAtomicHelper;
        try {
            z = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
        } catch (SecurityException unused) {
            z = false;
        }
        GENERATE_CANCELLATION_CAUSES = z;
        ?? r1 = 0;
        Throwable th2 = null;
        if (mightBeAndroid()) {
            try {
                synchronizedHelper = new UnsafeAtomicHelper();
            } catch (Error | Exception e) {
                try {
                    atomicReferenceFieldUpdaterAtomicHelper = new AtomicReferenceFieldUpdaterAtomicHelper();
                } catch (Error | Exception e2) {
                    SynchronizedHelper synchronizedHelper2 = new SynchronizedHelper();
                    th2 = e2;
                    atomicReferenceFieldUpdaterAtomicHelper = synchronizedHelper2;
                }
                AtomicHelper atomicHelper = atomicReferenceFieldUpdaterAtomicHelper;
                th = e;
                synchronizedHelper = atomicHelper;
                r1 = th2;
            }
        } else {
            try {
                synchronizedHelper = new AtomicReferenceFieldUpdaterAtomicHelper();
            } catch (NoClassDefFoundError unused2) {
                synchronizedHelper = new SynchronizedHelper();
            }
        }
        th = null;
        ATOMIC_HELPER = synchronizedHelper;
        if (r1 != 0) {
            LazyLogger lazyLogger = log;
            lazyLogger.get().log(Level.SEVERE, "UnsafeAtomicHelper is broken!", th);
            lazyLogger.get().log(Level.SEVERE, "AtomicReferenceFieldUpdaterAtomicHelper is broken!", r1);
        }
    }

    static final class Waiter {
        static final Waiter TOMBSTONE = new Waiter(false);
        volatile Waiter next;
        volatile Thread thread;

        Waiter(boolean unused) {
        }

        Waiter() {
            AbstractFutureState.putThread(this, Thread.currentThread());
        }

        void setNext(Waiter next) {
            AbstractFutureState.putNext(this, next);
        }

        void unpark() {
            Thread thread = this.thread;
            if (thread != null) {
                this.thread = null;
                LockSupport.unpark(thread);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void putThread(Waiter waiter, Thread newValue) {
        ATOMIC_HELPER.putThread(waiter, newValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void putNext(Waiter waiter, Waiter newValue) {
        ATOMIC_HELPER.putNext(waiter, newValue);
    }

    private boolean casWaiters(Waiter expect, Waiter update) {
        return ATOMIC_HELPER.casWaiters(this, expect, update);
    }

    private final Waiter gasWaiters(Waiter update) {
        return ATOMIC_HELPER.gasWaiters(this, update);
    }

    private void removeWaiter(Waiter node) {
        node.thread = null;
        while (true) {
            Waiter waiter = this.waitersField;
            if (waiter == Waiter.TOMBSTONE) {
                return;
            }
            Waiter waiter2 = null;
            while (waiter != null) {
                Waiter waiter3 = waiter.next;
                if (waiter.thread != null) {
                    waiter2 = waiter;
                } else if (waiter2 != null) {
                    waiter2.next = waiter3;
                    if (waiter2.thread == null) {
                        break;
                    }
                } else if (!casWaiters(waiter, waiter3)) {
                    break;
                }
                waiter = waiter3;
            }
            return;
        }
    }

    static String atomicHelperTypeForTest() {
        return ATOMIC_HELPER.atomicHelperTypeForTest();
    }

    private static abstract class AtomicHelper {
        abstract String atomicHelperTypeForTest();

        abstract boolean casListeners(AbstractFutureState<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update);

        abstract boolean casValue(AbstractFutureState<?> future, Object expect, Object update);

        abstract boolean casWaiters(AbstractFutureState<?> future, Waiter expect, Waiter update);

        abstract AbstractFuture.Listener gasListeners(AbstractFutureState<?> future, AbstractFuture.Listener update);

        abstract Waiter gasWaiters(AbstractFutureState<?> future, Waiter update);

        abstract void putNext(Waiter waiter, Waiter newValue);

        abstract void putThread(Waiter waiter, Thread newValue);

        private AtomicHelper() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class UnsafeAtomicHelper extends AtomicHelper {
        static final long LISTENERS_OFFSET;
        static final Unsafe UNSAFE;
        static final long VALUE_OFFSET;
        static final long WAITERS_OFFSET;
        static final long WAITER_NEXT_OFFSET;
        static final long WAITER_THREAD_OFFSET;

        private UnsafeAtomicHelper() {
            super();
        }

        static {
            Unsafe unsafe;
            try {
                try {
                    unsafe = Unsafe.getUnsafe();
                } catch (SecurityException unused) {
                    unsafe = (Unsafe) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: com.google.common.util.concurrent.AbstractFutureState$UnsafeAtomicHelper$$ExternalSyntheticLambda1
                        @Override // java.security.PrivilegedExceptionAction
                        public final Object run() {
                            return AbstractFutureState.UnsafeAtomicHelper.lambda$static$0();
                        }
                    });
                }
                try {
                    WAITERS_OFFSET = unsafe.objectFieldOffset(AbstractFutureState.class.getDeclaredField("waitersField"));
                    LISTENERS_OFFSET = unsafe.objectFieldOffset(AbstractFutureState.class.getDeclaredField("listenersField"));
                    VALUE_OFFSET = unsafe.objectFieldOffset(AbstractFutureState.class.getDeclaredField("valueField"));
                    WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("thread"));
                    WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("next"));
                    UNSAFE = unsafe;
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            } catch (PrivilegedActionException e2) {
                throw new RuntimeException("Could not initialize intrinsics", e2.getCause());
            }
        }

        static /* synthetic */ Unsafe lambda$static$0() throws Exception {
            for (Field field : Unsafe.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object obj = field.get(null);
                if (Unsafe.class.isInstance(obj)) {
                    return (Unsafe) Unsafe.class.cast(obj);
                }
            }
            throw new NoSuchFieldError("the Unsafe");
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        void putThread(Waiter waiter, Thread newValue) {
            UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        void putNext(Waiter waiter, Waiter newValue) {
            UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        boolean casWaiters(AbstractFutureState<?> future, Waiter expect, Waiter update) {
            return Striped$SmallLazyStriped$$ExternalSyntheticBackportWithForwarding0.m(UNSAFE, future, WAITERS_OFFSET, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        boolean casListeners(AbstractFutureState<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update) {
            return Striped$SmallLazyStriped$$ExternalSyntheticBackportWithForwarding0.m(UNSAFE, future, LISTENERS_OFFSET, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        AbstractFuture.Listener gasListeners(AbstractFutureState<?> future, AbstractFuture.Listener update) {
            AbstractFuture.Listener listener;
            do {
                listener = future.listenersField;
                if (update == listener) {
                    break;
                }
            } while (!casListeners(future, listener, update));
            return listener;
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        Waiter gasWaiters(AbstractFutureState<?> future, Waiter update) {
            Waiter waiter;
            do {
                waiter = future.waitersField;
                if (update == waiter) {
                    break;
                }
            } while (!casWaiters(future, waiter, update));
            return waiter;
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        boolean casValue(AbstractFutureState<?> future, Object expect, Object update) {
            return Striped$SmallLazyStriped$$ExternalSyntheticBackportWithForwarding0.m(UNSAFE, future, VALUE_OFFSET, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        String atomicHelperTypeForTest() {
            return "UnsafeAtomicHelper";
        }
    }

    private static final class AtomicReferenceFieldUpdaterAtomicHelper extends AtomicHelper {
        private static final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater = AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread");
        private static final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater = AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next");
        private static final AtomicReferenceFieldUpdater<? super AbstractFutureState<?>, Waiter> waitersUpdater = AtomicReferenceFieldUpdater.newUpdater(AbstractFutureState.class, Waiter.class, "waitersField");
        private static final AtomicReferenceFieldUpdater<? super AbstractFutureState<?>, AbstractFuture.Listener> listenersUpdater = AtomicReferenceFieldUpdater.newUpdater(AbstractFutureState.class, AbstractFuture.Listener.class, "listenersField");
        private static final AtomicReferenceFieldUpdater<? super AbstractFutureState<?>, Object> valueUpdater = AtomicReferenceFieldUpdater.newUpdater(AbstractFutureState.class, Object.class, "valueField");

        private AtomicReferenceFieldUpdaterAtomicHelper() {
            super();
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        void putThread(Waiter waiter, Thread newValue) {
            waiterThreadUpdater.lazySet(waiter, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        void putNext(Waiter waiter, Waiter newValue) {
            waiterNextUpdater.lazySet(waiter, newValue);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        boolean casWaiters(AbstractFutureState<?> future, Waiter expect, Waiter update) {
            return AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(waitersUpdater, future, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        boolean casListeners(AbstractFutureState<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update) {
            return AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(listenersUpdater, future, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        AbstractFuture.Listener gasListeners(AbstractFutureState<?> future, AbstractFuture.Listener update) {
            return listenersUpdater.getAndSet(future, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        Waiter gasWaiters(AbstractFutureState<?> future, Waiter update) {
            return waitersUpdater.getAndSet(future, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        boolean casValue(AbstractFutureState<?> future, Object expect, Object update) {
            return AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(valueUpdater, future, expect, update);
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        String atomicHelperTypeForTest() {
            return "AtomicReferenceFieldUpdaterAtomicHelper";
        }
    }

    private static final class SynchronizedHelper extends AtomicHelper {
        private SynchronizedHelper() {
            super();
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        void putThread(Waiter waiter, Thread newValue) {
            waiter.thread = newValue;
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        void putNext(Waiter waiter, Waiter newValue) {
            waiter.next = newValue;
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        boolean casWaiters(AbstractFutureState<?> future, Waiter expect, Waiter update) {
            synchronized (future) {
                if (future.waitersField != expect) {
                    return false;
                }
                future.waitersField = update;
                return true;
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        boolean casListeners(AbstractFutureState<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update) {
            synchronized (future) {
                if (future.listenersField != expect) {
                    return false;
                }
                future.listenersField = update;
                return true;
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        AbstractFuture.Listener gasListeners(AbstractFutureState<?> future, AbstractFuture.Listener update) {
            AbstractFuture.Listener listener;
            synchronized (future) {
                listener = future.listenersField;
                if (listener != update) {
                    future.listenersField = update;
                }
            }
            return listener;
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        Waiter gasWaiters(AbstractFutureState<?> future, Waiter update) {
            Waiter waiter;
            synchronized (future) {
                waiter = future.waitersField;
                if (waiter != update) {
                    future.waitersField = update;
                }
            }
            return waiter;
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        boolean casValue(AbstractFutureState<?> future, Object expect, Object update) {
            synchronized (future) {
                if (future.valueField != expect) {
                    return false;
                }
                future.valueField = update;
                return true;
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFutureState.AtomicHelper
        String atomicHelperTypeForTest() {
            return "SynchronizedHelper";
        }
    }

    private static boolean mightBeAndroid() {
        String property = System.getProperty("java.runtime.name", "");
        return property == null || property.contains("Android");
    }
}
