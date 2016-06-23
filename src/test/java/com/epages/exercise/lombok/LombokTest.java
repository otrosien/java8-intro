package com.epages.exercise.lombok;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.epages.exercise.lombok.Lombok.LineItem;
import com.epages.exercise.lombok.Lombok.Order;
import com.epages.exercise.lombok.Lombok.Product;

import lombok.SneakyThrows;

public class LombokTest {

    private final static Class<?> pc = Product.class;
    private final static Class<?> oc = Order.class;

    @Test
    public void product_fields_are_private() {
        Arrays.stream(pc.getDeclaredFields())
                .forEach(m -> assertThat(Modifier.isPrivate(m.getModifiers())).isTrue());
    }

    @Test
    public void most_product_methods_are_private() {
        Method[] ms = pc.getDeclaredMethods();

        getMethodNames(pc).stream().filter(m -> m.startsWith("get"));

        for (Method m : ms) {
            if ("setDescription".equals(m.getName())) {
                continue;
            }
            if (m.getName().startsWith("set")) {
                assertThat(Modifier.isPrivate(m.getModifiers())).isTrue();
            }
        }
    }

    @Test
    public void product_has_getter() {
        assertThat(getMethodNames(pc).stream().filter(m -> m.startsWith("get")).count()).isEqualTo(3);
    }

    @Test
    public void product_has_setter() {
        assertThat(getMethodNames(pc).stream().filter(m -> m.startsWith("set")).count()).isEqualTo(3);
    }

    @Test
    @SneakyThrows
    public void product_setDescription_is_public() {
        Class[] args = {String.class};
        Method m = pc.getDeclaredMethod("setDescription", args);

        assertThat(Modifier.isPublic(m.getModifiers())).isTrue();
    }

    // no NPE, but initialized
    @Test
    public void collection_is_initialized() {
        // hint: single
        Order.builder()
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void builder_does_not_like_null_one_way_or_another() {
        final Order[] order = new Order[1];
        Throwable thrown = catchThrowable(() -> order[0] = Order.builder().build());

        if (thrown == null) {
            assertThat(order[0].getCreatedAt()).isNotNull();
        } else {
            System.out.println("try the bonus");
            assertThat(thrown).isInstanceOf(NullPointerException.class);
        }
    }

    @Test
    public void line_items_field_not_null() {
        Assertions.assertThatThrownBy(() -> new LineItem(null, 1)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void line_items_are_equal_if_product_are_equal() {
        LineItem li1 = new LineItem(new Product("Deuter Backpack"), 1);
        LineItem li2 = new LineItem(new Product("Deuter Backpack"), 2);
        assertThat(li1).isEqualTo(li2);
    }

    @Test
    public void order_fields_are_final() {
        Arrays.stream(oc.getDeclaredFields())
                .forEach(m -> {
                    assertThat(Modifier.isPrivate(m.getModifiers())).isTrue();
                    assertThat(Modifier.isFinal((m.getModifiers()))).isTrue();
                });
    }

    @Test
    public void order_constructors_are_private() {
        Constructor<?>[] cs = oc.getConstructors();

        for (Constructor c: cs) {
            assertThat(Modifier.isPrivate(c.getModifiers())).isTrue();
        }
    }

    @Test
    @SneakyThrows
    public void bonus_order_builder_has_lineItem_method() {
        Class<?> c = orderBuilderClass();
        assertThat(getMethodNames(c)).contains("lineItem");
    }

    @Test
    @SneakyThrows
    public void bonus_order_builder_has_product_method() {
        Class<?> c = orderBuilderClass();
        assertThat(getMethodNames(c)).contains("product");
    }

    private static List<String> getMethodNames(Class<?> c) {
        return Arrays.stream(c.getDeclaredMethods())
                .map(Method::getName)
                .collect(Collectors.toList());
    }

    private static Class<?> orderBuilderClass() throws ClassNotFoundException {
        return Class.forName("com.epages.exercise.lombok.Lombok$Order$OrderBuilder");
    }
}
