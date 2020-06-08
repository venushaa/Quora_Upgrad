package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class QuestionService {
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        questionDao.createQuestion(questionEntity);
        return questionEntity;
    }

    @Transactional
    public List<QuestionEntity> getAllQuestions() {
        return questionDao.getAllQuestions();
    }

    @Transactional
    public QuestionEntity getQuestionById(String question_id) throws InvalidQuestionException {

        QuestionEntity questionEntity = questionDao.getQuestionById(question_id);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        return questionEntity;
    }


    @Transactional
    public void editQuestionContent(UserEntity signedinUser, QuestionEntity questionEntity, String questionContent) throws AuthorizationFailedException, InvalidQuestionException {

        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        //UserEntity questionOwner = questionEntity.getUser();
        UserEntity questionOwner = questionEntity.getUserEntity();
        if (!questionOwner.getUuid().equals(signedinUser.getUuid())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
        questionEntity.setContent(questionContent);
        questionDao.editQuestionContent(questionEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteQuestion(UserEntity signedinUser, QuestionEntity questionEntity) throws InvalidQuestionException, AuthorizationFailedException {
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        String role = signedinUser.getRole();
        UserEntity questionOwner = questionEntity.getUserEntity();
        if (!(questionOwner.getUuid().equals(signedinUser.getUuid()) || role.equals("admin"))) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }
        questionDao.deleteQuestion(questionEntity);
    }

    @Transactional
    public List<QuestionEntity> getQuestionsByUser(String  userId)  throws UserNotFoundException  {
        UserEntity user = userDao.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(
                    "USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        return questionDao.getQuestionsByUser(user);
    }
}



