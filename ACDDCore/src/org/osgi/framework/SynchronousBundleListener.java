/*
 * ACDD Project
 * file SynchronousBundleListener.java  is  part of ACCD
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

package org.osgi.framework;

/**
 * A synchronous <code>BundleEvent</code> listener.
 * <code>SynchronousBundleListener</code> is a listener interface that may be
 * implemented by a bundle developer. When a <code>BundleEvent</code> is
 * fired, it is synchronously delivered to a
 * <code>SynchronousBundleListener</code>. The Framework may deliver
 * <code>BundleEvent</code> objects to a
 * <code>SynchronousBundleListener</code> out of order and may concurrently
 * call and/or reenter a <code>SynchronousBundleListener</code>.
 * <p>
 * A <code>SynchronousBundleListener</code> object is registered with the
 * Framework using the {@link BundleContext#addBundleListener} method.
 * <code>SynchronousBundleListener</code> objects are called with a
 * <code>BundleEvent</code> object when a bundle has been installed, resolved,
 * starting, started, stopping, stopped, updated, unresolved, or uninstalled.
 * <p>
 * Unlike normal <code>BundleListener</code> objects,
 * <code>SynchronousBundleListener</code>s are synchronously called during
 * bundle lifecycle processing. The bundle lifecycle processing will not proceed
 * until all <code>SynchronousBundleListener</code>s have completed.
 * <code>SynchronousBundleListener</code> objects will be called prior to
 * <code>BundleListener</code> objects.
 * <p>
 * <code>AdminPermission[bundle,LISTENER]</code> is required to add or remove
 * a <code>SynchronousBundleListener</code> object.
 *
 * @version $Revision: 5673 $
 * @see BundleEvent
 * @since 1.1
 */

public interface SynchronousBundleListener extends BundleListener {
    // This is a marker interface
}
