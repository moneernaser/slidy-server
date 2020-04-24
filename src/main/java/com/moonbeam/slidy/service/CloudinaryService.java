package com.moonbeam.slidy.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.moonbeam.slidy.config.CloudinaryProperties;
import com.moonbeam.slidy.domain.Slide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Logger log = LoggerFactory.getLogger(SlideCreateUpdateListener.class);

    @Autowired
    private CloudinaryProperties cloudinaryProperties;


    public Map uploadImage(Slide slide) throws IOException {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudinaryProperties.getCloudName(),
            "api_key", cloudinaryProperties.getApiKey(),
            "api_secret", cloudinaryProperties.getApiSecret()));
        File file = new File("temp.txt");
        try (FileOutputStream fop = new FileOutputStream(file)) {

            // if file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            fop.write(slide.getData());
            fop.flush();

        } catch (IOException e) {
            log.debug("could not save slide data to file" + e.getMessage());
        }
        Map photoInfo =  cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        log.debug("file was uploaded: " + photoInfo.get("secure_url"));
        return photoInfo;
    }
}
