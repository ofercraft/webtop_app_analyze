package com.google.common.io;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.graph.SuccessorsFunction;
import com.google.common.graph.Traverser;
import com.google.common.io.ByteSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.SecureDirectoryStream;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

/* loaded from: classes.dex */
public final class MoreFiles {
    private MoreFiles() {
    }

    public static ByteSource asByteSource(Path path, OpenOption... options) {
        return new PathByteSource(path, options);
    }

    private static final class PathByteSource extends ByteSource {
        private static final LinkOption[] FOLLOW_LINKS = new LinkOption[0];
        private final boolean followLinks;
        private final OpenOption[] options;
        private final Path path;

        private PathByteSource(Path path, OpenOption... options) {
            this.path = (Path) Preconditions.checkNotNull(path);
            OpenOption[] openOptionArr = (OpenOption[]) options.clone();
            this.options = openOptionArr;
            this.followLinks = followLinks(openOptionArr);
        }

        private static boolean followLinks(OpenOption[] options) {
            for (OpenOption openOption : options) {
                if (openOption == LinkOption.NOFOLLOW_LINKS) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openStream() throws IOException {
            return java.nio.file.Files.newInputStream(this.path, this.options);
        }

        private BasicFileAttributes readAttributes() throws IOException {
            return java.nio.file.Files.readAttributes(this.path, BasicFileAttributes.class, this.followLinks ? FOLLOW_LINKS : new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
        }

        @Override // com.google.common.io.ByteSource
        public Optional<Long> sizeIfKnown() {
            try {
                BasicFileAttributes attributes = readAttributes();
                if (attributes.isDirectory() || attributes.isSymbolicLink()) {
                    return Optional.absent();
                }
                return Optional.of(Long.valueOf(attributes.size()));
            } catch (IOException unused) {
                return Optional.absent();
            }
        }

        @Override // com.google.common.io.ByteSource
        public long size() throws IOException {
            BasicFileAttributes attributes = readAttributes();
            if (attributes.isDirectory()) {
                throw new IOException("can't read: is a directory");
            }
            if (attributes.isSymbolicLink()) {
                throw new IOException("can't read: is a symbolic link");
            }
            return attributes.size();
        }

        @Override // com.google.common.io.ByteSource
        public byte[] read() throws IOException {
            SeekableByteChannel seekableByteChannelNewByteChannel = java.nio.file.Files.newByteChannel(this.path, this.options);
            try {
                byte[] byteArray = ByteStreams.toByteArray(Channels.newInputStream(seekableByteChannelNewByteChannel), seekableByteChannelNewByteChannel.size());
                if (seekableByteChannelNewByteChannel != null) {
                    seekableByteChannelNewByteChannel.close();
                }
                return byteArray;
            } catch (Throwable th) {
                if (seekableByteChannelNewByteChannel != null) {
                    try {
                        seekableByteChannelNewByteChannel.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        @Override // com.google.common.io.ByteSource
        public CharSource asCharSource(Charset charset) {
            if (this.options.length == 0) {
                return new ByteSource.AsCharSource(charset) { // from class: com.google.common.io.MoreFiles.PathByteSource.1
                    @Override // com.google.common.io.CharSource
                    public Stream<String> lines() throws IOException {
                        return java.nio.file.Files.lines(PathByteSource.this.path, this.charset);
                    }
                };
            }
            return super.asCharSource(charset);
        }

        public String toString() {
            return "MoreFiles.asByteSource(" + this.path + ", " + Arrays.toString(this.options) + ")";
        }
    }

    public static ByteSink asByteSink(Path path, OpenOption... options) {
        return new PathByteSink(path, options);
    }

    private static final class PathByteSink extends ByteSink {
        private final OpenOption[] options;
        private final Path path;

        private PathByteSink(Path path, OpenOption... options) {
            this.path = (Path) Preconditions.checkNotNull(path);
            this.options = (OpenOption[]) options.clone();
        }

        @Override // com.google.common.io.ByteSink
        public OutputStream openStream() throws IOException {
            return java.nio.file.Files.newOutputStream(this.path, this.options);
        }

        public String toString() {
            return "MoreFiles.asByteSink(" + this.path + ", " + Arrays.toString(this.options) + ")";
        }
    }

    public static CharSource asCharSource(Path path, Charset charset, OpenOption... options) {
        return asByteSource(path, options).asCharSource(charset);
    }

    public static CharSink asCharSink(Path path, Charset charset, OpenOption... options) {
        return asByteSink(path, options).asCharSink(charset);
    }

    public static ImmutableList<Path> listFiles(Path dir) throws IOException {
        try {
            DirectoryStream<Path> directoryStreamNewDirectoryStream = java.nio.file.Files.newDirectoryStream(dir);
            try {
                ImmutableList<Path> immutableListCopyOf = ImmutableList.copyOf(directoryStreamNewDirectoryStream);
                if (directoryStreamNewDirectoryStream != null) {
                    directoryStreamNewDirectoryStream.close();
                }
                return immutableListCopyOf;
            } finally {
            }
        } catch (DirectoryIteratorException e) {
            throw e.getCause();
        }
    }

    public static Traverser<Path> fileTraverser() {
        return Traverser.forTree(new SuccessorsFunction() { // from class: com.google.common.io.MoreFiles$$ExternalSyntheticLambda0
            @Override // com.google.common.graph.SuccessorsFunction, com.google.common.graph.Graph
            public final Iterable successors(Object obj) {
                return MoreFiles.fileTreeChildren((Path) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Iterable<Path> fileTreeChildren(Path dir) {
        if (java.nio.file.Files.isDirectory(dir, LinkOption.NOFOLLOW_LINKS)) {
            try {
                return listFiles(dir);
            } catch (IOException e) {
                throw new DirectoryIteratorException(e);
            }
        }
        return ImmutableList.of();
    }

    public static Predicate<Path> isDirectory(LinkOption... options) {
        final LinkOption[] linkOptionArr = (LinkOption[]) options.clone();
        return new Predicate<Path>() { // from class: com.google.common.io.MoreFiles.1
            @Override // com.google.common.base.Predicate
            public boolean apply(Path input) {
                return java.nio.file.Files.isDirectory(input, linkOptionArr);
            }

            public String toString() {
                return "MoreFiles.isDirectory(" + Arrays.toString(linkOptionArr) + ")";
            }
        };
    }

    private static boolean isDirectory(SecureDirectoryStream<Path> dir, Path name, LinkOption... options) throws IOException {
        return ((BasicFileAttributeView) dir.getFileAttributeView(name, BasicFileAttributeView.class, options)).readAttributes().isDirectory();
    }

    public static Predicate<Path> isRegularFile(LinkOption... options) {
        final LinkOption[] linkOptionArr = (LinkOption[]) options.clone();
        return new Predicate<Path>() { // from class: com.google.common.io.MoreFiles.2
            @Override // com.google.common.base.Predicate
            public boolean apply(Path input) {
                return java.nio.file.Files.isRegularFile(input, linkOptionArr);
            }

            public String toString() {
                return "MoreFiles.isRegularFile(" + Arrays.toString(linkOptionArr) + ")";
            }
        };
    }

    public static boolean equal(Path path1, Path path2) throws IOException {
        Preconditions.checkNotNull(path1);
        Preconditions.checkNotNull(path2);
        if (java.nio.file.Files.isSameFile(path1, path2)) {
            return true;
        }
        ByteSource byteSourceAsByteSource = asByteSource(path1, new OpenOption[0]);
        ByteSource byteSourceAsByteSource2 = asByteSource(path2, new OpenOption[0]);
        long jLongValue = byteSourceAsByteSource.sizeIfKnown().or((Optional<Long>) 0L).longValue();
        long jLongValue2 = byteSourceAsByteSource2.sizeIfKnown().or((Optional<Long>) 0L).longValue();
        if (jLongValue == 0 || jLongValue2 == 0 || jLongValue == jLongValue2) {
            return byteSourceAsByteSource.contentEquals(byteSourceAsByteSource2);
        }
        return false;
    }

    public static void touch(Path path) throws IOException {
        Preconditions.checkNotNull(path);
        try {
            java.nio.file.Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));
        } catch (NoSuchFileException unused) {
            try {
                java.nio.file.Files.createFile(path, new FileAttribute[0]);
            } catch (FileAlreadyExistsException unused2) {
            }
        }
    }

    public static void createParentDirectories(Path path, FileAttribute<?>... attrs) throws IOException {
        Path parent = path.toAbsolutePath().normalize().getParent();
        if (parent == null || java.nio.file.Files.isDirectory(parent, new LinkOption[0])) {
            return;
        }
        java.nio.file.Files.createDirectories(parent, attrs);
        if (!java.nio.file.Files.isDirectory(parent, new LinkOption[0])) {
            throw new IOException("Unable to create parent directories of " + path);
        }
    }

    public static String getFileExtension(Path path) {
        String string;
        int iLastIndexOf;
        Path fileName = path.getFileName();
        if (fileName == null || (iLastIndexOf = (string = fileName.toString()).lastIndexOf(46)) == -1) {
            return "";
        }
        return string.substring(iLastIndexOf + 1);
    }

    public static String getNameWithoutExtension(Path path) {
        Path fileName = path.getFileName();
        if (fileName == null) {
            return "";
        }
        String string = fileName.toString();
        int iLastIndexOf = string.lastIndexOf(46);
        return iLastIndexOf == -1 ? string : string.substring(0, iLastIndexOf);
    }

    public static void deleteRecursively(Path path, RecursiveDeleteOption... options) throws IOException {
        boolean z;
        Path parentPath = getParentPath(path);
        Collection<IOException> collectionDeleteRecursivelyInsecure = null;
        if (parentPath == null) {
            throw new FileSystemException(path.toString(), null, "can't delete recursively");
        }
        try {
            DirectoryStream<Path> directoryStreamNewDirectoryStream = java.nio.file.Files.newDirectoryStream(parentPath);
            try {
                if (directoryStreamNewDirectoryStream instanceof SecureDirectoryStream) {
                    collectionDeleteRecursivelyInsecure = deleteRecursivelySecure((SecureDirectoryStream) directoryStreamNewDirectoryStream, (Path) Objects.requireNonNull(path.getFileName()));
                    z = true;
                } else {
                    z = false;
                }
                if (directoryStreamNewDirectoryStream != null) {
                    directoryStreamNewDirectoryStream.close();
                }
                if (!z) {
                    checkAllowsInsecure(path, options);
                    collectionDeleteRecursivelyInsecure = deleteRecursivelyInsecure(path);
                }
            } finally {
            }
        } catch (IOException e) {
            if (collectionDeleteRecursivelyInsecure == null) {
                throw e;
            }
            collectionDeleteRecursivelyInsecure.add(e);
        }
        if (collectionDeleteRecursivelyInsecure != null) {
            throwDeleteFailed(path, collectionDeleteRecursivelyInsecure);
        }
    }

    public static void deleteDirectoryContents(Path path, RecursiveDeleteOption... options) throws IOException {
        Collection<IOException> collectionDeleteDirectoryContentsInsecure;
        Collection<IOException> collection = null;
        try {
            DirectoryStream<Path> directoryStreamNewDirectoryStream = java.nio.file.Files.newDirectoryStream(path);
            try {
                if (directoryStreamNewDirectoryStream instanceof SecureDirectoryStream) {
                    collectionDeleteDirectoryContentsInsecure = deleteDirectoryContentsSecure((SecureDirectoryStream) directoryStreamNewDirectoryStream);
                } else {
                    checkAllowsInsecure(path, options);
                    collectionDeleteDirectoryContentsInsecure = deleteDirectoryContentsInsecure(directoryStreamNewDirectoryStream);
                }
                collection = collectionDeleteDirectoryContentsInsecure;
                if (directoryStreamNewDirectoryStream != null) {
                    directoryStreamNewDirectoryStream.close();
                }
            } finally {
            }
        } catch (IOException e) {
            if (0 == 0) {
                throw e;
            }
            collection.add(e);
        }
        if (collection != null) {
            throwDeleteFailed(path, collection);
        }
    }

    private static Collection<IOException> deleteRecursivelySecure(SecureDirectoryStream<Path> dir, Path path) throws IOException {
        Collection<IOException> collectionDeleteDirectoryContentsSecure = null;
        try {
            if (isDirectory(dir, path, LinkOption.NOFOLLOW_LINKS)) {
                SecureDirectoryStream<Path> secureDirectoryStreamNewDirectoryStream = dir.newDirectoryStream(path, LinkOption.NOFOLLOW_LINKS);
                try {
                    collectionDeleteDirectoryContentsSecure = deleteDirectoryContentsSecure(secureDirectoryStreamNewDirectoryStream);
                    if (secureDirectoryStreamNewDirectoryStream != null) {
                        secureDirectoryStreamNewDirectoryStream.close();
                    }
                    if (collectionDeleteDirectoryContentsSecure == null) {
                        dir.deleteDirectory(path);
                    }
                    return collectionDeleteDirectoryContentsSecure;
                } finally {
                }
            }
            dir.deleteFile(path);
            return null;
        } catch (IOException e) {
            return addException(collectionDeleteDirectoryContentsSecure, e);
        }
    }

    private static Collection<IOException> deleteDirectoryContentsSecure(SecureDirectoryStream<Path> dir) {
        Collection<IOException> collectionConcat = null;
        try {
            Iterator<Path> it = dir.iterator();
            while (it.hasNext()) {
                collectionConcat = concat(collectionConcat, deleteRecursivelySecure(dir, it.next().getFileName()));
            }
            return collectionConcat;
        } catch (DirectoryIteratorException e) {
            return addException(collectionConcat, e.getCause());
        }
    }

    private static Collection<IOException> deleteRecursivelyInsecure(Path path) throws IOException {
        Collection<IOException> collectionDeleteDirectoryContentsInsecure = null;
        try {
            if (java.nio.file.Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                DirectoryStream<Path> directoryStreamNewDirectoryStream = java.nio.file.Files.newDirectoryStream(path);
                try {
                    collectionDeleteDirectoryContentsInsecure = deleteDirectoryContentsInsecure(directoryStreamNewDirectoryStream);
                    if (directoryStreamNewDirectoryStream != null) {
                        directoryStreamNewDirectoryStream.close();
                    }
                } finally {
                }
            }
            if (collectionDeleteDirectoryContentsInsecure == null) {
                java.nio.file.Files.delete(path);
            }
            return collectionDeleteDirectoryContentsInsecure;
        } catch (IOException e) {
            return addException(collectionDeleteDirectoryContentsInsecure, e);
        }
    }

    private static Collection<IOException> deleteDirectoryContentsInsecure(DirectoryStream<Path> dir) {
        Collection<IOException> collectionConcat = null;
        try {
            Iterator<Path> it = dir.iterator();
            while (it.hasNext()) {
                collectionConcat = concat(collectionConcat, deleteRecursivelyInsecure(it.next()));
            }
            return collectionConcat;
        } catch (DirectoryIteratorException e) {
            return addException(collectionConcat, e.getCause());
        }
    }

    private static Path getParentPath(Path path) {
        Path parent = path.getParent();
        if (parent != null) {
            return parent;
        }
        if (path.getNameCount() == 0) {
            return null;
        }
        return path.getFileSystem().getPath(".", new String[0]);
    }

    private static void checkAllowsInsecure(Path path, RecursiveDeleteOption[] options) throws InsecureRecursiveDeleteException {
        if (!Arrays.asList(options).contains(RecursiveDeleteOption.ALLOW_INSECURE)) {
            throw new InsecureRecursiveDeleteException(path.toString());
        }
    }

    private static Collection<IOException> addException(Collection<IOException> exceptions, IOException e) {
        if (exceptions == null) {
            exceptions = new ArrayList<>();
        }
        exceptions.add(e);
        return exceptions;
    }

    private static Collection<IOException> concat(Collection<IOException> exceptions, Collection<IOException> other) {
        if (exceptions == null) {
            return other;
        }
        if (other != null) {
            exceptions.addAll(other);
        }
        return exceptions;
    }

    private static void throwDeleteFailed(Path path, Collection<IOException> exceptions) throws FileSystemException {
        NoSuchFileException noSuchFileExceptionPathNotFound = pathNotFound(path, exceptions);
        if (noSuchFileExceptionPathNotFound != null) {
            throw noSuchFileExceptionPathNotFound;
        }
        FileSystemException fileSystemException = new FileSystemException(path.toString(), null, "failed to delete one or more files; see suppressed exceptions for details");
        Iterator<IOException> it = exceptions.iterator();
        while (it.hasNext()) {
            fileSystemException.addSuppressed(it.next());
        }
        throw fileSystemException;
    }

    private static NoSuchFileException pathNotFound(Path path, Collection<IOException> exceptions) {
        NoSuchFileException noSuchFileException;
        String file;
        Path parentPath;
        if (exceptions.size() != 1) {
            return null;
        }
        IOException iOException = (IOException) Iterables.getOnlyElement(exceptions);
        if ((iOException instanceof NoSuchFileException) && (file = (noSuchFileException = (NoSuchFileException) iOException).getFile()) != null && (parentPath = getParentPath(path)) != null && file.equals(parentPath.resolve((Path) Objects.requireNonNull(path.getFileName())).toString())) {
            return noSuchFileException;
        }
        return null;
    }
}
