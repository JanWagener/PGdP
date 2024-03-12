package pgdp.searchengine.utilities;

public class Tuple<S, T> {
    private S s;
    private T t;

    public static <S, T> Tuple<S, T> of(S s, T t) {
        return new Tuple<>(s, t);
    }

    private Tuple(S s, T t) {
        this.s = s;
        this.t = t;
    }

    public S getFirst() {
        return s;
    }

    public T getSecond() {
        return t;
    }

    @Override
    public String toString() {
        return "(" + s.toString() + ", " + t.toString() + ")";
    }

    @Override
    public int hashCode() {
        return s.hashCode() + t.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Tuple<?, ?> otherTuple) {
            return s.equals(otherTuple.s) && t.equals(otherTuple.t);
        }
        return false;
    }
}
