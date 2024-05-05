package ch.unige.biscuits.domain.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import ch.unige.biscuits.domain.Command;

public interface OrderRepository extends CrudRepository<Command, Long> {
    @NonNull List<Command> findAll();
}
