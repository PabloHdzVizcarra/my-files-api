package jvm.pablohdz.myfilesapi.service.implementations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import jvm.pablohdz.myfilesapi.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

@Service
public class S3AWSCSVFileStorageService implements FileStorageService {
  public static final String FILENAME = "filename";
  public static final String PRE_KEY_ID = "file_";

  @Value("${aws.bucket.name}")
  private String bucketName;

  @Value("${aws.bucket.prefix-name}")
  private String prefixKey;

  @Override
  public String upload(byte[] fileBytes, String filename, String username) {
    S3Client s3Client = createS3Client();
    String keyToFile = pathToFile(username);
    Map<String, String> metadata = new HashMap<>();
    metadata.put(FILENAME, filename);
    PutObjectRequest putObjectRequest =
        PutObjectRequest.builder().bucket(bucketName).metadata(metadata).key(keyToFile).build();
    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));

    return keyToFile;
  }

  @Override
  public @Nullable InputStreamResource getFile(String storageId) {
    S3Client s3Client = createS3Client();
    GetObjectRequest getObjectRequest =
        GetObjectRequest.builder().bucket(bucketName).key(storageId).build();
    ResponseInputStream<GetObjectResponse> s3ClientObject = s3Client.getObject(getObjectRequest);

    try {
      byte[] bytesFile = s3ClientObject.readAllBytes();
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytesFile);
      return new InputStreamResource(byteArrayInputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void update(String storageId, byte[] bytes, String filename) {
    S3Client s3Client = createS3Client();
    Map<String, String> metadata = new HashMap<>();
    metadata.put(FILENAME, filename);
    PutObjectRequest putObjectRequest =
        PutObjectRequest.builder().bucket(bucketName).metadata(metadata).key(storageId).build();
    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
  }

  @Override
  public List<String> findAllByPrefix(String username) {
    String findPrefix = createFindPrefix(username);

    try (S3Client s3Client = createS3Client()) {
      ListObjectsRequest listObjectsRequest =
          ListObjectsRequest.builder().bucket(bucketName).prefix(findPrefix).build();
      ListObjectsResponse response = s3Client.listObjects(listObjectsRequest);
      List<S3Object> objects = response.contents();
      return objects.stream().map(S3Object::key).collect(Collectors.toUnmodifiableList());

    } catch (SdkException exception) {
      throw new IllegalStateException(exception.getMessage());
    }
  }

  private String createFindPrefix(String username) {
    return prefixKey + username + "/" + PRE_KEY_ID;
  }

  private S3Client createS3Client() {
    Region region = Region.US_EAST_2;
    return S3Client.builder().region(region).build();
  }

  private String pathToFile(String username) {
    return prefixKey + username + "/" + generateUniqueKeyToFile();
  }

  private String generateUniqueKeyToFile() {
    return PRE_KEY_ID + UUID.randomUUID();
  }

  @Override
  public void delete(String storageId) {}
}
