package io.luan.learn4j.structure.impl;

import io.luan.learn4j.structure.Expression;
import io.luan.learn4j.structure.ExpressionType;
import io.luan.learn4j.structure.factory.ExpressionFactory;
import io.luan.learn4j.visitor.Visitor;

/**
 * Scalar Multiply
 *
 * @author Guangmiao Luan
 * @since 28/08/2017.
 */
public class Square extends UnaryOp {

    public Square(String name, Expression base) {
        super(name, base);
    }

    @Override
    public void accept(Visitor visitor, Object... params) {
        visitor.visitSquare(this, params);
    }

    @Override
    public int getRank() {
        // TODO: Should check for broadcast rules
        return getBase().getRank();
    }

    @Override
    public int[] getShape() {
        // TODO: Should check for broadcast rules
        return getBase().getShape();
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.Square;
    }

    protected Expression createGradient(Expression target) {
        Expression baseGrad = getBase().getGradient(target);
        String gradName = this.getName() + "_" + target.getName();

        String mulName = gradName + "$mul";
        Expression mul = ExpressionFactory.createMultiply(mulName, getBase(), baseGrad);

        Constant two = Constant.TWO;

        return ExpressionFactory.createMultiply(gradName, two, mul);
    }
}
