package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity){
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getAnswerbyId(Integer answerId){
        try {
            return entityManager.createNamedQuery("answerbyId",AnswerEntity.class).setParameter("uuid",answerId).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public AnswerEntity deleteAnswer(AnswerEntity answerEntity){
        entityManager.remove(answerEntity);
        return answerEntity;
    }

    public AnswerEntity editAnswer(AnswerEntity answerEntity){
        entityManager.merge(answerEntity);
        return answerEntity;
    }

    public List<AnswerEntity> getAnswersbyQUestionId(Integer questionId){
        try{
            return entityManager.createNamedQuery("answersByQuestionId",AnswerEntity.class).setParameter("question_id",questionId).getResultList();
        }catch (NoResultException nre){
            return null;
        }

    }

}
