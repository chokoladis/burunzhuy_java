package burunzhuy.repository.pay;

import burunzhuy.entity.pay.Wallet;
import burunzhuy.entity.pay.WalletHistory;
import burunzhuy.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletHistoryRepository extends JpaRepository<WalletHistory, Long> {

}
