package com.mobile.makefive.service;

import com.mobile.makefive.common.Fix;
import com.mobile.makefive.common.Methods;
import com.mobile.makefive.common.Validator;
import com.mobile.makefive.entity.TblAccount;
import com.mobile.makefive.model.Response;
import com.mobile.makefive.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class LoginService implements UserDetailsService {

    private AccountRepository accountRepository;

    public LoginService(AccountRepository userRepository) {
        this.accountRepository = userRepository;
    }

    public TblAccount findByUserName(String username) {
        Optional<TblAccount> optional = accountRepository.findByUsername(username);
        if (!optional.isPresent()) {
            return null;
        }
        return optional.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<TblAccount> optional = accountRepository.findByUsername(username);
        if (!optional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        return optional.get();
    }

}
