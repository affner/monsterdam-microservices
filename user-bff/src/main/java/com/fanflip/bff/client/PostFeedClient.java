package com.fanflip.bff.client;

import com.fanflip.bff.config.FeignClientInterceptor;
import com.fanflip.bff.service.dto.UserPostFeedDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "interactions", configuration = FeignClientInterceptor.class)
public interface PostFeedClient {

    @GetMapping("/api/post-feeds")
    List<UserPostFeedDTO> findAll(Pageable pageable);

    @GetMapping("/api/post-feeds/{id}")
    UserPostFeedDTO findOne(@PathVariable("id") Long id);

    @GetMapping("/_search")
    List<UserPostFeedDTO> search(@RequestParam("query") String query, Pageable pageable);
}
