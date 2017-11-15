package io.dgms.unity.modules.symbolics.symengine;

public class Lt extends Expr
{
    private final Expr operand1;
    private final Expr operand2;

    public Lt(Expr operand1, Expr operand2)
    {
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    @Override
    public Expr add(Expr operand)
    {
        return new Lt(operand1.add(operand), operand2.add(operand));
    }

    @Override
    public Expr lt(Expr operand)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString()
    {
        return operand1.toString() + " < " + operand2.toString();
    }
}
