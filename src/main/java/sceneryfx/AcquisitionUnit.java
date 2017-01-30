package sceneryfx;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
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
    private double mTimePoint;
    private long[] mLocation;
    private int mDimX;
    private int mDimY;
    private int mDimZ;


    public AcquisitionUnit(long[] pLocation, Img pSubstack, int pExposureTimeInMilliseconds, int
            pLaserPowerInMilliWatts,
                           double pTimestep) {
        this.mLocation = pLocation;
        this.mExposureTimeInMilliseconds = pExposureTimeInMilliseconds;
        this.mLaserPowerInMilliWatts = pLaserPowerInMilliWatts;
        this.mSubstack = pSubstack;
        this.mTimePoint = pTimestep;
        this.mDimX = (int) mSubstack.dimension(0);
        this.mDimY = (int) mSubstack.dimension(1);
        this.mDimZ = (int) mSubstack.dimension(2);

    }

    public static AcquisitionUnit getAcquisitionUnitWithRandomSubstack(long[] pLocation, long[] pDimensions) {
        return new AcquisitionUnit(pLocation, getRandomStack((int) pDimensions[0], (int) pDimensions[1],
                (int) pDimensions[2]), 100, 100, 0.0);
    }

    public static AcquisitionUnit getAcquisitionUnitFromSampleSpace(long[] pLocation, long[] pDimensions,
                                                                    SampleSpace pSampleSpace) {
        long[] pFin = {pLocation[0] + pDimensions[0] - 1, pLocation[1] + pDimensions[1] - 1, pLocation[2] +
                pDimensions[2] - 1};
        return new AcquisitionUnit(pLocation, pSampleSpace.getSubstack(pLocation, pFin), 100, 100, 0.0);
    }

    public AcquisitionUnit() {
        this(new long[]{0, 0, 0}, getRandomStack(100, 100, 100), 100, 100, 0.0);
    }

    public long[] getLocation() {
        return mLocation;
    }

    public int getDimY() {
        return mDimY;
    }

    public int getDimZ() {
        return mDimZ;
    }

    public int getDimX() {
        return mDimX;
    }

    public AcquisitionUnit(long[] pLocation) {
        this(pLocation, getRandomStack(100, 100, 100), 100, 100, 0.0);
    }

    public Img getSubstack() {
        return mSubstack;
    }

    public static Img getRandomStack(long pDimX, long pDimY, long pDimZ) {
        Random lRandom = new Random();
        Img<UnsignedByteType> lRandomSubstack = new ArrayImgFactory().create(new long[]{pDimX, pDimY, pDimZ},
                new UnsignedByteType());
        lRandomSubstack.forEach(t -> t.set(lRandom.nextInt()));
        return lRandomSubstack;
    }

    public double getTimePoint() {
        return mTimePoint;
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
