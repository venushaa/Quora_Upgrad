package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.CommonUserService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommonUserService commonUserService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@PathVariable("questionId") Integer questionId , final AnswerRequest answerRequest, @RequestHeader final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        //String[] bearerToken = authorization.split("Bearer ");
        final UserAuthEntity userAuthEntity = commonUserService.checkIfTokenIsValid(authorization);

        QuestionEntity question = questionService.getQuestionUsingId(questionId);

        if(question == null)
                throw new InvalidQuestionException("QUES-001","The question entered is invalid");
        else {

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
    }

   /* @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse>  editAnswerContent(@PathVariable("answerId") Integer answerId, final AnswerRequest answerRequest, @RequestHeader final String authorization){

    }*/

    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable Integer answerID, @RequestHeader String authorization, final AnswerRequest answerRequest) throws AuthorizationFailedException, AnswerNotFoundException {
        final UserAuthEntity userAuthEntity = commonUserService.checkIfTokenIsValid(authorization);

        AnswerEntity answerEntity = answerService.deleteAnswer(answerID,userAuthEntity);



    }

    /*@RequestMapping(method = RequestMethod.GET, path = "/answer/all/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDetailsResponse> getAllAnswersToQuestion(); */

}
