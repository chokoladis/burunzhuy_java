package burunzhuy.service;

import burunzhuy.entity.pay.PaymentSystem;
import burunzhuy.enums.user.Status;
import burunzhuy.repository.pay.PaymentSystemRepository;
import burunzhuy.resource.PaymentSystemResource;
import burunzhuy.tool.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentSystemService {

    private final PaymentSystemRepository paymentSystemRepository;

    public List<PaymentSystemResource> getList() {

        List<PaymentSystem> list = paymentSystemRepository.findByStatus(Status.ACTIVE);

        List<PaymentSystemResource> resources = new ArrayList<>();
        for (PaymentSystem entity : list) {
            resources.add(new PaymentSystemResource(entity));
        }
//        resources. map to debug file
//        Logger.logToFile("debug.txt", );
        return resources;
    }
}
