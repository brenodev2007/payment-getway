package com.brenodev.payment_getway.Services;

import com.brenodev.payment_getway.DTOs.MerchantDTO;
import com.brenodev.payment_getway.Entity.Merchant;
import com.brenodev.payment_getway.Repositories.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public Merchant findByMerchantId(Long id) {
        return merchantRepository.findById(id).orElse(null);
    }

    public Merchant create(MerchantDTO dto){

    Merchant merchant = new Merchant(
            dto.id(),
            dto.name()
    );
    merchantRepository.save(merchant);
    return merchant;

    }

    public Merchant update(Long id, MerchantDTO dto){
        Merchant merchant = findByMerchantId(dto.id());
        merchant.setName(dto.name());
        merchantRepository.save(merchant);
        return merchant;
    }

    public void delete(Long id){
        merchantRepository.deleteById(id);
    }

}
