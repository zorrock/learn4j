package io.luan.learn4j.structure.impl.base;

import io.luan.learn4j.core.Tensor;
import io.luan.learn4j.structure.Expression;
import io.luan.learn4j.structure.ExpressionState;
import io.luan.learn4j.structure.Graph;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A base implementation of the Expression interface.
 *
 * @author Guangmiao Luan
 * @since 31/08/2017.
 */
public abstract class BaseExpression implements Expression {

    private static AtomicInteger ID_COUNTER = new AtomicInteger(1);

    private Map<Expression, Expression> gradientMap = new HashMap<>();

    @Getter
    private Graph graph;

    @Getter
    private int id;

    @Getter
    private String name;

    @Getter
    @Setter
    private ExpressionState state;

    protected BaseExpression(String name) {
        this.id = ID_COUNTER.getAndIncrement();
        this.state = ExpressionState.Detached;
        this.name = name;
    }

    @Override
    public Tensor eval() {
        if (this.graph == null || this.graph.getSession() == null) {
            throw new IllegalStateException("Cannot Evaluate when not attached");
        }

        return this.graph.getSession().eval(this);
    }

    @Override
    public Expression getGradient(Expression target) {
        return gradientMap.get(target);
    }

    public Expression getGradient() {
        return gradientMap.values().stream().findFirst().get();
    }

    @Override
    public void attach(Graph graph) {
        if (this.state != ExpressionState.Detached) {
            throw new IllegalStateException("Not allowed, detach first");
        }
        this.graph = graph;
        this.state = ExpressionState.Attached;
    }

    @Override
    public Tensor getValue() {
        if (this.graph == null || this.graph.getSession() == null) {
            return null;
        }

        return this.graph.getSession().getValue(this);
    }

    @Override
    public void setValue(Tensor value) {
        if (this.graph == null || this.graph.getSession() == null) {
            throw new IllegalStateException("Cannot Set Value if not attached to a graph");
        }

        this.graph.getSession().setValue(this, value);
        this.state = ExpressionState.Modified;
    }

    @Override
    public void setGradient(Expression target, Expression gradient) {
        gradientMap.put(target, gradient);
    }

    @Override
    public String toString() {
        String result = this.getType() + "[" + this.getId() + "]: ";
        Tensor value = this.getValue();
        if (value != null) {
            result += "\n" + value.toString();
        } else {
            result += "Not Evaluated";
        }
        return result;
    }
}