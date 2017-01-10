package demo.chat.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import demo.chat.model.Message;
import demo.chat.model.User;


@Repository
public interface MessageDao extends JpaRepository<Message, Long> {
	
	static final String BIDIRECTIONAL_MESSAGE_QUERY = "SELECT m FROM Message m WHERE (m.sender = :customerUser AND m.recipient = :customerServiceUser) OR (m.sender = :customerServiceUser AND m.recipient = :customerUser)";
	
	@Query(BIDIRECTIONAL_MESSAGE_QUERY)
	public List<Message> find(@Param("customerUser") User customerUser, @Param("customerServiceUser") User customerServiceUser);

	@Query(BIDIRECTIONAL_MESSAGE_QUERY)
	public Page<Message> findAll(@Param("customerUser") User customerUser, @Param("customerServiceUser") User customerServiceUser, Pageable pageable);
}
