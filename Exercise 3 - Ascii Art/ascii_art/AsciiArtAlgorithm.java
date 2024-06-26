package ascii_art;

import image.ImageProcessing;
import image_char_matching.SubImgCharMatcher;

/**
 * Responsible for running the algorithm.
 *
 * @author Asaf Korman
 */
public class AsciiArtAlgorithm {

    private final ImageProcessing image;
    private final int resolution;
    private final SubImgCharMatcher subImgCharMatcher;

    /**
     * Constructor.
     *
     * @param image The processed image we will use in the algorithm.
     * @param resolution The resolution of the output.
     * @param subImgCharMatcher An Instance holds the character set inside.
     */
    public AsciiArtAlgorithm(ImageProcessing image, int resolution, SubImgCharMatcher subImgCharMatcher) {
        this.subImgCharMatcher = subImgCharMatcher;
        this.resolution = resolution;
        this.image = image;
    }

    /**
     * Runs the algorithm according to the given parameters.
     *
     * @return The ascii art as a two-dimensional character array.
     */
    public char[][] run() {
        double[] brightnessArray = image.getSubImagesBrightnessArray(resolution);
        int outputHeight = image.getHeight() / (image.getWidth() / resolution);
        int outputWidth = resolution;
        char[][] output = new char[outputHeight][outputWidth];
        int row = 0, col = 0;
        for (int i = 0; i < brightnessArray.length; i++) {
            if (col == outputWidth) {
                col = 0;
                row++;
            }
            output[row][col] = subImgCharMatcher.getCharByImageBrightness(brightnessArray[i]);
            col++;
        }
        return output;
    }
}
