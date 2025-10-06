package dao;

import domain.Comentario;
import java.util.List;

public interface CommentDao {

    void addComment(Comentario comentario);

    List<Comentario> listByTicket(int ticketId);
}

