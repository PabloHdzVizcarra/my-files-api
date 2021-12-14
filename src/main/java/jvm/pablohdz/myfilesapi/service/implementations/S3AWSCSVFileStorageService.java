package jvm.pablohdz.myfilesapi.service.implementations;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import jvm.pablohdz.myfilesapi.service.CSVFileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3AWSCSVFileStorageService implements CSVFileStorageService {
  @Value("${aws.bucket.name}")
  private String bucketName;

  @Value("${aws.bucket.prefix-name}")
  private String prefixKey;

  @Override
  public String upload(byte[] fileBytes, String fileName) {
    S3Client s3Client = createS3Client();
    String keyToFile = pathToFile();
    Map<String, String> metadata = new HashMap<>();
    metadata.put("file.name", fileName);
    PutObjectRequest putObjectRequest =
        PutObjectRequest.builder().bucket(bucketName).metadata(metadata).key(keyToFile).build();
    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));

    return keyToFile;
  }

  private S3Client createS3Client() {
    Region region = Region.US_EAST_2;
    return S3Client.builder().region(region).build();
  }

  private String pathToFile() {
    return prefixKey + generateUniqueKeyToFile();
  }

  private String generateUniqueKeyToFile() {
    return "csv_" + UUID.randomUUID();
  }
}
