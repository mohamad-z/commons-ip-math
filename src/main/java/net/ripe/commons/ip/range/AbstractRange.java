package net.ripe.commons.ip.range;

import net.ripe.commons.ip.resource.EqualsSupport;
import org.apache.commons.lang.Validate;

public abstract class AbstractRange<C extends Comparable<C>, R extends AbstractRange<C, R>>
        extends EqualsSupport {

    private final C start;
    private final C end;

    protected AbstractRange(C start, C end) {
        Validate.notNull(start, "start of range must not be null");
        Validate.notNull(end, "end of range must not be null");
        Validate.isTrue(start.compareTo(end) <= 0, String.format("Invalid range [%s..%s]", start.toString(), end.toString()));
        this.start = start;
        this.end = end;
    }

    protected abstract R newInstance(C start, C end);

    public C start() {
        return start;
    }

    public C end() {
        return end;
    }

    public boolean overlaps(R arg) {
        return arg.contains(start) || arg.contains(end) || this.contains(arg);
    }

    public boolean contains(R arg) {
        return this.contains(arg.start) && this.contains(arg.end);
    }

    public boolean contains(C arg) {
        Validate.notNull(arg, "A value is required");
        return start.compareTo(arg) <= 0 && end.compareTo(arg) >= 0;
    }

    public boolean intersects(R other) {
        C start = max(this.start(), other.start());
        C end = min(this.end(), other.end());
        return start.compareTo(end) <= 0;
    }

    public R intersection(R other) {
        C start = max(this.start(), other.start());
        C end = min(this.end(), other.end());
        return newInstance(start, end);
    }

    private C max(C a, C b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    private C min(C a, C b) {
        return a.compareTo(b) <= 0 ? a : b;
    }

    @Override
    public String toString() {
        return String.format("[%s..%s]", start.toString(), end.toString());
    }

    protected static abstract class AbstractRangeBuilder<C extends Comparable<C>, R extends AbstractRange<C, R>> {
        private final C start;
        private final Class<R> typeOfRange;

        protected AbstractRangeBuilder(C from, Class<R> typeOfRange) {
            this.start = from;
            this.typeOfRange = typeOfRange;
        }

        public R to(C end) {
            try {
                return typeOfRange
                        .getDeclaredConstructor(start.getClass(), end.getClass())
                        .newInstance(start, end);
            } catch (Exception e) {
                throw new RuntimeException(String.format("Failed to create range [%s..%s]", start.toString(), end.toString()));
            }
        }
    }
}