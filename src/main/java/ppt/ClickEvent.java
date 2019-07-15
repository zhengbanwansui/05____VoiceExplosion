package ppt;

import java.awt.Robot;
import java.awt.event.KeyEvent;

public class ClickEvent {
    public void PPTControl(int value){
        try{
            Robot unlimitedBladeWorld = new Robot();
            if(value == 1){
                unlimitedBladeWorld.keyPress(KeyEvent.VK_RIGHT);
//                Thread.sleep(200);
//                unlimitedBladeWorld.keyPress(KeyEvent.VK_RIGHT);
                System.out.println("--------------翻页--------------");
            }
            else if(value == 2){
                unlimitedBladeWorld.keyPress(KeyEvent.VK_LEFT);
                Thread.sleep(200);
                unlimitedBladeWorld.keyPress(KeyEvent.VK_LEFT);
                System.out.println("--------------回退--------------");
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

}
