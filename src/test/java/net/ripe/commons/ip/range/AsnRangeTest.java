package net.ripe.commons.ip.range;

import static junit.framework.Assert.assertEquals;
import net.ripe.commons.ip.resource.Asn;
import org.junit.Test;

public class AsnRangeTest extends AbstractRangeTest<Asn, AsnRange> {

    private final Asn as1 = Asn.valueOf(1L);
    private final Asn as3 = Asn.valueOf(3L);

    @Override
    protected Asn from(String s) {
        return Asn.valueOf(s);
    }

    @Override
    protected Asn to(String s) {
        return Asn.valueOf(s);
    }

    @Override
    protected AsnRange getTestRange(Asn start, Asn end) {
        return new AsnRange(start, end);
    }

    @Test
    public void testBuilder() {
        AsnRange range = AsnRange.from(as1).to(as3);
        assertEquals(as1, range.start());
        assertEquals(as3, range.end());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderWithInvalidRange() {
        Range.from(as3).to(as1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderWithNullStart() {
        Range.from((Asn)null).to(as3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderWithNullEnd() {
        Range.from(as1).to((Asn)null);
    }
}