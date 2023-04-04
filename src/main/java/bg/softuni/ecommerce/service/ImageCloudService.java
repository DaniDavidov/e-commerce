package bg.softuni.ecommerce.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageCloudService {

    private Cloudinary cloudinary;

    public ImageCloudService() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "du90acnb6",
                "api_key", "822523692679856",
                "api_secret", "uj_V7_awLcVs8-d-t-M2uFCeXzY",
                "secure", true
        ));
    }

    public String saveImage(MultipartFile multipartFile) {
        String imageId = UUID.randomUUID().toString();

        Map params = ObjectUtils.asMap(
                "public_id", imageId,
                "overwrite", true,
                "resource_type", "image"
        );

        File tempFile = new File(imageId);

        try {
            Files.write(tempFile.toPath(), multipartFile.getBytes());
            cloudinary.uploader().upload(tempFile, params);
            Files.delete(tempFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return String.format("https://res.cloudinary.com/du90acnb6/image/upload/v1680529770/" +
                imageId + "." + getFileExtension(multipartFile.getOriginalFilename()));
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
