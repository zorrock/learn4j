package io.luan.learn4j.structure.impl;

import io.luan.learn4j.structure.Tensor;
import lombok.Getter;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.Arrays;

/**
 * A Tensor in Mathematics is similar to a multi-dimensional array.
 * <p>
 * The Tensor class in Learn4j is a wrapper class to the underlying data store
 * <p>
 * For now the data store is ND4J.INDArray, but may change later
 *
 * @author Guangmiao Luan
 * @since 24/09/2017.
 */
public class TensorImpl implements Tensor {

    /**
     * The underlying value of the tensor.
     * This MAY change to a different type
     */
    @Getter
    private INDArray array;

    public TensorImpl(INDArray value) {
        this.array = value;
    }

    public int getRank() {
        return array.rank();
    }

    public int[] getShape() {
        return array.shape();
    }

    @Override
    public String toString() {
        if (this.array.isScalar()) {
            return "" + this.array.getDouble(0);
        }
        return this.array.toString() + " @ " + Arrays.toString(this.array.shape());
    }
}
