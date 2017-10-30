/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.api;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class DGException extends Exception
{
    /**
     *
     */
    public DGException()
    {
    }

    /**
     * @param cause
     */
    public DGException(final Exception cause)
    {
        super(cause);
    }

    /**
     * @param message
     */
    public DGException(final String message)
    {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public DGException(final String message, final Exception cause)
    {
        super(message, cause);
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
