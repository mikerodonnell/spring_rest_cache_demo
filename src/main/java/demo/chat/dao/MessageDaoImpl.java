package demo.chat.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import demo.chat.model.Message;
import demo.chat.model.User;


@Repository
public interface MessageDaoImpl extends JpaRepository<Message, Long> {
	
	@Query("SELECT m FROM Message m WHERE m.customerUser = :customerUser AND m.customerServiceUser = :customerServiceUser")
	public Message find(@Param("customerUser") User customerUser, @Param("customerServiceUser") User customerServiceUser);

}
