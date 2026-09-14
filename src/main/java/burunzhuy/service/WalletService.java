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
import burunzhuy.exception.pay.PayException;
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

    @Transactional
    public WalletHistory createTransaction(
            BigDecimal amount,
            String currency,
            TypeTransaction type
    ) {

        Wallet wallet = null;

        if (type.isMoneyOut()) {

            wallet = this.firstOrFail(currency);

            Logger.logToFile("test.txt", " debug - " + amount + " // " + amount.compareTo(wallet.getBalance()));
            if (amount.compareTo(wallet.getBalance()) > 0){
                throw new PayException("Попытка списания больше чем есть на балансе (%f)", wallet.getBalance());
            }

            wallet.setBalance(wallet.getBalance().subtract(amount));
            walletRepository.save(wallet);
        } else {
            wallet = this.firstOrCreate(currency);
        }

        WalletHistory walletHistory = new WalletHistory();
        walletHistory.setAmount(amount);
        walletHistory.setType(type);
        walletHistory.setWallet(wallet);

        walletHistoryRepository.save(walletHistory);

        return walletHistory;
    }

    public Wallet firstOrCreate(String currency)
    {
        Optional<Wallet> walletCurrentUser = walletRepository.findByCurrencyAndOwner(currency, profileService.getCurrentUser());

        Logger.debug("user is " + profileService.getCurrentUser().getEmail());

        Wallet currentWallet = new Wallet();

        Logger.debug("wallet is empty - " + walletCurrentUser.isEmpty());
        if (!walletCurrentUser.isEmpty()) {
            var res = walletCurrentUser.get();

            Logger.debug("wallet is - " + res.getOwner().getEmail() + " // " + res.getCurrency());

            return walletCurrentUser.get();
        } else {
            currentWallet.setBalance(new BigDecimal(0));
            currentWallet.setCurrency(currency);
            currentWallet.setOwner(profileService.getCurrentUser());
            walletRepository.save(currentWallet);

            return currentWallet;
        }
    }

    public Wallet firstOrFail(String currency) {
        Optional<Wallet> currentWallet = walletRepository.findByCurrencyAndOwner(currency, profileService.getCurrentUser());

        if (currentWallet.isEmpty())
            throw new EntityNotFound("У вас нет кошелька с данной валютой");

        return currentWallet.get();
    }

}
