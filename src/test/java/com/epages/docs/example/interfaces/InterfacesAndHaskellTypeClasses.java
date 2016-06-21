package com.epages.docs.example.interfaces;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Just for funsies!
 *
 * This is an proof of concept modelling of some of Haskell's type classes for numbers.
 * It implements the type classes Eq and Ord, Num and Bounded and the instances for Int.
 *
 * Some of this code will look weird because the concepts are different and there is
 * a lot of shoehorning required.
 */
@RunWith(JUnitQuickcheck.class)
public class InterfacesAndHaskellTypeClasses {

    // necessary, otherwise there is no ref to this T
    interface Necessary<T extends Necessary<T>> {
        T getThis();
    }

    interface Eq<T extends Eq<T>> extends Necessary<T> {
        // minimal complete definition:
        //   either equal or unequal

        default boolean equal(T v) {
            return unequal(v);
        }

        default boolean unequal(T v) {
            return !equal(v);
        }
    }

    interface Ord<T extends Ord<T>> extends Eq<T> {
        // minimal complete definition:
        //   either lessEqual or compare

        default CompareResult compare(T v) {
            if (equal(v)) {
                return CompareResult.EQ;
            } else if (this.lessEqual(v)) {
                return CompareResult.LT;
            }
            return CompareResult.GT;
        }

        default boolean lessEqual(T v) {
            return compare(v).unequal(CompareResult.GT);
        }

        default boolean lessThan(T v) {
            return compare(v).equal(CompareResult.LT);
        }

        default boolean greaterEqual(T v) {
            return compare(v).unequal(CompareResult.LT);
        }

        default boolean greaterThan(T v) {
            return compare(v).equal(CompareResult.GT);
        }

        default T max(T v) {
            return this.greaterEqual(v) ? getThis() : v;
        }

        default T min(T v) {
            return this.lessThan(v) ? getThis(): v;
        }
    }

    // does not make too much sense as instance methods
    interface Bounded<T extends Bounded<T>> {
        T minBound();
        T maxBound();
    }

    interface Num<T extends Num<T>> extends Eq<T> {
        // Minimal complete definition
        //   all except negate or minus

        // not a proper translation, but necessary
        // also weird as an instance method
        T zero();

        default T negate() {
            return zero().minus(this.getThis());
        };

        default T minus(T v) {
            return this.plus(v.negate());
        }

        T plus(T v);

        T multiply(T v);

        T abs();

        T signum();
    }

    enum CompareResult implements Eq<CompareResult> {
        LT,
        EQ,
        GT;

        @Override
        public CompareResult getThis() {
            return this;
        }

        @Override
        public boolean equal(CompareResult v) {
            return this == v;
        }
    }

    @AllArgsConstructor
    static class Int implements Num<Int>, Bounded<Int>, Ord<Int> {

        private final static Int ZERO = new Int(0);
        private final static Int MIN_VALUE = new Int(Integer.MIN_VALUE);
        private final static Int MAX_VALUE = new Int(Integer.MAX_VALUE);

        private final int value;

        @Override
        public Int getThis() {
            return this;
        }

        @Override
        public boolean equal(Int v) {
            return this.value == v.value;
        }

        @Override
        public boolean lessEqual(Int v) {
            return this.value <= v.value;
        }

        @Override
        public Int zero() {
            return ZERO;
        }

        @Override
        public Int negate() {
            return new Int(-value);
        }

        @Override
        public Int plus(Int v) {
            return new Int(this.value + v.value);
        }

        @Override
        public Int multiply(Int v) {
            return new Int(this.value * v.value);
        }

        @Override
        public Int abs() {
            return new Int(Math.abs(this.value));
        }

        @Override
        public Int signum() {
            return new Int(Integer.signum(this.value));
        }

        @Override
        public Int minBound() {
            return MIN_VALUE;
        }

        @Override
        public Int maxBound() {
            return MAX_VALUE;
        }
    }

    // to make testing easy for now
    @Property
    public void test_everything(int n0, int m0) {
        Int n = new Int(n0);
        Int m = new Int(m0);

        if (n0 == m0) {
            assertThat(n.equal(m)).isTrue();
            assertThat(n.greaterEqual(m)).isTrue();
            assertThat(n.lessEqual(m)).isTrue();
        } else {
            assertThat(n.unequal(m)).isTrue();
        }

        if (n0 < m0) {
            assertThat(n.lessThan(m)).isTrue();
        }

        if (n0 > m0) {
            assertThat(n.greaterThan(m)).isTrue();
        }

        assertThat(n.max(m).value).isEqualTo(Math.max(n0, m0));
        assertThat(n.min(m).value).isEqualTo(Math.min(n0, m0));

        assertThat(n.plus(m).value).isEqualTo(n0+m0);
        assertThat(n.minus(m).value).isEqualTo(n0-m0);
        assertThat(n.multiply(m).value).isEqualTo(n0*m0);

        // just for n
        assertThat(n.signum().value).isEqualTo(Integer.signum(n0));
        assertThat(n.abs().value).isEqualTo(Math.abs(n0));
        assertThat(n.zero().value).isEqualTo(0);
    }

    @Property
    public void equal(int n) {
        Assertions.assertThat(new Int(n).equal(new Int(n))).isTrue();
    }

    @Property
    public void unequal(int n, int offset) {
        Assertions.assertThat(new Int(n).unequal(new Int(n+offset))).isTrue();
    }

    @Property
    public void lessThan(
            @InRange(minInt = (Integer.MIN_VALUE/2), maxInt = (Integer.MAX_VALUE / 2)-1) int n,
            @InRange(minInt = 1                    , maxInt = (Integer.MAX_VALUE / 2)-1) int offset)
    {
        Assertions.assertThat(new Int(n).lessThan(new Int(n+offset))).isTrue();
    }

    @Property
    public void greaterThan(
            @InRange(minInt = (Integer.MIN_VALUE/2), maxInt = (Integer.MAX_VALUE / 2)-1) int n,
            @InRange(minInt = 1                    , maxInt = (Integer.MAX_VALUE / 2)-1) int offset)
    {
        Assertions.assertThat(new Int(n).greaterThan((new Int(n-offset)))).isTrue();
    }

    @Property
    public void lessEqual(
            @InRange(minInt = (Integer.MIN_VALUE/2), maxInt = (Integer.MAX_VALUE / 2)-1) int n,
            @InRange(minInt = 0                    , maxInt = (Integer.MAX_VALUE / 2)-1) int offset)
    {
        Assertions.assertThat(new Int(n).lessEqual(new Int(n+offset))).isTrue();
    }

    @Property
    public void lessEqual_more_equals(
            @InRange(minInt = (Integer.MIN_VALUE/2), maxInt = (Integer.MAX_VALUE / 2)-1) int n)
    {
        Assertions.assertThat(new Int(n).lessEqual(new Int(n))).isTrue();
    }

    @Property
    public void greaterEqual(
            @InRange(minInt = (Integer.MIN_VALUE/2), maxInt = (Integer.MAX_VALUE / 2)-1) int n,
            @InRange(minInt = 0                    , maxInt = (Integer.MAX_VALUE / 2)-1) int offset)
    {
        Assertions.assertThat(new Int(n).greaterEqual(new Int(n-offset))).isTrue();
    }

    @Property
    public void min(int n, int m) {
        Assertions.assertThat(new Int(n).min(new Int(m)).value).isEqualTo(Math.min(n, m));
    }

    @Property
    public void max(int n, int m) {
        Assertions.assertThat(new Int(n).max(new Int(m)).value).isEqualTo(Math.max(n, m));
    }

    @Property
    public void plus(int n, int m) {
        assertThat(new Int(n).plus(new Int(m)).value).isEqualTo(n+m);
    }

    @Property
    public void minus(int n, int m) {
        assertThat(new Int(n).minus(new Int(m)).value).isEqualTo(n-m);
    }

    @Property
    public void multiply(int n, int m) {
        assertThat(new Int(n).multiply(new Int(m)).value).isEqualTo(n*m);
    }

    @Property
    public void zero(int n) {
        assertThat(new Int(n).zero().value).isEqualTo(0);
    }

    @Property
    public void abs(int n) {
        assertThat(new Int(n).abs().value).isEqualTo(Math.abs(n));
    }

    @Property
    public void signum(int n) {
        assertThat(new Int(n).signum().value).isEqualTo(Integer.signum(n));
    }

    @Property
    public void minBound(int n) {
        assertThat(new Int(n).minBound().value).isEqualTo(Integer.MIN_VALUE);
    }

    @Property
    public void maxBound(int n) {
        assertThat(new Int(n).maxBound().value).isEqualTo(Integer.MAX_VALUE);
    }
}