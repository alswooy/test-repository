package com.zerobase.cms.user.service.seller;

import static com.zerobase.cms.user.exception.ErrorCode.ALREADY_VERIFY;
import static com.zerobase.cms.user.exception.ErrorCode.EXPIRE_CODE;
import static com.zerobase.cms.user.exception.ErrorCode.NOT_FOUND_USER;
import static com.zerobase.cms.user.exception.ErrorCode.WRONG_VERIFICATION;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.domain.repository.SellerRepository;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;

    public Optional<Seller> findByIdAndEmail(Long id, String email){
        return sellerRepository.findByIdAndEmail(id,email);

    }
    public Optional<Seller> findValidSeller(String email,String password){
        return sellerRepository.findByEmailAndPasswordAndVerifyIsTrue(email,password);
    }
    public Seller signUp(SignUpForm form){
        return sellerRepository.save(Seller.from(form));
    }
    public boolean isEmailExist(String email){
        return sellerRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void verifyEmail(String email,String code){
        Seller seller = sellerRepository.findByEmail(email)
            .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_USER));
        if(seller.isVerify()){
            throw new CustomException(ALREADY_VERIFY);
        } else if(!seller.getVerificationCode().equals(code)){
            throw new CustomException(WRONG_VERIFICATION);
        } else if(seller.getVerifyExpiredAt().isBefore(LocalDateTime.now())){
            throw new CustomException(EXPIRE_CODE);
        }
        seller.setVerify(true);
    }

    @Transactional
    public LocalDateTime changeCustomerValidateEmail(Long customerId, String verificationCode) {
        Optional<Seller> sellerOptional =sellerRepository.findById(customerId);

        if (sellerOptional.isPresent()) {
            Seller seller = sellerOptional.get();
            seller.setVerificationCode(verificationCode);
            seller.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
            return  seller.getVerifyExpiredAt();
        }
        throw new CustomException(NOT_FOUND_USER);
    }

}
