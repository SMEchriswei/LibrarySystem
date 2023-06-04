package com.ascending.training.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "customer", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<IssueStatus> issueStatuses;

    public Customer(){}

    public Customer(long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Customer(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<IssueStatus> getIssueStatuses() {
        return issueStatuses;
    }

    public void setIssueStatuses(Set<IssueStatus> issueStatuses) {
        this.issueStatuses = issueStatuses;
    }

    @Override
    public String toString() {
        return String.format(id + ", " + name + ", " + address);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }

        Customer other = (Customer) o;
        boolean nameEquals = (this.name == null && other.name == null)
                || (this.name != null && this.name.equals(other.name));
        boolean addressEquals = (this.address == null && other.name == null)
                || (this.address != null && this.address.equals(other.address));

        return nameEquals && addressEquals;
    }
}
