package unity.modules.symbolics.symengine;

public abstract class Expr
{
    public final boolean symbolic = true;

    public abstract Expr add(Expr operand);

    public abstract Expr lt(Expr operand);
}
