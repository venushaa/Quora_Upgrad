package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerService {

    @Autowired
    AnswerDao answerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(AnswerEntity answerEntity, final String authorizationToken) {
        return answerDao.createAnswer(answerEntity);
    }

    public AnswerEntity getAnswerbyId(Integer answerId){
        return answerDao.getAnswerbyId(answerId);
    }

    public AnswerEntity deleteAnswer(Integer answerId, UserAuthEntity userAuthEntity) throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity answerEntity = getAnswerbyId(answerId);
        if (answerEntity == null)
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");

        else {
            if ((answerEntity.getUser().getId() == userAuthEntity.getUserEntity().getId())
                    || userAuthEntity.getUserEntity().getRole().equals("admin")) {
                return answerDao.deleteAnswer(answerEntity);
            } else {
                throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
            }
        }
    }

}
