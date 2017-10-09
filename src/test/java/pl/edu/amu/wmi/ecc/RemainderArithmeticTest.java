package pl.edu.amu.wmi.ecc;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.*;
import static pl.edu.amu.wmi.ecc.Remainder.remainderWithBaseAndValue;

@RunWith(ZohhakRunner.class)
public class RemainderArithmeticTest {

    @TestWith({
            "4,4,4,0",
            "4,0,4,0",
            "4,1,3,0",
            "4,2,2,0",
            "5,1,1416,2",
            "1117,123,2226,115"
    })
    public void shouldAddRemaindersProperly(final int base, final int a, final int b, final int expectedResult) {
        assertThat(remainderWithBaseAndValue(base, a).add(remainderWithBaseAndValue(base, b)))
                .isEqualTo(remainderWithBaseAndValue(base, expectedResult));
    }

    @TestWith({
            "4,4,4,0",
            "4,0,4,0",
            "4,1,3,2",
            "4,2,2,0",
            "7,8,13,2",
            "192,13,234,163"
    })
    public void shouldSubtractRemaindersProperly(final int base, final int a, final int b, final int expectedResult) {
        assertThat(remainderWithBaseAndValue(base, a).subtract(remainderWithBaseAndValue(base, b)))
                .isEqualToComparingFieldByField(remainderWithBaseAndValue(base, expectedResult));
    }

    @TestWith({
            "4,3,3",
            "31,2222,3",
            "177,989,80"
    })
    public void shouldInverseRemaindersProperly(final int base, final int value, final int expectedResult) {
        assertThat(remainderWithBaseAndValue(base, value).inverse().getValue().intValue()).isEqualTo(expectedResult);
    }

    @TestWith({
            "7,5,false",
            "5,4,true",
            "13,1,true",
            "11,2,false",
            "11,5,true"
    })
    public void shouldCheckQuadraticResidue(final int base, final int value, final boolean isQuadraticResidue) {
        assertThat(remainderWithBaseAndValue(base, value).isQuadraticResidue()).isEqualTo(isQuadraticResidue);
    }
}
