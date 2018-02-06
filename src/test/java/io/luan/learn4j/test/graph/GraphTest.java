package io.luan.learn4j.test.graph;

import io.luan.learn4j.core.Tensor;
import lombok.experimental.var;
import org.junit.Test;

import java.util.Date;

import static io.luan.learn4j.Learn4j.*;

/**
 * @author Guangmiao Luan
 * @since 28/08/2017.
 */
public class GraphTest {

    @Test
    public void testCreate() {

        Date now = new Date();
        var x_data = Tensor.create(new double[]{0.2, 0.3, 0.3, 0.4, 0.1, 0.1}, new int[]{3, 2});
        var y_data = Tensor.create(new double[]{0.3, 0.4, 0.4, 0.5}, new int[]{2, 2});
        var W_data = Tensor.create(new double[]{0.1, 0.2, 0.3, 0.4, 0.5, 0.6}, new int[]{2, 3});
        var b_data = Tensor.create(new double[]{0.6, 0.5}, new int[]{2, 1});

        var x = variable(new int[]{3, 2}, "X");
        var y = variable(new int[]{2, 2}, "Y");
        var W = parameter(W_data, "W");
        var b = parameter(b_data, "b");

        var yHat = sigmoid(add(matmul(W, x), b));
        var loss = reduceSum(square(subtract(y, yHat)));

        var optimizer = gradientDescentOptimizer(0.1);
        var train = optimizer.minimize(loss);

        Date beforeEval = new Date();

        x.setValue(x_data);
        y.setValue(y_data);

        for (int i = 0; i < 10000; i++) {
            train.eval();
        }

        Date after = new Date();
        println(W);
        println(b);
        println("Initial Step: " + (beforeEval.getTime() - now.getTime()));
        println("after Step: " + (after.getTime() - beforeEval.getTime()));
    }

}
