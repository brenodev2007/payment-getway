package com.brenodev.payment_getway.Controllers;


import com.brenodev.payment_getway.DTOs.MerchantDTO;
import com.brenodev.payment_getway.Entity.Merchant;
import com.brenodev.payment_getway.Services.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

;

@RestController
@RequestMapping("/merchants")
public class MerchantController {

    @Autowired
    MerchantService merchantService;


    @PostMapping
    public Merchant createMerchant(@RequestBody MerchantDTO merchantDTO){
        return merchantService.create(merchantDTO);
    }

    @GetMapping("/{id}")
    public Merchant getMerchants(Long id){
        return merchantService.findByMerchantId(id);
    }

    @PatchMapping("/{id}")
    public Merchant updateMerchant(@PathVariable Long id, @RequestBody MerchantDTO merchantDTO){
        return merchantService.update(id,merchantDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteMerchant(@PathVariable Long id){
        merchantService.delete(id);
    }
}
