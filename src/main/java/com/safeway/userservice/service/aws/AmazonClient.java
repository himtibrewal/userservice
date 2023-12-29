package com.safeway.userservice.service.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class AmazonClient {

    public static Logger logger = LoggerFactory.getLogger(AmazonClient.class);

    private AmazonS3 s3client;

    @Value("${amazonProperties.region}")
    private String region;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                //.withRegion(Regions.US_EAST_1)
                .build();
    }

    public String uploadFile(MultipartFile multipartFile, String folderPath) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = getAwsS3EndPoint() + "/" + bucketName + "/" + folderPath + "/" + fileName;
            uploadFileTos3bucket(folderPath, fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public String uploadFile(File file, String folderPath, boolean isDeleteLocal) {
        String fileUrl = "";
        try {
            String fileName = generateFileName(file);
            fileUrl = getAwsS3EndPoint() + "/" + bucketName + "/" + folderPath + "/" + fileName;
            uploadFileTos3bucket(folderPath, fileName, file);
            if (isDeleteLocal) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }


    private String getAwsS3EndPoint() {
        return "https://s3." + region + ".amazonaws.com";
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private String generateFileName(File file) {
        return new Date().getTime() + "-" + file.getName().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String filePath, String fileName, File file) {
        String finalPath = filePath == null ? fileName : filePath + "/" + fileName;
        if (s3client == null) {
            initializeAmazon();
        }
        s3client.putObject(new PutObjectRequest(bucketName, finalPath, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String deleteFileFromS3Bucket(String path, String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName, path + "/" + fileName));
        return "Successfully deleted";
    }
}