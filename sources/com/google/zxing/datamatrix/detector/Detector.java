package com.google.zxing.datamatrix.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.detector.WhiteRectangleDetector;

/* loaded from: classes2.dex */
public final class Detector {
    private final BitMatrix image;
    private final WhiteRectangleDetector rectangleDetector;

    public Detector(BitMatrix bitMatrix) throws NotFoundException {
        this.image = bitMatrix;
        this.rectangleDetector = new WhiteRectangleDetector(bitMatrix);
    }

    public DetectorResult detect() throws NotFoundException {
        int iMax;
        ResultPoint[] resultPointArrDetectSolid2 = detectSolid2(detectSolid1(this.rectangleDetector.detect()));
        ResultPoint resultPointCorrectTopRight = correctTopRight(resultPointArrDetectSolid2);
        resultPointArrDetectSolid2[3] = resultPointCorrectTopRight;
        if (resultPointCorrectTopRight == null) {
            throw NotFoundException.getNotFoundInstance();
        }
        ResultPoint[] resultPointArrShiftToModuleCenter = shiftToModuleCenter(resultPointArrDetectSolid2);
        ResultPoint resultPoint = resultPointArrShiftToModuleCenter[0];
        ResultPoint resultPoint2 = resultPointArrShiftToModuleCenter[1];
        ResultPoint resultPoint3 = resultPointArrShiftToModuleCenter[2];
        ResultPoint resultPoint4 = resultPointArrShiftToModuleCenter[3];
        int iTransitionsBetween = transitionsBetween(resultPoint, resultPoint4);
        int i = iTransitionsBetween + 1;
        int iTransitionsBetween2 = transitionsBetween(resultPoint3, resultPoint4);
        int i2 = iTransitionsBetween2 + 1;
        if ((i & 1) == 1) {
            i = iTransitionsBetween + 2;
        }
        if ((i2 & 1) == 1) {
            i2 = iTransitionsBetween2 + 2;
        }
        if (i * 4 >= i2 * 6 || i2 * 4 >= i * 6) {
            iMax = i;
        } else {
            iMax = Math.max(i, i2);
            i2 = iMax;
        }
        return new DetectorResult(sampleGrid(this.image, resultPoint, resultPoint2, resultPoint3, resultPoint4, iMax, i2), new ResultPoint[]{resultPoint, resultPoint2, resultPoint3, resultPoint4});
    }

    private static ResultPoint shiftPoint(ResultPoint resultPoint, ResultPoint resultPoint2, int i) {
        float f = i + 1;
        return new ResultPoint(resultPoint.getX() + ((resultPoint2.getX() - resultPoint.getX()) / f), resultPoint.getY() + ((resultPoint2.getY() - resultPoint.getY()) / f));
    }

    private static ResultPoint moveAway(ResultPoint resultPoint, float f, float f2) {
        float x = resultPoint.getX();
        float y = resultPoint.getY();
        return new ResultPoint(x < f ? x - 1.0f : x + 1.0f, y < f2 ? y - 1.0f : y + 1.0f);
    }

    private ResultPoint[] detectSolid1(ResultPoint[] resultPointArr) {
        ResultPoint resultPoint = resultPointArr[0];
        ResultPoint resultPoint2 = resultPointArr[1];
        ResultPoint resultPoint3 = resultPointArr[3];
        ResultPoint resultPoint4 = resultPointArr[2];
        int iTransitionsBetween = transitionsBetween(resultPoint, resultPoint2);
        int iTransitionsBetween2 = transitionsBetween(resultPoint2, resultPoint3);
        int iTransitionsBetween3 = transitionsBetween(resultPoint3, resultPoint4);
        int iTransitionsBetween4 = transitionsBetween(resultPoint4, resultPoint);
        ResultPoint[] resultPointArr2 = {resultPoint4, resultPoint, resultPoint2, resultPoint3};
        if (iTransitionsBetween > iTransitionsBetween2) {
            resultPointArr2[0] = resultPoint;
            resultPointArr2[1] = resultPoint2;
            resultPointArr2[2] = resultPoint3;
            resultPointArr2[3] = resultPoint4;
            iTransitionsBetween = iTransitionsBetween2;
        }
        if (iTransitionsBetween > iTransitionsBetween3) {
            resultPointArr2[0] = resultPoint2;
            resultPointArr2[1] = resultPoint3;
            resultPointArr2[2] = resultPoint4;
            resultPointArr2[3] = resultPoint;
        } else {
            iTransitionsBetween3 = iTransitionsBetween;
        }
        if (iTransitionsBetween3 > iTransitionsBetween4) {
            resultPointArr2[0] = resultPoint3;
            resultPointArr2[1] = resultPoint4;
            resultPointArr2[2] = resultPoint;
            resultPointArr2[3] = resultPoint2;
        }
        return resultPointArr2;
    }

    private ResultPoint[] detectSolid2(ResultPoint[] resultPointArr) {
        ResultPoint resultPoint = resultPointArr[0];
        ResultPoint resultPoint2 = resultPointArr[1];
        ResultPoint resultPoint3 = resultPointArr[2];
        ResultPoint resultPoint4 = resultPointArr[3];
        int iTransitionsBetween = (transitionsBetween(resultPoint, resultPoint4) + 1) * 4;
        if (transitionsBetween(shiftPoint(resultPoint2, resultPoint3, iTransitionsBetween), resultPoint) < transitionsBetween(shiftPoint(resultPoint3, resultPoint2, iTransitionsBetween), resultPoint4)) {
            resultPointArr[0] = resultPoint;
            resultPointArr[1] = resultPoint2;
            resultPointArr[2] = resultPoint3;
            resultPointArr[3] = resultPoint4;
            return resultPointArr;
        }
        resultPointArr[0] = resultPoint2;
        resultPointArr[1] = resultPoint3;
        resultPointArr[2] = resultPoint4;
        resultPointArr[3] = resultPoint;
        return resultPointArr;
    }

    private ResultPoint correctTopRight(ResultPoint[] resultPointArr) {
        ResultPoint resultPoint = resultPointArr[0];
        ResultPoint resultPoint2 = resultPointArr[1];
        ResultPoint resultPoint3 = resultPointArr[2];
        ResultPoint resultPoint4 = resultPointArr[3];
        int iTransitionsBetween = transitionsBetween(resultPoint, resultPoint4);
        ResultPoint resultPointShiftPoint = shiftPoint(resultPoint, resultPoint2, (transitionsBetween(resultPoint2, resultPoint4) + 1) * 4);
        ResultPoint resultPointShiftPoint2 = shiftPoint(resultPoint3, resultPoint2, (iTransitionsBetween + 1) * 4);
        int iTransitionsBetween2 = transitionsBetween(resultPointShiftPoint, resultPoint4);
        int iTransitionsBetween3 = transitionsBetween(resultPointShiftPoint2, resultPoint4);
        float f = iTransitionsBetween2 + 1;
        ResultPoint resultPoint5 = new ResultPoint(resultPoint4.getX() + ((resultPoint3.getX() - resultPoint2.getX()) / f), resultPoint4.getY() + ((resultPoint3.getY() - resultPoint2.getY()) / f));
        float f2 = iTransitionsBetween3 + 1;
        ResultPoint resultPoint6 = new ResultPoint(resultPoint4.getX() + ((resultPoint.getX() - resultPoint2.getX()) / f2), resultPoint4.getY() + ((resultPoint.getY() - resultPoint2.getY()) / f2));
        if (!isValid(resultPoint5)) {
            if (!isValid(resultPoint6)) {
                return null;
            }
        } else if (!isValid(resultPoint6) || transitionsBetween(resultPointShiftPoint, resultPoint5) + transitionsBetween(resultPointShiftPoint2, resultPoint5) > transitionsBetween(resultPointShiftPoint, resultPoint6) + transitionsBetween(resultPointShiftPoint2, resultPoint6)) {
            return resultPoint5;
        }
        return resultPoint6;
    }

    private ResultPoint[] shiftToModuleCenter(ResultPoint[] resultPointArr) {
        ResultPoint resultPoint = resultPointArr[0];
        ResultPoint resultPoint2 = resultPointArr[1];
        ResultPoint resultPoint3 = resultPointArr[2];
        ResultPoint resultPoint4 = resultPointArr[3];
        int iTransitionsBetween = transitionsBetween(resultPoint, resultPoint4) + 1;
        ResultPoint resultPointShiftPoint = shiftPoint(resultPoint, resultPoint2, (transitionsBetween(resultPoint3, resultPoint4) + 1) * 4);
        ResultPoint resultPointShiftPoint2 = shiftPoint(resultPoint3, resultPoint2, iTransitionsBetween * 4);
        int iTransitionsBetween2 = transitionsBetween(resultPointShiftPoint, resultPoint4);
        int i = iTransitionsBetween2 + 1;
        int iTransitionsBetween3 = transitionsBetween(resultPointShiftPoint2, resultPoint4);
        int i2 = iTransitionsBetween3 + 1;
        if ((i & 1) == 1) {
            i = iTransitionsBetween2 + 2;
        }
        if ((i2 & 1) == 1) {
            i2 = iTransitionsBetween3 + 2;
        }
        float x = (((resultPoint.getX() + resultPoint2.getX()) + resultPoint3.getX()) + resultPoint4.getX()) / 4.0f;
        float y = (((resultPoint.getY() + resultPoint2.getY()) + resultPoint3.getY()) + resultPoint4.getY()) / 4.0f;
        ResultPoint resultPointMoveAway = moveAway(resultPoint, x, y);
        ResultPoint resultPointMoveAway2 = moveAway(resultPoint2, x, y);
        ResultPoint resultPointMoveAway3 = moveAway(resultPoint3, x, y);
        ResultPoint resultPointMoveAway4 = moveAway(resultPoint4, x, y);
        int i3 = i2 * 4;
        int i4 = i * 4;
        return new ResultPoint[]{shiftPoint(shiftPoint(resultPointMoveAway, resultPointMoveAway2, i3), resultPointMoveAway4, i4), shiftPoint(shiftPoint(resultPointMoveAway2, resultPointMoveAway, i3), resultPointMoveAway3, i4), shiftPoint(shiftPoint(resultPointMoveAway3, resultPointMoveAway4, i3), resultPointMoveAway2, i4), shiftPoint(shiftPoint(resultPointMoveAway4, resultPointMoveAway3, i3), resultPointMoveAway, i4)};
    }

    private boolean isValid(ResultPoint resultPoint) {
        return resultPoint.getX() >= 0.0f && resultPoint.getX() <= ((float) (this.image.getWidth() - 1)) && resultPoint.getY() > 0.0f && resultPoint.getY() <= ((float) (this.image.getHeight() - 1));
    }

    private static BitMatrix sampleGrid(BitMatrix bitMatrix, ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4, int i, int i2) throws NotFoundException {
        float f = i - 0.5f;
        float f2 = i2 - 0.5f;
        return GridSampler.getInstance().sampleGrid(bitMatrix, i, i2, 0.5f, 0.5f, f, 0.5f, f, f2, 0.5f, f2, resultPoint.getX(), resultPoint.getY(), resultPoint4.getX(), resultPoint4.getY(), resultPoint3.getX(), resultPoint3.getY(), resultPoint2.getX(), resultPoint2.getY());
    }

    private int transitionsBetween(ResultPoint resultPoint, ResultPoint resultPoint2) {
        int x = (int) resultPoint.getX();
        int y = (int) resultPoint.getY();
        int x2 = (int) resultPoint2.getX();
        int iMin = Math.min(this.image.getHeight() - 1, (int) resultPoint2.getY());
        int i = 0;
        boolean z = Math.abs(iMin - y) > Math.abs(x2 - x);
        if (z) {
            y = x;
            x = y;
            iMin = x2;
            x2 = iMin;
        }
        int iAbs = Math.abs(x2 - x);
        int iAbs2 = Math.abs(iMin - y);
        int i2 = (-iAbs) / 2;
        int i3 = y < iMin ? 1 : -1;
        int i4 = x >= x2 ? -1 : 1;
        boolean z2 = this.image.get(z ? y : x, z ? x : y);
        while (x != x2) {
            boolean z3 = this.image.get(z ? y : x, z ? x : y);
            if (z3 != z2) {
                i++;
                z2 = z3;
            }
            i2 += iAbs2;
            if (i2 > 0) {
                if (y == iMin) {
                    return i;
                }
                y += i3;
                i2 -= iAbs;
            }
            x += i4;
        }
        return i;
    }
}
