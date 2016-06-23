package com.epages.docs.streams;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.epages.docs.streams.Order.PaymentStatus;
import com.google.common.collect.ImmutableMap;
import com.neovisionaries.i18n.CurrencyCode;

public class OptionalStreamTest {

    private CustomerService service;

    private TestFixture fixture;

    @Before
    public void setupCustomerService() {
        fixture = new TestFixture();
        service = new CustomerService(fixture.customers());
    }

    @Test
    public void should_find_all() {
        assertThat(service.findAll()).hasSize(fixture.customers().size());
    }

    @Test
    public void should_find_by_name() {
        assertThat(service.findByName("Tom Sawyer")).isPresent();
    }

    @Test
    public void should_get_all_paid_orders() {
        assertThat(service.getAllPaidOrders(CurrencyCode.GBP).isEmpty()).isTrue();
    }

    @Test
    public void should_not_find_any_cancelled_orders() {
        assertThat(service.getOrdersByStatus("Tom Sawyer")).doesNotContainKey(PaymentStatus.CANCELLED);
    }

    @Test
    public void should_find_one_paid_order() {
        assertThat(service.getOrdersByStatus("Tom Sawyer").get(PaymentStatus.PAID)).hasSize(1);
    }

    @Test
    public void should_find_paid_chocolate_chip_order() {
        assertThat(service.getOrdersByStatus("Tom Sawyer").get(PaymentStatus.PAID)).allMatch(
                o -> "Chocolate Chip Cookie".equals(o.getLineItems().get(0).getProduct().getName())
        );
    }

    @Test
    public void should_have_invalid_lolly_order() {
        assertThat(service.validateOrder(fixture.openLollyOrder())).isFalse();
    }

    @Test
    public void should_have_valid_cookie_order() {
        assertThat(service.validateOrder(fixture.paidCookieOrder())).isTrue();
    }

    @Test
    public void should_find_happy_tom() {
        assertThat(service.getHappyCustomers()).allMatch(
                c -> "Tom Sawyer".equals(c.getName())
        );
    }

    @Test
    public void should_get_correct_outstanding_amount() {
        assertThat(service.getOutstandingAmount()).isEqualByComparingTo(1L);
    }

    private static class TestFixture {

        private List<Customer> customers() {
            return Arrays.asList(customerTom(), customerBetty());
        }

        private Customer customerTom() {
            return new Customer("Tom Sawyer", tomsOrders());
        }

        private Customer customerBetty() {
            return new Customer("Betty Barkeley", Collections.emptyList());
        }

        private List<Order> tomsOrders() {
            return Arrays.asList(openLollyOrder(), paidCookieOrder());
        }

        private Order paidCookieOrder() {
            return new Order(LocalDateTime.now(), cookieLineItems(), CurrencyCode.EUR, PaymentStatus.PAID);
        }

        private Order openLollyOrder() {
            return new Order(LocalDateTime.now(), lollyLineItems(), CurrencyCode.EUR, PaymentStatus.OPEN);
        }

        private List<LineItem> lollyLineItems() {
            return Arrays.asList(new LineItem(
                    new Product("Lolly", "delicious lolly", Collections.emptyMap()), 1L));
        }

        private List<LineItem> cookieLineItems() {
            return Arrays.asList(new LineItem(
                    new Product("Chocolate Chip Cookie", "Yummy.", ImmutableMap.of(CurrencyCode.EUR, 2L)), 2L));
        }

    }
}
