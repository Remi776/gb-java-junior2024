package ru.gb.lesson4.homework4;

public class PostComment {
    private Long id;
    private String text;
    private Long post_id;

    public PostComment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

    @Override
    public String toString() {
        return "PostComment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", post_id=" + post_id +
                '}';
    }
}
