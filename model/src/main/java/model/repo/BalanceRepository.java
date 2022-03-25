package model.repo;

import model.entity.Balance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BalanceRepository extends CrudRepository<Balance, Long> {
    List<Balance> findByAccountId(Long accountId);
}
