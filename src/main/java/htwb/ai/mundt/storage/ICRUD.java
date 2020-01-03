package htwb.ai.mundt.storage;

import org.brudergrimm.jmonad.option.Option;

public interface ICRUD<Key, Value> {
    Option<Key> create(Value song);
    Option<Value> read(Key id);
    boolean update(Value song);
    boolean delete(Key id);
}
