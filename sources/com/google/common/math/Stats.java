package com.google.common.math;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/* loaded from: classes.dex */
public final class Stats implements Serializable {
    static final int BYTES = 40;
    private static final long serialVersionUID = 0;
    private final long count;
    private final double max;
    private final double mean;
    private final double min;
    private final double sumOfSquaresOfDeltas;

    Stats(long count, double mean, double sumOfSquaresOfDeltas, double min, double max) {
        this.count = count;
        this.mean = mean;
        this.sumOfSquaresOfDeltas = sumOfSquaresOfDeltas;
        this.min = min;
        this.max = max;
    }

    public static Stats of(Iterable<? extends Number> values) {
        StatsAccumulator statsAccumulator = new StatsAccumulator();
        statsAccumulator.addAll(values);
        return statsAccumulator.snapshot();
    }

    public static Stats of(Iterator<? extends Number> values) {
        StatsAccumulator statsAccumulator = new StatsAccumulator();
        statsAccumulator.addAll(values);
        return statsAccumulator.snapshot();
    }

    public static Stats of(double... values) {
        StatsAccumulator statsAccumulator = new StatsAccumulator();
        statsAccumulator.addAll(values);
        return statsAccumulator.snapshot();
    }

    public static Stats of(int... values) {
        StatsAccumulator statsAccumulator = new StatsAccumulator();
        statsAccumulator.addAll(values);
        return statsAccumulator.snapshot();
    }

    public static Stats of(long... values) {
        StatsAccumulator statsAccumulator = new StatsAccumulator();
        statsAccumulator.addAll(values);
        return statsAccumulator.snapshot();
    }

    public static Stats of(DoubleStream values) {
        return ((StatsAccumulator) values.collect(new Stats$$ExternalSyntheticLambda0(), new Stats$$ExternalSyntheticLambda4(), new Stats$$ExternalSyntheticLambda2())).snapshot();
    }

    public static Stats of(IntStream values) {
        return ((StatsAccumulator) values.collect(new Stats$$ExternalSyntheticLambda0(), new Stats$$ExternalSyntheticLambda3(), new Stats$$ExternalSyntheticLambda2())).snapshot();
    }

    public static Stats of(LongStream values) {
        return ((StatsAccumulator) values.collect(new Stats$$ExternalSyntheticLambda0(), new Stats$$ExternalSyntheticLambda1(), new Stats$$ExternalSyntheticLambda2())).snapshot();
    }

    public static Collector<Number, StatsAccumulator, Stats> toStats() {
        return Collector.of(new Stats$$ExternalSyntheticLambda0(), new BiConsumer() { // from class: com.google.common.math.Stats$$ExternalSyntheticLambda5
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((StatsAccumulator) obj).add(((Number) obj2).doubleValue());
            }
        }, new BinaryOperator() { // from class: com.google.common.math.Stats$$ExternalSyntheticLambda6
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return Stats.lambda$toStats$1((StatsAccumulator) obj, (StatsAccumulator) obj2);
            }
        }, new Function() { // from class: com.google.common.math.Stats$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((StatsAccumulator) obj).snapshot();
            }
        }, Collector.Characteristics.UNORDERED);
    }

    static /* synthetic */ StatsAccumulator lambda$toStats$1(StatsAccumulator statsAccumulator, StatsAccumulator statsAccumulator2) {
        statsAccumulator.addAll(statsAccumulator2);
        return statsAccumulator;
    }

    public long count() {
        return this.count;
    }

    public double mean() {
        Preconditions.checkState(this.count != 0);
        return this.mean;
    }

    public double sum() {
        return this.mean * this.count;
    }

    public double populationVariance() {
        Preconditions.checkState(this.count > 0);
        if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
            return Double.NaN;
        }
        if (this.count == 1) {
            return 0.0d;
        }
        return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / count();
    }

    public double populationStandardDeviation() {
        return Math.sqrt(populationVariance());
    }

    public double sampleVariance() {
        Preconditions.checkState(this.count > 1);
        if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
            return Double.NaN;
        }
        return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / (this.count - 1);
    }

    public double sampleStandardDeviation() {
        return Math.sqrt(sampleVariance());
    }

    public double min() {
        Preconditions.checkState(this.count != 0);
        return this.min;
    }

    public double max() {
        Preconditions.checkState(this.count != 0);
        return this.max;
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Stats stats = (Stats) obj;
        return this.count == stats.count && Double.doubleToLongBits(this.mean) == Double.doubleToLongBits(stats.mean) && Double.doubleToLongBits(this.sumOfSquaresOfDeltas) == Double.doubleToLongBits(stats.sumOfSquaresOfDeltas) && Double.doubleToLongBits(this.min) == Double.doubleToLongBits(stats.min) && Double.doubleToLongBits(this.max) == Double.doubleToLongBits(stats.max);
    }

    public int hashCode() {
        return Objects.hashCode(Long.valueOf(this.count), Double.valueOf(this.mean), Double.valueOf(this.sumOfSquaresOfDeltas), Double.valueOf(this.min), Double.valueOf(this.max));
    }

    public String toString() {
        if (count() > 0) {
            return MoreObjects.toStringHelper(this).add("count", this.count).add("mean", this.mean).add("populationStandardDeviation", populationStandardDeviation()).add("min", this.min).add("max", this.max).toString();
        }
        return MoreObjects.toStringHelper(this).add("count", this.count).toString();
    }

    double sumOfSquaresOfDeltas() {
        return this.sumOfSquaresOfDeltas;
    }

    public static double meanOf(Iterable<? extends Number> values) {
        return meanOf(values.iterator());
    }

    public static double meanOf(Iterator<? extends Number> values) {
        Preconditions.checkArgument(values.hasNext());
        double dDoubleValue = values.next().doubleValue();
        long j = 1;
        while (values.hasNext()) {
            double dDoubleValue2 = values.next().doubleValue();
            j++;
            dDoubleValue = (Doubles.isFinite(dDoubleValue2) && Doubles.isFinite(dDoubleValue)) ? dDoubleValue + ((dDoubleValue2 - dDoubleValue) / j) : StatsAccumulator.calculateNewMeanNonFinite(dDoubleValue, dDoubleValue2);
        }
        return dDoubleValue;
    }

    public static double meanOf(double... values) {
        Preconditions.checkArgument(values.length > 0);
        double dCalculateNewMeanNonFinite = values[0];
        for (int i = 1; i < values.length; i++) {
            double d = values[i];
            dCalculateNewMeanNonFinite = (Doubles.isFinite(d) && Doubles.isFinite(dCalculateNewMeanNonFinite)) ? dCalculateNewMeanNonFinite + ((d - dCalculateNewMeanNonFinite) / (i + 1)) : StatsAccumulator.calculateNewMeanNonFinite(dCalculateNewMeanNonFinite, d);
        }
        return dCalculateNewMeanNonFinite;
    }

    public static double meanOf(int... values) {
        Preconditions.checkArgument(values.length > 0);
        double dCalculateNewMeanNonFinite = values[0];
        for (int i = 1; i < values.length; i++) {
            double d = values[i];
            dCalculateNewMeanNonFinite = (Doubles.isFinite(d) && Doubles.isFinite(dCalculateNewMeanNonFinite)) ? dCalculateNewMeanNonFinite + ((d - dCalculateNewMeanNonFinite) / (i + 1)) : StatsAccumulator.calculateNewMeanNonFinite(dCalculateNewMeanNonFinite, d);
        }
        return dCalculateNewMeanNonFinite;
    }

    public static double meanOf(long... values) {
        Preconditions.checkArgument(values.length > 0);
        double dCalculateNewMeanNonFinite = values[0];
        for (int i = 1; i < values.length; i++) {
            double d = values[i];
            dCalculateNewMeanNonFinite = (Doubles.isFinite(d) && Doubles.isFinite(dCalculateNewMeanNonFinite)) ? dCalculateNewMeanNonFinite + ((d - dCalculateNewMeanNonFinite) / (i + 1)) : StatsAccumulator.calculateNewMeanNonFinite(dCalculateNewMeanNonFinite, d);
        }
        return dCalculateNewMeanNonFinite;
    }

    public byte[] toByteArray() {
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(40).order(ByteOrder.LITTLE_ENDIAN);
        writeTo(byteBufferOrder);
        return byteBufferOrder.array();
    }

    void writeTo(ByteBuffer buffer) {
        Preconditions.checkNotNull(buffer);
        Preconditions.checkArgument(buffer.remaining() >= 40, "Expected at least Stats.BYTES = %s remaining , got %s", 40, buffer.remaining());
        buffer.putLong(this.count).putDouble(this.mean).putDouble(this.sumOfSquaresOfDeltas).putDouble(this.min).putDouble(this.max);
    }

    public static Stats fromByteArray(byte[] byteArray) {
        Preconditions.checkNotNull(byteArray);
        Preconditions.checkArgument(byteArray.length == 40, "Expected Stats.BYTES = %s remaining , got %s", 40, byteArray.length);
        return readFrom(ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN));
    }

    static Stats readFrom(ByteBuffer buffer) {
        Preconditions.checkNotNull(buffer);
        Preconditions.checkArgument(buffer.remaining() >= 40, "Expected at least Stats.BYTES = %s remaining , got %s", 40, buffer.remaining());
        return new Stats(buffer.getLong(), buffer.getDouble(), buffer.getDouble(), buffer.getDouble(), buffer.getDouble());
    }
}
