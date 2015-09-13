/*
 * ACDD Project
 * file InvalidSyntaxException.java  is  part of ACCD
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
 * A Framework exception used to indicate that a filter string has an invalid
 * syntax.
 * <p>
 * <p>
 * An <code>InvalidSyntaxException</code> object indicates that a filter
 * string parameter has an invalid syntax and cannot be parsed. See
 * {@link Filter} for a description of the filter string syntax.
 * <p>
 * <p>
 * This exception conforms to the general purpose exception chaining mechanism.
 *
 * @version $Revision: 6083 $
 */

public class InvalidSyntaxException extends Exception {
    static final long serialVersionUID = -4295194420816491875L;
    /**
     * The invalid filter string.
     */
    private final String filter;

    /**
     * Creates an exception of type <code>InvalidSyntaxException</code>.
     * <p>
     * <p>
     * This method creates an <code>InvalidSyntaxException</code> object with
     * the specified message and the filter string which generated the
     * exception.
     *
     * @param msg    The message.
     * @param filter The invalid filter string.
     */
    public InvalidSyntaxException(String msg, String filter) {
        super(msg);
        this.filter = filter;
    }

    /**
     * Creates an exception of type <code>InvalidSyntaxException</code>.
     * <p>
     * <p>
     * This method creates an <code>InvalidSyntaxException</code> object with
     * the specified message and the filter string which generated the
     * exception.
     *
     * @param msg    The message.
     * @param filter The invalid filter string.
     * @param cause  The cause of this exception.
     * @since 1.3
     */
    public InvalidSyntaxException(String msg, String filter, Throwable cause) {
        super(msg, cause);
        this.filter = filter;
    }

    /**
     * Returns the filter string that generated the
     * <code>InvalidSyntaxException</code> object.
     *
     * @return The invalid filter string.
     */
    public String getFilter() {
        return filter;
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
}
