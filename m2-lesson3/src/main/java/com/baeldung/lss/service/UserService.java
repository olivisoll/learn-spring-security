package com.baeldung.lss.service;

import com.baeldung.lss.persistence.UserRepository;
import com.baeldung.lss.persistence.VerificationTokenRepository;
import com.baeldung.lss.validation.EmailExistsException;
import com.baeldung.lss.web.model.User;
import com.baeldung.lss.web.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
class UserService implements IUserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public User registerNewUser(final User user) throws EmailExistsException {
        if (emailExist(user.getEmail())) {
            throw new EmailExistsException("There is an account with that email address: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    private boolean emailExist(final String email) {
        final User user = repository.findByEmail(email);
        return user != null;
    }

    @Override
    public User updateExistingUser(final User user) throws EmailExistsException {
        final Long id = user.getId();
        final String email = user.getEmail();
        final User emailOwner = repository.findByEmail(email);
        if (emailOwner != null && !id.equals(emailOwner.getId())) {
            throw new EmailExistsException("Email not available.");
        }
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return repository.save(user);
    }

    public User save(final User user) {
        return repository.save(user);
    }

    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }


}
