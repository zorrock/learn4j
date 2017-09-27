package io.luan.learn4j.structure.impl;

import io.luan.learn4j.Tensor;
import io.luan.learn4j.compute.ComputeNode;
import io.luan.learn4j.compute.impl.ParameterNode;
import io.luan.learn4j.structure.Expression;
import io.luan.learn4j.structure.ExpressionType;
import lombok.Getter;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * Parameter is holder for parameters to be updated on each gradient descend run
 * <p>
 * This is equivalent to TensorFlow.Variable
 *
 * @author Guangmiao Luan
 * @since 28/08/2017.
 */
public class Parameter extends BaseExpression {

    @Getter
    private Tensor value;

    public Parameter(String name, Tensor value) {
        super(name);
        this.value = value;
    }

    @Override
    public Expression getGradient(Expression target) {
        if (target == this) {
            return Constant.ONE;
        }
        return Constant.ZERO;
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.Parameter;
    }

}