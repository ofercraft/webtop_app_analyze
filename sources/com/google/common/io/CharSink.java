package com.google.common.io;

import com.google.common.base.Preconditions;
import com.google.common.base.StandardSystemProperty;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.stream.Stream;

/* loaded from: classes.dex */
public abstract class CharSink {
    public abstract Writer openStream() throws IOException;

    protected CharSink() {
    }

    public Writer openBufferedStream() throws IOException {
        Writer writerOpenStream = openStream();
        if (writerOpenStream instanceof BufferedWriter) {
            return (BufferedWriter) writerOpenStream;
        }
        return new BufferedWriter(writerOpenStream);
    }

    public void write(CharSequence charSequence) throws IOException {
        Preconditions.checkNotNull(charSequence);
        Writer writerOpenStream = openStream();
        try {
            writerOpenStream.append(charSequence);
            if (writerOpenStream != null) {
                writerOpenStream.close();
            }
        } catch (Throwable th) {
            if (writerOpenStream != null) {
                try {
                    writerOpenStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public void writeLines(Iterable<? extends CharSequence> lines) throws IOException {
        writeLines(lines, System.getProperty("line.separator"));
    }

    public void writeLines(Iterable<? extends CharSequence> lines, String lineSeparator) throws IOException {
        writeLines(lines.iterator(), lineSeparator);
    }

    public void writeLines(Stream<? extends CharSequence> lines) throws IOException {
        writeLines(lines, StandardSystemProperty.LINE_SEPARATOR.value());
    }

    public void writeLines(Stream<? extends CharSequence> lines, String lineSeparator) throws IOException {
        writeLines(lines.iterator(), lineSeparator);
    }

    private void writeLines(Iterator<? extends CharSequence> lines, String lineSeparator) throws IOException {
        Preconditions.checkNotNull(lineSeparator);
        Writer writerOpenBufferedStream = openBufferedStream();
        while (lines.hasNext()) {
            try {
                writerOpenBufferedStream.append(lines.next()).append((CharSequence) lineSeparator);
            } catch (Throwable th) {
                if (writerOpenBufferedStream != null) {
                    try {
                        writerOpenBufferedStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (writerOpenBufferedStream != null) {
            writerOpenBufferedStream.close();
        }
    }

    public long writeFrom(Readable readable) throws IOException {
        Preconditions.checkNotNull(readable);
        Writer writerOpenStream = openStream();
        try {
            long jCopy = CharStreams.copy(readable, writerOpenStream);
            if (writerOpenStream != null) {
                writerOpenStream.close();
            }
            return jCopy;
        } catch (Throwable th) {
            if (writerOpenStream != null) {
                try {
                    writerOpenStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }
}
