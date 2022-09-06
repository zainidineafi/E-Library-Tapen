/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 *
 * @author Illuminate
 */
public class InputNumber {
    public void getInputNumber(TextField field) {
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                try {
                    if (!newValue.matches("\\d*")) {
                        field.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                } catch(Exception e) {}
            }
        });
    }
}
