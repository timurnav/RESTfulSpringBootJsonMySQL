package ru.timurnav.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByOnlineAndStatusTimestampAfter (boolean online, Timestamp statusTimestamp);
    List<User> findByOnline (boolean online);
    List<User> findByStatusTimestampAfter (Timestamp statusTimestamp);

//    @Modifying
//    @Transactional
//    @Query("update users u set u.status=:status, u.status_timestamp=:status_timestamp where u.id=:id")
//    User changeStatus(@Param("id") long id,
//                      @Param("status") boolean status,
//                      @Param("status_timestamp") Timestamp timestamp);

}
