package com.example.group27_inclass07;

import java.io.Serializable;
import java.util.ArrayList;

class Question implements Serializable {

    String question;
    String image_url;
    ArrayList<String> choices;
    int ans;

    @Override
    public String toString() {
        return "Questions{" +
                "question='" + question + '\'' +
                ", image_url='" + image_url + '\'' +
                ", choices=" + choices +
                ", ans=" + ans +
                '}';
    }

    public Question (String question, String image_url, ArrayList<String> choices, int ans) {
        this.question = question;
        this.image_url = image_url;
        this.choices = choices;
        this.ans = ans;
    }
}
