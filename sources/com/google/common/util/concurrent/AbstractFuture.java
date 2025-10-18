package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import com.google.common.util.concurrent.internal.InternalFutures;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

/* loaded from: classes2.dex */
public abstract class AbstractFuture<V> extends AbstractFutureState<V> {

    interface Trusted<V> extends ListenableFuture<V> {
    }

    protected void afterDone() {
    }

    protected void interruptTask() {
    }

    static abstract class TrustedFuture<V> extends AbstractFuture<V> implements Trusted<V> {
        TrustedFuture() {
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final V get() throws ExecutionException, InterruptedException {
            return (V) super.get();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final V get(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
            return (V) super.get(j, timeUnit);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isDone() {
            return super.isDone();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isCancelled() {
            return super.isCancelled();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, com.google.common.util.concurrent.ListenableFuture
        public final void addListener(Runnable listener, Executor executor) {
            super.addListener(listener, executor);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean cancel(boolean mayInterruptIfRunning) {
            return super.cancel(mayInterruptIfRunning);
        }
    }

    static final class Listener {
        static final Listener TOMBSTONE = new Listener();
        final Executor executor;
        Listener next;
        final Runnable task;

        Listener(Runnable task, Executor executor) {
            this.task = task;
            this.executor = executor;
        }

        Listener() {
            this.task = null;
            this.executor = null;
        }
    }

    private static final class Failure {
        static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.") { // from class: com.google.common.util.concurrent.AbstractFuture.Failure.1
            @Override // java.lang.Throwable
            public Throwable fillInStackTrace() {
                return this;
            }
        });
        final Throwable exception;

        Failure(Throwable exception) {
            this.exception = (Throwable) Preconditions.checkNotNull(exception);
        }
    }

    private static final class Cancellation {
        static final Cancellation CAUSELESS_CANCELLED;
        static final Cancellation CAUSELESS_INTERRUPTED;
        final Throwable cause;
        final boolean wasInterrupted;

        static {
            if (AbstractFutureState.GENERATE_CANCELLATION_CAUSES) {
                CAUSELESS_CANCELLED = null;
                CAUSELESS_INTERRUPTED = null;
            } else {
                CAUSELESS_CANCELLED = new Cancellation(false, null);
                CAUSELESS_INTERRUPTED = new Cancellation(true, null);
            }
        }

        Cancellation(boolean wasInterrupted, Throwable cause) {
            this.wasInterrupted = wasInterrupted;
            this.cause = cause;
        }
    }

    private static final class DelegatingToFuture<V> implements Runnable {
        final ListenableFuture<? extends V> future;
        final AbstractFuture<V> owner;

        DelegatingToFuture(AbstractFuture<V> owner, ListenableFuture<? extends V> future) {
            this.owner = owner;
            this.future = future;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.owner.value() != this) {
                return;
            }
            if (AbstractFutureState.casValue(this.owner, this, AbstractFuture.getFutureValue(this.future))) {
                AbstractFuture.complete(this.owner, false);
            }
        }
    }

    protected AbstractFuture() {
    }

    @Override // java.util.concurrent.Future
    public V get(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return (V) Platform.get(this, j, timeUnit);
    }

    @Override // java.util.concurrent.Future
    public V get() throws ExecutionException, InterruptedException {
        return (V) Platform.get(this);
    }

    final V getFromAlreadyDoneTrustedFuture() throws ExecutionException {
        Object objValue = value();
        if ((objValue == null) | (objValue instanceof DelegatingToFuture)) {
            throw new IllegalStateException("Cannot get() on a pending future.");
        }
        return (V) getDoneValue(objValue);
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <V> V getDoneValue(Object obj) throws ExecutionException {
        if (obj instanceof Cancellation) {
            throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation) obj).cause);
        }
        if (obj instanceof Failure) {
            throw new ExecutionException(((Failure) obj).exception);
        }
        return obj == NULL ? (V) NullnessCasts.uncheckedNull() : obj;
    }

    static boolean notInstanceOfDelegatingToFuture(Object obj) {
        return !(obj instanceof DelegatingToFuture);
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        Object objValue = value();
        return notInstanceOfDelegatingToFuture(objValue) & (objValue != null);
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return value() instanceof Cancellation;
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        Cancellation cancellation;
        Object objRequireNonNull;
        Object objValue = value();
        if (!(objValue == null) && !(objValue instanceof DelegatingToFuture)) {
            return false;
        }
        if (GENERATE_CANCELLATION_CAUSES) {
            objRequireNonNull = new Cancellation(mayInterruptIfRunning, new CancellationException("Future.cancel() was called."));
        } else {
            if (mayInterruptIfRunning) {
                cancellation = Cancellation.CAUSELESS_INTERRUPTED;
            } else {
                cancellation = Cancellation.CAUSELESS_CANCELLED;
            }
            objRequireNonNull = Objects.requireNonNull(cancellation);
        }
        boolean z = false;
        while (true) {
            if (casValue(this, objValue, objRequireNonNull)) {
                complete(this, mayInterruptIfRunning);
                if (!(objValue instanceof DelegatingToFuture)) {
                    break;
                }
                ListenableFuture<? extends V> listenableFuture = ((DelegatingToFuture) objValue).future;
                if (listenableFuture instanceof Trusted) {
                    this = (AbstractFuture) listenableFuture;
                    objValue = this.value();
                    if (!(objValue == null) && !(objValue instanceof DelegatingToFuture)) {
                        break;
                    }
                    z = true;
                } else {
                    listenableFuture.cancel(mayInterruptIfRunning);
                    break;
                }
            } else {
                objValue = this.value();
                if (notInstanceOfDelegatingToFuture(objValue)) {
                    return z;
                }
            }
        }
        return true;
    }

    protected final boolean wasInterrupted() {
        Object objValue = value();
        return (objValue instanceof Cancellation) && ((Cancellation) objValue).wasInterrupted;
    }

    @Override // com.google.common.util.concurrent.ListenableFuture
    public void addListener(Runnable listener, Executor executor) {
        Listener listenerListeners;
        Preconditions.checkNotNull(listener, "Runnable was null.");
        Preconditions.checkNotNull(executor, "Executor was null.");
        if (!isDone() && (listenerListeners = listeners()) != Listener.TOMBSTONE) {
            Listener listener2 = new Listener(listener, executor);
            do {
                listener2.next = listenerListeners;
                if (casListeners(listenerListeners, listener2)) {
                    return;
                } else {
                    listenerListeners = listeners();
                }
            } while (listenerListeners != Listener.TOMBSTONE);
        }
        executeListener(listener, executor);
    }

    protected boolean set(V v) {
        if (v == null) {
            v = (V) NULL;
        }
        if (!casValue(this, null, v)) {
            return false;
        }
        complete(this, false);
        return true;
    }

    protected boolean setException(Throwable throwable) {
        if (!casValue(this, null, new Failure((Throwable) Preconditions.checkNotNull(throwable)))) {
            return false;
        }
        complete(this, false);
        return true;
    }

    protected boolean setFuture(ListenableFuture<? extends V> future) {
        Failure failure;
        Preconditions.checkNotNull(future);
        Object objValue = value();
        if (objValue == null) {
            if (future.isDone()) {
                if (!casValue(this, null, getFutureValue(future))) {
                    return false;
                }
                complete(this, false);
                return true;
            }
            DelegatingToFuture delegatingToFuture = new DelegatingToFuture(this, future);
            if (casValue(this, null, delegatingToFuture)) {
                try {
                    future.addListener(delegatingToFuture, DirectExecutor.INSTANCE);
                } catch (Throwable th) {
                    try {
                        failure = new Failure(th);
                    } catch (Error | Exception unused) {
                        failure = Failure.FALLBACK_INSTANCE;
                    }
                    casValue(this, delegatingToFuture, failure);
                }
                return true;
            }
            objValue = value();
        }
        if (objValue instanceof Cancellation) {
            future.cancel(((Cancellation) objValue).wasInterrupted);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public static Object getFutureValue(ListenableFuture<?> future) {
        Throwable thTryInternalFastPathGetFailure;
        if (future instanceof Trusted) {
            Object objValue = ((AbstractFuture) future).value();
            if (objValue instanceof Cancellation) {
                Cancellation cancellation = (Cancellation) objValue;
                if (cancellation.wasInterrupted) {
                    objValue = cancellation.cause != null ? new Cancellation(false, cancellation.cause) : Cancellation.CAUSELESS_CANCELLED;
                }
            }
            return Objects.requireNonNull(objValue);
        }
        if ((future instanceof InternalFutureFailureAccess) && (thTryInternalFastPathGetFailure = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess) future)) != null) {
            return new Failure(thTryInternalFastPathGetFailure);
        }
        boolean zIsCancelled = future.isCancelled();
        if ((!GENERATE_CANCELLATION_CAUSES) & zIsCancelled) {
            return Objects.requireNonNull(Cancellation.CAUSELESS_CANCELLED);
        }
        try {
            Object uninterruptibly = getUninterruptibly(future);
            if (zIsCancelled) {
                return new Cancellation(false, new IllegalArgumentException("get() did not throw CancellationException, despite reporting isCancelled() == true: " + future));
            }
            return uninterruptibly == null ? NULL : uninterruptibly;
        } catch (Error | Exception e) {
            return new Failure(e);
        } catch (CancellationException e2) {
            if (!zIsCancelled) {
                return new Failure(new IllegalArgumentException("get() threw CancellationException, despite reporting isCancelled() == false: " + future, e2));
            }
            return new Cancellation(false, e2);
        } catch (ExecutionException e3) {
            if (zIsCancelled) {
                return new Cancellation(false, new IllegalArgumentException("get() did not throw CancellationException, despite reporting isCancelled() == true: " + future, e3));
            }
            return new Failure(e3.getCause());
        }
    }

    private static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
        V v;
        boolean z = false;
        while (true) {
            try {
                v = future.get();
                break;
            } catch (InterruptedException unused) {
                z = true;
            } catch (Throwable th) {
                if (z) {
                    Platform.interruptCurrentThread();
                }
                throw th;
            }
        }
        if (z) {
            Platform.interruptCurrentThread();
        }
        return v;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void complete(AbstractFuture<?> abstractFuture, boolean z) {
        Listener listener = null;
        while (true) {
            abstractFuture.releaseWaiters();
            if (z) {
                abstractFuture.interruptTask();
                z = false;
            }
            abstractFuture.afterDone();
            Listener listenerClearListeners = abstractFuture.clearListeners(listener);
            while (listenerClearListeners != null) {
                listener = listenerClearListeners.next;
                Runnable runnable = (Runnable) Objects.requireNonNull(listenerClearListeners.task);
                if (runnable instanceof DelegatingToFuture) {
                    DelegatingToFuture delegatingToFuture = (DelegatingToFuture) runnable;
                    abstractFuture = delegatingToFuture.owner;
                    if (abstractFuture.value() != delegatingToFuture || !casValue(abstractFuture, delegatingToFuture, getFutureValue(delegatingToFuture.future))) {
                    }
                } else {
                    executeListener(runnable, (Executor) Objects.requireNonNull(listenerClearListeners.executor));
                }
                listenerClearListeners = listener;
            }
            return;
        }
    }

    @Override // com.google.common.util.concurrent.internal.InternalFutureFailureAccess
    protected final Throwable tryInternalFastPathGetFailure() {
        if (!(this instanceof Trusted)) {
            return null;
        }
        Object objValue = value();
        if (objValue instanceof Failure) {
            return ((Failure) objValue).exception;
        }
        return null;
    }

    final void maybePropagateCancellationTo(Future<?> related) {
        if ((related != null) && isCancelled()) {
            related.cancel(wasInterrupted());
        }
    }

    private Listener clearListeners(Listener onto) {
        Listener listenerGasListeners = gasListeners(Listener.TOMBSTONE);
        Listener listener = onto;
        while (listenerGasListeners != null) {
            Listener listener2 = listenerGasListeners.next;
            listenerGasListeners.next = listener;
            listener = listenerGasListeners;
            listenerGasListeners = listener2;
        }
        return listener;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getClass().getName().startsWith("com.google.common.util.concurrent.")) {
            sb.append(getClass().getSimpleName());
        } else {
            sb.append(getClass().getName());
        }
        sb.append('@').append(Integer.toHexString(System.identityHashCode(this))).append("[status=");
        if (isCancelled()) {
            sb.append("CANCELLED");
        } else if (isDone()) {
            addDoneString(sb);
        } else {
            addPendingString(sb);
        }
        return sb.append("]").toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected String pendingToString() {
        if (this instanceof ScheduledFuture) {
            return "remaining delay=[" + ((ScheduledFuture) this).getDelay(TimeUnit.MILLISECONDS) + " ms]";
        }
        return null;
    }

    private void addPendingString(StringBuilder builder) {
        String strEmptyToNull;
        int length = builder.length();
        builder.append("PENDING");
        Object objValue = value();
        if (objValue instanceof DelegatingToFuture) {
            builder.append(", setFuture=[");
            appendUserObject(builder, ((DelegatingToFuture) objValue).future);
            builder.append("]");
        } else {
            try {
                strEmptyToNull = Strings.emptyToNull(pendingToString());
            } catch (Throwable th) {
                Platform.rethrowIfErrorOtherThanStackOverflow(th);
                strEmptyToNull = "Exception thrown from implementation: " + th.getClass();
            }
            if (strEmptyToNull != null) {
                builder.append(", info=[").append(strEmptyToNull).append("]");
            }
        }
        if (isDone()) {
            builder.delete(length, builder.length());
            addDoneString(builder);
        }
    }

    private void addDoneString(StringBuilder builder) {
        try {
            Object uninterruptibly = getUninterruptibly(this);
            builder.append("SUCCESS, result=[");
            appendResultObject(builder, uninterruptibly);
            builder.append("]");
        } catch (CancellationException unused) {
            builder.append("CANCELLED");
        } catch (ExecutionException e) {
            builder.append("FAILURE, cause=[").append(e.getCause()).append("]");
        } catch (Exception e2) {
            builder.append("UNKNOWN, cause=[").append(e2.getClass()).append(" thrown from get()]");
        }
    }

    private void appendResultObject(StringBuilder builder, Object o) {
        if (o == null) {
            builder.append("null");
        } else if (o == this) {
            builder.append("this future");
        } else {
            builder.append(o.getClass().getName()).append("@").append(Integer.toHexString(System.identityHashCode(o)));
        }
    }

    private void appendUserObject(StringBuilder builder, Object o) {
        try {
            if (o == this) {
                builder.append("this future");
            } else {
                builder.append(o);
            }
        } catch (Throwable th) {
            Platform.rethrowIfErrorOtherThanStackOverflow(th);
            builder.append("Exception thrown from implementation: ").append(th.getClass());
        }
    }

    private static void executeListener(Runnable runnable, Executor executor) {
        try {
            executor.execute(runnable);
        } catch (Exception e) {
            log.get().log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, (Throwable) e);
        }
    }

    private static CancellationException cancellationExceptionWithCause(String message, Throwable cause) {
        CancellationException cancellationException = new CancellationException(message);
        cancellationException.initCause(cause);
        return cancellationException;
    }
}
