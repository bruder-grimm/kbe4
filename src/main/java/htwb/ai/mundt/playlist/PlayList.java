package htwb.ai.mundt.playlist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import htwb.ai.mundt.song.Song;
import htwb.ai.mundt.storage.Identifiable;
import htwb.ai.mundt.user.User;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "playlists")
@XmlRootElement(name = "songList")
@XmlAccessorType (XmlAccessType.FIELD)
public class PlayList implements Identifiable<Integer> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String name;

    @JsonProperty("isPrivate")
    @XmlAttribute(name="isPrivate")
    @Column(name="private")
    private boolean invisible;

    @JsonIgnore
    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "userId")
    private User owner;

    @JsonProperty("songs")
    @XmlElement(name="song")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "song_playlist",
            joinColumns = {@JoinColumn(name = "playlist_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "song_id", referencedColumnName = "id")})
    List<Song> songs;

    public PlayList() {}

    @Override public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @Override public String toString() {
        return String.format("PlayList(%s, %s, private = %s, %s)", id, name, invisible, songs);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayList other = (PlayList) o;

        return
                id.equals(other.id) &&
                name.equals(other.name) &&
                invisible == other.invisible;
    }

    @Override public int hashCode() {
        return id + name.hashCode() + Boolean.hashCode(invisible);
    }
}
