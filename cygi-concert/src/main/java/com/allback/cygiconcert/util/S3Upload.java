package com.allback.cygiconcert.util;

import com.allback.cygiconcert.util.exception.FileUploadFailedException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
@RequiredArgsConstructor
public class S3Upload {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private String CLOUD_FRONT_DOMAIN_NAME = "https://dlvlaecyg32se.cloudfront.net";

    public String uploadFileV1(String fileName, MultipartFile image)
        throws Exception {
        validateFileExists(image);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(image.getContentType());
        String dir = "concert/";
        try (InputStream inputStream = image.getInputStream()) {
            amazonS3.putObject(
                new PutObjectRequest(bucketName, dir + fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new FileUploadFailedException();
        }

        return CLOUD_FRONT_DOMAIN_NAME + "/" + fileName;
    }

    private void validateFileExists(MultipartFile multipartFile) throws Exception {
        if (multipartFile.isEmpty()) {
            throw new Exception();
        }
    }
}
