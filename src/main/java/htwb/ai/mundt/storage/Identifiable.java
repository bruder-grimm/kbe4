package htwb.ai.mundt.storage;

/** must only be extended by hibernate entities
 * @param <A> */
public interface Identifiable<A> {
    A getId();
}
