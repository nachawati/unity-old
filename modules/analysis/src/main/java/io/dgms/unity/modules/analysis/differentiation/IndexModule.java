package io.dgms.unity.modules.analysis.differentiation;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.basex.query.QueryModule.Permission;
import org.basex.query.QueryModule.Requires;

import symjava.symbolic.Expr;

public class IndexModule
{
    /**
     * @return
     */
    @Requires(Permission.READ)
    public Object differentiate(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr)
            return ((Expr) arg1).diff((Expr) arg2);
        if (arg1 instanceof BigDecimal)
            return 0;
        if (arg1 instanceof BigInteger)
            return 0;
        throw new RuntimeException();
    }
}