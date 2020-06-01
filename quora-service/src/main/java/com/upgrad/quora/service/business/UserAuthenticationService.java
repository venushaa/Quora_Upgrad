package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class UserAuthenticationService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserAuthDao userAuthDao;

    @Autowired
    private
    PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * This method is used for the user to signin.
     *
     * @param username Username of the user who is singing in.
     * @param password Credentials of the user who is singing in
     * @return UserAuthEntity which contains the access-token and other details.
     * @throws AuthenticationFailedException ATH-001 if the username doesn't exist in DB or ATH-002 if
     *                                       the password is wrong.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signin(final String username, final String password)
            throws AuthenticationFailedException {

        UserEntity userEntity = userDao.getUserByUserName(username);
        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }
        final String encryptedPassword =
                passwordCryptographyProvider.encrypt(password, userEntity.getSalt());
        if (!encryptedPassword.equals(userEntity.getPassword())) {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }

        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
        UserAuthEntity userAuthEntity = new UserAuthEntity();
        userAuthEntity.setUuid(UUID.randomUUID().toString());
        userAuthEntity.setUserEntity(userEntity);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiresAt = now.plusHours(8);
        userAuthEntity.setAccessToken(
                jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
        userAuthEntity.setLoginAt(now);
        userAuthEntity.setExpiresAt(expiresAt);

        userAuthDao.createAuthToken(userAuthEntity);
        userDao.updateUserEntity(userEntity);

        return userAuthEntity;
    }
}
