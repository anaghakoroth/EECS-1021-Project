package eecs1021;

import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;
import java.util.TimerTask;

public class sensorTask extends TimerTask {
    private final Pin moistureSensor;
    private final Pin waterPump;
    private final SSD1306 theOledObject;

    public sensorTask(Pin moistureSensor, Pin waterPump, SSD1306 display) {
        this.moistureSensor = moistureSensor;
        this.waterPump = waterPump;
        this.theOledObject = display;

    }

    @Override
    public void run() {

        try {
            theOledObject.getCanvas().clear();
            theOledObject.getCanvas().drawString(0, 0, Long.toString(moistureSensor.getValue()));
            theOledObject.getCanvas().drawString(0, 0, "Moisture: " + moistureSensor.getValue());
            theOledObject.display();

            if (moistureSensor.getValue() > 30) {
                waterPump.setValue(1);
                Thread.sleep(4000);
                waterPump.setValue(0);
                theOledObject.getCanvas().drawString(0, 9, "Pump is on.");
                theOledObject.display();
            }
            else {
                waterPump.setValue(0);
                theOledObject.getCanvas().drawString(0, 9, "Pump is off.");
                theOledObject.display();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
