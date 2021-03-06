package AppPackage;

import AppPackage.commands.*;
import AppPackage.factoryMethodSingleton.DarkThemeFrame;
import AppPackage.factoryMethodSingleton.LightThemeFrame;
import AppPackage.factoryMethodSingleton.ThemedFrame;
import AppPackage.sortStrategy.DurationSort;
import AppPackage.sortStrategy.NameSort;
import AppPackage.sortStrategy.SongSortStrategy;
import AppPackage.sortStrategy.ArtistSort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ProjectForm extends JFrame implements KeyListener{

    private JPanel panel1;
    private JButton jButtonClearPlaylist;
    private JButton jButtonPlay;
    private JButton jButtonPreviousSong;
    private JButton jButtonBrowse;
    private JButton jButtonDisplayPlaylist;
    private JButton jButtonPause;
    private JButton jButtonNextSong;
    private JButton jButtonPrintPlaylist;
    private JButton jButtonShuffle;
    private JButton jButtonStop;
    private JButton jButtonAddToPlaylist;
    private JList jListPlaylist;
    private JTextField jTextFieldPlayingFile;
    private JButton jButtonColorMode;
    private JButton jButtonBrowsePlaylist;
    private JScrollPane scrollPane;
    private JButton sortByName;
    private JButton sortByArtist;
    private JButton sortByDuration;
    private JPanel backgroundPanel;
    private JLabel sortlabel;

    private boolean isLightModeOn = false;

    private boolean isFileBrowsed = false;

    private boolean hidden = true;
    private boolean firstBrowse = true;

    private ThemedFrame themedFrame;

    private int displayHeight = 525;
    private SongSortStrategy songSortStrategy;
    private MP3Player mp3Player;
    private ProjectForm thisFrame;

    public class MyKeyListener implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                switch( e.getKeyChar() ){
                    case 'x':
                        executeCommand( new PauseCommand() );
                        break;
                    case 'c':
                        executeCommand( new PlayCommand() );
                        break;
                }
            }
            return false;
        }
    }
    public ProjectForm(MP3Player k){
        mp3Player = k;
        thisFrame = this;

        themedFrame = DarkThemeFrame.getDarkThemeFrame();
        themedFrame.changeTheme(thisFrame);
        sortlabel.setVisible(false);

        this.setContentPane(panel1);
        KeyEventDispatcher listener = new MyKeyListener();
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(listener);
        setFocusable(false);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
        this.revalidate();
        this.repaint();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.DrawPlaylist();

        addAllActionListeners();
//      first read a playlist
        executeCommand( new BrowsePlaylistCommand() );
    }

    public void WritePlaylistFile() {
        if(mp3Player.getFilePlaylist() != null) {
            try{
                FileOutputStream fs = new FileOutputStream( mp3Player.getPlaylistPhysicalFile() );
                ObjectOutputStream os = new ObjectOutputStream( fs );
                os.writeObject( mp3Player.getFilePlaylist() );
                os.close();
                fs.close();
            }
            catch(FileNotFoundException e) {
                System.out.printf("FileNotFoundException: %s\n", e);
            }
            catch(IOException e){
                System.out.printf("IOException: %s\n", e);
            }
        }
    }
    public void DrawPlaylist() {
        String[] myString = new String[1000];
        int i=0;
        if( mp3Player.getFilePlaylist() != null ) {
            for(File s : mp3Player.getFilePlaylist() ){
                if( s != null ) {
                    myString[i] = s.getName();
                    ++i;
                }
            }
        }
        jListPlaylist.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() { return myString.length; }
            public String getElementAt(int i) { return myString[i]; }
        });
    }

    private void jButtonSetColorMode() {
        if(isLightModeOn)
            themedFrame = DarkThemeFrame.getDarkThemeFrame();
        else
            themedFrame = LightThemeFrame.getLightThemeFrame();

        themedFrame.changeTheme(this);
        isLightModeOn = !isLightModeOn;
    }
    public LinkedList<JComponent> returnAllThemeComponents(){
        LinkedList<JComponent> components = new LinkedList<>();
        components.push(panel1);
        components.push(backgroundPanel);
        components.push(jButtonClearPlaylist);
        components.push(jButtonPlay);
        components.push(jButtonPreviousSong);
        components.push(jButtonBrowse);
        components.push(jButtonDisplayPlaylist);
        components.push(jButtonPause);
        components.push(jButtonNextSong);
        components.push(jButtonPrintPlaylist);
        components.push(jButtonShuffle);
        components.push(jButtonStop);
        components.push(jButtonAddToPlaylist);
        components.push(jListPlaylist);
        components.push(jTextFieldPlayingFile);
        components.push(jButtonColorMode);
        components.push(jButtonBrowsePlaylist);
        components.push(scrollPane);
        components.push(sortByName);
        components.push(sortByArtist);
        components.push(sortByDuration);
        return components;
    }
    public void keyTyped(KeyEvent e) {
        System.out.println(e + "KEY TYPED: ");
    }
    public void keyPressed(KeyEvent e) {
        System.out.println(e + "KEY PRESSED: ");
        System.out.println("keyPressed="+KeyEvent.getKeyText(e.getKeyCode()));
    }
    public void keyReleased(KeyEvent e) {
        System.out.println(e + "KEY RELEASED: ");
    }

    public JButton getjButtonPause() {
        return jButtonPause;
    }
    public JTextField getjTextFieldPlayingFile() {
        return jTextFieldPlayingFile;
    }

    public boolean getIsFileBrowsed() { return isFileBrowsed; }
    public void setIsFileBrowsed(boolean fileBrowsed) { isFileBrowsed = fileBrowsed; }
    public boolean getIsFirstBrowse() { return firstBrowse;}
    public void setFirstBrowse(boolean firstBrowse) { this.firstBrowse = firstBrowse; }

    public boolean getIsHidden() { return hidden; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }
    public int getDisplayHeight() { return displayHeight; }
    public JButton getjButtonDisplayPlaylist() { return jButtonDisplayPlaylist; }

    public JScrollPane getScrollPane() { return scrollPane; }
    public JLabel getSortlabel() { return sortlabel; }

    private void executeCommand( AlternateCommand command ){ command.execute( thisFrame, mp3Player ); }

    public void addAllActionListeners(){
        jButtonPrintPlaylist.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeCommand( new PrintPlaylistCommand() );
            }
        });
        jButtonBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeCommand( new BrowseCommand() );
            }
        });
        jButtonClearPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeCommand( new ClearPlaylistCommand() );
            }
        });
        jButtonPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeCommand( new PlayCommand() );
            }
        });
        jButtonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeCommand( new PauseCommand());
            }
        });
        jButtonPreviousSong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeCommand( new PreviousSongCommand() );
            }
        });
        jButtonNextSong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeCommand( new NextSongCommand() );
            }
        });
        jButtonDisplayPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeCommand( new DisplayPlaylistCommand() );
            }
        });

        jButtonAddToPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeCommand( new AddToPlaylistCommand() );
            }
        });

        jButtonShuffle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeCommand( new ShuffleCommand() );
            }
        });
        jButtonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeCommand( new StopCommand() );
            }
        });
        jButtonColorMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSetColorMode();
            }
        });
        jButtonBrowsePlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeCommand( new BrowsePlaylistCommand() );
            }
        });
        sortByName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                songSortStrategy = new NameSort();
                songSortStrategy.sort( mp3Player.getActualPlaylist() );
                mp3Player.getColorSelectedProxy().changeJlist( mp3Player.getActualPlaylist(), jListPlaylist, "Sort by name", sortlabel);
            }
        });
        sortByDuration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                songSortStrategy = new DurationSort();
                songSortStrategy.sort( mp3Player.getActualPlaylist() );
                mp3Player.getColorSelectedProxy().changeJlist( mp3Player.getActualPlaylist(), jListPlaylist,
                        "Sort by duration", sortlabel );
            }
        });
        sortByArtist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                songSortStrategy = new ArtistSort();
                songSortStrategy.sort( mp3Player.getActualPlaylist() );
                mp3Player.getColorSelectedProxy().changeJlist( mp3Player.getActualPlaylist(), jListPlaylist,
                        "Sort by artist", sortlabel );
            }
        });
    }
}
