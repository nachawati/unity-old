package io.dgms.unity.modules.symbolics.symengine;

import com.sun.jna.Pointer;

public class Basic extends Expr
{
    protected final Pointer s;

    private Basic()
    {
        s = sym.basic_new_heap();
    }

    public Basic(String name)
    {
        s = sym.basic_new_heap();
        sym.symbol_set(s, name);
    }

    @Override
    public Expr add(Expr operand)
    {
        final Basic r = new Basic();
        sym.basic_add(r.s, s, ((Basic) operand).s);
        return r;
    }

    @Override
    public void finalize()
    {
        sym.basic_free_heap(s);
    }

    @Override
    public Expr lt(Expr operand)
    {
        return new Lt(this, operand);
    }

    @Override
    public String toString()
    {
        final Pointer p = sym.basic_str(s);
        final String str = p.getString(0);
        sym.basic_str_free(p);
        return str;
    }

    private static final SymEngine sym = SymEngine.INSTANCE;

    public static Basic of(Number value)
    {
        final Basic b = new Basic();
        if (value instanceof Double)
            sym.real_double_set_d(b.s, (double) value);
        else
            sym.integer_set_si(b.s, (long) value);
        return b;
    }

}
