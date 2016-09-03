package edu.umich.si.inteco.minuku_2.model;

import edu.umich.si.inteco.minuku.model.AnnotatedImageDataRecord;

/**
 * Created by shriti on 7/31/16.
 */
public class FoodImage extends AnnotatedImageDataRecord {

    public FoodImage() {

    }

    public FoodImage(String base64Data, String annotation, String imageSource) {
        super(base64Data, annotation, imageSource);
    }
}
