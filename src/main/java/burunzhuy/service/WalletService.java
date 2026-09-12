package burunzhuy.service;

import burunzhuy.dto.idea.CreateRequest;
import burunzhuy.dto.idea.UpdateRequest;
import burunzhuy.entity.File;
import burunzhuy.entity.Idea;
import burunzhuy.entity.pay.PaymentInvoice;
import burunzhuy.entity.pay.Wallet;
import burunzhuy.entity.pay.WalletHistory;
import burunzhuy.enums.pay.TypeTransaction;
import burunzhuy.exception.common.ContentTypeNotAllowedException;
import burunzhuy.exception.common.EntityNotFound;
import burunzhuy.repository.pay.WalletHistoryRepository;
import burunzhuy.repository.pay.WalletRepository;
import burunzhuy.resource.idea.FullResource;
import burunzhuy.service.user.ProfileService;
import burunzhuy.tool.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletHistoryRepository walletHistoryRepository;
    private final ProfileService profileService;

    public WalletHistory createTransaction(
            BigDecimal amount,
            String currency
    ) {
        Wallet wallet = this.firstOrCreate(currency);

        WalletHistory walletHistory = new WalletHistory();
        walletHistory.setAmount(amount);
        walletHistory.setType(TypeTransaction.DEPOSIT);
        walletHistory.setWallet(wallet);

        walletHistoryRepository.save(walletHistory);

        return walletHistory;
    }

    public Wallet firstOrCreate(String currency)
    {
        Optional<Wallet> walletCurrentUser = walletRepository.findByCurrencyAndOwner(currency, profileService.getCurrentUser());

        Wallet currentWallet = new Wallet();

        if (!walletCurrentUser.isEmpty()) {
            return walletCurrentUser.get();
        } else {
            currentWallet.setBalance(new BigDecimal(0));
            currentWallet.setCurrency(currency);
            currentWallet.setOwner(profileService.getCurrentUser());
            walletRepository.save(currentWallet);

            return currentWallet;
        }
    }

}
