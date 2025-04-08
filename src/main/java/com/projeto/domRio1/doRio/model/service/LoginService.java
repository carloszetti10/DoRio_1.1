package com.projeto.domRio1.doRio.model.service;


import com.projeto.domRio1.doRio.model.PosException;
import com.projeto.domRio1.doRio.model.entity.Account;
import com.projeto.domRio1.doRio.model.repo.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LoginService {

    @Autowired
    private AccountRepo repo;

    public Account login(String loginId, String password) {

        if(StringUtils.isEmpty(loginId)) {
            throw new PosException("Please enter Login Id.");
        }

        if(StringUtils.isEmpty(password)) {
            throw new PosException("Please enter Password.");
        }

        Account account = repo.findById(loginId)
                .orElseThrow(() -> new PosException("Please check your Login Id."));

        if(!password.equals(account.getPassword())) {
            throw new PosException("Please check your password.");
        }

        return account;
    }
}
