package com.goular.rxjava.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lotus on 2017/1/26.
 */

public class Author {
    public List<String> Articles=new ArrayList<>();
    public String name;

    public Author(String name) {
        this.name = name;
    }

    public List<String> getArticles() {
        return Articles;
    }

    public void setArticles(List<String> articles) {
        Articles = articles;
    }

    @Override
    public String toString() {
        return "Author{" +
                "Articles=" + Articles +
                ", name='" + name + '\'' +
                '}';
    }
}
