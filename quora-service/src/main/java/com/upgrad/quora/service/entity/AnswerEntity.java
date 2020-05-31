package com.upgrad.quora.service.entity;

import org.aspectj.weaver.patterns.TypePatternQuestions;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name="answer", schema="public")

public class AnswerEntity implements Serializable {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="UUID")
    @NotNull
    @Size(max=200)
    private String uuid;

    @NotNull
    @Size(max=255)
    @Column(name="ANS")
    private String answer;

    @NotNull
    @Column(name="DATE")
    private ZonedDateTime date;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="QUESTION_ID")
    private Question question;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}
