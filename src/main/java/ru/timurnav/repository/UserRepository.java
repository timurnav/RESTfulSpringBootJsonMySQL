package ru.timurnav.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.timurnav.domain.User;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByOnlineAndStatusTimestampAfter (boolean online, Timestamp statusTimestamp);
    List<User> findByOnline (boolean online);
    List<User> findByStatusTimestampAfter (Timestamp statusTimestamp);

//    @Modifying
//    @Query("update users u set u.status=:status, u.status_timestamp=:status_timestamp where u.id=:id")
//    User changeStatus(@Param("id") long id,
//                      @Param("status") boolean status,
//                      @Param("status_timestamp") Timestamp timestamp);

}
