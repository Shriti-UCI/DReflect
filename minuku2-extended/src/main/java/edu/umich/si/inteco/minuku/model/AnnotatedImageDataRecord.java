package edu.umich.si.inteco.minuku.model;

/**
 * Created by shriti on 7/21/16.
 */
public class AnnotatedImageDataRecord extends ImageDataRecord {

    public String imageAnnotation;
    public String imageSource;

    public AnnotatedImageDataRecord() {
        super();
    }

    public AnnotatedImageDataRecord(String base64Data, String annotation, String source) {
        super(base64Data);
        this.imageAnnotation = annotation;
        this.imageSource = source;
    }

    public String getImageAnnotation() {
        return imageAnnotation;
    }

    public String getImageSource() {return imageSource; }

    public void setImageAnnotation(String imageAnnotation) {
        this.imageAnnotation = imageAnnotation;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }
}
