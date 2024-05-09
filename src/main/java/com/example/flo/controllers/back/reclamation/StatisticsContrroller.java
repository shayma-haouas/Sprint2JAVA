package com.example.flo.controllers.back.reclamation;

import com.example.flo.entities.Reclamation;
import com.example.flo.services.ReclamationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticsContrroller implements Initializable {

    @FXML
    PieChart pieChart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Reclamation> listReclamation = ReclamationService.getInstance().getAll();

        // Most used type

        List<String> types = new ArrayList<>();

        listReclamation.forEach(reclamation -> {
            if (!types.contains(reclamation.getType())) {
                types.add(reclamation.getType());
            }
        });

        List<Integer> typeCount = new ArrayList<>();

        types.forEach(type -> {
            int count = 0;
            for (Reclamation reclamation : listReclamation) {
                if (reclamation.getType().equals(type)) {
                    count++;
                }
            }
            typeCount.add(count);
        });

        for (int i = 0; i < types.size(); i++) {
            pieChart.getData().add(new PieChart.Data(types.get(i), typeCount.get(i)));
        }
    }
}
