package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.CommonUserService;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionBusinessService questionBusinessService;

    @Autowired
    private CommonUserService commonUserService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@PathVariable("questionId") Integer questionId , final AnswerRequest answerRequest, @RequestHeader final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        final UserAuthEntity userAuthEntity = commonUserService.checkIfTokenIsValid(authorization);

        QuestionEntity question = questionBusinessService.getQuestionById(questionId);

     /*   QuestionEntity question = new QuestionEntity();
        question.setId(1024);
        question.setUuid("database_question_uuid");
        question.setContent("database_question_content");
        question.setDate(ZonedDateTime.now());
        question.setUser(userAuthEntity.getUserEntity());*/


        final AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setQuestion(question);
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setAnswer(answerRequest.getAnswer());
        answerEntity.setUser(userAuthEntity.getUserEntity());

        final AnswerEntity createdAnswerEntity = answerService.createAnswer(answerEntity, authorization);
        AnswerResponse answerResponse = new AnswerResponse().id(createdAnswerEntity.getUuid().toString()).status("ANSWER CREATED");

        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse>  editAnswerContent(@PathVariable("answerId") Integer answerId, final AnswerRequest answerRequest, @RequestHeader final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
        final UserAuthEntity userAuthEntity = commonUserService.checkIfTokenIsValid(authorization);

        AnswerEntity editedAnswerEntity = answerService.editAnswer(answerId,userAuthEntity);

        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(editedAnswerEntity.getUuid().toString()).status("ANSWER EDITED");

        return new ResponseEntity<AnswerEditResponse>(answerEditResponse,HttpStatus.CREATED);

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable Integer answerID, @RequestHeader String authorization, final AnswerRequest answerRequest) throws AuthorizationFailedException, AnswerNotFoundException {
        final UserAuthEntity userAuthEntity = commonUserService.checkIfTokenIsValid(authorization);

        AnswerEntity deletedAnswerEntity = answerService.deleteAnswer(answerID,userAuthEntity);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(deletedAnswerEntity.getUuid().toString()).status("ANSWER DELETED");

        return  new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);


    }

    @RequestMapping(method = RequestMethod.GET, path = "/answer/all/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(@PathVariable Integer questionId, @RequestHeader String authorization, final AnswerRequest answerRequest) throws AuthorizationFailedException, InvalidQuestionException {
        final UserAuthEntity userAuthEntity = commonUserService.checkIfTokenIsValid(authorization);

        QuestionEntity questionEntity = questionBusinessService.getQuestionById(questionId);

        List<AnswerEntity> allAnswers = answerService.getAnswersbyQuestionID(questionId,userAuthEntity);
     //   List<AnswerEntity> allAnswers = answerService.getAnswersbyQuestionID(1024,userAuthEntity);

        Iterator<AnswerEntity> allAnswersIterator = allAnswers.iterator();

        List<AnswerDetailsResponse> answerDetailsResponseList = new ArrayList<AnswerDetailsResponse>() ;

        while(allAnswersIterator.hasNext()){
            AnswerEntity answerEntity = allAnswersIterator.next();
          //  answerDetailsResponseList.add(new AnswerDetailsResponse().id(answerEntity.getUuid().toString()).answerContent(answerEntity.getAnswer()).questionContent("temp"));
            answerDetailsResponseList.add(new AnswerDetailsResponse().id(answerEntity.getUuid().toString()).answerContent(answerEntity.getAnswer()).questionContent(answerEntity.getQuestion().getContent()));
        }

        return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsResponseList, HttpStatus.OK);
    }

}
