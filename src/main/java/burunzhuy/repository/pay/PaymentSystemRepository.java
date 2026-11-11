package burunzhuy.repository.pay;

import burunzhuy.entity.pay.PaymentSystem;
import burunzhuy.enums.user.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentSystemRepository extends JpaRepository<PaymentSystem, Long> {
    Optional<PaymentSystem> findByCode(String code);
    List<PaymentSystem> findByStatus(Status status);
}
