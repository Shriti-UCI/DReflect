package edu.umich.si.inteco.minuku.model;

/**
 * Created by shriti on 7/21/16.
 */
public class AnnotatedImageDataRecord extends ImageDataRecord {

    public String imageAnnotation;

    public AnnotatedImageDataRecord(String base64Data, String annotation) {
        super(base64Data);
        this.imageAnnotation = annotation;
    }


}
