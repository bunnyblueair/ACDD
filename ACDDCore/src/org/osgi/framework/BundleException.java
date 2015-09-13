/*
 * ACDD Project
 * file BundleException.java  is  part of ACCD
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
 * A Framework exception used to indicate that a bundle lifecycle problem
 * occurred.
 * <p>
 * <p>
 * A <code>BundleException</code> object is created by the Framework to denote
 * an exception condition in the lifecycle of a bundle.
 * <code>BundleException</code>s should not be created by bundle developers.
 * A type code is used to identify the exception type for future extendability.
 * <p>
 * <p>
 * OSGi Alliance reserves the right to extend the set of types.
 * <p>
 * <p>
 * This exception conforms to the general purpose exception chaining mechanism.
 *
 * @version $Revision: 6083 $
 */

public class BundleException extends Exception {
    static final long serialVersionUID = 3571095144220455665L;
    /**
     * Type of bundle exception.
     *
     * @since 1.5
     */
    private final int type;

    /**
     * No exception type is unspecified.
     *
     * @since 1.5
     */
    public static final int UNSPECIFIED = 0;
    /**
     * The operation was unsupported.
     *
     * @since 1.5
     */
    public static final int UNSUPPORTED_OPERATION = 1;
    /**
     * The operation was invalid.
     *
     * @since 1.5
     */
    public static final int INVALID_OPERATION = 2;
    /**
     * The bundle manifest was in error.
     *
     * @since 1.5
     */
    public static final int MANIFEST_ERROR = 3;
    /**
     * The bundle was not resolved.
     *
     * @since 1.5
     */
    public static final int RESOLVE_ERROR = 4;
    /**
     * The bundle activator was in error.
     *
     * @since 1.5
     */
    public static final int ACTIVATOR_ERROR = 5;
    /**
     * The operation failed due to insufficient permissions.
     *
     * @since 1.5
     */
    public static final int SECURITY_ERROR = 6;
    /**
     * The operation failed to complete the requested lifecycle state change.
     *
     * @since 1.5
     */
    public static final int STATECHANGE_ERROR = 7;

    /**
     * The bundle could not be resolved due to an error with the
     * Bundle-NativeCode header.
     *
     * @since 1.5
     */
    public static final int NATIVECODE_ERROR = 8;

    /**
     * The install or update operation failed because another
     * already installed bundle has the same symbolic name and version.
     *
     * @since 1.5
     */
    public static final int DUPLICATE_BUNDLE_ERROR = 9;

    /**
     * The start transient operation failed because the start level of the
     * bundle is greater than the current framework start level
     *
     * @since 1.5
     */
    public static final int START_TRANSIENT_ERROR = 10;

    /**
     * Creates a <code>BundleException</code> with the specified message and
     * exception cause.
     *
     * @param msg   The associated message.
     * @param cause The cause of this exception.
     */
    public BundleException(String msg, Throwable cause) {
        this(msg, UNSPECIFIED, cause);
    }

    /**
     * Creates a <code>BundleException</code> with the specified message.
     *
     * @param msg The message.
     */
    public BundleException(String msg) {
        this(msg, UNSPECIFIED);
    }

    /**
     * Creates a <code>BundleException</code> with the specified message, type
     * and exception cause.
     *
     * @param msg   The associated message.
     * @param type  The type for this exception.
     * @param cause The cause of this exception.
     * @since 1.5
     */
    public BundleException(String msg, int type, Throwable cause) {
        super(msg, cause);
        this.type = type;
    }

    /**
     * Creates a <code>BundleException</code> with the specified message and
     * type.
     *
     * @param msg  The message.
     * @param type The type for this exception.
     * @since 1.5
     */
    public BundleException(String msg, int type) {
        super(msg);
        this.type = type;
    }

    /**
     * Returns the cause of this exception or <code>null</code> if no cause was
     * specified when this exception was created.
     * <p>
     * <p>
     * This method predates the general purpose exception chaining mechanism.
     * The <code>getCause()</code> method is now the preferred means of
     * obtaining this information.
     *
     * @return The result of calling <code>getCause()</code>.
     */
    public Throwable getNestedException() {
        return getCause();
    }

    /**
     * Returns the cause of this exception or <code>null</code> if no cause was
     * set.
     *
     * @return The cause of this exception or <code>null</code> if no cause was
     * set.
     * @since 1.3
     */
    @Override
    public Throwable getCause() {
        return super.getCause();
    }

    /**
     * Initializes the cause of this exception to the specified value.
     *
     * @param cause The cause of this exception.
     * @return This exception.
     * @throws IllegalArgumentException If the specified cause is this
     *                                  exception.
     * @throws IllegalStateException    If the cause of this exception has already
     *                                  been set.
     * @since 1.3
     */
    @Override
    public Throwable initCause(Throwable cause) {
        return super.initCause(cause);
    }

    /**
     * Returns the type for this exception or <code>UNSPECIFIED</code> if the
     * type was unspecified or unknown.
     *
     * @return The type of this exception.
     * @since 1.5
     */
    public int getType() {
        return type;
    }
}
