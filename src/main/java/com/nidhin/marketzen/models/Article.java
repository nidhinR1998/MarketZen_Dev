package com.nidhin.marketzen.models;

import lombok.Data;

@Data
public class Article {
    public String title;
    public String description;
    public String content;
    public String url;
    public String image;
    public String publishedAt;
    public Source source;
}
