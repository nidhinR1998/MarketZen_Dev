package com.nidhin.marketzen.response;

import com.nidhin.marketzen.models.Article;
import lombok.Data;

import java.util.List;

@Data
public class NewsResponse {
    private List<Article> articles;
}
