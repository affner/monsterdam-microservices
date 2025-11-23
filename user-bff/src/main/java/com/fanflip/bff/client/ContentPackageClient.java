package com.fanflip.bff.client;

import com.fanflip.bff.config.FeignClientInterceptor;
import com.fanflip.bff.service.dto.UserContentPackageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "multimedia", configuration = FeignClientInterceptor.class)
public interface ContentPackageClient {

    @GetMapping("/api/content-packages/by-post-id/{postId}")
    UserContentPackageDTO findOne(@PathVariable("postId") Long postId);

}
