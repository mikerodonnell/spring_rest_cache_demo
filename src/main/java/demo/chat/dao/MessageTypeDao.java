package demo.chat.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import demo.chat.model.MessageType;

@Repository
public interface MessageTypeDao extends JpaRepository<MessageType, Long> {

	@Query("SELECT mt FROM MessageType mt WHERE mt.code = :code")
	MessageType find(@Param("code") String code);
}
