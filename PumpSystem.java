package eecs1021;

import org.firmata4j.I2CDevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;
import org.firmata4j.IODevice;
import java.io.IOException;
import java.util.Timer;

public class PumpSystem {
    public static void main(String[] args) throws
            IOException, InterruptedException{
        //Initialize board
        var arduinoObject = new FirmataDevice("COM3");
        try {
            arduinoObject.start();
            System.out.println("Board started");
            arduinoObject.ensureInitializationIsDone();
        } catch (Exception ex) {
            System.out.println("Couldn't connect to board");
        }

        //Pins
        var moistureSensor = arduinoObject.getPin(16);
        moistureSensor.setMode(Pin.Mode.ANALOG);

        var waterPump = arduinoObject.getPin(2);
        waterPump.setMode(Pin.Mode.OUTPUT);

        //OLED
        I2CDevice i2cObject = arduinoObject.getI2CDevice((byte) 0x3C); // Use 0x3C for the Grove OLED
        SSD1306 theOledObject = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64); // 128x64 OLED SSD1515
        theOledObject.init();

        //Task method
        Timer sensorTimer = new Timer();
        var task = new sensorTask(moistureSensor, waterPump, theOledObject);
        sensorTimer.schedule(task, 0, 1000);


    }
}
