package io.luan.learn4j.structure.impl.transform;

import io.luan.learn4j.structure.Expression;
import io.luan.learn4j.structure.ExpressionType;
import io.luan.learn4j.structure.impl.base.TransformExpression;
import io.luan.learn4j.visitor.Visitor;

/**
 * @author Guangmiao Luan
 * @since 28/08/2017.
 */
public class Sine extends TransformExpression {

    public Sine(Expression base, String name) {
        super(base, name);
    }

    @Override
    public void accept(Visitor visitor, Object... params) {
        visitor.visitSine(this, params);
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.Sine;
    }

}