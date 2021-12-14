package jvm.pablohdz.myfilesapi.service;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

class MyCSVServiceTest {

  @Test
  void listAllBuckets() {
    Region region = Region.US_EAST_2;
    S3Client s3Client = S3Client.builder().region(region).build();

    ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
    ListBucketsResponse listBucketsResponse = s3Client.listBuckets(listBucketsRequest);
    listBucketsResponse.buckets().forEach(bucket -> System.out.println(bucket.name()));
  }
}
