/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.modules.symbolics.operators.basex;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.basex.query.QueryModule.Deterministic;
import org.basex.query.QueryModule.Permission;
import org.basex.query.QueryModule.Requires;
import org.basex.query.value.Value;

import symjava.logic.And;
import symjava.logic.Every;
import symjava.logic.Or;
import symjava.logic.Some;
import symjava.relational.Eq;
import symjava.relational.Ge;
import symjava.relational.Gt;
import symjava.relational.Le;
import symjava.relational.Lt;
import symjava.relational.Neq;
import symjava.symbolic.Expr;
import symjava.symbolic.Pow;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class IndexModule
{

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_add(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return ((Expr) arg1).add((Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return ((Expr) arg1).add(((BigDecimal) arg2).doubleValue());
            if (arg2 instanceof BigInteger)
                return ((Expr) arg1).add(((BigInteger) arg2).longValue());
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return Expr.valueOf(((BigDecimal) arg1).doubleValue()).add((Expr) arg2);
            if (arg1 instanceof BigInteger)
                return Expr.valueOf(((BigInteger) arg1).longValue()).add((Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_and(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return new And((Expr) arg1, (Expr) arg2);
            if (arg2 instanceof Boolean)
                if ((Boolean) arg2 == true)
                    return arg1;
                else if ((Boolean) arg2 == false)
                    return false;
            if (arg2 instanceof BigDecimal)
                return new And((Expr) arg1, Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return new And((Expr) arg1, Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof Boolean)
                if ((Boolean) arg1 == true)
                    return arg2;
                else if ((Boolean) arg1 == false)
                    return false;
            if (arg1 instanceof BigDecimal)
                return new And(Expr.valueOf(((BigDecimal) arg1).doubleValue()), (Expr) arg2);
            if (arg1 instanceof BigInteger)
                return new And(Expr.valueOf(((BigInteger) arg1).longValue()), (Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_divide(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return ((Expr) arg1).divide((Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return ((Expr) arg1).divide(((BigDecimal) arg2).doubleValue());
            if (arg2 instanceof BigInteger)
                return ((Expr) arg1).divide(((BigInteger) arg2).longValue());
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return Expr.valueOf(((BigDecimal) arg1).doubleValue()).divide((Expr) arg2);
            if (arg1 instanceof BigInteger)
                return Expr.valueOf(((BigInteger) arg1).longValue()).divide((Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param args
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_every(Object[] args)
    {
        final Expr[] exprs = new Expr[args.length];
        for (int i = 0; i < args.length; i++)
            exprs[i] = (Expr) args[i];
        return new Every(exprs);
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_general_equal(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return new Eq((Expr) arg1, (Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return new Eq((Expr) arg1, Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return new Eq((Expr) arg1, Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return new Eq(Expr.valueOf(((BigDecimal) arg1).doubleValue()), (Expr) arg2);
            if (arg1 instanceof BigInteger)
                return new Eq(Expr.valueOf(((BigInteger) arg1).longValue()), (Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_general_greater_than(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return new Gt((Expr) arg1, (Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return new Gt((Expr) arg1, Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return new Gt((Expr) arg1, Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return new Gt(Expr.valueOf(((BigDecimal) arg1).doubleValue()), (Expr) arg2);
            if (arg1 instanceof BigInteger)
                return new Gt(Expr.valueOf(((BigInteger) arg1).longValue()), (Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_general_greater_than_or_equal(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return new Ge((Expr) arg1, (Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return new Ge((Expr) arg1, Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return new Ge((Expr) arg1, Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return new Ge(Expr.valueOf(((BigDecimal) arg1).doubleValue()), (Expr) arg2);
            if (arg1 instanceof BigInteger)
                return new Ge(Expr.valueOf(((BigInteger) arg1).longValue()), (Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_general_less_than(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return new Lt((Expr) arg1, (Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return new Lt((Expr) arg1, Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return new Lt((Expr) arg1, Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return new Lt(Expr.valueOf(((BigDecimal) arg1).doubleValue()), (Expr) arg2);
            if (arg1 instanceof BigInteger)
                return new Lt(Expr.valueOf(((BigInteger) arg1).longValue()), (Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_general_less_than_or_equal(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return new Le((Expr) arg1, (Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return new Le((Expr) arg1, Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return new Le((Expr) arg1, Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return new Le(Expr.valueOf(((BigDecimal) arg1).doubleValue()), (Expr) arg2);
            if (arg1 instanceof BigInteger)
                return new Le(Expr.valueOf(((BigInteger) arg1).longValue()), (Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_general_not_equal(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return new Neq((Expr) arg1, (Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return new Neq((Expr) arg1, Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return new Neq((Expr) arg1, Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return new Neq(Expr.valueOf(((BigDecimal) arg1).doubleValue()), (Expr) arg2);
            if (arg1 instanceof BigInteger)
                return new Neq(Expr.valueOf(((BigInteger) arg1).longValue()), (Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @param arg3
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_if(Object arg1, Object arg2, Object arg3)
    {
        if (!(arg1 instanceof Expr))
            throw new RuntimeException();

        Expr expr2;
        if (arg2 instanceof Expr)
            expr2 = (Expr) arg2;
        else if (arg2 instanceof BigDecimal)
            expr2 = Expr.valueOf(((BigDecimal) arg2).doubleValue());
        else if (arg2 instanceof BigInteger)
            expr2 = Expr.valueOf(((BigInteger) arg2).longValue());
        else
            throw new RuntimeException();

        Expr expr3;
        if (arg3 instanceof Expr)
            expr3 = (Expr) arg3;
        else if (arg3 instanceof BigDecimal)
            expr3 = Expr.valueOf(((BigDecimal) arg3).doubleValue());
        else if (arg3 instanceof BigInteger)
            expr3 = Expr.valueOf(((BigInteger) arg3).longValue());
        else
            throw new RuntimeException();

        return new symjava.logic.If((Expr) arg1, expr2, expr3);
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_integer_divide(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return ((Expr) arg1).divide((Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return ((Expr) arg1).divide(((BigDecimal) arg2).doubleValue());
            if (arg2 instanceof BigInteger)
                return ((Expr) arg1).divide(((BigInteger) arg2).longValue());
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return Expr.valueOf(((BigDecimal) arg1).doubleValue()).divide((Expr) arg2);
            if (arg1 instanceof BigInteger)
                return Expr.valueOf(((BigInteger) arg1).longValue()).divide((Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public boolean symbolic_isJavaType(Value arg)
    {
        return false;
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_mod(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return ((Expr) arg1).remainder((Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return ((Expr) arg1).remainder(Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return ((Expr) arg1).remainder(Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return Expr.valueOf(((BigDecimal) arg1).doubleValue()).remainder((Expr) arg2);
            if (arg1 instanceof BigInteger)
                return Expr.valueOf(((BigInteger) arg1).longValue()).remainder((Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_multiply(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return ((Expr) arg1).multiply((Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return ((Expr) arg1).multiply(((BigDecimal) arg2).doubleValue());
            if (arg2 instanceof BigInteger)
                return ((Expr) arg1).multiply(((BigInteger) arg2).longValue());
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return Expr.valueOf(((BigDecimal) arg1).doubleValue()).multiply((Expr) arg2);
            if (arg1 instanceof BigInteger)
                return Expr.valueOf(((BigInteger) arg1).longValue()).multiply((Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_or(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return new Or((Expr) arg1, (Expr) arg2);
            if (arg2 instanceof Boolean)
                if ((Boolean) arg2 == true)
                    return true;
                else if ((Boolean) arg2 == false)
                    return arg1;
            if (arg2 instanceof BigDecimal)
                return new Or((Expr) arg1, Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return new Or((Expr) arg1, Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof Boolean)
                if ((Boolean) arg1 == true)
                    return true;
                else if ((Boolean) arg1 == false)
                    return arg2;
            if (arg1 instanceof BigDecimal)
                return new Or(Expr.valueOf(((BigDecimal) arg1).doubleValue()), (Expr) arg2);
            if (arg1 instanceof BigInteger)
                return new Or(Expr.valueOf(((BigInteger) arg1).longValue()), (Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_pow(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return new Pow((Expr) arg1, (Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return new Pow((Expr) arg1, Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return new Pow((Expr) arg1, Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return new Pow(Expr.valueOf(((BigDecimal) arg1).doubleValue()), (Expr) arg2);
            if (arg1 instanceof BigInteger)
                return new Pow(Expr.valueOf(((BigInteger) arg1).longValue()), (Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param args
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_some(Expr[] args)
    {
        final Expr[] exprs = new Expr[args.length];
        for (int i = 0; i < args.length; i++)
            exprs[i] = args[i];
        return new Some(exprs);
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_subtract(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return ((Expr) arg1).subtract((Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return ((Expr) arg1).subtract(((BigDecimal) arg2).doubleValue());
            if (arg2 instanceof BigInteger)
                return ((Expr) arg1).subtract(((BigInteger) arg2).longValue());
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return Expr.valueOf(((BigDecimal) arg1).doubleValue()).subtract((Expr) arg2);
            if (arg1 instanceof BigInteger)
                return Expr.valueOf(((BigInteger) arg1).longValue()).subtract((Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_unary_minus(Object arg)
    {
        if (arg instanceof Expr)
            return ((Expr) arg).negate();

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_value_equal(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return new Eq((Expr) arg1, (Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return new Eq((Expr) arg1, Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return new Eq((Expr) arg1, Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return new Eq(Expr.valueOf(((BigDecimal) arg1).doubleValue()), (Expr) arg2);
            if (arg1 instanceof BigInteger)
                return new Eq(Expr.valueOf(((BigInteger) arg1).longValue()), (Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_value_greater_than(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return new Gt((Expr) arg1, (Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return new Gt((Expr) arg1, Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return new Gt((Expr) arg1, Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return new Gt(Expr.valueOf(((BigDecimal) arg1).doubleValue()), (Expr) arg2);
            if (arg1 instanceof BigInteger)
                return new Gt(Expr.valueOf(((BigInteger) arg1).longValue()), (Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_value_greater_than_or_equal(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return new Ge((Expr) arg1, (Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return new Ge((Expr) arg1, Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return new Ge((Expr) arg1, Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return new Ge(Expr.valueOf(((BigDecimal) arg1).doubleValue()), (Expr) arg2);
            if (arg1 instanceof BigInteger)
                return new Ge(Expr.valueOf(((BigInteger) arg1).longValue()), (Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_value_less_than(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return new Lt((Expr) arg1, (Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return new Lt((Expr) arg1, Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return new Lt((Expr) arg1, Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return new Lt(Expr.valueOf(((BigDecimal) arg1).doubleValue()), (Expr) arg2);
            if (arg1 instanceof BigInteger)
                return new Lt(Expr.valueOf(((BigInteger) arg1).longValue()), (Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_value_less_than_or_equal(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return new Le((Expr) arg1, (Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return new Le((Expr) arg1, Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return new Le((Expr) arg1, Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return new Le(Expr.valueOf(((BigDecimal) arg1).doubleValue()), (Expr) arg2);
            if (arg1 instanceof BigInteger)
                return new Le(Expr.valueOf(((BigInteger) arg1).longValue()), (Expr) arg2);
        }

        throw new RuntimeException();
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    @Deterministic
    @Requires(Permission.READ)
    public Object symbolic_value_not_equal(Object arg1, Object arg2)
    {
        if (arg1 instanceof Expr) {
            if (arg2 instanceof Expr)
                return new Neq((Expr) arg1, (Expr) arg2);
            if (arg2 instanceof BigDecimal)
                return new Neq((Expr) arg1, Expr.valueOf(((BigDecimal) arg2).doubleValue()));
            if (arg2 instanceof BigInteger)
                return new Neq((Expr) arg1, Expr.valueOf(((BigInteger) arg2).longValue()));
        }

        if (arg2 instanceof Expr) {
            if (arg1 instanceof BigDecimal)
                return new Neq(Expr.valueOf(((BigDecimal) arg1).doubleValue()), (Expr) arg2);
            if (arg1 instanceof BigInteger)
                return new Neq(Expr.valueOf(((BigInteger) arg1).longValue()), (Expr) arg2);
        }

        throw new RuntimeException();
    }

}
