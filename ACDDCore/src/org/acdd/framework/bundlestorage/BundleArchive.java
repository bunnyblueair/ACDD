/*
 * ACDD Project
 * file BundleArchive.java  is  part of ACCD
 * The MIT License (MIT)  Copyright (c) 2015 Bunny Blue,achellies.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */
package org.acdd.framework.bundlestorage;

import android.text.TextUtils;

import org.acdd.framework.Framework;
import org.acdd.log.Logger;
import org.acdd.log.LoggerFactory;
import org.acdd.runtime.RuntimeVariables;
import org.acdd.util.StringUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
/**
 * This is Bundle Archive  implementation.<br>
 * Archive  BundleArchive  BundleArchiveRevision() ,decorator mode,looks like Context
 * ,ContextWrap and ContextImpl
 * **/
public class BundleArchive implements Archive {

    Logger log = LoggerFactory.getInstance("BundleArchive");

    public static final String REVISION_DIRECTORY = "version";
    public static final String DEPRECATED_MARK = "deprecated";
    private File bundleDir;
    private final BundleArchiveRevision currentRevision;
    private final SortedMap<Long, BundleArchiveRevision> revisions;

    public BundleArchive(String location, File bundleDir) throws IOException {
        this.revisions = new TreeMap<Long, BundleArchiveRevision>();
        File[] listFiles = bundleDir.listFiles();
        String currentProcessName = RuntimeVariables.currentProcessName;
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.getName().startsWith(REVISION_DIRECTORY)) {
                    if (new File(file, DEPRECATED_MARK).exists()) {
                        try {
                            if (!TextUtils.isEmpty(currentProcessName) && currentProcessName.equals(RuntimeVariables.androidApplication.getPackageName())) {
                                for (File delete : file.listFiles()) {
                                    delete.delete();
                                }
                                file.delete();
                            }
                        } catch (Exception e) {
                        }
                    } else {
                        long parseLong = Long.parseLong(StringUtils.substringAfter(file.getName(), "."));
                        if (parseLong > 0) {
                            this.revisions.put(Long.valueOf(parseLong), null);
                        }
                    }
                }
            }
        }
        if (this.revisions.isEmpty()) {
            try {
                if (!TextUtils.isEmpty(currentProcessName) && currentProcessName.equals(RuntimeVariables.androidApplication.getPackageName())) {

                    for (File file : listFiles) {
                        file.delete();
                    }

                    bundleDir.delete();
                }
            } catch (Exception e2) {
            }
            throw new IOException("No valid revisions in bundle archive directory: " + bundleDir);
        }
        this.bundleDir = bundleDir;
        long longValue = this.revisions.lastKey().longValue();
        BundleArchiveRevision bundleArchiveRevision = new BundleArchiveRevision(location, longValue, new File(bundleDir, "version." + String.valueOf(longValue)));
        this.revisions.put(Long.valueOf(longValue), bundleArchiveRevision);
        this.currentRevision = bundleArchiveRevision;
        if (!RuntimeVariables.inSubProcess){
            try {
                purge();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //remove  old version
//            for (int i = 2; i < longValue; i++) {
//                File mBundleDir = new File(bundleDir, "version." + String.valueOf(i));
//                if (mBundleDir.isDirectory()) {
//                    File[] listFilesSub = mBundleDir.listFiles();
//                    for (File file : listFilesSub) {
//                        file.delete();
//                    }
//
//                    mBundleDir.delete();
//                }
//                log.info("remove old  bundle@" + mBundleDir.getAbsolutePath() + " last version : " + currentRevision);
//
//            }
        }
        //remove old version
    }


    public BundleArchive(String location, File bundleDir, InputStream inputStream)
            throws IOException {
        this.revisions = new TreeMap<Long, BundleArchiveRevision>();
        this.bundleDir = bundleDir;
        BundleArchiveRevision bundleArchiveRevision = new BundleArchiveRevision(
                location, 1, new File(bundleDir, "version." + String.valueOf(1)),
                inputStream);
        this.revisions.put(Long.valueOf(1), bundleArchiveRevision);
        this.currentRevision = bundleArchiveRevision;
    }

    public BundleArchive(String location, File bundleDir, File archiveFile) throws IOException {
        this.revisions = new TreeMap<Long, BundleArchiveRevision>();
        this.bundleDir = bundleDir;
        BundleArchiveRevision bundleArchiveRevision = new BundleArchiveRevision(
                location, 1, new File(bundleDir, "version." + String.valueOf(1)), archiveFile);
        this.revisions.put(Long.valueOf(1), bundleArchiveRevision);
        this.currentRevision = bundleArchiveRevision;
    }

    @Override
    public BundleArchiveRevision newRevision(String location, File bundleDir,
                                             InputStream inputStream) throws IOException {
        long longValue = 1 + this.revisions.lastKey().longValue();
        BundleArchiveRevision bundleArchiveRevision = new BundleArchiveRevision(
                location, longValue, new File(bundleDir, "version."
                + String.valueOf(longValue)), inputStream);
        this.revisions.put(Long.valueOf(longValue), bundleArchiveRevision);
        return bundleArchiveRevision;
    }

    @Override
    public BundleArchiveRevision newRevision(String packageName, File bundleDir, File archiveFile)
            throws IOException {
        long revision = 1 + this.revisions.lastKey().longValue();
        BundleArchiveRevision bundleArchiveRevision = new BundleArchiveRevision(
                packageName, revision, new File(bundleDir, "version."
                + String.valueOf(revision)), archiveFile);
        this.revisions.put(Long.valueOf(revision), bundleArchiveRevision);
        return bundleArchiveRevision;
    }

    @Override
    public BundleArchiveRevision getCurrentRevision() {
        return this.currentRevision;
    }

    @Override
    public File getArchiveFile() {
        return this.currentRevision.getRevisionFile();
    }

    public File getBundleDir() {
        return this.bundleDir;
    }

    @Override
    public boolean isDexOpted() {
        return this.currentRevision.isDexOpted();
    }

    @Override
    public void optDexFile() {
        this.currentRevision.optDexFile();
    }

    @Override
    public InputStream openAssetInputStream(String name) throws IOException {
        return this.currentRevision.openAssetInputStream(name);
    }

    @Override
    public InputStream openNonAssetInputStream(String name) throws IOException {
        return this.currentRevision.openNonAssetInputStream(name);
    }

    @Override
    public Class<?> findClass(String clazz, ClassLoader classLoader)
            throws ClassNotFoundException {
        return this.currentRevision.findClass(clazz, classLoader);
    }

    @Override
    public File findLibrary(String name) {
        return this.currentRevision.findSoLibrary(name);
    }

    @Override
    public List<URL> getResources(String name) throws IOException {
        return this.currentRevision.getResources(name);
    }

    @Override
    public void purge() throws Exception {
        if (this.revisions.size() > 1) {
            long revisionNum = this.currentRevision.getRevisionNum();
            for (Long longValue : this.revisions.keySet()) {
                long longValue2 = longValue.longValue();
                if (longValue2 != revisionNum) {
                    File file = new File(this.bundleDir, "version."
                            + String.valueOf(longValue2));
                    if (file.exists()) {
                        Framework.deleteDirectory(file);
                    }
                }
            }
            this.revisions.clear();
            this.revisions.put(Long.valueOf(revisionNum), this.currentRevision);
        }
    }

    public static boolean downgradeRevision(File file) throws IOException {

        File[] listFiles = file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {
                return !(!filename.startsWith(BundleArchive.REVISION_DIRECTORY) ||
                        new File(dir, BundleArchive.DEPRECATED_MARK).exists());
            }

        });
        if (listFiles == null || listFiles.length <= 0) {
            return false;
        }
        new File(listFiles[listFiles.length - 1], DEPRECATED_MARK).createNewFile();

        return true;
    }

    @Override
    public void close() {
    }
    @Override
    public boolean isUpdated() {
        return this.currentRevision.isUpdated();

    }

    /**
     * @version start  from 1.0.0
     * **/
    public static class MisMatchException extends RuntimeException {
        public MisMatchException(String message) {
            super(message);
        }

        public MisMatchException(String message, Throwable th) {
            super(message, th);
        }
    }

}
