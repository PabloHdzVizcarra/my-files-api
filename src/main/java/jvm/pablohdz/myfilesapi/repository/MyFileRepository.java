package jvm.pablohdz.myfilesapi.repository;

import java.util.Optional;
import jvm.pablohdz.myfilesapi.model.MyFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MyFileRepository extends JpaRepository<MyFile, String> {
  Optional<MyFile> findByName(String filename);
}
