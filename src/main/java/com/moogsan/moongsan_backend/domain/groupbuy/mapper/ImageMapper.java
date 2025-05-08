package com.moogsan.moongsan_backend.domain.groupbuy.mapper;

import com.moogsan.moongsan_backend.domain.groupbuy.entity.GroupBuy;
import com.moogsan.moongsan_backend.domain.image.entity.Image;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImageMapper {
    public void mapImagesToGroupBuy(List<String> imageKeys, GroupBuy gb) {
        if (imageKeys == null || imageKeys.isEmpty()) {
            return;
        }

        for (int i = 0; i < imageKeys.size(); i++) {
            String key = imageKeys.get(i);
            boolean thumbnail = (i == 0);
            Image img = Image.builder()
                    .imageKey(key)
                    //.imageUrlResized(imageUrlResized)
                    .imageSeqNo(i)
                    .thumbnail(thumbnail)
                    .groupBuy(gb)
                    .build();

            gb.getImages().add(img);
        }
    }
}
