package htwb.ai.mundt.storage;

import org.brudergrimm.jmonad.option.Option;
import org.brudergrimm.jmonad.tried.Try;

import java.io.Closeable;
import java.util.List;

public interface IRepository<Key, Value> extends Closeable {
    List<Value> getAll();
    Option<Value> findBy(Key id);
    Try<Key> create(Value entity);
    boolean update(Value entity);
    boolean delete(Key id);
}
