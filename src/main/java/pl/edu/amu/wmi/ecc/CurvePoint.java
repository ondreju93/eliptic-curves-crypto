package pl.edu.amu.wmi.ecc;

import lombok.Data;

@Data
public class CurvePoint {
    public static final CurvePoint POINT_AT_INFINITY = new CurvePoint(null,null);

    private final Remainder x;
    private final Remainder y;
}
