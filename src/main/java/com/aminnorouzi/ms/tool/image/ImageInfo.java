package com.aminnorouzi.ms.tool.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ImageInfo {

    private String name;
    private int width;
    private int height;
    private boolean cacheable;
}