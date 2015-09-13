/*
 * ACDD Project
 * file ExportedPackage.java  is  part of ACCD
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
package org.osgi.service.packageadmin;

import org.osgi.framework.Bundle;

/**
 * An exported package.
 * <p>
 * Instances implementing this interface are created by the
 * Package Admin service.
 * <p>
 * <p>The information about an exported package provided by
 * this object is valid only until the next time
 * <tt>PackageAdmin.refreshPackages()</tt> is
 * called.
 * If an <tt>ExportedPackage</tt> object becomes stale (that is, the package it references
 * has been updated or removed as a result of calling
 * <tt>PackageAdmin.refreshPackages()</tt>),
 * its <tt>getName()</tt> and <tt>getSpecificationVersion()</tt> continue to return their
 * old values, <tt>isRemovalPending()</tt> returns <tt>true</tt>, and <tt>getExportingBundle()</tt>
 * and <tt>getImportingBundles()</tt> return <tt>null</tt>.
 *
 * @author Open Services Gateway Initiative
 * @version $Revision: 1.5 $
 */
public interface ExportedPackage {

    /**
     * Returns the name of the package associated with this <tt>ExportedPackage</tt> object.
     *
     * @return The name of this <tt>ExportedPackage</tt> object.
     */
    String getName();

    /**
     * Returns the bundle exporting the package associated with this <tt>ExportedPackage</tt> object.
     *
     * @return The exporting bundle, or <tt>null</tt> if this <tt>ExportedPackage</tt> object
     * has become stale.
     */
    Bundle getExportingBundle();

    /**
     * Returns the resolved bundles that are currently importing the package
     * associated with this <tt>ExportedPackage</tt> object.
     * <p>
     * <p> The returned array always includes the bundle returned by
     * {@link #getExportingBundle}since an exporter always implicitly
     * imports its exported packages.
     *
     * @return The array of resolved bundles currently importing the package
     * associated with this <tt>ExportedPackage</tt> object, or <tt>null</tt> if this <tt>ExportedPackage</tt>
     * object has become stale.
     */
    Bundle[] getImportingBundles();

    /**
     * Returns the specification version of this <tt>ExportedPackage</tt>, as
     * specified in the exporting bundle's manifest file.
     *
     * @return The specification version of this <tt>ExportedPackage</tt> object, or
     * <tt>null</tt> if no version information is available.
     */
    String getSpecificationVersion();

    /**
     * Returns <tt>true</tt> if the package associated with this <tt>ExportedPackage</tt> object has been
     * exported by a bundle that has been updated or uninstalled.
     *
     * @return <tt>true</tt> if the associated package is being
     * exported by a bundle that has been updated or uninstalled, or if this
     * <tt>ExportedPackage</tt> object has become stale; <tt>false</tt> otherwise.
     */
    boolean isRemovalPending();
}
