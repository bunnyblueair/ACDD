/*
 * ACDD Project
 * file FrameworkListener.java  is  part of ACCD
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

import java.util.EventListener;

/**
 * A <code>FrameworkEvent</code> listener. <code>FrameworkListener</code> is
 * a listener interface that may be implemented by a bundle developer. When a
 * <code>FrameworkEvent</code> is fired, it is asynchronously delivered to a
 * <code>FrameworkListener</code>. The Framework delivers
 * <code>FrameworkEvent</code> objects to a <code>FrameworkListener</code>
 * in order and must not concurrently call a <code>FrameworkListener</code>.
 * <p>
 * <p>
 * A <code>FrameworkListener</code> object is registered with the Framework
 * using the {@link BundleContext#addFrameworkListener} method.
 * <code>FrameworkListener</code> objects are called with a
 * <code>FrameworkEvent</code> objects when the Framework starts and when
 * asynchronous errors occur.
 *
 * @version $Revision: 5673 $
 * @see FrameworkEvent
 */

public interface FrameworkListener extends EventListener {

    /**
     * Receives notification of a general <code>FrameworkEvent</code> object.
     *
     * @param event The <code>FrameworkEvent</code> object.
     */
    void frameworkEvent(FrameworkEvent event);
}
