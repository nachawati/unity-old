package io.dgms.unity.modules.symbolics.symengine;

public abstract class Expr
{
    public abstract Expr add(Expr operand);

    public abstract Expr lt(Expr operand);
}
