package me.cameronwhyte.pufferfish.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.cameronwhyte.pufferfish.PufferfishApplication;
import me.cameronwhyte.pufferfish.repositories.CustomerRepository;
import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class Customer implements Serializable {

    @Id
    private long id;
    @Nullable
    private String IGN;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();
    @ManyToMany(mappedBy = "shares", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Account> shared = new HashSet<>();

    public Customer(Long id) {
        this.id = id;
    }

    private static CustomerRepository getRepository() {
        return PufferfishApplication.contextProvider().getApplicationContext().getBean("customerRepository", CustomerRepository.class);
    }

    public static Customer getUser(Long id) {
        CustomerRepository repository = PufferfishApplication.contextProvider().getApplicationContext().getBean("customerRepository", CustomerRepository.class);
        return repository.findById(id).orElseGet(() -> {
            Customer user = new Customer(id);
            repository.save(user);
            return user;
        });
    }

    public void setIGN(@NonNull String ign) {
        CustomerRepository repository = PufferfishApplication.contextProvider().getApplicationContext().getBean("customerRepository", CustomerRepository.class);
        this.IGN = ign;
        repository.save(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Customer other) {
            return this.getId() == other.getId();
        } else if (obj instanceof Long l) {
            return this.getId() == l;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
