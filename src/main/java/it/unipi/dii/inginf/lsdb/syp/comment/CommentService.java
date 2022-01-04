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
        try{
            return commentRepository.save(newComment);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Comment> getSongComments(String songId) {
        try{
            return commentRepository.findCommentsBySongId(songId);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Comment> getUserComments(String userId) {
        try{
            return commentRepository.findCommentsByUserId(userId);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Comment updateComment(Comment updatedComment){
        try{
            return commentRepository.save(updatedComment);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void deleteComment(Long id){
        try{
            commentRepository.deleteById(id);
        } catch (Exception e){
            e.printStackTrace();
            //return null;
            return;
        }
    }
}
