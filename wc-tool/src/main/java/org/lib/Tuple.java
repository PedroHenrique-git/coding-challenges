package org.lib;

public class Tuple<A, B, C, D> {
    public final A valueA;
    public final B valueB;
    public final C valueC;
    public final D valueD;

    public Tuple(A valueA, B valueB, C valueC, D valueD) {
        this.valueA = valueA;
        this.valueB = valueB;
        this.valueC = valueC;
        this.valueD = valueD;
    }
}
