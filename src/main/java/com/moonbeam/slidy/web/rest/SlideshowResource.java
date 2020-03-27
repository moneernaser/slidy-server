package com.moonbeam.slidy.web.rest;

import com.moonbeam.slidy.domain.Slide;
import com.moonbeam.slidy.repository.SlideRepository;
import com.moonbeam.slidy.repository.UserRepository;
import com.moonbeam.slidy.security.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;
import java.util.List;

/**
 * REST controller for managing Slide.
 */
@RestController
@RequestMapping("/slideshow")
public class SlideshowResource {



    private final SlideRepository slideRepository;
    private final UserRepository userRepository;

    public SlideshowResource(SlideRepository slideRepository, UserRepository userRepository) {
        this.slideRepository = slideRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/")
    public RedirectView redirectToSlideshowPage(RedirectAttributes attributes) {
        attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
        System.out.println(attributes.asMap().get("user"));
        String username = SecurityUtils.getCurrentUserLogin().orElse((String) attributes.asMap().get("user"));
        attributes.addAttribute("user", username);
        return new RedirectView("/slideshow.html");
    }


    @GetMapping("/get")
    public List<Slide> getSlidesForUser(@RequestParam("user") String userlogin,
                                        @RequestParam(value = "lastCheck", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Date lastCheck,
                                        @RequestParam(value = "count", required = false) Integer count) {
        return userRepository.findOneByLogin(userlogin).map(user -> {

            // if no previous fetch
            if (lastCheck == null) {
                return slideRepository.findByUser(user);
            }

            // else: check if resources Updated since lastCheck
            boolean resourceUpdated = slideRepository.existsByLastModifiedDateAfter(lastCheck.toInstant());
            if (resourceUpdated) {
                return slideRepository.findByUser(user);
            }

            // check if some resources were Deleted (by comparing with user-specified count)
            Long userSlidesCount = slideRepository.countByUser(user);
            if (count == null || userSlidesCount.intValue() != count) {
                return slideRepository.findByUser(user);
            }

            return null;
        }).orElse(null);
    }

}
