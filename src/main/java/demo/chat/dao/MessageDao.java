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
	
	@Query("SELECT m FROM Message m WHERE m.customerUser = :customerUser AND m.customerServiceUser = :customerServiceUser")
	public List<Message> find(@Param("customerUser") User customerUser, @Param("customerServiceUser") User customerServiceUser);

	@Query("SELECT m FROM Message m WHERE m.customerUser = :customerUser AND m.customerServiceUser = :customerServiceUser")
	public Page<Message> findAll(@Param("customerUser") User customerUser, @Param("customerServiceUser") User customerServiceUser, Pageable pageable);
}
