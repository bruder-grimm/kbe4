package htwb.ai.mundt.song;

import htwb.ai.mundt.storage.ICRUD;

import java.util.List;

public interface ISongService extends ICRUD<Integer, Song> {
    List<Song> getAll();
}
