package com.nidhin.marketzen.models;

import lombok.Data;

import java.util.List;

@Data
public class NewsDTO {

    private int totalArticles;
    private List<Article> articles;
}
