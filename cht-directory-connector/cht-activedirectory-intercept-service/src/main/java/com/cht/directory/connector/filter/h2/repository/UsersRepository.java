package com.cht.directory.connector.filter.h2.repository;

import com.cht.directory.connector.filter.h2.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
    //@Query("select s from Users s where s.status = FALSE")
    public List<Users> findByStatusFalseAndEcounterLessThanEqual(int encounter);
}
