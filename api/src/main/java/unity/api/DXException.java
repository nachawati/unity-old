/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class DXException extends Exception
{
    /**
     *
     */
    public DXException()
    {
    }

    /**
     * @param cause
     */
    public DXException(final Exception cause)
    {
        super(cause);
    }

    /**
     * @param message
     */
    public DXException(final String message)
    {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public DXException(final String message, final Exception cause)
    {
        super(message, cause);
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
