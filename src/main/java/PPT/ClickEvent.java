package PPT;

import java.awt.Robot;
import java.awt.event.KeyEvent;

public class ClickEvent {
    public void PPTControl(int value){
        try{
            Robot unlimitedBladeWorld = new Robot();
            if(value == 1){
                unlimitedBladeWorld.keyPress(KeyEvent.VK_RIGHT);
            }
            else if(value == 2){
                unlimitedBladeWorld.keyPress(KeyEvent.VK_LEFT);
            }
        }catch(Exception e){

        }
    }


}
