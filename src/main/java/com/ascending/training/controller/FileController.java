package com.ascending.training.controller;

import com.amazonaws.services.s3.model.Bucket;
import com.ascending.training.service.FileService;
import com.ascending.training.service.MessageService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping(value = {"/files"})
public class FileController {
    private static final String queueName = "training_queue_ascending_com";
    private static final String fileDownloadDir = "/Users/gardness/Downloads/ASCENDING/Notes";

    @Autowired
    private Logger logger;
    @Autowired
    private FileService fileService;
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/{bucketName}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadFile(@PathVariable String bucketName, @RequestParam("file") MultipartFile file) {
        String msg = String.format("The file name=%s, size=%d could not be uploaded.", file.getOriginalFilename(), file.getSize());
        ResponseEntity responseEntity = ResponseEntity.status(HttpServletResponse.SC_NOT_ACCEPTABLE).body(msg);

        try {
            // Get local file path
            String path = System.getProperty("user.dir") + File.separator + "temp";

            // Save the file to path
            fileService.saveFile(file, path);

            // Upload the url
            String url = fileService.uploadFile(bucketName, file);

            if (url != null) {
                msg = String.format("The file name=%s, size=%d was uploaded, url=%s", file.getOriginalFilename(), file.getSize(), url);
                messageService.sendMessage(queueName, url);
                responseEntity = ResponseEntity.status(HttpServletResponse.SC_OK).body(msg);
            }

            logger.info(msg);
        } catch (Exception e) {
            responseEntity = ResponseEntity.status(HttpServletResponse.SC_NOT_ACCEPTABLE).body(e.getMessage());
            logger.error(e.getMessage());
        }

        return responseEntity;
    }

    // Download the file from server, have nothing to do with S3
    @RequestMapping(value = "/{fileName}", method = RequestMethod.GET, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = null;
        String msg = "The file doesn't exist.";
        ResponseEntity responseEntity;

        try {
            Path filePath = Paths.get(fileDownloadDir).toAbsolutePath().resolve(fileName).normalize();
            resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(msg);
            }

            responseEntity = ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

            msg = String.format("The file %s was downloaded", resource.getFilename());
            //Send message to SQS
            messageService.sendMessage(queueName, msg);
            logger.info(msg);
        } catch (Exception ex) {
            responseEntity = ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body(ex.getMessage());
            logger.error(ex.getMessage());
        }

        return responseEntity;
    }

    @RequestMapping(value = "/{bucketName}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteBucket(@PathVariable String bucketName) {
        String msg = String.format("Unable to delete bucket %s.", bucketName);
        ResponseEntity responseEntity = ResponseEntity.status(HttpServletResponse.SC_NOT_ACCEPTABLE).body(msg);

        boolean isSuccess = fileService.deleteAnUnversionedBucket(bucketName);

        if (isSuccess) {
            msg = String.format("Bucket %s has been deleted.", bucketName);
            responseEntity = ResponseEntity.status(HttpServletResponse.SC_OK).body(msg);
            messageService.sendMessage(queueName, msg);
        }

        logger.info(msg);

        return responseEntity;
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createBucket(@RequestBody Map<String, String> map) {
        String msg = String.format("Bucket %s can't be created.", map.get("bucketName"));
        ResponseEntity responseEntity = ResponseEntity.status(HttpServletResponse.SC_NOT_ACCEPTABLE).body(msg);

        Bucket bucket = fileService.createBucket(map.get("bucketName"));

        if (bucket != null) {
            msg = String.format("Bucket %s has been created successfully.", map.get("bucketName"));
            responseEntity = ResponseEntity.status(HttpServletResponse.SC_OK).body(msg);
            messageService.sendMessage(queueName, msg);
        }

        logger.info(msg);

        return responseEntity;
    }
}