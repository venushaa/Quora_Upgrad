package com.upgrad.quora.api.Controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;

import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")

public class QuestionController {

 //   @Autowired
  //  private CommonBusinessService commonBusinessService;

    @Autowired
    private QuestionBusinessService questionBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        String[] bearerToken = authorization.split("Bearer ");
        final QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());
        final ZonedDateTime now = ZonedDateTime.now();
        questionEntity.setDate(now);
        //UserEntity userEntity = commonBusinessService.userProfile(bearerToken[1]);
        //Venkat - Uncomment above line and remove below line
        UserEntity userEntity = new UserEntity();
        questionEntity.setUser(userEntity);
        final QuestionEntity createdQuestionEntity = questionBusinessService.createQuestion(questionEntity);
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid()).status("QUESTION CREATED");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        String[] bearerToken = authorization.split("Bearer ");
//        UserEntity userEntity = commonBusinessService.userProfile(bearerToken[1]);
        //Venkat - Uncomment above line and remove below line
        UserEntity userEntity = new UserEntity();
        Iterator<QuestionEntity> itrQuestions = questionBusinessService.getAllQuestions().iterator();
        List<QuestionDetailsResponse> questionResponseList = new ArrayList<QuestionDetailsResponse>();
        while (itrQuestions.hasNext()) {
            QuestionEntity questionEntity = itrQuestions.next();
            /*questionResponseList.add(new QuestionDetailsResponse().id(itrQuestions.next().getUuid()).
                    content(itrQuestions.next().getContent()));*/
            ((ArrayList) questionResponseList).add(new QuestionDetailsResponse().id(questionEntity.getUuid()).
                    content(questionEntity.getContent()));
        }

        return new ResponseEntity<List<QuestionDetailsResponse>>(questionResponseList, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestion(final QuestionEditRequest questionEditRequest, @PathVariable("questionId") final String question_id, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException, InvalidQuestionException {
        String[] bearerToken = authorization.split("Bearer ");
//        UserEntity signedinUser = commonBusinessService.userProfile(bearerToken[1]);
        //Venkat - Uncomment above line and remove below line
        UserEntity signedinUser = new UserEntity();

        QuestionEntity questionEntity = questionBusinessService.getQuestionById(question_id);
        questionEntity.setContent(questionEditRequest.getContent());
        //check if the user is authorized to edit a question before allowing to do so
        questionBusinessService.editQuestionContent(signedinUser, questionEntity);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(questionEntity.getUuid()).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") final String question_id, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        String[] bearerToken = authorization.split("Bearer ");
        //check if the user is signed in
//        UserEntity signedinUser = commonBusinessService.userProfile(bearerToken[1]);
        //Venkat - Uncomment above line and remove below line
        UserEntity signedinUser = new UserEntity();
        QuestionEntity questionEntity = questionBusinessService.getQuestionById(question_id);

        //checks if the user is authorized to delete a question before allowing him to do so
        questionBusinessService.deleteQuestion(signedinUser, questionEntity);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(questionEntity.getUuid()).status("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
    }

    // get all the questions posted by a given user in the json request
    @RequestMapping(method = RequestMethod.GET, path = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getQuestionsByUser(@PathVariable("userId")final String userId,@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        String[] bearerToken = authorization.split("Bearer ");
        //UserEntity userEntity = commonBusinessService.getUser(userId,bearerToken[1]);
        //Long user_id =userEntity.getId();
        //Venkat Uncomment above two lines and remove below two lines
        Long user_id = 1L;
        UserEntity userEntity = new UserEntity();
        //Venkat uncomment lone below
        Iterator<QuestionEntity> itrQuestions = questionBusinessService.getQuestionsByUser(userEntity).iterator();
        List<QuestionDetailsResponse> questionResponseList = new ArrayList<QuestionDetailsResponse>();
        while (itrQuestions.hasNext()) {
            QuestionEntity questionEntity = itrQuestions.next();
            /*questionResponseList.add(new QuestionDetailsResponse().id(itrQuestions.next().getUuid()).
                    content(itrQuestions.next().getContent()));*/
            questionResponseList.add(new QuestionDetailsResponse().id(questionEntity.getUuid()).
                    content(questionEntity.getContent()));
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionResponseList, HttpStatus.OK);


    }

}
