package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "QUESTION", schema = "public")
@NamedQueries(value = {
        @NamedQuery(name = "QuestionByUserId", query = "select q from QuestionEntity  q where q.user_id = :user_id"),
        @NamedQuery(name = "QuestionById", query = "select q from QuestionEntity q where q.uuid = :uuid"),
        @NamedQuery(name = "AllQuestions", query = "select q from QuestionEntity q"),
        @NamedQuery(name = "DeleteQuestion", query = "delete from QuestionEntity q where q.id = :id")

})


public class  QuestionEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "UUID")
    @Size(max = 64)
    private String uuid;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "DATE")
    private ZonedDateTime date;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;


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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
