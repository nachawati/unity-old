package unity.modules.engines.nashorn;

import java.util.Iterator;

import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.BlockStatement;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.CaseNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.EmptyNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.GetSplitState;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.JumpToInlinedFinally;
import jdk.nashorn.internal.ir.LabelNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.PropertyNode;
import jdk.nashorn.internal.ir.ReturnNode;
import jdk.nashorn.internal.ir.RuntimeNode;
import jdk.nashorn.internal.ir.SetSplitState;
import jdk.nashorn.internal.ir.SplitNode;
import jdk.nashorn.internal.ir.SplitReturn;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.TernaryNode;
import jdk.nashorn.internal.ir.ThrowNode;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.runtime.Source;

public class NashornDXNodeVisitor extends NodeVisitor<LexicalContext>
{
    private final StringBuilder sb = new StringBuilder();
    private final Source        source;

    public NashornDXNodeVisitor(Source source)
    {
        super(new LexicalContext());
        this.source = source;
        sb.append("load('classpath:symbolics.js');");
    }

    @Override
    public boolean enterAccessNode(final AccessNode accessNode)
    {
        accessNode.getBase().accept(this);
        sb.append("." + accessNode.getProperty());
        // sb.append(source.getString(accessNode.getStart(), accessNode.getFinish() -
        // accessNode.getStart()));
        return false;
    }

    @Override
    public boolean enterBinaryNode(final BinaryNode binaryNode)
    {
        switch (binaryNode.tokenType()) {
        case ASSIGN:
        case EQ_STRICT:
        case COMMARIGHT:
            binaryNode.lhs().accept(this);
            sb.append(binaryNode.tokenType());
            binaryNode.rhs().accept(this);
            return false;
        }

        sb.append("_._");
        switch (binaryNode.tokenType()) {
        case ADD:
            sb.append("add");
            break;
        case LE:
            sb.append("le");
            break;
        case LT:
            sb.append("lt");
            break;
        case AND:
            sb.append("and");
            break;
        case OR:
            sb.append("or");
            break;
        default:
            System.err.println(binaryNode);
        }
        sb.append("(");
        binaryNode.lhs().accept(this);
        sb.append(",");
        binaryNode.rhs().accept(this);
        sb.append(")");
        return false;
    }

    @Override
    public boolean enterBlock(final Block block)
    {
        sb.append("{");
        block.getStatements().forEach(s -> s.accept(this));
        sb.append("}");
        return false;
    }

    @Override
    public boolean enterBlockStatement(final BlockStatement blockStatement)
    {
        blockStatement.getBlock().accept(this);
        return false;
    }

    @Override
    public boolean enterBreakNode(final BreakNode breakNode)
    {
        sb.append("break ");
        if (breakNode.getLabelName() != null)
            sb.append(breakNode.getLabelName());
        sb.append(";");
        return false;
    }

    @Override
    public boolean enterCallNode(final CallNode callNode)
    {
        if (callNode.getFunction() instanceof FunctionNode)
            sb.append("(");
        callNode.getFunction().accept(this);
        if (callNode.getFunction() instanceof FunctionNode)
            sb.append(")");
        sb.append("(");
        for (final Iterator<Expression> i = callNode.getArgs().iterator(); i.hasNext();) {
            i.next().accept(this);
            if (i.hasNext())
                sb.append(",");
        }
        sb.append(")");
        return false;
    }

    @Override
    public boolean enterCaseNode(final CaseNode caseNode)
    {
        return false;
    }

    @Override
    public boolean enterCatchNode(final CatchNode catchNode)
    {
        return false;
    }

    @Override
    public boolean enterContinueNode(final ContinueNode continueNode)
    {
        sb.append("continue ");
        if (continueNode.getLabelName() != null)
            sb.append(continueNode.getLabelName());
        sb.append(";");
        return false;
    }

    @Override
    public boolean enterEmptyNode(final EmptyNode emptyNode)
    {
        return false;
    }

    @Override
    public boolean enterExpressionStatement(final ExpressionStatement expressionStatement)
    {
        expressionStatement.getExpression().accept(this);
        sb.append(";");
        return false;
    }

    @Override
    public boolean enterForNode(final ForNode forNode)
    {
        if (forNode.isForIn()) {
            sb.append("for(");
            if (forNode.getInit() != null)
                forNode.getInit().accept(this);
            sb.append(" in ");
            if (forNode.getModify() != null)
                forNode.getModify().accept(this);
            sb.append(")");
            forNode.getBody().accept(this);
        } else {
            sb.append("for(");
            if (forNode.getInit() != null)
                forNode.getInit().accept(this);
            sb.append(";");
            if (forNode.getTest() != null)
                forNode.getTest().accept(this);
            sb.append(";");
            if (forNode.getModify() != null)
                forNode.getModify().accept(this);
            sb.append(")");
            forNode.getBody().accept(this);
        }

        return false;
    }

    @Override
    public boolean enterFunctionNode(final FunctionNode functionNode)
    {
        if (!functionNode.isProgram()) {
            sb.append("function ");
            if (!functionNode.isAnonymous())
                functionNode.getIdent().accept(this);
            sb.append("(");
            for (final Iterator<IdentNode> i = functionNode.getParameters().iterator(); i.hasNext();) {
                i.next().accept(this);
                if (i.hasNext())
                    sb.append(",");
            }
            sb.append(")");
            functionNode.getBody().accept(this);
        } else
            functionNode.getBody().getStatements().forEach(s -> s.accept(this));
        return false;
    }

    @Override
    public boolean enterGetSplitState(final GetSplitState getSplitState)
    {
        return false;
    }

    @Override
    public boolean enterIdentNode(final IdentNode identNode)
    {
        sb.append(source.getString(identNode.getStart(), identNode.getFinish() - identNode.getStart()));
        return false;
    }

    @Override
    public boolean enterIfNode(final IfNode ifNode)
    {
        sb.append("if(");
        ifNode.getTest().accept(this);
        sb.append(")");
        ifNode.getPass().accept(this);
        if (ifNode.getFail() != null) {
            sb.append("else");
            ifNode.getFail().accept(this);
        }
        return false;
    }

    @Override
    public boolean enterIndexNode(final IndexNode indexNode)
    {
        sb.append(source.getString(indexNode.getStart(), indexNode.getFinish() - indexNode.getStart()));
        return false;
    }

    @Override
    public boolean enterJoinPredecessorExpression(final JoinPredecessorExpression expr)
    {
        sb.append("(");
        expr.getExpression().accept(this);
        sb.append(")");
        return false;
    }

    @Override
    public boolean enterJumpToInlinedFinally(final JumpToInlinedFinally jumpToInlinedFinally)
    {
        return true;
    }

    @Override
    public boolean enterLabelNode(final LabelNode labelNode)
    {
        sb.append(labelNode.getLabelName() + ":");
        labelNode.getBody().accept(this);
        return false;
    }

    @Override
    public boolean enterLiteralNode(final LiteralNode<?> literalNode)
    {
        if (literalNode.isString())
            sb.append("\"");
        sb.append(source.getString(literalNode.getStart(), literalNode.getFinish() - literalNode.getStart()));
        if (literalNode.isString())
            sb.append("\"");
        return enterDefault(literalNode);
    }

    @Override
    public boolean enterObjectNode(final ObjectNode objectNode)
    {
        sb.append("{");
        for (final Iterator<PropertyNode> i = objectNode.getElements().iterator(); i.hasNext();) {
            i.next().accept(this);
            if (i.hasNext())
                sb.append(",");
        }
        sb.append("}");
        return false;
    }

    @Override
    public boolean enterPropertyNode(final PropertyNode propertyNode)
    {
        propertyNode.getKey().accept(this);
        sb.append(":");
        propertyNode.getValue().accept(this);
        return false;
    }

    @Override
    public boolean enterReturnNode(final ReturnNode returnNode)
    {
        sb.append("return ");
        returnNode.getExpression().accept(this);
        sb.append(";");
        return false;
    }

    @Override
    public boolean enterRuntimeNode(final RuntimeNode runtimeNode)
    {
        return false;
    }

    @Override
    public boolean enterSetSplitState(final SetSplitState setSplitState)
    {
        return false;
    }

    @Override
    public boolean enterSplitNode(final SplitNode splitNode)
    {
        return false;
    }

    @Override
    public boolean enterSplitReturn(final SplitReturn splitReturn)
    {
        return false;
    }

    @Override
    public boolean enterSwitchNode(final SwitchNode switchNode)
    {
        return false;
    }

    @Override
    public boolean enterTernaryNode(final TernaryNode ternaryNode)
    {
        sb.append("_._if(");
        ternaryNode.getTest().accept(this);
        sb.append(",");
        ternaryNode.getTrueExpression().accept(this);
        sb.append(",");
        ternaryNode.getFalseExpression().accept(this);
        sb.append(")");
        return false;
    }

    @Override
    public boolean enterThrowNode(final ThrowNode throwNode)
    {
        sb.append("throw ");
        throwNode.getExpression().accept(this);
        sb.append(";");
        return false;
    }

    @Override
    public boolean enterTryNode(final TryNode tryNode)
    {
        return false;
    }

    @Override
    public boolean enterUnaryNode(final UnaryNode unaryNode)
    {
        switch (unaryNode.tokenType()) {
        case NEW:
            sb.append("new ");
            unaryNode.getExpression().accept(this);
            return false;
        }

        sb.append("_._");
        switch (unaryNode.tokenType()) {
        case INCPREFIX:
            sb.append("incprefix");
            break;
        case INCPOSTFIX:
            sb.append("incpostfix");
            break;
        }
        sb.append("(");
        unaryNode.getExpression().accept(this);
        sb.append(")");
        return false;
    }

    @Override
    public boolean enterVarNode(final VarNode varNode)
    {
        if (varNode.isFunctionDeclaration()) {
            varNode.getInit().accept(this);
            return false;
        }
        // varNode.getAssignmentDest().accept(this);
        sb.append("var ");
        varNode.getName().accept(this);
        // varNode.getAssignmentSource().accept(this);
        if (varNode.getInit() != null) {
            sb.append("=");
            varNode.getInit().accept(this);
        }
        sb.append(";");
        return false;
    }

    @Override
    public boolean enterWhileNode(final WhileNode whileNode)
    {
        if (whileNode.isDoWhile()) {
            sb.append("do {");
            whileNode.getBody().accept(this);
            sb.append("} while (");
            whileNode.getTest().accept(this);
            sb.append(")");
        } else
            sb.append("while");

        return false;
    }

    @Override
    public boolean enterWithNode(final WithNode withNode)
    {
        return false;
    }

    @Override
    public String toString()
    {
        return sb.toString();
    }
}
