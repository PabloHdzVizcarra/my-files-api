package jvm.pablohdz.myfilesapi.repository;

import jvm.pablohdz.myfilesapi.model.MyFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyFileRepository extends JpaRepository<MyFile, String> {}
