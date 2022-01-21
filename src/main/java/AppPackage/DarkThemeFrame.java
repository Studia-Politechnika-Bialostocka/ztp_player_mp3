package AppPackage;

import javax.swing.*;
import java.awt.*;

public class DarkThemeFrame implements ThemedFrame{
    final Color colorOfForeground= new Color(149,214,0);
    final Color colorOfBackground= new Color(42, 41, 41);
    public void changeTheme( ProjectForm projectForm ){
        for(JComponent d : projectForm.returnAllThemeComponents()){
            d.setForeground(colorOfForeground);
            d.setBackground(colorOfBackground);
        }
    };
}