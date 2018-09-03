package afilbert.imgresizer;

        import android.graphics.Bitmap;
        import android.os.Parcel;
        import android.os.Parcelable;

        import java.lang.reflect.Array;
        import java.util.ArrayList;
        import java.util.BitSet;

/**
 * Image contains a Bitmap object, an array of integers representing pixel values
 * and methods related to drawing images.
 *
 * @author CMPUT301W18T06
 */

public class Image implements Parcelable {
    private Bitmap image;
    private int width;
    private int height;

    private int[] image_array;

    private String id;
    /**
     * Creates an instance of Image.
     * Copies the bitmap pixel data values onto image_array.
     * @param bitmap Bitmap object representing the image
     */
    public Image(Bitmap bitmap) {
        this.image = bitmap;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        this.image_array = new int[this.width * this.height];
        bitmap.getPixels(this.image_array, 0, this.width, 0, 0, this.width, this.height);
    }

    /**
     * Recreates the bitmap object with pixel values based on image_array.
     */
    public void recreateRecycledBitmap() {
        this.image = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
        this.image.setPixels(this.image_array, 0, this.width, 0, 0, width, height);
    }

    /**
     * Creates an instance of Image.
     * @param in Parcel object containing image data
     */
    protected Image(Parcel in) {
        image = in.readParcelable(Bitmap.class.getClassLoader());
        width = in.readInt();
        height = in.readInt();
        image_array = in.createIntArray();
    }

    /**
     * Writes image data to Parcel object.
     * @param dest Parcel object
     * @param flags How the object should be written
     * @see Parcelable
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(image, flags);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeIntArray(image_array);
    }

    /**
     * This method is not implemented.
     * @return 0
     * @see Parcelable
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Creates an Image Creator object to generate Images from Parcel objects.
     */
    public static final Creator<Image> CREATOR = new Creator<Image>() {

        /**
         * Generates an Image object from Parcel
         * @param in Parcel object
         * @return new Image object
         */
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        /**
         * Creates an empty array of Images.
         * @param size integer representing Image array size
         * @return new array of Images
         */
        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    /** @return bitmap object representing the image */
    public Bitmap getImageBitmap() {
        return this.image;
    }

    /** @return Integer array representing image pixel values */
    public int[] getImageArray() { return this.image_array; }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
