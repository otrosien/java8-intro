package com.epages.docs.example.lambda;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

import java.util.function.Function;

@RunWith(Theories.class)
public class LambdaExamples {


    // just a marker interface, can be omitted
    // but will not compile if you add another abstract method
    // also known as SAM type - Single Abstract Method
    @FunctionalInterface
    interface Transformer {

        String transform(Integer number);

        default String defaultMethod() {
            return "still a FunctionalInterface, just one abstract method";
        }
    }

    // almost Function
    @FunctionalInterface
    interface GenericTransformer<FROM, TO> {
        TO transform(FROM number);
    }

    static class ClassWithIntegerConstructor {
        public ClassWithIntegerConstructor(Integer number) {
        }
    }

    public static void print(Integer number, Transformer transformer) {
        System.out.println(transformer.transform(number));
    }

    public static <FROM, TO> void genericPrint(FROM number, GenericTransformer<FROM, TO> transformer) {
        System.out.println(transformer.transform(number));
    }

    public static <FROM, TO> void functionPrint(FROM number, Function<FROM,TO> transformer) {
        System.out.println(transformer.apply(number));
    }

    @Test
    public void test() {
        // these are more of less equivalent
        print(1, n -> n.toString()); // lambda expression
        print(1, Object::toString);  // method reference
        print(1, String::valueOf);   // static method reference
//      print(1, String::new);       // would work if String had a Constructor with an Integer argument
    }

    @Test
    public void test2() {
        genericPrint(1, n -> n.toString());
        genericPrint(1, Object::toString);
        genericPrint(1, String::valueOf);

        // try some types
        genericPrint("42", Integer::valueOf);
    }

    @Test
    public void test3() {
        functionPrint(1, n -> n.toString());
        functionPrint(1, Object::toString);
        functionPrint(1, String::valueOf);

        // try some types
        functionPrint("42", Integer::valueOf);
    }
}