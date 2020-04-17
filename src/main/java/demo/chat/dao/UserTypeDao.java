package demo.chat.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import demo.chat.model.UserType;

@Repository
public interface UserTypeDao extends JpaRepository<UserType, Long> {

	@Query("SELECT ut FROM UserType ut WHERE ut.code = :code")
	UserType find(@Param("code") String code);
}
