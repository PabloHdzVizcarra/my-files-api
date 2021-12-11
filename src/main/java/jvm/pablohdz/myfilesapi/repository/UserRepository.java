package jvm.pablohdz.myfilesapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jvm.pablohdz.myfilesapi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
