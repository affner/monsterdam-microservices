package com.fanflip.bff.client;

import com.fanflip.bff.config.FeignClientInterceptor;
import com.fanflip.bff.service.dto.AdminPostFeedDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "interactions", configuration = FeignClientInterceptor.class)
public interface PostFeedClient {

    @GetMapping("/api/post-feeds")
    List<AdminPostFeedDTO> findAll(Pageable pageable);

    @GetMapping("/api/post-feeds/{id}")
    AdminPostFeedDTO findOne(@PathVariable("id") Long id);

    @GetMapping("/_search")
    List<AdminPostFeedDTO> search(@RequestParam("query") String query, Pageable pageable);
}
