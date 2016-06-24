package com.epages.docs.streams;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        throw new UnsupportedOperationException();
    }

    /**
     * Get all Orders paid in the given currency.
     */
    public List<Order> getAllPaidOrders(CurrencyCode currency) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get a customer's orders by paymentStatus status.
     */
    public Map<Order.PaymentStatus, List<Order>> getOrdersByStatus(String customerName) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get all customers that have cancelled at least one order
     */
    public List<Customer> getUnhappyCustomers() {
        throw new UnsupportedOperationException();
    }

    /**
     * Validates if the Order contains only Products that are available in that currency.
     */
    public boolean validateOrder(final Order order) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the total amount in EUR for all open orders.
     * Make sure you take care of invalid orders,
     *
     * Try not to use getOrDefault, you have a new best friend.
     */
    public long getOutstandingAmount() {
        throw new UnsupportedOperationException();
    }

}