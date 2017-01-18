package sceneryfx;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.io.yaml.YamlConfigIO;

import java.io.IOException;
import java.util.Random;

/**
 * Created by dibrov on 18/01/17.
 */
public class AcquisitionUnit {
    private Img mSubstack;
    private int mExposureTimeInMilliseconds;
    private int mLaserPowerInMilliWatts;
    private double mTimeStep;
    private int mLocation;
    private int mDimX;
    private int mDimY;
    private int mDimZ;

    public AcquisitionUnit(int pLocation, Img pSubstack, int pExposureTimeInMilliseconds, int pLaserPowerInMilliWatts,
                           double pTimestep) {
        this.mLocation = pLocation;
        this.mExposureTimeInMilliseconds = pExposureTimeInMilliseconds;
        this.mLaserPowerInMilliWatts = pLaserPowerInMilliWatts;
        this.mSubstack = pSubstack;
        this.mTimeStep = pTimestep;
        this.mDimX = (int) mSubstack.dimension(0);
        this.mDimY = (int) mSubstack.dimension(1);
        this.mDimZ = (int) mSubstack.dimension(2);


    }

    public AcquisitionUnit() {
        this(0, getRandomStack(100, 100, 100), 100, 100, 0.0);
    }

    public AcquisitionUnit(int pLocation) {
        this(pLocation, getRandomStack(100, 100, 100), 100, 100, 0.0);
    }

    public Img getSubstack() {
        return mSubstack;
    }

    private static Img getRandomStack(int pDimX, int pDimY, int pDimZ) {
        Random lRandom = new Random();
        Img<UnsignedByteType> lRandomSubstack = new ArrayImgFactory().create(new long[]{pDimX, pDimY, pDimZ},
                new UnsignedByteType());
        lRandomSubstack.forEach(t -> t.set(lRandom.nextInt()));
        return lRandomSubstack;
    }

    public static void main(String[] args) {
        AcquisitionUnit lAcquisitionUnit = new AcquisitionUnit();

        System.setProperty("apple.laf.useScreenMenuBar", "true");

        Img img2 = lAcquisitionUnit.getSubstack();
        InputTriggerConfig conf = null;
        try {
            /* load input config from file */
            conf = new InputTriggerConfig(YamlConfigIO.read("./sandbox/src/main/img/bdvkeyconfig.yaml"));
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }

        //final Bdv bdv3D = BdvFunctions.show( img2, "greens");
        final Bdv bdv2D = BdvFunctions.show(img2, "reds", Bdv.options().is2D().inputTriggerConfig(conf));
    }
}
