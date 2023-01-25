package com.aminnorouzi.ms.tool.image;

import lombok.Data;

@Data
public class ImageInfo {

    private String name;
    private String type; // poster, backdrop
    private int width;
    private int height;
    private boolean cacheable;
}