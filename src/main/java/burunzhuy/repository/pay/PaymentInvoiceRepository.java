package burunzhuy.repository.pay;

import burunzhuy.entity.pay.PaymentInvoice;
import burunzhuy.entity.pay.Wallet;
import burunzhuy.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentInvoiceRepository extends JpaRepository<PaymentInvoice, Long> {
    Optional<PaymentInvoice> findByExternalId(String externalId);
}
