package demo.chat.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.chat.model.MessageMeta;

@Repository
public interface MessageMetaDao extends JpaRepository<MessageMeta, Long> {

}
