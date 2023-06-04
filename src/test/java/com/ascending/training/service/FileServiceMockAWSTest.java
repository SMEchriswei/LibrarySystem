package com.ascending.training.service;

import com.amazonaws.services.s3.AmazonS3;
import com.ascending.training.init.AppInitializer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppInitializer.class)
public class FileServiceMockAWSTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private AmazonS3 amazonS3;

    @Autowired
    @Spy
    private Logger logger;

    @InjectMocks
    private FileService fileService;

    @Mock
    private MultipartFile multipartFile;

    private String bucketName = "gardnessbucket";
    private String fileName = "hello.txt";
    private URL fakeFileUrl;

    @Before
    public void setUp() throws MalformedURLException, FileNotFoundException, IOException {
        logger.debug(">>>>>>>>>>>>>>>>> Start testing ...");

        // Mocks are initialized before each test method
        MockitoAnnotations.initMocks(this);

        fakeFileUrl = new URL("http://www.fakeQueueUrl.com/abc/123/fake");

        // Stubbing for the methods in the object multipartfile
        when(multipartFile.getOriginalFilename()).thenReturn("anyFileName");
        when(multipartFile.getContentType()).thenReturn("Application");
        when(multipartFile.getSize()).thenReturn(9999L);
        when(multipartFile.getInputStream()).thenReturn(mock(InputStream.class));

        // Stubbing for the methods doesObjectExist and generatePresignedUrl in the object amazonS3
        when(amazonS3.doesObjectExist(anyString(), anyString())).thenReturn(false);
        when(amazonS3.generatePresignedUrl(any())).thenReturn(fakeFileUrl);
    }

    @After
    public void tearDown() {
        logger.debug(">>>>>>>>> End test");
    }

    @Test
    public void getFileUrl() {
        String fileUrl = fileService.getFileUrl(bucketName, fileName);
        Assert.assertEquals(fileUrl, fakeFileUrl.toString());
        verify(amazonS3, times(1)).generatePresignedUrl(any());
    }

    @Test
    public void uploadFile() throws IOException {
        fileService.uploadFile(bucketName, multipartFile);
        verify(amazonS3, times(1)).doesObjectExist(anyString(), anyString());
        verify(amazonS3, times(1)).putObject(anyString(), anyString(), any(), any());
    }

    @Test
    public void saveFile() throws IOException, FileNotFoundException {
        // Dummies
        MultipartFile multipartFile = new MockMultipartFile(" ", new byte[1]);
        String path = " ";

        // Annotation @Mock can only be used for calls variables
        // Create mocked object fshttps://tomcat.apache.org/download-80.cgi
        FileService fs = Mockito.mock(FileService.class);

        // Stubbing
        //when(fs.saveFile(any(), anyString())).thenReturn(true);
        doReturn(true).when(fs).saveFile(any(), anyString());

        // Use dummies as parameters
        boolean isSuccess = fs.saveFile(multipartFile, path);

        // Verify state
        Assert.assertTrue(isSuccess);

        // Verify behavior
        verify(fs, times(1)).saveFile(any(), anyString());
    }
}
