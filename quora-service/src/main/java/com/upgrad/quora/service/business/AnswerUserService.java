package com.upgrad.quora.service.business;

import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Abhishek
 */

@Service
public class AnswerUserService {

    @Autowired
    UserAuthDao userAuthDao;

    public UserAuthEntity checkIfTokenIsValid(String accessToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByToken(accessToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException(
                    "ATHR-001", "User has not signed in");
        }
        if (userAuthEntity.getLogoutAt() != null) {
            return null;
        }

        return userAuthEntity;
    }

}
