package com.epages.exercise.example.optional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static java.util.Locale.GERMANY;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Theories.class)
public class OptionalExamples {

    @Getter
    @Setter
    @AllArgsConstructor
    static class Message<T> {
        private final Optional<T> payload;
    }

    @Getter
    @RequiredArgsConstructor
    static class Product {
        private final Optional<Price> price;
        private boolean purchasable;
    }

    @Getter
    @AllArgsConstructor
    static class Price {
        private final Optional<Currency> currency;

    }

    private static Currency EUR = Currency.getInstance(GERMANY);

    private final static Message<Product> message = new Message<>(Optional.empty());
    private final static Message<Product> message1 = new Message<>(Optional.of(new Product(Optional.empty())));
    private final static Message<Product> message2 = new Message<>(Optional.of(new Product(Optional.of(new Price(Optional.empty())))));
    private final static Message<Product> message3 = new Message<>(Optional.of(new Product(Optional.of(new Price(Optional.of(EUR))))));

    @DataPoints("bad")
    public static List<Message<Product>> bad() {
        return ImmutableList.of(message, message1, message2);
    }

    @DataPoints("good")
    public static List<Message<Product>> good() {
        return ImmutableList.of(message3);
    }

    @Test
    public void create_of_anything() {
        Optional<Message<Product>> result = Optional.of(message);// Optional<?>

        assertThat(result).isPresent();
    }

    @Test(expected = NullPointerException.class)
    public void create_of() {
        Optional.of(null); // throws NPE immediately instead of deferring it to later access
    }

    @Test
    public void create_ofNullable() {
        Optional<Message<Product>> result = Optional.ofNullable(null); // Optional<?>

        assertThat(result).isEmpty();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class NullableBool {
        boolean value;
    }

    @Test
    public void conditional_do() {
        NullableBool bool = new NullableBool(false);

        message1.getPayload().ifPresent(msg -> bool.setValue(true));

        assertThat(bool.isValue()).isTrue();
    }

    @Test
    public void conditional_dont() {
        NullableBool bool = new NullableBool(false);

        message.getPayload().ifPresent(msg -> bool.setValue(true));

        assertThat(bool.isValue()).isFalse();
    }

    @Theory
    public void default_or(Message<Product> message) {
        Product product = message.getPayload().orElse(new Product(Optional.empty()));

        assertThat(product).isNotNull();
    }

    @Test(expected = RuntimeException.class)
    public void default_throw() {
        message.getPayload().orElseThrow(RuntimeException::new);
    }

    @Getter
    @Builder
    static class Service<T> {
        T value;
    }

    @Test
    public void default_get() {
        Product value = new Product(Optional.empty());
        Service<Product> service = Service.<Product>builder().value(value).build();

        Product product = message.getPayload().orElseGet(service::getValue);

        assertThat(value).isSameAs(product);
    }

    @Theory
    public void flatMap_simple(@FromDataPoints("bad") Message<Product> message) {
        Optional<Currency> currency =
                message.getPayload()          // Optional<Product>
                .flatMap(Product::getPrice)   // Optional<Price>
                .flatMap(Price::getCurrency); // Optional<Currency>

        assertThat(currency).isEmpty();
    }

    @Theory
    public void flatMap_with_defensive_init(@FromDataPoints("bad") Message<Product> message) {
        Optional<Currency> currency =
                Optional.ofNullable(message)  // Optional<Message>
                .flatMap(Message::getPayload) // Optional<Product>
                .flatMap(Product::getPrice)   // Optional<Price>
                .flatMap(Price::getCurrency); // Optional<Currency>

        assertThat(currency).isEmpty();
    }

    @Test
    public void map() {
        Optional<Integer> value =
                Optional.of("17 33 42")         // Optional<String>
                .map(s -> s.split(" "))         // Optional<String[]>
                .map(Arrays::asList)            // Optional<List<String>>
                .map(Lists::reverse)            // Optional<List<String>>
                .map(sl -> String.join("", sl)) // Optional<String>
                .map(Integer::parseInt);        // Optional<Integer>

        value.ifPresent(System.out::println);
    }


    @Test
    public void filter() {
        Optional<Integer> value =
                Optional.of("17 33 42")           // Optional<String>
                .map(s -> s.split(" "))           // Optional<String[]>
                .map(Arrays::asList)              // Optional<List<String>>
                .filter(sl -> !sl.isEmpty())
                .map(Lists::reverse)              // Optional<List<String>>
                .map(sl -> String.join("", sl))   // Optional<String>
                .filter(s -> s.matches("[0-9]+"))
                .map(Integer::parseInt);          // Optional<Integer>

        value.ifPresent(System.out::println);
    }

    @Theory
    public void flatMap_without_optional_return_type(@FromDataPoints("bad") Message<Product> message) {
        Currency currency =
                Optional.ofNullable(message)  // Optional<Message>
                .flatMap(Message::getPayload) // Optional<Product>
                .flatMap(Product::getPrice)   // Optional<Price>
                .flatMap(Price::getCurrency)  // Optional<Currency>
                .orElse(EUR);                 // Currency

        assertThat(currency).isEqualTo(EUR);
    }

    @Theory
    public void no_flatmap_makes_you_sad(@FromDataPoints("bad") Message<Product> message) {
        Optional<Optional<Product>> soSad =
                Optional.ofNullable(message)              // Optional<Message>
                .map(Message::getPayload);                // Optional<Optional<Product>>
    }


    @AllArgsConstructor
    @Getter
    static class Money {
        private final String currency;
        private final double amount;

        @Override
        public String toString() {
            return String.format("{} {}", amount, currency);
        }
    }

}