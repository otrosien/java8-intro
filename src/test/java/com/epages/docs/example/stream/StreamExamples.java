package com.epages.docs.example.stream;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Currency;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Locale.GERMANY;

@RunWith(Theories.class)
public class StreamExamples {

    private static Currency EUR = Currency.getInstance(GERMANY);

    @Getter
    @AllArgsConstructor
    static class Product {
        private final Optional<Price> price;
    }
    @Getter
    @AllArgsConstructor
    static class Price {
        private final Optional<Currency> currency;
    }


    @Getter
    @AllArgsConstructor
    static class Product2 {
        private final Price2 price;

    }
    @Getter
    @AllArgsConstructor()
    static class Price2 {
        private final Currency currency;
        private final Double amount;
    }

    @Test
    public void simple_example() {
        Collection<Product> products = Lists.newArrayList();

        for (Product product : products) {
            System.out.println(product);
        }

        products.stream().forEach(System.out::println);

    }

    @Test
    public void example_for_and_stream() {
        Collection<Product2> products = Lists.newArrayList();

        double sum = 0;
        for (Product2 product : products) {
            if (product.getPrice().getCurrency().equals(EUR)) {
                sum += product.getPrice().getAmount();
            }
        }

        products
                .stream()
                .map(Product2::getPrice)
                .filter(price -> price.getCurrency().equals(EUR))
                .mapToDouble(Price2::getAmount)
                .sum();
    }

    @Test
    public void datasources() {
        // Collection.stream()
        Stream<Product> stream1 = new ArrayList<Product>().stream();

        // static method to create a stream from an array
        IntStream stream2 = Arrays.stream(new int[] { 1, 2, 3 });

        // unlimited stream of doubles
        DoubleStream doubles = new Random().doubles();

        // static method
        Stream<String> strings = Stream.of("a", "b", "c");
    }

    @Test
    public void stream_operations() {
        Stream<Product> stream = new ArrayList<Product>().stream();

        Stream<Currency> limit =
                stream
                .map(Product::getPrice)
                .filter(Optional::isPresent) // nobody thought about Optional<->Stream interoperability?
                .map(Optional::get)
                .map(Price::getCurrency)
                .filter(Optional::isPresent) // this is painful
                .map(Optional::get)
                .filter(c -> c.equals(EUR))
                .limit(10);
    }


    @Test
    public void stream_example() {
        Stream<Product2> stream = new ArrayList<Product2>().stream();

        stream
                .map(Product2::getPrice)
                .peek(System.out::println)
                .filter(p -> p.getCurrency().equals(EUR))
                .sorted((e1, e2) -> Double.compare(e1.getAmount(), e2.getAmount()))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Test
    public void peek() {
        Optional<String> reduce =
                Stream.of("java", "haskell", "scala", "rust")
                .peek(System.out::println)
                .filter(s -> !s.contains("a"))
                .map(String::toUpperCase)
                .reduce(String::join);

        reduce.ifPresent(System.out::println);
    }

    @Test
    public void toMap() {
        Stream<Product2> stream = new ArrayList<Product2>().stream();

        stream
                .map(Product2::getPrice)
                .collect(Collectors.toMap(
                        Price2::getCurrency,
                        Price2::getAmount,
                        (p1, p2) -> p1+p2));
    }

    @Test
    public void groupingBy() {
        Stream<Product2> stream = new ArrayList<Product2>().stream();

        stream
                .collect(Collectors.groupingBy(p -> p.getPrice().getCurrency()));
    }

}