package jvm.pablohdz.myfilesapi.service;

import java.util.List;
import org.springframework.core.io.ByteArrayResource;

public interface FileStorageService {
  String upload(byte[] file, String fileName, String username);

  ByteArrayResource getFile(String storageId);

  void update(String storageId, byte[] bytes, String filename);

  List<String> findAllByPrefix(String username);

  void delete(String storageId);
}
