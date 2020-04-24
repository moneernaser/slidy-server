package com.moonbeam.slidy.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.moonbeam.slidy.config.CloudinaryProperties;
import com.moonbeam.slidy.domain.Slide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Component
public class SlideCreateUpdateListener {



    @Autowired
    CloudinaryService cloudinaryService;


    @PrePersist
    @PreUpdate
    public void methodExecuteBeforeSave(final Slide slide) throws IOException {
        //upload photo to cloudinary
        if (slide.getData() != null && slide.getData().length > 0) {
            Map map = uploadPhoto(slide);
            slide.setUrl((String) map.get("secure_url"));
            slide.setData(null);
        }
    }


    private Map uploadPhoto(Slide slide) throws IOException {

        Map map = cloudinaryService.uploadImage(slide);
        slide.setData(null);
        return map;
    }

}
