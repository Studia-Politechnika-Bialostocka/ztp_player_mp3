package AppPackage.commands;

import AppPackage.MP3Player;
import AppPackage.state.PlayerHoldingState;
import AppPackage.ProjectForm;

public class PauseCommand implements AlternateCommand {
    @Override
    public void execute(ProjectForm frame, MP3Player mp3Player ) {
        if( mp3Player.getA() != null ) {
            PlayerHoldingState playerHoldingState = mp3Player.getPlayerHoldingState();
            playerHoldingState.getState().doAction( playerHoldingState, frame, mp3Player);
        }
    }
}
