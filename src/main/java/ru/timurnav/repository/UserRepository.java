package ru.timurnav.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.timurnav.domain.User;

import java.sql.Timestamp;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByOnlineAndStatusTimestampAfter (boolean online, Timestamp statusTimestamp);
    List<User> findByOnline (boolean online);
    List<User> findByStatusTimestampAfter (Timestamp statusTimestamp);

//    @Modifying
//    @Query("update users u set u.status=:status, u.status_timestamp=:status_timestamp where u.id=:id")
//    User changeStatus(@Param("id") long id,
//                      @Param("status") boolean status,
//                      @Param("status_timestamp") Timestamp timestamp);

}
