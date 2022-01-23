package AppPackage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Playlist implements Iterable<Song> {
    private ArrayList<Song> collectionOfSongs = new ArrayList<>();
    int actualPosition = 0;

    public Iterator<Song> iterator(){
        return new Iterator<>() {
            public boolean hasNext(){ return true; }
            public Song next(){
                Song song = collectionOfSongs.get(actualPosition);
                if(actualPosition >= collectionOfSongs.size() - 1)
                    actualPosition = 0;
                else
                    ++actualPosition;
                return(song);
            }
        };
    }
    public Iterator<Song> iteratorToPrevious(){
        return new Iterator<>() {
            public boolean hasNext(){ return true; }
            public Song next(){
                System.out.println( "pozycja " +actualPosition);
                Song song = collectionOfSongs.get(actualPosition);
                if( actualPosition == 0 )
                    actualPosition = collectionOfSongs.size()-1;
                else
                    --actualPosition;
                return(song);
            }
        };
    }

    public void addToPlaylist(Song song){
        collectionOfSongs.add(song);
    }
    public void removeAllSongs(){
        collectionOfSongs.clear();
    }
}
