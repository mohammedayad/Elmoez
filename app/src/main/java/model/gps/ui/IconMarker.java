package model.gps.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import model.gps.ui.objects.PaintableIcon;

//import com.jwetherell.augmented_reality.ui.objects.PaintableIcon;

/**
 * This class extends Marker and draws an icon instead of a circle for it's
 * visual representation.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class IconMarker extends Marker {

    private Bitmap bitmap = null;

    //constractor for google places data source without summary
    public IconMarker(String name, double latitude, double longitude, double altitude, int color, Bitmap bitmap) {
        super(name, latitude, longitude, altitude, color);
        this.bitmap = bitmap;
    }
    //constractor for wiki data source to get summary
    public IconMarker(String name, double latitude, double longitude, double altitude, int color, Bitmap bitmap,String summary) {
        super(name, latitude, longitude, altitude, color,summary);
        this.bitmap = bitmap;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void drawIcon(Canvas canvas) {
        if (canvas == null || bitmap == null) throw new NullPointerException();

        // gpsSymbol is defined in Marker
        if (gpsSymbol == null) gpsSymbol = new PaintableIcon(bitmap, 96, 96);
        super.drawIcon(canvas);
    }
}
