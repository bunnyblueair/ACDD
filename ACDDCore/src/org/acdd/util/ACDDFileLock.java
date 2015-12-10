/*
 * ACDD Project
 * file ACDDFileLock.java  is  part of ACCD
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
package org.acdd.util;

import org.acdd.log.Logger;
import org.acdd.log.LoggerFactory;
import org.acdd.runtime.RuntimeVariables;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ACDDFileLock used for ACDD internal
 * @author BunnyBlue
 * **/
public class ACDDFileLock {
    static final Logger log = LoggerFactory.getInstance("ACDDFileLock");
    private static String processName;
    private static ACDDFileLock singleton;
    private Map<String, FileLockCount> mRefCountMap = new ConcurrentHashMap();

    private class FileLockCount {
        FileChannel fChannel;
        RandomAccessFile fOs;
        FileLock mFileLock;
        int mRefCount;

        FileLockCount(FileLock fileLock, int mRefCount, RandomAccessFile fOs, FileChannel fChannel) {
            this.mFileLock = fileLock;
            this.mRefCount = mRefCount;
            this.fOs = fOs;
            this.fChannel = fChannel;
        }
    }

    static {
        if (RuntimeVariables.androidApplication.getApplicationContext() != null) {
            processName = RuntimeVariables.currentProcessName;
        }
    }

    public static ACDDFileLock getInstance() {

        if (singleton == null) {
            singleton = new ACDDFileLock();
        }
        return singleton;
    }

    private int RefCntInc(String filePath, FileLock fileLock, RandomAccessFile randomAccessFile, FileChannel fileChannel) {
        Integer valueOf;

        Integer.valueOf(0);
        if (this.mRefCountMap.containsKey(filePath)) {
            FileLockCount fileLockCount = this.mRefCountMap.get(filePath);
            int i = fileLockCount.mRefCount;
            fileLockCount.mRefCount = i + 1;
            valueOf = Integer.valueOf(i);
        } else {
            valueOf = Integer.valueOf(1);
            this.mRefCountMap.put(filePath, new FileLockCount(fileLock, valueOf.intValue(), randomAccessFile, fileChannel));

        }
        return valueOf.intValue();
    }

    private int RefCntDec(String filePath) {

        Integer valueOf = Integer.valueOf(0);
        if (this.mRefCountMap.containsKey(filePath)) {
            FileLockCount fileLockCount = this.mRefCountMap.get(filePath);
            int i = fileLockCount.mRefCount - 1;
            fileLockCount.mRefCount = i;
            valueOf = Integer.valueOf(i);
            if (valueOf.intValue() <= 0) {
                this.mRefCountMap.remove(filePath);
            }
        }
        return valueOf.intValue();
    }

    /**
     * lock odex
     *
     * @param bundleDexFile optimize dex file
     **/
    public boolean LockExclusive(File bundleDexFile) {

        if (bundleDexFile == null) {
            return false;
        }
        try {
            File lockFile = new File(bundleDexFile.getParentFile().getAbsolutePath().concat("/lock"));
            if (!lockFile.exists()) {
                lockFile.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(lockFile.getAbsolutePath(), "rw");
            FileChannel channel = randomAccessFile.getChannel();
            FileLock lock = channel.lock();
            if (!lock.isValid()) {
                return false;
            }
            RefCntInc(lockFile.getAbsolutePath(), lock, randomAccessFile, channel);
            return true;
        } catch (Exception e) {
            log.error(processName + " FileLock " + bundleDexFile.getParentFile().getAbsolutePath().concat("/lock") + " Lock FAIL! " + e.getMessage());
            return false;
        }
    }

    /**
     * unlock odex file
     **/
    public void unLock(File bundleDexFile) {

        File lockFile = new File(bundleDexFile.getParentFile().getAbsolutePath().concat("/lock"));
        if (!lockFile.exists()) {
            return;
        }
        if (lockFile == null || this.mRefCountMap.containsKey(lockFile.getAbsolutePath())) {
            FileLockCount fileLockCount = this.mRefCountMap.get(lockFile.getAbsolutePath());
            if (fileLockCount != null) {
                FileLock fileLock = fileLockCount.mFileLock;
                RandomAccessFile randomAccessFile = fileLockCount.fOs;
                FileChannel fileChannel = fileLockCount.fChannel;
                try {
                    if (RefCntDec(lockFile.getAbsolutePath()) <= 0) {
                        if (fileLock != null && fileLock.isValid()) {
                            fileLock.release();
                        }
                        if (randomAccessFile != null) {
                            randomAccessFile.close();
                        }
                        if (fileChannel != null) {
                            fileChannel.close();
                        }
                    }
                } catch (IOException e) {
                    log.error(processName + " FileLock " + bundleDexFile.getParentFile().getAbsolutePath().concat("/lock") + " unlock FAIL! " + e.getMessage());
                }
            }
        }
    }
}