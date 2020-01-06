package htwb.ai.mundt.song;

import htwb.ai.mundt.playlist.PlayList;
import htwb.ai.mundt.storage.Identifiable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "songs")
@XmlRootElement
public class Song implements Identifiable<Integer> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String title;
    private String artist;
    private String label;
    private int released;

    @ManyToMany(mappedBy = "songs")
    private List<PlayList> playLists;

    public Song() {}

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getReleased() {
        return released;
    }

    public void setReleased(int released) {
        this.released = released;
    }

    public List<PlayList> getPlayLists() {
        return playLists;
    }

    public void setPlayLists(List<PlayList> playLists) {
        this.playLists = playLists;
    }

    @Override public String toString() {
        return String.format("Song(%s, %s, %s, %s, %s)", id, title, artist, label, released);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song other = (Song) o;

        return
                id.equals(other.id) &&
                released == other.released &&
                title.equals(other.title) &&
                artist.equals(other.artist) &&
                label.equals(other.label);
    }

    @Override public int hashCode() {
        return id + released + title.hashCode() + artist.hashCode() + label.hashCode();
    }
}
