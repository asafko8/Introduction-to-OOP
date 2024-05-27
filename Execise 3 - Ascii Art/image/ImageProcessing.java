package image;

import java.awt.*;

/**
 * Responsible for processing and preparing the image to the run of the AsciiArtAlgorithm.
 *
 * @author Asaf Korman
 */
public class ImageProcessing {

    private static final int DEFAULT_RESOLUTION = 128;
    private static final double RED_TO_GRAY = 0.2126;
    private static final double GREEN_TO_GRAY = 0.7152;
    private static final double BLUE_TO_GRAY = 0.0722;
    private static final double MAX_BRIGHT_VALUE = 255;
    private final Image image;
    private double[] subImagesBrightnessArray;
    private final int width;
    private final int height;
    private int resolution;
    private final int minCharsInRow;
    private final int maxCharsInRow;
    private Color[][] pixelsArray;

    /**
     * Constructor.
     *
     * @param image The image it wil process.
     */
    public ImageProcessing(Image image) {
        this.height = (int)Math.pow(2, (int)Math.floor(Math.log(image.getHeight()) / Math.log(2)));
        this.width = (int)Math.pow(2, (int)Math.floor(Math.log(image.getWidth()) / Math.log(2)));
        this.image = imagePadding(image, image.getHeight(), image.getWidth());
        this.maxCharsInRow = this.width;
        this.minCharsInRow = Math.max(1, width / height);
        this.resolution = DEFAULT_RESOLUTION;
        if (width >= resolution) {
            splitToSubImages();
        }
    }

    /**
     * Getter.
     *
     * @param resolution The resolution according to which we will split our image to sub-images.
     * @return The data structure holds the sub-images and their brightness.
     */
    public double[] getSubImagesBrightnessArray(int resolution) {
        if (this.resolution != resolution) {
            this.resolution = resolution;
            splitToSubImages();
        }
        return this.subImagesBrightnessArray;
    }

    /*
     * Splits the image to sub-images according to the current resolution, calculate its brightness, and put
     * holds it in a data structure.
     */
    private void splitToSubImages() {
        int subImageSize = image.getWidth() / resolution;
        subImagesBrightnessArray = new double[resolution * (image.getHeight() / subImageSize)];
        int index = 0;
        for (int y = 0; y < image.getHeight(); y += subImageSize) {
            for (int x = 0; x < image.getWidth(); x += subImageSize) {
                Color[][] subImagePixels = new Color[subImageSize][subImageSize];
                for (int i = 0; i < subImageSize; i++) {
                    System.arraycopy(pixelsArray[y + i], x, subImagePixels[i], 0, subImageSize);
                }
                subImagesBrightnessArray[index] = subImageBrightness(subImagePixels);
                index++;
            }
        }
    }

    /*
     * Pads the image with white pixels so that the dimensions of the image are a power of 2.
     */
    private Image imagePadding(Image image, int prevHeight, int prevWidth) {
        pixelsArray = new Color[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i < prevHeight && j < prevWidth) {
                    pixelsArray[i][j] = image.getPixel(i, j);
                }
                else {
                    pixelsArray[i][j] = Color.WHITE;
                }
            }
        }
        return new Image(pixelsArray, width, height);
    }

    /*
     * Returns the gray shade of a given pixel.
     */
    private static double getGrayShade(Color pixel) {
        return pixel.getRed() * RED_TO_GRAY + pixel.getGreen() * GREEN_TO_GRAY +
                pixel.getBlue() * BLUE_TO_GRAY;
    }

    /*
     * Calculate and returns the brightness of image.
     */
    private static double subImageBrightness(Color[][] pixels) {
        double sumGrayShades = 0;
        for (Color[] pixel : pixels) {
            for (int j = 0; j < pixels.length; j++) {
                sumGrayShades += getGrayShade(pixel[j]);
            }
        }
        return (sumGrayShades / (pixels.length * pixels[0].length)) / MAX_BRIGHT_VALUE;
    }

    /**
     * Getter.
     *
     * @return The height of the image after the padding.
     */
    public int getHeight() {
        return image.getHeight();
    }

    /**
     * Getter.
     *
     * @return The width of the image after the padding.
     */
    public int getWidth() {
        return image.getWidth();
    }

    /**
     * Getter.
     *
     * @return The minimum Resolution image can get.
     */
    public int getMinCharsInRow() {
        return minCharsInRow;
    }

    /**
     * Getter.
     *
     * @return The maximum resolution image can get.
     */
    public int getMaxCharsInRow() {
        return maxCharsInRow;
    }
}
