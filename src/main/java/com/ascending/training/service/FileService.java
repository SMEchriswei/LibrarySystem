package com.ascending.training.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private Logger logger;

    public Bucket createBucket(String bucketName) {
        Bucket bucket = null;

        if (amazonS3.doesBucketExistV2(bucketName)) {
            logger.info(String.format("Bucket %s already exists.\n", bucketName));
            bucket = getBucket(bucketName);
        } else {
            try {
                bucket = amazonS3.createBucket(bucketName);
            } catch (AmazonS3Exception e) {
                logger.error(e.getErrorMessage());
            }
        }

        return bucket;
    }

    // Remove an unemptied and unversioned bucket
    public boolean deleteAnUnversionedBucket(String bucketName) {
        boolean isSuccess = false;

        if (!amazonS3.doesBucketExistV2(bucketName)) {
            logger.info(String.format("Bucket %s doesn't exist.\n", bucketName));

            return isSuccess;
        }

        logger.info("Deleting S3 bucket: " + bucketName);
        logger.info("The bucket location is " + amazonS3.getBucketLocation(bucketName));

        try {
            logger.info(" - Removing objects from bucket");
            ObjectListing object_listing = amazonS3.listObjects(bucketName);

            while (true) {
                for (Iterator<?> iterator =
                     object_listing.getObjectSummaries().iterator();
                     iterator.hasNext(); ) {
                    S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
                    amazonS3.deleteObject(bucketName, summary.getKey());
                }

                if (object_listing.isTruncated()) {
                    object_listing = amazonS3.listNextBatchOfObjects(object_listing);
                } else {
                    break;
                }
            }

            logger.info(" - Removing versions from bucket");

            VersionListing version_listing = amazonS3.listVersions(
                    new ListVersionsRequest().withBucketName(bucketName));

            while (true) {
                for (Iterator<?> iterator =
                     version_listing.getVersionSummaries().iterator();
                     iterator.hasNext(); ) {
                    S3VersionSummary vs = (S3VersionSummary) iterator.next();
                    amazonS3.deleteVersion(bucketName, vs.getKey(), vs.getVersionId());
                }

                if (version_listing.isTruncated()) {
                    version_listing = amazonS3.listNextBatchOfVersions(version_listing);
                } else {
                    break;
                }
            }

            logger.info("Bucket is ready to be deleted.");

            amazonS3.deleteBucket(bucketName);
        } catch (AmazonServiceException e) {
            logger.error(e.getErrorMessage());
            return isSuccess;
        }

        isSuccess = true;
        logger.info("Bucket deleted.");

        return isSuccess;
    }

    public boolean doesBucketExist(String bucketName) {
        return amazonS3.doesBucketExistV2(bucketName);
    }

    public Bucket getBucket(String bucketName) {
        Bucket namedBucket = null;
        List<Bucket> buckets = amazonS3.listBuckets();

        for (Bucket bucket : buckets) {
            if (bucket.getName().equals(bucketName)) {
                namedBucket = bucket;
            }
        }

        return namedBucket;
    }

    public String getFileUrl(String bucketName, String fileName) {
        LocalDateTime expiration = LocalDateTime.now().plusDays(1);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName);
        generatePresignedUrlRequest.withMethod(HttpMethod.GET);
        generatePresignedUrlRequest.withExpiration(Date.from(expiration.toInstant(ZoneOffset.UTC)));

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    public void listBuckets() {
        List<Bucket> buckets = amazonS3.listBuckets();

        System.out.println("Your Amazon S3 buckets are: ");

        for (Bucket bucket : buckets) {
            System.out.println("* " + bucket.getName());
        }
    }

    public String uploadFile(String bucketName, MultipartFile file) throws IOException {
        try {
            if (amazonS3.doesObjectExist(bucketName, file.getOriginalFilename())) {
                logger.debug(String.format("The bucket location is %s", amazonS3.getBucketLocation(bucketName)));
                logger.debug(String.format("The file '%s' exists in the bucket %s", file.getOriginalFilename(), bucketName));
                return null;
            }

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());
            amazonS3.putObject(bucketName, file.getOriginalFilename(), file.getInputStream(), objectMetadata);
            logger.debug(String.format("The file name = %s, size = %d was uploaded to bucket %s", file.getOriginalFilename(), file.getSize(), bucketName));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

        return getFileUrl(bucketName, file.getOriginalFilename());
    }

    // Save files to a local directory, have nothing to do with S3
    public boolean saveFile(MultipartFile multipartFile, String filePath) {
        boolean isSuccess = false;

        try {
            // Create a directory if it doesn't exist
            File directory = new File(filePath);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // File
            Path filepath = Paths.get(filePath, multipartFile.getOriginalFilename());
            multipartFile.transferTo(filepath);
            isSuccess = true;
            logger.info(String.format("The file %s is saved in %s.", multipartFile.getOriginalFilename(), filepath));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return isSuccess;
    }
}
