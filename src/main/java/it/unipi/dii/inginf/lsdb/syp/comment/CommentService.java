package it.unipi.dii.inginf.lsdb.syp.comment;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }


    public Comment addComment(Comment newComment) {
        return commentRepository.save(newComment);
    }

    public List<Comment> getSongComments(String songId) {
        return commentRepository.findCommentsBySongId(songId);
    }

    public List<Comment> getUserComments(String userId) {
        return commentRepository.findCommentsByUserId(userId);
    }

    public Comment updateComment(Comment updatedComment){
        return commentRepository.save(updatedComment);
    }

    public void deleteComment(Long id){
        commentRepository.deleteById(id);
    }
}
