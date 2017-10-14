package io.luan.learn4j.visitor.impl;

import io.luan.learn4j.structure.Expression;
import io.luan.learn4j.structure.Tensor;
import io.luan.learn4j.structure.impl.*;
import io.luan.learn4j.utils.TensorMath;
import lombok.Getter;

import java.util.Map;

/**
 * @author Guangmiao Luan
 * @since 31/08/2017.
 */
public class EvaluationVisitor extends BaseVisitor {

    @Getter
    private Map<Expression, Tensor> feedMap;

    @Getter
    private Map<Expression, Tensor> valueMap;

    public EvaluationVisitor(Map<Expression, Tensor> feedMap, Map<Expression, Tensor> valueMap) {
        this.feedMap = feedMap;
        this.valueMap = valueMap;
    }

    public Tensor getValue(Expression exp) {
        return valueMap.get(exp);
    }

    @Override
    protected void visitAdd(Add node, Object... params) {
        super.visitAdd(node);
        Tensor left = valueMap.get(node.getLeft());
        Tensor right = valueMap.get(node.getRight());

        Tensor sum = TensorMath.add(left, right);
        valueMap.put(node, sum);
    }

    @Override
    protected void visitConstant(Constant node, Object... params) {
        valueMap.put(node, node.getValue());
    }

    @Override
    protected void visitMatMul(MatMul node, Object... params) {
        super.visitMatMul(node);
        Tensor left = valueMap.get(node.getLeft());
        Tensor right = valueMap.get(node.getRight());

        Tensor prod = TensorMath.matmul(left, right);
        valueMap.put(node, prod);
    }

    @Override
    protected void visitMultiply(Multiply node, Object... params) {
        super.visitMultiply(node);
        Tensor left = valueMap.get(node.getLeft());
        Tensor right = valueMap.get(node.getRight());

        Tensor prod = TensorMath.multiply(left, right);
        valueMap.put(node, prod);
    }

    @Override
    protected void visitParameter(Parameter node, Object... params) {
        valueMap.put(node, node.getValue());
    }

    //
//    @Override
//    public void visitPower(PowerNode node) {
//        super.visitPower(node);
//
//        INDArray base = node.getBase().getValue();
//        INDArray power = node.getPower().getValue();
//
//        int intPower = power.getInt(0, 0);
//        double doubleBase = base.getDouble(0, 0);
//        double doubleResult = Math.pow(doubleBase, intPower);
//        INDArray result = Nd4j.zeros(1, 1).addi(doubleResult);
//        node.setValue(result);
//    }
//
    @Override
    protected void visitReduceMean(ReduceMean node, Object... params) {
        super.visitReduceMean(node);
        Tensor base = valueMap.get(node.getBase());
        Tensor reduced = TensorMath.reduceMean(base);
        valueMap.put(node, reduced);
    }

    @Override
    protected void visitSquare(Square node, Object... params) {
        super.visitSquare(node);
        Tensor base = valueMap.get(node.getBase());
        Tensor squared = TensorMath.square(base);
        valueMap.put(node, squared);
    }

    @Override
    protected void visitSubtract(Subtract node, Object... params) {
        super.visitSubtract(node);
        Tensor left = valueMap.get(node.getLeft());
        Tensor right = valueMap.get(node.getRight());

        Tensor diff = TensorMath.subtract(left, right);
        valueMap.put(node, diff);
    }

    @Override
    protected void visitVariable(Variable node, Object... params) {
        Tensor feedVal = feedMap.get(node);
        if (feedVal != null) {
            valueMap.put(node, feedVal);
        }
    }

    @Override
    protected void visitAssign(Assign node, Object... params) {
        super.visitAssign(node);
        Tensor newTensor = valueMap.get(node.getNewValue());

        Expression target = node.getTarget();
        if (target instanceof Parameter) {
            Parameter targetParam = (Parameter) target;
            targetParam.setValue(newTensor);
        }

        valueMap.put(node, newTensor);
        valueMap.put(target, newTensor);
    }

}
