package com.epages.exercise.stream;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.neovisionaries.i18n.CurrencyCode;

class CustomerService {

    private final List<Customer> customers;

    public CustomerService(List<Customer> customers) {
        this.customers = Collections.unmodifiableList(customers);
    }

    /**
     * Return all customers.
     */
    public List<Customer> findAll() {
        return customers;
    }

    /**
     * Find a customer by name.
     * Assume that customer names are unique.
     */
    Optional<Customer> findByName(String name) {
        return findAll().stream()
                .filter(c -> name.equals(c.getName()))
                .findFirst();
    }

    /**
     * Get all Orders paid in the given currency.
     */
    public List<Order> getAllPaidOrders(CurrencyCode currency) {
        return findAll()
        .stream()
        .map(Customer::getOrders)
        .flatMap(List::stream)
        .filter(o -> currency.equals(o.getCurrency()))
        .filter(Order::isPaid)
        .collect(Collectors.toList())
        ;
    }

    /**
     * Get a customer's orders by paymentStatus status.
     */
    public Map<Order.PaymentStatus, List<Order>> getOrdersByStatus(String customerName) {
        return findByName(customerName)
                .map(Customer::getOrders)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.groupingBy(Order::getPaymentStatus));
    }

    /**
     * Get all customers that have cancelled at least one order
     */
    public List<Customer> getUnhappyCustomers() {
        return findAll()
                .stream()
                .filter(c -> 
                    c.getOrders().stream().anyMatch(o -> Order.PaymentStatus.CANCELLED.equals(o.getPaymentStatus()))
                )
                .collect(Collectors.toList());
    }

    /**
     * Validates if the Order contains only Products that are available in that currency.
     */
    public boolean validateOrder(final Order order) {
        return order.getLineItems()
                .stream()
                .map(LineItem::getProduct)
                .allMatch(p -> p.getPrices().containsKey(order.getCurrency()))
        ;
    }

    /**
     * Get the total amount in EUR for all open orders.
     * Make sure you take care of invalid orders,
     *
     * Try not to use getOrDefault, you have a new best friend.
     */
    public long getOutstandingAmount() {
        return findAll()
                .stream()
                .map(Customer::getOrders)
                .flatMap(List::stream)
                .filter(o -> ! o.isPaid())
                .filter(o -> CurrencyCode.EUR.equals(o.getCurrency()))
                .map(Order::getLineItems)
                .flatMap(List::stream)
                .mapToLong(LineItem::getAmount)
                .sum();
    }

}