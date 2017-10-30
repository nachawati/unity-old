/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.modules.symbolics.jsoniq;

import java.util.UUID;

import org.basex.query.QueryModule.Permission;
import org.basex.query.QueryModule.Requires;

import symjava.symbolic.Symbol;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class BaseXModule
{
    /**
     * @return
     */
    @Requires(Permission.READ)
    public Object parameter()
    {
        return new Symbol(UUID.randomUUID().toString());
    }

    /**
     * @param name
     * @return
     */
    @Requires(Permission.READ)
    public Object parameter(String name)
    {
        return new Symbol(name);
    }

    /**
     * @param name
     * @param type
     * @return
     */
    @Requires(Permission.READ)
    public Object parameter(String name, String type)
    {
        return new Symbol(name);
    }

    /**
     * @return
     */
    @Requires(Permission.READ)
    public Object variable()
    {
        return new Symbol(UUID.randomUUID().toString());
    }

    /**
     * @param name
     * @return
     */
    @Requires(Permission.READ)
    public Object variable(String name)
    {
        return new Symbol(name);
    }

    /**
     * @param name
     * @param type
     * @return
     */
    @Requires(Permission.READ)
    public Object variable(String name, String type)
    {
        return new Symbol(name);
    }
}
