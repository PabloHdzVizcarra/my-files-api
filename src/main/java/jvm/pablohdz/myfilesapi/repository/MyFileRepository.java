package jvm.pablohdz.myfilesapi.repository;

import java.util.Collection;
import java.util.Optional;
import jvm.pablohdz.myfilesapi.model.MyFile;
import jvm.pablohdz.myfilesapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyFileRepository extends JpaRepository<MyFile, String> {
  Optional<MyFile> findByName(String filename);

  Collection<MyFile> findAllByUser(User user);
}
