package sk.tuke.kp.kpi.pexeso.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import sk.tuke.kp.kpi.pexeso.entity.Comment;

import java.util.Arrays;
import java.util.List;

public class CommentServiceRestClient implements CommentService {
    //See value of remote.server.api property in application.properties file
    @Value("${remote.server.api}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addComment(Comment comment) {
        restTemplate.postForEntity(url + "/comment", comment, Comment.class);
    }

    @Override
    public List<Comment> getComments(String player, String game) {
        return Arrays.asList(restTemplate.getForEntity(url + "/comment/" + game + "/" + player, Comment[].class).getBody());
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }

    @Override
    public List<Comment> getAllComments(String game) {
        return Arrays.asList(restTemplate.getForEntity(url + "/comment/" + game,Comment[].class).getBody());
    }
}
