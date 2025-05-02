package com.moogsan.moongsan_backend.domain.groupbuy.mapper;

import com.moogsan.moongsan_backend.domain.groupbuy.entity.GroupBuy;
import com.moogsan.moongsan_backend.domain.groupbuy.entity.Image;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImageMapper {
    public void mapImagesToGroupBuy(List<String> imageUrls, GroupBuy gb) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }

        for (int i = 0; i < imageUrls.size(); i++) {
            String url = imageUrls.get(i);
            boolean thumbnail = (i == 0);
            Image img = Image.builder()
                    .imageUrl(url)
                    //.imageUrlResized(imageUrlResized)
                    .imageSeqNo(i)
                    .thumbnail(thumbnail)
                    .groupBuy(gb)
                    .build();

            gb.getImages().add(img);
        }
    }
}
