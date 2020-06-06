package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService {

    @Autowired
    AnswerDao answerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(AnswerEntity answerEntity, final String authorizationToken) {
        return answerDao.createAnswer(answerEntity);
    }

    public AnswerEntity getAnswerbyId(String answerId) throws AnswerNotFoundException {
        AnswerEntity answerEntity = answerDao.getAnswerbyId(answerId);
        if (answerEntity == null)
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");

        return answerEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(String answerId, UserAuthEntity userAuthEntity) throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity answerEntity = getAnswerbyId(answerId);

        if ((answerEntity.getUser().getId() == userAuthEntity.getUserEntity().getId())
             || userAuthEntity.getUserEntity().getRole().equals("admin")) {
                return answerDao.deleteAnswer(answerEntity);
        } else {
                throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswer(String answerId, UserAuthEntity userAuthEntity, String editedAnswer) throws AnswerNotFoundException, AuthorizationFailedException {
        AnswerEntity answerEntity = getAnswerbyId(answerId);
        if ((answerEntity.getUser().getId() == userAuthEntity.getUserEntity().getId())) {
            answerEntity.setAnswer(editedAnswer);
            return answerDao.editAnswer(answerEntity);
        } else {
                throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }
    }

    public List<AnswerEntity> getAnswersbyQuestionID(QuestionEntity questionId, UserAuthEntity userAuthEntity){
        List<AnswerEntity> allAnswers = answerDao.getAnswersbyQUestionId(questionId);

        return allAnswers;
    }
}
