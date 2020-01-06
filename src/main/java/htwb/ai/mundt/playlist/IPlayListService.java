package htwb.ai.mundt.playlist;

import htwb.ai.mundt.storage.ICRUD;

import java.util.List;

public interface IPlayListService extends ICRUD<Integer, PlayList> {
    List<PlayList> getAll();
}
