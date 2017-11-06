/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.modules.languages.jsoniq.compiler.util;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class StringEscape
{
    /**
     * @param text
     * @return
     */
    public String escape(String text)
    {
        return text.replaceAll("&", "&amp;").replace("\\\"", "&quot;");
    }
}
